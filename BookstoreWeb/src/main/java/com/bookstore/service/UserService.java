package com.bookstore.service;

import com.bookstore.dao.UserDAO;
import com.bookstore.model.User;

import java.util.List;

/**
 * 用户服务类，处理用户相关业务逻辑
 */
public class UserService {
    private UserDAO userDAO;
    
    public UserService() {
        this.userDAO = new UserDAO();
    }
    
    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param email 邮箱
     * @return 注册成功返回用户对象，失败返回null
     */
    public User register(String username, String password, String email) {
        // 检查用户名是否已存在
        if (userDAO.getUserByUsername(username) != null) {
            return null;
        }
        
        User user = new User(username, password, email);
        int userId = userDAO.addUser(user);
        
        if (userId > 0) {
            return user;
        }
        
        return null;
    }
    
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户对象，失败返回null
     */
    public User login(String username, String password) {
        return userDAO.validateUser(username, password);
    }
    
    /**
     * 更新用户信息
     * @param user 用户对象
     * @return 更新成功返回true，失败返回false
     */
    public boolean updateUser(User user) {
        return userDAO.updateUser(user);
    }
    
    /**
     * 获取用户信息
     * @param userId 用户ID
     * @return 用户对象
     */
    public User getUserById(int userId) {
        return userDAO.getUserById(userId);
    }
    
    /**
     * 获取所有用户
     * @return 用户列表
     */
    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }
    
    /**
     * 删除用户
     * @param userId 用户ID
     * @return 删除成功返回true，失败返回false
     */
    public boolean deleteUser(int userId) {
        return userDAO.deleteUser(userId);
    }
}
