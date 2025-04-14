package com.example.InventoryManagement.controller;

import com.example.InventoryManagement.entity.Product;
import com.example.InventoryManagement.exception.ResourceNotFoundException;
import com.example.InventoryManagement.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {
  private final ProductService productService;

  public ProductController(ProductService productService) {
        this.productService = productService;
  }

  @PostMapping("/addproduct")
  public ResponseEntity<?> addProduct(@Valid @RequestBody Product product){
    try{
        return ResponseEntity.ok(productService.addProduct(product));
     }catch (Exception e){
           return ResponseEntity.badRequest().body("Error adding product: " + e.getMessage());
    }
  }

  @GetMapping("/getallproduct")
  public ResponseEntity<List<Product>> getAllProduct(){
    return ResponseEntity.ok(productService.getAllProducts());
  }

  @GetMapping("/get/{productId}")
  public ResponseEntity<?> getProductById(@PathVariable Long productId) {
    try {
      return ResponseEntity.ok(productService.getProductById(productId));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @PutMapping("/updateproduct/{productId}")
  public ResponseEntity<?> updateProduct(@PathVariable Long productId, @Valid @RequestBody Product updatedProduct) {
    try {
      return ResponseEntity.ok(productService.updateProduct(productId, updatedProduct));
    } catch (ResourceNotFoundException e) {
      return ResponseEntity.badRequest().body(e.getMessage());
    }
  }

  @DeleteMapping("/delete/{productId}")
  public ResponseEntity<String> softDeleteProduct(@PathVariable Long productId) {
      try {
          return ResponseEntity.ok(productService.softDeleteProduct(productId));
      } catch (Exception e) {
          return ResponseEntity.badRequest().body("Error: " + e.getMessage());
      }
  }

  
//  public ResponseEntity<String> deleteProduct(@PathVariable Long productId) {
//    try {
//      productService.deleteProduct(productId);
//      return ResponseEntity.ok("Product deleted successfully.");
//    } catch (ResourceNotFoundException e) {
//      return ResponseEntity.badRequest().body(e.getMessage());
//    }
//  }
  
  
  //check 
  @GetMapping("/checkstock/{productId}")
  public ResponseEntity<Integer> checkStock(@PathVariable Long productId) {
      try {
          return ResponseEntity.ok(productService.checkStock(productId));
      } catch (Exception e) {
          return ResponseEntity.badRequest().body(-1); // Returning -1 in case of an error
      }
  }




}
