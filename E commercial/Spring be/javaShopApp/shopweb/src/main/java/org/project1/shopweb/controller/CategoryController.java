package org.project1.shopweb.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.project1.shopweb.component.LocalizationUtils;
import org.project1.shopweb.component.convert.CategoryMessageConvert;
import org.project1.shopweb.dto.CategoryDTO;
import org.project1.shopweb.model.Category;
import org.project1.shopweb.service.CategoryService;
import org.project1.shopweb.respon.CategoryRespon;
import org.project1.shopweb.utils.MessageKeys;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("${api.prefix}/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final LocalizationUtils localizationUtils;
    private final KafkaTemplate<String, Object> kafkaTemplate;

    @GetMapping("/all")
    public ResponseEntity<?> getAllCategories(){
        List<Category> categoryList = categoryService.getAllCategories();
        this.kafkaTemplate.send("get-all-categories", categoryList);
        return ResponseEntity.ok(categoryList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getCategory(@PathVariable long id){
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
    @PostMapping("")
    public ResponseEntity<CategoryRespon> insertCategories(@Valid @RequestBody CategoryDTO categoryDto){
        try{
        Category category = categoryService.creatCategory(categoryDto);
            this.kafkaTemplate.send("insert-a-category", category);
            this.kafkaTemplate.setMessageConverter(new CategoryMessageConvert());

        return ResponseEntity.ok(CategoryRespon.builder()
                .message(localizationUtils.getLocalizationMessages(MessageKeys.CATEGORY_INSERT)).build());
    }catch (Exception e){
            return ResponseEntity.badRequest().body(
                    CategoryRespon.builder().message(localizationUtils.getLocalizationMessages(MessageKeys.CATEGORY_CREAT_FAIL,e.getMessage())).
                            build()
            );
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<CategoryRespon> updateCategories(@PathVariable long id,@Valid @RequestBody CategoryDTO categoryDTO)

    {
        categoryService.updateCategory(id,categoryDTO);
        return ResponseEntity.ok(CategoryRespon.builder().message(
                localizationUtils.getLocalizationMessages(MessageKeys.CATEGORY_UPDATE)
        ).build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryRespon> deleteCategories(@PathVariable Long id){

        categoryService.deleteCategory(id);
        return ResponseEntity.ok(CategoryRespon.builder().message(
                localizationUtils.getLocalizationMessages(MessageKeys.CATEGPRY_DELETE,id)
        ).build());
    }


}
