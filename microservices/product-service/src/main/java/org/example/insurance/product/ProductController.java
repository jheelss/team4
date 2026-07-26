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
    public ResponseEntity<InsuranceProduct> create(@RequestBody InsuranceProduct p) {
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
    public InsuranceProduct status(@PathVariable Long id, @RequestParam String value) {
        InsuranceProduct p = get(id);
        p.setStatus(value);
        return products.save(p);
    }
}
