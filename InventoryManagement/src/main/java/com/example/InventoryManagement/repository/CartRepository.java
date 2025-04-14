package com.example.InventoryManagement.repository;

import com.example.InventoryManagement.entity.CartItem;
import com.example.InventoryManagement.entity.Product;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<CartItem,Long>
{
	 Optional<CartItem> findByProduct(Product product);
}
