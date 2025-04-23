package org.project1.shopweb.service;

import lombok.RequiredArgsConstructor;
import org.project1.shopweb.dto.CategoryDTO;
import org.project1.shopweb.model.Category;
import org.project1.shopweb.repository.CategoryRepository;
import org.project1.shopweb.exception.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;
    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategoryById(long id) {
        return categoryRepository.findById(id).orElseThrow(() -> new RuntimeException("do not exist this"));
    }

    @Override
        public Category creatCategory(CategoryDTO categoryDTO) {
        if(categoryRepository.existsByName(categoryDTO.getName())){
            throw new NotFoundException("this category is already exist");
        }
        Category newCategory = Category
                .builder()
                .name(categoryDTO.getName())
                .build();

        return categoryRepository.save(newCategory);
    }

    @Override
    public Category updateCategory(Long id, CategoryDTO categoryDTO) {
        Category fromdb = getCategoryById(id);
        fromdb.setName(categoryDTO.getName());
        categoryRepository.save(fromdb);
        return fromdb;
    }

    @Override
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);

    }
}
