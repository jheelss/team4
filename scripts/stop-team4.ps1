$ErrorActionPreference = "Stop"

$projectRoot = Split-Path -Parent $PSScriptRoot
$logsDir = Join-Path $projectRoot "logs"
$pidFile = Join-Path $logsDir "team4-stack.pids.json"

if (-not (Test-Path $pidFile)) {
    Write-Host "No PID file found at $pidFile"
    exit 0
}

$entries = Get-Content $pidFile | ConvertFrom-Json

foreach ($entry in $entries) {
    if (-not $entry.pid) {
        continue
    }

    try {
        $process = Get-Process -Id $entry.pid -ErrorAction Stop
        Stop-Process -Id $entry.pid
        Write-Host "Stopped $($entry.name) (PID $($entry.pid))"
    } catch {
        Write-Host "$($entry.name) is not running anymore."
    }
}

Remove-Item -LiteralPath $pidFile -Force
Write-Host "Team 4 stack stop complete."
