package com.bookstore.model;

import java.util.Date;

/**
 * 订单实体类
 */
public class Order {
    private int id;
    private int userId;
    private double totalAmount;
    private String shippingAddress;
    private String status; // 待付款、已付款、已发货、已完成、已取消
    private Date orderTime;
    private Date paymentTime;
    private Date shippingTime;
    private Date completionTime;
    
    public Order() {
    }
    
    public Order(int userId, double totalAmount, String shippingAddress) {
        this.userId = userId;
        this.totalAmount = totalAmount;
        this.shippingAddress = shippingAddress;
        this.status = "待付款";
        this.orderTime = new Date();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public int getUserId() {
        return userId;
    }
    
    public void setUserId(int userId) {
        this.userId = userId;
    }
    
    public double getTotalAmount() {
        return totalAmount;
    }
    
    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }
    
    public String getShippingAddress() {
        return shippingAddress;
    }
    
    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Date getOrderTime() {
        return orderTime;
    }
    
    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }
    
    public Date getPaymentTime() {
        return paymentTime;
    }
    
    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }
    
    public Date getShippingTime() {
        return shippingTime;
    }
    
    public void setShippingTime(Date shippingTime) {
        this.shippingTime = shippingTime;
    }
    
    public Date getCompletionTime() {
        return completionTime;
    }
    
    public void setCompletionTime(Date completionTime) {
        this.completionTime = completionTime;
    }
    
    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", userId=" + userId +
                ", totalAmount=" + totalAmount +
                ", shippingAddress='" + shippingAddress + '\'' +
                ", status='" + status + '\'' +
                ", orderTime=" + orderTime +
                '}';
    }
}
