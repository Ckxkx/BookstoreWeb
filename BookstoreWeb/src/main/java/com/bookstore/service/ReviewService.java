package com.bookstore.service;

import com.bookstore.dao.ReviewDAO;
import com.bookstore.model.Review;

import java.util.List;

/**
 * 评论服务类，处理评论相关业务逻辑
 */
public class ReviewService {
    private ReviewDAO reviewDAO;
    
    public ReviewService() {
        this.reviewDAO = new ReviewDAO();
    }
    
    /**
     * 添加评论
     * @param review 评论对象
     * @return 添加成功返回评论ID，失败返回-1
     */
    public int addReview(Review review) {
        return reviewDAO.addReview(review);
    }
    
    /**
     * 根据图书ID获取评论
     * @param bookId 图书ID
     * @return 评论列表
     */
    public List<Review> getReviewsByBookId(int bookId) {
        return reviewDAO.getReviewsByBookId(bookId);
    }
    
    /**
     * 根据用户ID获取评论
     * @param userId 用户ID
     * @return 评论列表
     */
    public List<Review> getReviewsByUserId(int userId) {
        return reviewDAO.getReviewsByUserId(userId);
    }
    
    /**
     * 删除评论
     * @param reviewId 评论ID
     * @return 删除成功返回true，失败返回false
     */
    public boolean deleteReview(int reviewId) {
        return reviewDAO.deleteReview(reviewId);
    }
    
    /**
     * 获取图书的平均评分
     * @param bookId 图书ID
     * @return 平均评分
     */
    public double getAverageRatingByBookId(int bookId) {
        return reviewDAO.getAverageRatingByBookId(bookId);
    }
}
