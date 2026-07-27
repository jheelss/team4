package org.example.insurance.product;

import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductRepository products;

    ProductController(ProductRepository products) {
        this.products = products;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody InsuranceProduct p) {
        if (!valid(p)) return ResponseEntity.badRequest().body(Map.of("error", "Product name/type, positive coverage/premium amounts, a positive term, and status ACTIVE or INACTIVE are required"));
        return ResponseEntity.status(201).body(products.save(p));
    }

    @GetMapping
    public List<InsuranceProduct> all() {
        return products.findAll();
    }

    @GetMapping("/{id}")
    public InsuranceProduct get(@PathVariable Long id) {
        return products.findById(id).orElseThrow(() -> new NoSuchElementException("Product not found"));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> status(@PathVariable Long id, @RequestParam String value) {
        if (!Set.of("ACTIVE", "INACTIVE").contains(value)) return ResponseEntity.badRequest().body(Map.of("error", "Status must be ACTIVE or INACTIVE"));
        InsuranceProduct p = get(id);
        p.setStatus(value);
        return ResponseEntity.ok(products.save(p));
    }

    private boolean valid(InsuranceProduct p) {
        return p != null && p.getProductName() != null && !p.getProductName().isBlank()
                && p.getProductType() != null && !p.getProductType().isBlank()
                && p.getCoverageAmount() != null && p.getCoverageAmount().signum() > 0
                && p.getPremiumAmount() != null && p.getPremiumAmount().signum() > 0
                && p.getPolicyTerm() != null && p.getPolicyTerm() > 0
                && Set.of("ACTIVE", "INACTIVE").contains(p.getStatus());
    }
}
