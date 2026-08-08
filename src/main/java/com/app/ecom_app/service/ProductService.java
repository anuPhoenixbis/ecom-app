package com.app.ecom_app.service;

import com.app.ecom_app.dto.ProductRequest;
import com.app.ecom_app.dto.ProductResponse;
import com.app.ecom_app.model.CartItem;
import com.app.ecom_app.model.Product;
import com.app.ecom_app.repository.ProductRepo;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;

    private final CartService cartService;

    private final RedisService redisService;

    public ProductResponse createProduct(ProductRequest productRequest) {
        Product savedProduct = mapProductRequestToProduct(productRequest);//convert from req->product
        productRepo.save(savedProduct);//save product
        redisService.deleteAll();//re-cache
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

        String key = "product:" + id;
        Product cachedProduct = redisService.get(key,Product.class);
        if (cachedProduct != null) {
            return true;
        }

        Optional<Product> productOptional = productRepo.findById(id);
        if (productOptional.isPresent()) {
            redisService.set(key,productOptional.get(),300L);
            return true;
        }

        return false;
    }

    public ProductResponse updateProduct(String id, ProductRequest product) {

        Optional<Product> productOptional = productRepo.findById(id);
        if (productOptional.isEmpty()) {
            return null;
        }

        Product savedProduct = productOptional.get();

        savedProduct.setName(product.getName());
        savedProduct.setCategory(product.getCategory());
        savedProduct.setImageUrl(product.getImageUrl());
        savedProduct.setPrice(product.getPrice());
        savedProduct.setQuantity(product.getQuantity());
        savedProduct.setDescription(product.getDescription());
        productRepo.save(savedProduct);
        redisService.delete("product:" + id);
        return mapProductToResponse(savedProduct);

    }

    public List<ProductResponse> getAllProducts() {
//        the naming convention automatically causes the repo to define the required query

        String key = "product:all";
        List<ProductResponse> cachedProducts = redisService.get(key, new TypeReference<List<ProductResponse>>() {});

        if (cachedProducts != null) return cachedProducts;

        List<ProductResponse> products = productRepo.findByActiveTrue().stream()
                .map(this::mapProductToResponse)
                .collect(Collectors.toList());

        redisService.set(key,products, 300L);
        return products;
    }

    public void deActivateProduct(String id) {

        Product product = productRepo.findById(id).get();
        if(product==null){
            return;
        }
        product.setActive(false);
        productRepo.save(product);
        redisService.delete("product:" + id);
    }

    public boolean isProductActive(String id) {
        return productRepo.findById(id)
                .map(Product::getActive)
                .orElse(false);
    }

    public List<ProductResponse> searchProducts(String keyword) {

        String key = "product:search:" + keyword.toLowerCase().trim();

        List<ProductResponse> cachedProductList = redisService.get(key, new TypeReference<List<ProductResponse>>() {});

        if (cachedProductList != null) {
            return cachedProductList;
        }
        List<ProductResponse> products = productRepo.searchProducts(keyword).stream()
                .map(this::mapProductToResponse)
                .collect(Collectors.toList());

        redisService.set(key,products,300L);

        return products;
    }

    public void deleteProduct(String id) {
       Optional<Product> product =  productRepo.findById(id);
       if(product.isEmpty()){
           return;
       }
       productRepo.deleteById(id);
       redisService.delete("product:" + id);
    }

    public void activateProduct(String id) {
       Product product = productRepo.findById(id).orElse(null);
       if(product == null){ return;}

        product.setActive(true);
        productRepo.save(product);
        redisService.delete("product:"+id);
    }

    public void updateStockQuantity(String userId) {
        List<CartItem> cartItems = cartService.getCartItems(userId);
        for (CartItem cartItem : cartItems) {
            productRepo.findById(cartItem.getProduct().getId()).ifPresent(product -> {
                product.setQuantity(product.getQuantity().subtract(cartItem.getQuantityOnHand()));
                productRepo.save(product);
                redisService.delete("product:" + product.getId());
            });
        }
    }

}
