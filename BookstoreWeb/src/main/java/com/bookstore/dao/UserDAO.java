package com.bookstore.dao;

import com.bookstore.model.User;
import com.bookstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户数据访问对象
 */
public class UserDAO {
    
    /**
     * 添加用户
     * @param user 用户对象
     * @return 新增用户的ID，失败返回-1
     */
    public int addUser(User user) {
        String sql = "INSERT INTO users (username, password, email, phone, address, register_time, role) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int userId = -1;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getAddress());
            pstmt.setTimestamp(6, new Timestamp(user.getRegisterTime().getTime()));
            pstmt.setInt(7, user.getRole());
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    userId = rs.getInt(1);
                    user.setId(userId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return userId;
    }
    
    /**
     * 根据ID获取用户
     * @param userId 用户ID
     * @return 用户对象，未找到返回null
     */
    public User getUserById(int userId) {
        String sql = "SELECT * FROM users WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return user;
    }
    
    /**
     * 根据用户名获取用户
     * @param username 用户名
     * @return 用户对象，未找到返回null
     */
    public User getUserByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return user;
    }
    
    /**
     * 验证用户登录
     * @param username 用户名
     * @param password 密码
     * @return 用户对象，验证失败返回null
     */
    public User validateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        User user = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                user = mapResultSetToUser(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return user;
    }
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 是否更新成功
     */
    public boolean updateUser(User user) {
        String sql = "UPDATE users SET username = ?, password = ?, email = ?, " +
                     "phone = ?, address = ?, role = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getEmail());
            pstmt.setString(4, user.getPhone());
            pstmt.setString(5, user.getAddress());
            pstmt.setInt(6, user.getRole());
            pstmt.setInt(7, user.getId());
            
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
     * 删除用户
     * @param userId 用户ID
     * @return 是否删除成功
     */
    public boolean deleteUser(int userId) {
        String sql = "DELETE FROM users WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            
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
     * 获取所有用户
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        String sql = "SELECT * FROM users";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                User user = mapResultSetToUser(rs);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return users;
    }
    
    /**
     * 将ResultSet映射为User对象
     * @param rs ResultSet结果集
     * @return User对象
     * @throws SQLException SQL异常
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setAddress(rs.getString("address"));
        user.setRegisterTime(rs.getTimestamp("register_time"));
        user.setRole(rs.getInt("role"));
        return user;
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
