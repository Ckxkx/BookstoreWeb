package com.bookstore.controller;

import com.bookstore.model.Review;
import com.bookstore.model.User;
import com.bookstore.service.ReviewService;
import com.bookstore.service.BookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;

/**
 * 评论控制器，处理评论相关请求
 */
@WebServlet("/review/*")
public class ReviewController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private ReviewService reviewService;
    private BookService bookService;
    
    public ReviewController() {
        this.reviewService = new ReviewService();
        this.bookService = new BookService();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }
        
        if (pathInfo.equals("/add")) {
            // 添加评论
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            int rating = Integer.parseInt(request.getParameter("rating"));
            String comment = request.getParameter("comment");
            
            Review review = new Review(user.getId(), bookId, rating, comment);
            int reviewId = reviewService.addReview(review);
            
            if (reviewId > 0) {
                response.sendRedirect(request.getContextPath() + "/book/" + bookId);
            } else {
                request.setAttribute("errorMessage", "添加评论失败");
                response.sendRedirect(request.getContextPath() + "/book/" + bookId);
            }
        } else if (pathInfo.equals("/delete")) {
            // 删除评论
            int reviewId = Integer.parseInt(request.getParameter("reviewId"));
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            
            boolean success = reviewService.deleteReview(reviewId);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/book/" + bookId);
            } else {
                request.setAttribute("errorMessage", "删除评论失败");
                response.sendRedirect(request.getContextPath() + "/book/" + bookId);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
