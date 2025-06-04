package com.bookstore.controller;

import com.bookstore.model.CartItem;
import com.bookstore.model.Book;
import com.bookstore.model.User;
import com.bookstore.service.CartService;
import com.bookstore.service.BookService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * 购物车控制器，处理购物车相关请求
 */
@WebServlet("/cart/*")
public class CartController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private CartService cartService;
    private BookService bookService;
    
    public CartController() {
        this.cartService = new CartService();
        this.bookService = new BookService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // 显示购物车
            List<CartItem> cartItems = cartService.getCartItems(user.getId());
            request.setAttribute("cartItems", cartItems);
            
            // 获取购物车中每本书的详细信息
            for (CartItem item : cartItems) {
                Book book = bookService.getBookById(item.getBookId());
                request.setAttribute("book_" + item.getBookId(), book);
            }
            
            // 计算总金额
            double total = cartService.getCartTotal(user.getId());
            request.setAttribute("cartTotal", total);
            
            request.getRequestDispatcher("/WEB-INF/views/cart/view.jsp").forward(request, response);
        } else if (pathInfo.equals("/clear")) {
            // 清空购物车
            cartService.clearCart(user.getId());
            response.sendRedirect(request.getContextPath() + "/cart");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
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
            // 添加商品到购物车
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            boolean success = cartService.addToCart(user.getId(), bookId, quantity);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/cart");
            } else {
                request.setAttribute("errorMessage", "添加到购物车失败，可能库存不足");
                response.sendRedirect(request.getContextPath() + "/book/" + bookId);
            }
        } else if (pathInfo.equals("/update")) {
            // 更新购物车项数量
            int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
            int quantity = Integer.parseInt(request.getParameter("quantity"));
            
            boolean success = cartService.updateCartItemQuantity(cartItemId, quantity);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/cart");
            } else {
                request.setAttribute("errorMessage", "更新购物车失败");
                response.sendRedirect(request.getContextPath() + "/cart");
            }
        } else if (pathInfo.equals("/remove")) {
            // 从购物车中移除商品
            int cartItemId = Integer.parseInt(request.getParameter("cartItemId"));
            
            boolean success = cartService.removeCartItem(cartItemId);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/cart");
            } else {
                request.setAttribute("errorMessage", "从购物车移除商品失败");
                response.sendRedirect(request.getContextPath() + "/cart");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
