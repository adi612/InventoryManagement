package com.example.InventoryManagement.repository;

import com.example.InventoryManagement.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByDeletedFalse();

    Optional<Product> findByProductIdAndDeletedFalse(Long productId);
}
