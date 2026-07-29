$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$microservicesRoot = Join-Path $projectRoot "microservices"
$logsDir = Join-Path $projectRoot "logs"
$pidFile = Join-Path $logsDir "team4-stack.pids.json"

$services = @(
    @{ Name = "service-registry"; Port = 8761; Health = "http://localhost:8761/actuator/health"; DelayAfterStartMs = 0 },
    @{ Name = "identity-service"; Port = 8081; Health = "http://localhost:8081/actuator/health"; DelayAfterStartMs = 0 },
    @{ Name = "policyholder-service"; Port = 8082; Health = "http://localhost:8082/actuator/health"; DelayAfterStartMs = 0 },
    @{ Name = "product-service"; Port = 8083; Health = "http://localhost:8083/actuator/health"; DelayAfterStartMs = 0 },
    @{ Name = "policy-service"; Port = 8084; Health = "http://localhost:8084/actuator/health"; DelayAfterStartMs = 0 },
    @{ Name = "premium-service"; Port = 8085; Health = "http://localhost:8085/actuator/health"; DelayAfterStartMs = 0 },
    @{ Name = "claims-service"; Port = 8086; Health = "http://localhost:8086/actuator/health"; DelayAfterStartMs = 0 },
    @{ Name = "statement-service"; Port = 8087; Health = "http://localhost:8087/actuator/health"; DelayAfterStartMs = 0 },
    @{ Name = "api-gateway"; Port = 8080; Health = "http://localhost:8080/actuator/health"; DelayAfterStartMs = 0 }
)

function Test-ServiceRunning {
    param(
        [int]$Port
    )

    try {
        $connection = Get-NetTCPConnection -LocalPort $Port -State Listen -ErrorAction Stop
        return $connection.Count -gt 0
    } catch {
        return $false
    }
}

function Wait-ForHttpOk {
    param(
        [string]$Url,
        [string]$DisplayName,
        [int]$TimeoutSeconds = 120
    )

    $deadline = (Get-Date).AddSeconds($TimeoutSeconds)
    while ((Get-Date) -lt $deadline) {
        try {
            $response = Invoke-WebRequest -Uri $Url -UseBasicParsing -TimeoutSec 5
            if ($response.StatusCode -ge 200 -and $response.StatusCode -lt 300) {
                Write-Host "$DisplayName is healthy at $Url"
                return
            }
        } catch {
        }
        Start-Sleep -Seconds 2
    }

    throw "Timed out waiting for $DisplayName at $Url"
}

function Start-ServiceProcess {
    param(
        [hashtable]$Service
    )

    if (Test-ServiceRunning -Port $Service.Port) {
        Write-Host "$($Service.Name) already appears to be running on port $($Service.Port). Skipping start."
        return @{
            name = $Service.Name
            pid = $null
            health = $Service.Health
            reused = $true
        }
    }

    $serviceDir = Join-Path $microservicesRoot $Service.Name
    $stdoutLog = Join-Path $logsDir "$($Service.Name).out.log"
    $stderrLog = Join-Path $logsDir "$($Service.Name).err.log"

    $process = Start-Process `
        -FilePath "mvn.cmd" `
        -ArgumentList @("spring-boot:run") `
        -WorkingDirectory $serviceDir `
        -RedirectStandardOutput $stdoutLog `
        -RedirectStandardError $stderrLog `
        -PassThru

    if ($Service.DelayAfterStartMs -gt 0) {
        Start-Sleep -Milliseconds $Service.DelayAfterStartMs
    }

    return @{
        name = $Service.Name
        pid = $process.Id
        health = $Service.Health
        reused = $false
    }
}

New-Item -ItemType Directory -Force -Path $logsDir | Out-Null

$started = New-Object System.Collections.Generic.List[object]

Write-Host "Starting service-registry first..."
$registry = Start-ServiceProcess -Service $services[0]
$started.Add($registry) | Out-Null
Wait-ForHttpOk -Url $services[0].Health -DisplayName $services[0].Name

Write-Host "Starting internal services..."
foreach ($service in $services[1..7]) {
    $started.Add((Start-ServiceProcess -Service $service)) | Out-Null
}

foreach ($service in $services[1..7]) {
    Wait-ForHttpOk -Url $service.Health -DisplayName $service.Name
}

Write-Host "Starting api-gateway last..."
$gateway = Start-ServiceProcess -Service $services[8]
$started.Add($gateway) | Out-Null
Wait-ForHttpOk -Url $services[8].Health -DisplayName $services[8].Name

$started | ConvertTo-Json | Set-Content -Path $pidFile

Write-Host ""
Write-Host "Team 4 stack is up."
Write-Host "Gateway Swagger: http://localhost:8080/swagger-ui/index.html"
Write-Host "PID file: $pidFile"
Write-Host "Logs folder: $logsDir"
