package vn.edu.hcmute.de05.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class OrderHistory_23110290 {
    private final Integer orderId;
    private final String orderCode;
    private final String customerName;
    private final String phone;
    private final String shippingAddress;
    private final String paymentMethod;
    private final String orderStatus;
    private final String statusLabel;
    private final BigDecimal total;
    private final Date createdAt;
    private final List<OrderHistoryItem_23110290> items;

    public OrderHistory_23110290(Integer orderId, String orderCode, String customerName,
            String phone, String shippingAddress, String paymentMethod,
            String orderStatus, String statusLabel, BigDecimal total, Date createdAt,
            List<OrderHistoryItem_23110290> items) {
        this.orderId = orderId;
        this.orderCode = orderCode;
        this.customerName = customerName;
        this.phone = phone;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.orderStatus = orderStatus;
        this.statusLabel = statusLabel;
        this.total = total;
        this.createdAt = createdAt;
        this.items = items;
    }

    public Integer getOrderId() { return orderId; }
    public String getOrderCode() { return orderCode; }
    public String getCustomerName() { return customerName; }
    public String getPhone() { return phone; }
    public String getShippingAddress() { return shippingAddress; }
    public String getPaymentMethod() { return paymentMethod; }
    public String getOrderStatus() { return orderStatus; }
    public String getStatusLabel() { return statusLabel; }
    public BigDecimal getTotal() { return total; }
    public Date getCreatedAt() { return createdAt; }
    public List<OrderHistoryItem_23110290> getItems() { return items; }
}
