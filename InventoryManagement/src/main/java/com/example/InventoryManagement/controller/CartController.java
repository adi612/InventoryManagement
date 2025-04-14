package com.example.InventoryManagement.controller;


import com.example.InventoryManagement.entity.CartItem;
import com.example.InventoryManagement.exception.ResourceNotFoundException;
import com.example.InventoryManagement.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")

public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/addtocart/{productId}/{quantity}")
    public ResponseEntity<?> addToCart(@PathVariable Long productId, @PathVariable int quantity) {
        try {
            return ResponseEntity.ok(cartService.addToCart(productId, quantity));
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getall")
    public ResponseEntity<List<CartItem>> getAllCartItems() {
        return ResponseEntity.ok(cartService.getAllItems());
    }

    @GetMapping("/total")
    public ResponseEntity<Double> getTotalCartPrice() {
        return ResponseEntity.ok(cartService.getTotalCartPrice());
    }

    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseCartItems() {
        try {
            cartService.purchaseCartItems();
            return ResponseEntity.ok("All cart items purchased successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error purchasing items: " + e.getMessage());
        }
    }

    @DeleteMapping("/remove/{cartId}/{quantity}")
    public ResponseEntity<String> removeFromCart(@PathVariable Long cartId, @PathVariable int quantity) {
        try {
            cartService.removeFromCart(cartId, quantity);
            return ResponseEntity.ok("Item removed successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    //increases or decreases the quantity of an item
    @PutMapping("/update/{cartId}/{quantity}")
    public ResponseEntity<String> updateCart(@PathVariable Long cartId, @PathVariable int quantity) {
    	 try {
    	   return ResponseEntity.ok(cartService.updateCartItemQuantity(cartId, quantity));
         } catch (Exception e) {
             return ResponseEntity.badRequest().body(e.getMessage());
         }
    }
    
    //confirms the purchase, updates stock, and clears the cart.
    @PostMapping("/checkout")
    public ResponseEntity<String> checkout() {
    	 try {
    	        return ResponseEntity.ok(cartService.checkout());
           } catch (Exception e) {
               return ResponseEntity.badRequest().body(e.getMessage());
           }
    }
    
    //discount on 1000
    @GetMapping("/discount")
    public ResponseEntity<Map<String, Double>> getDiscounts() {
        try {
            return ResponseEntity.ok(cartService.getCartDiscounts());
        } catch (Exception e) {
            Map<String, Double> errorResponse = new HashMap<>();
            errorResponse.put("error", -1.0); // Using -1.0 to indicate an error
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }

    
    //total number of item
    @GetMapping("/itemcount")
    public ResponseEntity<Integer> getItemCount() {
        try {
            return ResponseEntity.ok(cartService.getCartItemCount());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(-1);
        }
    }
    
   





}
