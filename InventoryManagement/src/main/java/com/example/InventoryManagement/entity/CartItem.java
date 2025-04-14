package com.example.InventoryManagement.entity;

import com.example.InventoryManagement.util.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @NotNull(message = Constants.PRODUCT_REQUIRED)
    private Product product;

    @Min(value = 1, message = Constants.QUANTITY_MIN)
    private int quantity;

    public CartItem(){

    }

    public CartItem(Long cartId, Product product, int quantity) {
        this.cartId = cartId;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public @NotNull(message = Constants.PRODUCT_REQUIRED) Product getProduct() {
        return product;
    }

    public void setProduct(@NotNull(message = Constants.PRODUCT_REQUIRED) Product product) {
        this.product = product;
    }

    @Min(value = 1, message = Constants.QUANTITY_MIN)
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(@Min(value = 1, message = Constants.QUANTITY_MIN) int quantity) {
        this.quantity = quantity;
    }
}
