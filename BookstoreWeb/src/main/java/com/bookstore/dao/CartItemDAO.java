package com.bookstore.dao;

import com.bookstore.model.CartItem;
import com.bookstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 购物车项数据访问对象
 */
public class CartItemDAO {
    
    /**
     * 添加购物车项
     * @param cartItem 购物车项对象
     * @return 新增购物车项的ID，失败返回-1
     */
    public int addCartItem(CartItem cartItem) {
        String sql = "INSERT INTO cart_items (user_id, book_id, quantity, add_time) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int cartItemId = -1;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, cartItem.getUserId());
            pstmt.setInt(2, cartItem.getBookId());
            pstmt.setInt(3, cartItem.getQuantity());
            pstmt.setTimestamp(4, new Timestamp(cartItem.getAddTime().getTime()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    cartItemId = rs.getInt(1);
                    cartItem.setId(cartItemId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return cartItemId;
    }
    
    /**
     * 获取用户的购物车项
     * @param userId 用户ID
     * @return 购物车项列表
     */
    public List<CartItem> getCartItemsByUserId(int userId) {
        String sql = "SELECT * FROM cart_items WHERE user_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<CartItem> cartItems = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                CartItem cartItem = mapResultSetToCartItem(rs);
                cartItems.add(cartItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return cartItems;
    }
    
    /**
     * 更新购物车项数量
     * @param cartItemId 购物车项ID
     * @param quantity 新数量
     * @return 是否更新成功
     */
    public boolean updateCartItemQuantity(int cartItemId, int quantity) {
        String sql = "UPDATE cart_items SET quantity = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, cartItemId);
            
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
     * 删除购物车项
     * @param cartItemId 购物车项ID
     * @return 是否删除成功
     */
    public boolean deleteCartItem(int cartItemId) {
        String sql = "DELETE FROM cart_items WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, cartItemId);
            
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
     * 清空用户购物车
     * @param userId 用户ID
     * @return 是否清空成功
     */
    public boolean clearCart(int userId) {
        String sql = "DELETE FROM cart_items WHERE user_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows >= 0); // 即使没有项目被删除也视为成功
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        
        return success;
    }
    
    /**
     * 检查购物车中是否已存在相同图书
     * @param userId 用户ID
     * @param bookId 图书ID
     * @return 如果存在返回购物车项ID，否则返回-1
     */
    public int checkBookInCart(int userId, int bookId) {
        String sql = "SELECT id FROM cart_items WHERE user_id = ? AND book_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int cartItemId = -1;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, bookId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                cartItemId = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return cartItemId;
    }
    
    /**
     * 将ResultSet映射为CartItem对象
     * @param rs ResultSet结果集
     * @return CartItem对象
     * @throws SQLException SQL异常
     */
    private CartItem mapResultSetToCartItem(ResultSet rs) throws SQLException {
        CartItem cartItem = new CartItem();
        cartItem.setId(rs.getInt("id"));
        cartItem.setUserId(rs.getInt("user_id"));
        cartItem.setBookId(rs.getInt("book_id"));
        cartItem.setQuantity(rs.getInt("quantity"));
        cartItem.setAddTime(rs.getTimestamp("add_time"));
        return cartItem;
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
