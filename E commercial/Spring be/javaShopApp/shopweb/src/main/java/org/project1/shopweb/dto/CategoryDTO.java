package org.project1.shopweb.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CategoryDTO {
    @NotEmpty(message = "bro wtf, what is your name")
    private String name;
}
