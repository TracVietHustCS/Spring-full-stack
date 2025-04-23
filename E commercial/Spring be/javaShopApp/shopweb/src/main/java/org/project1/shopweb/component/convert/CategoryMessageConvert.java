package org.project1.shopweb.component.convert;

import org.project1.shopweb.model.Category;
import org.springframework.kafka.support.converter.JsonMessageConverter;
import org.springframework.kafka.support.mapping.DefaultJackson2JavaTypeMapper;
import org.springframework.kafka.support.mapping.Jackson2JavaTypeMapper;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class CategoryMessageConvert extends JsonMessageConverter {
    public CategoryMessageConvert() {
        super();
        DefaultJackson2JavaTypeMapper typeMapper = new DefaultJackson2JavaTypeMapper();
        typeMapper.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.TYPE_ID);
        typeMapper.addTrustedPackages("org.project1.shopweb");
        typeMapper.setIdClassMapping(Collections.singletonMap("category", Category.class)); // chỉ phục vụ cho phần deserialization
        this.setTypeMapper(typeMapper);
    }

}
