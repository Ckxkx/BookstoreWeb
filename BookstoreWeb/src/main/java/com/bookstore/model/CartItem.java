package com.bookstore.model;

import java.util.Date;

/**
 * 购物车项实体类
 */
public class CartItem {
    private int id;
    private int userId;
    private int bookId;
    private int quantity;
    private Date addTime;
    
    public CartItem() {
    }
    
    public CartItem(int userId, int bookId, int quantity) {
        this.userId = userId;
        this.bookId = bookId;
        this.quantity = quantity;
        this.addTime = new Date();
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
    
    public int getBookId() {
        return bookId;
    }
    
    public void setBookId(int bookId) {
        this.bookId = bookId;
    }
    
    public int getQuantity() {
        return quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    
    public Date getAddTime() {
        return addTime;
    }
    
    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }
    
    @Override
    public String toString() {
        return "CartItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", quantity=" + quantity +
                ", addTime=" + addTime +
                '}';
    }
}
