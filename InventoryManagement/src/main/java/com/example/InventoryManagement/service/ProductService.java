package com.example.InventoryManagement.service;

import com.example.InventoryManagement.entity.Product;
import com.example.InventoryManagement.exception.ResourceNotFoundException;
import com.example.InventoryManagement.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        try {
            return productRepository.save(product);
        } catch (Exception e) {
            throw new RuntimeException("Error adding product: " + e.getMessage());
        }
    }

    public List<Product> getAllProducts() {
        try {
            return productRepository.findByDeletedFalse();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching products: " + e.getMessage());
        }
    }

    public Product getProductById(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));
    }

    public Product updateProduct(Long productId, Product updatedProduct) {
        try {
            Product existingProduct = getProductById(productId);
            existingProduct.setProductName(updatedProduct.getProductName());
            existingProduct.setProductPrice(updatedProduct.getProductPrice());
            existingProduct.setStock(updatedProduct.getStock());
            existingProduct.setCategory(updatedProduct.getCategory());
            return productRepository.save(existingProduct);
        } catch (Exception e) {
            throw new RuntimeException("Error updating product with ID: " + productId);
        }
    }

    public void deleteProduct(Long productId) {
        try {
            Product product = getProductById(productId);
            productRepository.delete(product);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting product with ID: " + productId);
        }
    }

    public String softDeleteProduct(Long productId) {
        try {
            Product product = getProductById(productId);
            product.setDeleted(true); // Mark as deleted
            productRepository.save(product);
            return "Product soft deleted successfully.";
        } catch (ResourceNotFoundException e) {
            return "Product not found with ID: " + productId;
        } catch (Exception e) {
            throw new RuntimeException("Error soft deleting product: " + e.getMessage());
        }
    }
    
  //check Stock availability 
    public int checkStock(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return product.getStock();
    }
}
