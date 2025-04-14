package com.example.InventoryManagement.service;

import com.example.InventoryManagement.entity.CartItem;
import com.example.InventoryManagement.entity.Product;
import com.example.InventoryManagement.exception.CartException;
import com.example.InventoryManagement.exception.OutOfStockException;
import com.example.InventoryManagement.exception.ResourceNotFoundException;
import com.example.InventoryManagement.repository.CartRepository;
import com.example.InventoryManagement.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

//    public CartItem addToCart(Long productId, int quantity) {
//        try {
//            Product product = productRepository.findById(productId)
//                    .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
//
//            if (quantity > product.getStock()) {
//                throw new IllegalArgumentException("Requested quantity exceeds available stock");
//            }
//
//            CartItem cartItem = new CartItem();
//            cartItem.setProduct(product);
//            cartItem.setQuantity(quantity);
//            return cartRepository.save(cartItem);
//        } catch (Exception e) {
//            throw new RuntimeException("Error adding product to cart: " + e.getMessage());
//        }
//    }
    
    public CartItem addToCart(Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        if (quantity > product.getStock()) {
            throw new IllegalArgumentException("Requested quantity exceeds available stock");
        }

        // Check if product already exists in the cart
        Optional<CartItem> existingCartItem = cartRepository.findByProduct(product);
        if (existingCartItem.isPresent()) {
            CartItem cartItem = existingCartItem.get();
            int newQuantity = cartItem.getQuantity() + quantity;
            if (newQuantity > product.getStock()) {
                throw new IllegalArgumentException("Total quantity in cart exceeds available stock");
            }
            cartItem.setQuantity(newQuantity);
            return cartRepository.save(cartItem);
        }

        // Otherwise, create a new cart entry
        CartItem cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        return cartRepository.save(cartItem);
    }


    public List<CartItem> getAllItems() {
        try {
            return cartRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching cart items: " + e.getMessage());
        }
    }

    public double getTotalCartPrice() {
        try {
            return cartRepository.findAll().stream()
                    .mapToDouble(cartItem -> cartItem.getProduct().getProductPrice() * cartItem.getQuantity())
                    .sum();
        } catch (Exception e) {
            throw new RuntimeException("Error calculating total cart price: " + e.getMessage());
        }
    }

    public void purchaseCartItems() {
        try {
            List<CartItem> cartItems = cartRepository.findAll();
            for (CartItem cartItem : cartItems) {
                Product product = cartItem.getProduct();
                int purchaseQuantity = cartItem.getQuantity();

                if (product.getStock() < purchaseQuantity) {
                    throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
                }

                product.setStock(product.getStock() - purchaseQuantity);
                productRepository.save(product);
            }
            cartRepository.deleteAll();
        } catch (Exception e) {
            throw new RuntimeException("Error processing cart purchase: " + e.getMessage());
        }
    }

    public void removeFromCart(Long cartId, int quantity) {
        try {
            CartItem cartItem = cartRepository.findById(cartId)
                    .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

            if (cartItem.getQuantity() > quantity) {
                cartItem.setQuantity(cartItem.getQuantity() - quantity);
                cartRepository.save(cartItem);
            } else {
                cartRepository.delete(cartItem);
            }
        } catch (Exception e) {
            throw new RuntimeException("Error removing item from cart: " + e.getMessage());
        }
    }
    
    //increases or decreases the quantity of an item
    public String updateCartItemQuantity(Long cartId, int quantity) {
        CartItem cartItem = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));

        Product product = cartItem.getProduct();
        
        if (quantity > product.getStock()) {
            throw new OutOfStockException("Quantity exceeds available stock");
        }

        if (quantity == 0) {
            cartRepository.delete(cartItem);
            return "Cart item removed.";
        }

        cartItem.setQuantity(quantity);
        cartRepository.save(cartItem);
        return "Cart item quantity updated successfully.";
    }
    
 
    //checkout clear all 
    public String checkout() {
        List<CartItem> cartItems = cartRepository.findAll();

        if (cartItems.isEmpty()) {
            throw new CartException("Cart is empty. Cannot checkout.");
        }

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();
            int purchaseQuantity = cartItem.getQuantity();

            if (product.getStock() < purchaseQuantity) {
                throw new OutOfStockException("Not enough stock for " + product.getProductName());
            }

            product.setStock(product.getStock() - purchaseQuantity);
            productRepository.save(product);
        }

        cartRepository.deleteAll();
        return "Checkout successful. Stock updated and cart cleared.";
    }
    //discont over 1000
    public Map<String, Double> getCartDiscounts() {
        List<CartItem> cartItems = cartRepository.findAll();
        Map<String, Double> discounts = new HashMap<>();

        for (CartItem cartItem : cartItems) {
            double price = cartItem.getProduct().getProductPrice() * cartItem.getQuantity();
            double discount = (price > 1000) ? price * 0.10 : 0; // 10% discount if > 1000
            discounts.put(cartItem.getProduct().getProductName(), discount);
        }
        
        return discounts;
    }
    
//returns the total number of items in the cart.
    public int getCartItemCount() {
        return (int) cartRepository.count();
    }





}
