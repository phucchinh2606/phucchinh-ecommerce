package com.phucchinh.ecommerce.repository;

import com.phucchinh.ecommerce.entity.Order;
import com.phucchinh.ecommerce.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {
    List<Product> findByCategoryId(Long categoryId);
    List<Product> findByNameContainingOrDescriptionContaining(String name,String description);
}
