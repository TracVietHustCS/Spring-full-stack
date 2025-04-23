package org.project1.shopweb.respon;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.project1.shopweb.model.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterRespon {
    @JsonProperty("message")
    private String message;

    @JsonProperty("user")
    private User user;

}
