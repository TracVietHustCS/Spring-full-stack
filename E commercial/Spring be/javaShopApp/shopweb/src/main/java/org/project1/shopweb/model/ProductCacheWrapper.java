package org.project1.shopweb.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.project1.shopweb.respon.ProductRespon;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCacheWrapper {

    private List<ProductRespon> products;
    private int totalPages;
}
