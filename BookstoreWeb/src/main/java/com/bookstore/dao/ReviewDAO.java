package com.bookstore.dao;

import com.bookstore.model.Review;
import com.bookstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 评论数据访问对象
 */
public class ReviewDAO {
    
    /**
     * 添加评论
     * @param review 评论对象
     * @return 新增评论的ID，失败返回-1
     */
    public int addReview(Review review) {
        String sql = "INSERT INTO reviews (user_id, book_id, rating, comment, review_time) VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int reviewId = -1;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, review.getUserId());
            pstmt.setInt(2, review.getBookId());
            pstmt.setInt(3, review.getRating());
            pstmt.setString(4, review.getComment());
            pstmt.setTimestamp(5, new Timestamp(review.getReviewTime().getTime()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    reviewId = rs.getInt(1);
                    review.setId(reviewId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return reviewId;
    }
    
    /**
     * 根据图书ID获取评论
     * @param bookId 图书ID
     * @return 评论列表
     */
    public List<Review> getReviewsByBookId(int bookId) {
        String sql = "SELECT * FROM reviews WHERE book_id = ? ORDER BY review_time DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Review> reviews = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Review review = mapResultSetToReview(rs);
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return reviews;
    }
    
    /**
     * 根据用户ID获取评论
     * @param userId 用户ID
     * @return 评论列表
     */
    public List<Review> getReviewsByUserId(int userId) {
        String sql = "SELECT * FROM reviews WHERE user_id = ? ORDER BY review_time DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Review> reviews = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Review review = mapResultSetToReview(rs);
                reviews.add(review);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return reviews;
    }
    
    /**
     * 删除评论
     * @param reviewId 评论ID
     * @return 是否删除成功
     */
    public boolean deleteReview(int reviewId) {
        String sql = "DELETE FROM reviews WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reviewId);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        
        return success;
    }
    
    /**
     * 获取图书的平均评分
     * @param bookId 图书ID
     * @return 平均评分
     */
    public double getAverageRatingByBookId(int bookId) {
        String sql = "SELECT AVG(rating) as avg_rating FROM reviews WHERE book_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        double avgRating = 0.0;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                avgRating = rs.getDouble("avg_rating");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return avgRating;
    }
    
    /**
     * 将ResultSet映射为Review对象
     * @param rs ResultSet结果集
     * @return Review对象
     * @throws SQLException SQL异常
     */
    private Review mapResultSetToReview(ResultSet rs) throws SQLException {
        Review review = new Review();
        review.setId(rs.getInt("id"));
        review.setUserId(rs.getInt("user_id"));
        review.setBookId(rs.getInt("book_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));
        review.setReviewTime(rs.getTimestamp("review_time"));
        return review;
    }
    
    /**
     * 关闭数据库资源
     * @param conn 数据库连接
     * @param pstmt PreparedStatement对象
     * @param rs ResultSet结果集
     */
    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (pstmt != null) {
                pstmt.close();
            }
            if (conn != null) {
                DBConnection.closeConnection(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
