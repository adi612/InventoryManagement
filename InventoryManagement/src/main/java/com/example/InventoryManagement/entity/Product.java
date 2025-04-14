package com.example.InventoryManagement.entity;

import com.example.InventoryManagement.util.Constants;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;

    @NotBlank(message = Constants.PRODUCT_NAME_REQUIRED)
    private String productName;

    @NotNull(message = Constants.PRODUCT_PRICE_REQUIRED)
    @Min(value = 1, message = Constants.PRODUCT_PRICE_MIN)
    private double productPrice;

    @NotNull(message = Constants.STOCK_REQUIRED)
    @Min(value = 0, message = Constants.STOCK_MIN)
    private int stock;

    @NotBlank(message = Constants.CATEGORY_REQUIRED)
    private String category;

    public Product(boolean deleted) {
        this.deleted = deleted;
    }

    private boolean deleted = false;

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public Product(){

    }

    public Product(Long productId, String productName, double productPrice, int stock, String category) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.stock = stock;
        this.category = category;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public @NotBlank(message = Constants.PRODUCT_NAME_REQUIRED) String getProductName() {
        return productName;
    }

    public void setProductName(@NotBlank(message = Constants.PRODUCT_NAME_REQUIRED) String productName) {
        this.productName = productName;
    }

    @NotNull(message = Constants.PRODUCT_PRICE_REQUIRED)
    @Min(value = 1, message = Constants.PRODUCT_PRICE_MIN)
    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(@NotNull(message = Constants.PRODUCT_PRICE_REQUIRED) @Min(value = 1, message = Constants.PRODUCT_PRICE_MIN) double productPrice) {
        this.productPrice = productPrice;
    }

    @NotNull(message = Constants.STOCK_REQUIRED)
    @Min(value = 0, message = Constants.STOCK_MIN)
    public int getStock() {
        return stock;
    }

    public void setStock(@NotNull(message = Constants.STOCK_REQUIRED) @Min(value = 0, message = Constants.STOCK_MIN) int stock) {
        this.stock = stock;
    }

    public @NotBlank(message = Constants.CATEGORY_REQUIRED) String getCategory() {
        return category;
    }

    public void setCategory(@NotBlank(message = Constants.CATEGORY_REQUIRED) String category) {
        this.category = category;
    }
}
