package org.project1.shopweb.respon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CouponRespon {

    @JsonProperty("result")
    Double result;

    @JsonProperty("message")
    String message;
}
