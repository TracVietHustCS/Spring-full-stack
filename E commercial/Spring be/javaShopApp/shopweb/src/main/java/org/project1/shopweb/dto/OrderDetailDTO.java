package org.project1.shopweb.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderDetailDTO {
    @JsonProperty("order_id")
    @Min(value = 1, message = "id has to bigger than 0")
    private Long orderId;

    @Min(value = 1, message = "id has to bigger than 0")
    @JsonProperty("product_id")
    private Long productId;

    @Min(value = 0, message = "price has to bigger than 0")
    private float price;

    @Min(value = 1, message = "amount of product has to bigger than 0")
    @JsonProperty("number_of_product")
    private int numberOfProduct;

    @Min(value = 0, message = "total money has to bigger than 0")
    @JsonProperty("total_money")
    private float totalMoney;

    private String color;


}
