package com.usk.dto;

import java.time.LocalDate;
import java.util.List;

public class OrderEvent {
    public Long orderId;
    public Long userId;
    public Double totalAmount;
    public LocalDate orderDate;
    public LocalDate expectedDeliveryDate;
    public String paymentStatus;
    public String orderStatus;
    public List<OrderProductDto> products;

}
