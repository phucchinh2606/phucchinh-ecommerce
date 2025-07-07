package com.phucchinh.ecommerce.serivce.interf;

import com.phucchinh.ecommerce.dto.CategoryDto;
import com.phucchinh.ecommerce.dto.Response;

public interface CategoryService {
    Response createCategory(CategoryDto categoryRequest);
    Response updateCategory(Long categoryId, CategoryDto categoryRequest);
    Response getAllCategories();
    Response getCategoryById(Long categoryId);
    Response deleteCategory(Long categoryId);
}
