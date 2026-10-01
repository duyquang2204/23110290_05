package vn.edu.hcmute.de05.dto;

import java.math.BigDecimal;

public class CartLine_23110290 {
    private final Integer cartItemId;
    private final Integer productId;
    private final String productName;
    private final String image;
    private final BigDecimal unitPrice;
    private final Integer quantity;
    private final Integer stock;

    public CartLine_23110290(Integer cartItemId, Integer productId,
            String productName, String image, BigDecimal unitPrice,
            Integer quantity, Integer stock) {
        this.cartItemId = cartItemId;
        this.productId = productId;
        this.productName = productName;
        this.image = image;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.stock = stock;
    }

    public Integer getCartItemId() { return cartItemId; }
    public Integer getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getImage() { return image; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public Integer getQuantity() { return quantity; }
    public Integer getStock() { return stock; }
    public BigDecimal getLineTotal() { return unitPrice.multiply(BigDecimal.valueOf(quantity)); }
}
