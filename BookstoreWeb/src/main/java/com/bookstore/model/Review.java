package com.bookstore.model;

import java.util.Date;

/**
 * 评论实体类
 */
public class Review {
    private int id;
    private int userId;
    private int bookId;
    private int rating;
    private String comment;
    private Date reviewTime;
    
    public Review() {
    }
    
    public Review(int userId, int bookId, int rating, String comment) {
        this.userId = userId;
        this.bookId = bookId;
        this.rating = rating;
        this.comment = comment;
        this.reviewTime = new Date();
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
    
    public int getRating() {
        return rating;
    }
    
    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public Date getReviewTime() {
        return reviewTime;
    }
    
    public void setReviewTime(Date reviewTime) {
        this.reviewTime = reviewTime;
    }
    
    @Override
    public String toString() {
        return "Review{" +
                "id=" + id +
                ", userId=" + userId +
                ", bookId=" + bookId +
                ", rating=" + rating +
                ", comment='" + comment + '\'' +
                ", reviewTime=" + reviewTime +
                '}';
    }
}
