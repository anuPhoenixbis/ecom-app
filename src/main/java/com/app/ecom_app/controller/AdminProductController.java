package com.app.ecom_app.controller;

import com.app.ecom_app.dto.ProductRequest;
import com.app.ecom_app.dto.ProductResponse;
import com.app.ecom_app.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class AdminProductController {

    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest product) {
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.CREATED);
    }


    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(@PathVariable String id, @Valid @RequestBody ProductRequest product) {
        if(!productService.fetchProductById(id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(productService.updateProduct(id,product), HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable String id) {
        if(!productService.fetchProductById(id)) return new ResponseEntity<>("Product id is invalid",HttpStatus.NOT_FOUND);
        if(!productService.isProductActive(id)) return new ResponseEntity<>("Product is already in-active",HttpStatus.CONFLICT);
        productService.deActivateProduct(id);
        return new ResponseEntity<>("Product de-activated", HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> permanentDeleteProduct(@PathVariable String id) {
        if(!productService.fetchProductById(id)) return new ResponseEntity<>("Product id is invalid",HttpStatus.NOT_FOUND);
        productService.deleteProduct(id);
        return new ResponseEntity<>("Product deleted", HttpStatus.OK);
    }

    @PutMapping("/activate/{id}")
    public ResponseEntity<String> activateProduct(@PathVariable String id) {
        if(!productService.fetchProductById(id)) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        productService.activateProduct(id);
        return new ResponseEntity<>("Product activated", HttpStatus.OK);

    }
}
