package com.phucchinh.ecommerce.repository;

import com.phucchinh.ecommerce.entity.Address;
import com.phucchinh.ecommerce.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category,Long> {
}
