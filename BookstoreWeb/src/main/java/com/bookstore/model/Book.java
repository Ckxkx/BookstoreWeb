package com.bookstore.model;

import java.util.Date;

/**
 * 图书实体类
 */
public class Book {
    private int id;
    private String title;
    private String author;
    private String publisher;
    private String isbn;
    private double price;
    private int stock;
    private String category;
    private String description;
    private String coverImage;
    private Date publishDate;
    
    public Book() {
    }
    
    public Book(String title, String author, String publisher, String isbn, double price, int stock, String category) {
        this.title = title;
        this.author = author;
        this.publisher = publisher;
        this.isbn = isbn;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.publishDate = new Date();
    }
    
    // Getters and Setters
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public String getAuthor() {
        return author;
    }
    
    public void setAuthor(String author) {
        this.author = author;
    }
    
    public String getPublisher() {
        return publisher;
    }
    
    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
    
    public String getIsbn() {
        return isbn;
    }
    
    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public int getStock() {
        return stock;
    }
    
    public void setStock(int stock) {
        this.stock = stock;
    }
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getCoverImage() {
        return coverImage;
    }
    
    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }
    
    public Date getPublishDate() {
        return publishDate;
    }
    
    public void setPublishDate(Date publishDate) {
        this.publishDate = publishDate;
    }
    
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", publisher='" + publisher + '\'' +
                ", isbn='" + isbn + '\'' +
                ", price=" + price +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                '}';
    }
}
