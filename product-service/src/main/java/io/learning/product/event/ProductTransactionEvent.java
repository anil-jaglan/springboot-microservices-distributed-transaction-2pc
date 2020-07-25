package io.learning.product.event;

import io.learning.core.domain.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductTransactionEvent {

    private String transactionId;

    private Product product;

}
