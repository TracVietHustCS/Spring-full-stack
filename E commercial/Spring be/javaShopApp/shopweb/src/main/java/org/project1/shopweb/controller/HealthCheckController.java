package org.project1.shopweb.controller;

import lombok.RequiredArgsConstructor;
import org.project1.shopweb.model.Category;
import org.project1.shopweb.service.CategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/healthcheck")
public class HealthCheckController {
    private final CategoryService categoryService;

    @GetMapping("health")
    public ResponseEntity<?> getHealth(){

            List<Category> categoryList = categoryService.getAllCategories();
            return ResponseEntity.ok("ok");


    }



}
