package org.project1.shopweb.respon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.project1.shopweb.model.Product;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductRespon extends BaseRespon{
    private Long id;
    private String name;
    private Float price;
    private String thumbnail;
    private String description;
    @JsonProperty("category_id")
    private Long categoryId;
    private int totalPages;

    public static ProductRespon changeType (Product product){
        ProductRespon productRespon = ProductRespon.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .thumbnail(product.getThumbnail())
                .description(product.getDescription())
                .categoryId(product.getCategory().getId())
        .build();

        productRespon.setCreatedAt(product.getCreatedAt());
        productRespon.setUpdatedAt(product.getUpdatedAt());
        return productRespon;
    }

}
