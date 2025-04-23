package org.project1.shopweb.service;

import org.project1.shopweb.dto.CategoryDTO;
import org.project1.shopweb.model.Category;

import java.util.List;

public interface ICategoryService {
    List<Category> getAllCategories();

    Category getCategoryById(long id);

    Category creatCategory(CategoryDTO categoryDTO);

    Category updateCategory(Long id, CategoryDTO categoryDTO);

    void deleteCategory(Long id);




}
