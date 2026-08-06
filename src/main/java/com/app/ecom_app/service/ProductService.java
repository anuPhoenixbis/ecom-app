package com.app.ecom_app.service;

import com.app.ecom_app.dto.ProductRequest;
import com.app.ecom_app.dto.ProductResponse;
import com.app.ecom_app.model.Product;
import com.app.ecom_app.repository.ProductRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product savedProduct = mapProductRequestToProduct(productRequest);//convert from req->product
        productRepo.save(savedProduct);//save product
        return mapProductToResponse(savedProduct);//convert product->res
    }
    public Product mapProductRequestToProduct(ProductRequest productRequest) {
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setCategory(productRequest.getCategory());
        product.setImageUrl(productRequest.getImageUrl());
        product.setPrice(productRequest.getPrice());
        product.setQuantity(productRequest.getQuantity());
        product.setDescription(productRequest.getDescription());
        return product;
    }
    public ProductResponse mapProductToResponse(Product product){
        ProductResponse productResponse = new ProductResponse();
        productResponse.setId(product.getId());
        productResponse.setName(product.getName());
        productResponse.setCategory(String.valueOf(product.getCategory()));
        productResponse.setImageUrl(product.getImageUrl());
        productResponse.setPrice(product.getPrice());
        productResponse.setQuantity(product.getQuantity());
        productResponse.setDescription(product.getDescription());
        productResponse.setActive(product.getActive());
        return productResponse;
    }

    public boolean fetchProductById(String id) {
        return productRepo.findById(id).isPresent();
    }

    public ProductResponse updateProduct(String id, ProductRequest product) {
        if(!productRepo.findById(id).isPresent()) return null;
        Product savedProduct = productRepo.findById(id).get();
        savedProduct.setName(product.getName());
        savedProduct.setCategory(product.getCategory());
        savedProduct.setImageUrl(product.getImageUrl());
        savedProduct.setPrice(product.getPrice());
        savedProduct.setQuantity(product.getQuantity());
        savedProduct.setDescription(product.getDescription());
        productRepo.save(savedProduct);
        return mapProductToResponse(savedProduct);

    }

    public List<ProductResponse> getAllProducts() {
//        the naming convention automatically causes the repo to define the required query
        return productRepo.findByActiveTrue().stream()
                .map(this::mapProductToResponse)
                .collect(Collectors.toList());
    }

    public void deActivateProduct(String id) {
//        productRepo.deleteById(id);
//        we won't remove the product from the db rather we shall it in-active
        Product savedProduct = productRepo.findById(id).get();
        savedProduct.setActive(false);
        productRepo.save(savedProduct);
    }

    public boolean isProductActive(String id) {
        return productRepo.findById(id).get().getActive();
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepo.searchProducts(keyword).stream()
                .map(this::mapProductToResponse)
                .collect(Collectors.toList());
    }

    public void deleteProduct(String id) {
        productRepo.deleteById(id);
    }

    public void activateProduct(String id) {
        Product savedProduct = productRepo.findById(id).get();
        savedProduct.setActive(true);
        productRepo.save(savedProduct);
    }
}
