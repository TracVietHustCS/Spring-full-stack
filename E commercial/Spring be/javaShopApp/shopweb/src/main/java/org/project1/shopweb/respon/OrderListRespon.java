package org.project1.shopweb.respon;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderListRespon {


    private List<OrderRespon> orders;
    private int totalPages;
}
