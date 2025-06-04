package com.bookstore.dao;

import com.bookstore.model.OrderItem;
import com.bookstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单项数据访问对象
 */
public class OrderItemDAO {
    
    /**
     * 添加订单项
     * @param orderItem 订单项对象
     * @return 新增订单项的ID，失败返回-1
     */
    public int addOrderItem(OrderItem orderItem) {
        String sql = "INSERT INTO order_items (order_id, book_id, quantity, price) VALUES (?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int orderItemId = -1;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, orderItem.getOrderId());
            pstmt.setInt(2, orderItem.getBookId());
            pstmt.setInt(3, orderItem.getQuantity());
            pstmt.setDouble(4, orderItem.getPrice());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    orderItemId = rs.getInt(1);
                    orderItem.setId(orderItemId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return orderItemId;
    }
    
    /**
     * 根据订单ID获取所有订单项
     * @param orderId 订单ID
     * @return 订单项列表
     */
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        String sql = "SELECT * FROM order_items WHERE order_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<OrderItem> orderItems = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                OrderItem orderItem = mapResultSetToOrderItem(rs);
                orderItems.add(orderItem);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return orderItems;
    }
    
    /**
     * 删除订单项
     * @param orderItemId 订单项ID
     * @return 是否删除成功
     */
    public boolean deleteOrderItem(int orderItemId) {
        String sql = "DELETE FROM order_items WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderItemId);
            
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
     * 删除订单的所有订单项
     * @param orderId 订单ID
     * @return 是否删除成功
     */
    public boolean deleteOrderItemsByOrderId(int orderId) {
        String sql = "DELETE FROM order_items WHERE order_id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            
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
     * 将ResultSet映射为OrderItem对象
     * @param rs ResultSet结果集
     * @return OrderItem对象
     * @throws SQLException SQL异常
     */
    private OrderItem mapResultSetToOrderItem(ResultSet rs) throws SQLException {
        OrderItem orderItem = new OrderItem();
        orderItem.setId(rs.getInt("id"));
        orderItem.setOrderId(rs.getInt("order_id"));
        orderItem.setBookId(rs.getInt("book_id"));
        orderItem.setQuantity(rs.getInt("quantity"));
        orderItem.setPrice(rs.getDouble("price"));
        return orderItem;
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
