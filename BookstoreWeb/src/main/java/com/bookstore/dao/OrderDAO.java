package com.bookstore.dao;

import com.bookstore.model.Order;
import com.bookstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单数据访问对象
 */
public class OrderDAO {
    
    /**
     * 创建订单
     * @param order 订单对象
     * @return 新创建订单的ID，失败返回-1
     */
    public int createOrder(Order order) {
        String sql = "INSERT INTO orders (user_id, total_amount, shipping_address, status, order_time) " +
                     "VALUES (?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int orderId = -1;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setInt(1, order.getUserId());
            pstmt.setDouble(2, order.getTotalAmount());
            pstmt.setString(3, order.getShippingAddress());
            pstmt.setString(4, order.getStatus());
            pstmt.setTimestamp(5, new Timestamp(order.getOrderTime().getTime()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    orderId = rs.getInt(1);
                    order.setId(orderId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return orderId;
    }
    
    /**
     * 根据ID获取订单
     * @param orderId 订单ID
     * @return 订单对象，未找到返回null
     */
    public Order getOrderById(int orderId) {
        String sql = "SELECT * FROM orders WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Order order = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, orderId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                order = mapResultSetToOrder(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return order;
    }
    
    /**
     * 获取用户的所有订单
     * @param userId 用户ID
     * @return 订单列表
     */
    public List<Order> getOrdersByUserId(int userId) {
        String sql = "SELECT * FROM orders WHERE user_id = ? ORDER BY order_time DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Order> orders = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return orders;
    }
    
    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 新状态
     * @return 是否更新成功
     */
    public boolean updateOrderStatus(int orderId, String status) {
        String sql = "UPDATE orders SET status = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, status);
            pstmt.setInt(2, orderId);
            
            int affectedRows = pstmt.executeUpdate();
            success = (affectedRows > 0);
            
            // 根据状态更新相应的时间字段
            if (success) {
                updateOrderTimeByStatus(orderId, status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
        
        return success;
    }
    
    /**
     * 根据状态更新订单时间字段
     * @param orderId 订单ID
     * @param status 订单状态
     */
    private void updateOrderTimeByStatus(int orderId, String status) {
        String sql = null;
        
        switch (status) {
            case "已付款":
                sql = "UPDATE orders SET payment_time = ? WHERE id = ?";
                break;
            case "已发货":
                sql = "UPDATE orders SET shipping_time = ? WHERE id = ?";
                break;
            case "已完成":
                sql = "UPDATE orders SET completion_time = ? WHERE id = ?";
                break;
            default:
                return;
        }
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
            pstmt.setInt(2, orderId);
            
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, null);
        }
    }
    
    /**
     * 获取所有订单
     * @return 订单列表
     */
    public List<Order> getAllOrders() {
        String sql = "SELECT * FROM orders ORDER BY order_time DESC";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Order> orders = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Order order = mapResultSetToOrder(rs);
                orders.add(order);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return orders;
    }
    
    /**
     * 删除订单
     * @param orderId 订单ID
     * @return 是否删除成功
     */
    public boolean deleteOrder(int orderId) {
        String sql = "DELETE FROM orders WHERE id = ?";
        
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
     * 将ResultSet映射为Order对象
     * @param rs ResultSet结果集
     * @return Order对象
     * @throws SQLException SQL异常
     */
    private Order mapResultSetToOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getInt("id"));
        order.setUserId(rs.getInt("user_id"));
        order.setTotalAmount(rs.getDouble("total_amount"));
        order.setShippingAddress(rs.getString("shipping_address"));
        order.setStatus(rs.getString("status"));
        order.setOrderTime(rs.getTimestamp("order_time"));
        
        Timestamp paymentTime = rs.getTimestamp("payment_time");
        if (paymentTime != null) {
            order.setPaymentTime(paymentTime);
        }
        
        Timestamp shippingTime = rs.getTimestamp("shipping_time");
        if (shippingTime != null) {
            order.setShippingTime(shippingTime);
        }
        
        Timestamp completionTime = rs.getTimestamp("completion_time");
        if (completionTime != null) {
            order.setCompletionTime(completionTime);
        }
        
        return order;
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
