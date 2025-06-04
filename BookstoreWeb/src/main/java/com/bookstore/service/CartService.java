package com.bookstore.service;

import com.bookstore.dao.CartItemDAO;
import com.bookstore.dao.BookDAO;
import com.bookstore.model.CartItem;
import com.bookstore.model.Book;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

/**
 * 购物车服务类，处理购物车相关业务逻辑
 */
public class CartService {
    private CartItemDAO cartItemDAO;
    private BookDAO bookDAO;
    
    public CartService() {
        this.cartItemDAO = new CartItemDAO();
        this.bookDAO = new BookDAO();
    }
    
    /**
     * 添加商品到购物车
     * @param userId 用户ID
     * @param bookId 图书ID
     * @param quantity 数量
     * @return 添加成功返回true，失败返回false
     */
    public boolean addToCart(int userId, int bookId, int quantity) {
        // 检查图书是否存在
        Book book = bookDAO.getBookById(bookId);
        if (book == null || book.getStock() < quantity) {
            return false;
        }
        
        // 检查购物车中是否已存在该图书
        int cartItemId = cartItemDAO.checkBookInCart(userId, bookId);
        
        if (cartItemId > 0) {
            // 已存在，更新数量
            CartItem existingItem = new CartItem();
            existingItem.setId(cartItemId);
            existingItem.setQuantity(quantity);
            return cartItemDAO.updateCartItemQuantity(cartItemId, quantity);
        } else {
            // 不存在，添加新项
            CartItem cartItem = new CartItem(userId, bookId, quantity);
            return cartItemDAO.addCartItem(cartItem) > 0;
        }
    }
    
    /**
     * 获取用户购物车
     * @param userId 用户ID
     * @return 购物车项列表
     */
    public List<CartItem> getCartItems(int userId) {
        return cartItemDAO.getCartItemsByUserId(userId);
    }
    
    /**
     * 更新购物车项数量
     * @param cartItemId 购物车项ID
     * @param quantity 新数量
     * @return 更新成功返回true，失败返回false
     */
    public boolean updateCartItemQuantity(int cartItemId, int quantity) {
        if (quantity <= 0) {
            return cartItemDAO.deleteCartItem(cartItemId);
        }
        return cartItemDAO.updateCartItemQuantity(cartItemId, quantity);
    }
    
    /**
     * 删除购物车项
     * @param cartItemId 购物车项ID
     * @return 删除成功返回true，失败返回false
     */
    public boolean removeCartItem(int cartItemId) {
        return cartItemDAO.deleteCartItem(cartItemId);
    }
    
    /**
     * 清空购物车
     * @param userId 用户ID
     * @return 清空成功返回true，失败返回false
     */
    public boolean clearCart(int userId) {
        return cartItemDAO.clearCart(userId);
    }
    
    /**
     * 获取购物车总金额
     * @param userId 用户ID
     * @return 购物车总金额
     */
    public double getCartTotal(int userId) {
        List<CartItem> cartItems = cartItemDAO.getCartItemsByUserId(userId);
        double total = 0;
        
        for (CartItem item : cartItems) {
            Book book = bookDAO.getBookById(item.getBookId());
            if (book != null) {
                total += book.getPrice() * item.getQuantity();
            }
        }
        
        return total;
    }
}
