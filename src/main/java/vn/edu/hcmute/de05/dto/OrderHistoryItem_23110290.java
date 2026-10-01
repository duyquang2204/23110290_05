package vn.edu.hcmute.de05.dto;

import java.math.BigDecimal;

public class OrderHistoryItem_23110290 {
    private final String productName;
    private final BigDecimal unitPrice;
    private final Integer quantity;

    public OrderHistoryItem_23110290(String productName, BigDecimal unitPrice, Integer quantity) {
        this.productName = productName;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
    }

    public String getProductName() { return productName; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public Integer getQuantity() { return quantity; }
    public BigDecimal getLineTotal() { return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
}
