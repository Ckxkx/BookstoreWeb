package com.bookstore.controller;

import com.bookstore.model.User;
import com.bookstore.service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 用户控制器，处理用户相关请求
 */
@WebServlet("/user/*")
public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    
    public UserController() {
        this.userService = new UserService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else if (pathInfo.equals("/login")) {
            request.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(request, response);
        } else if (pathInfo.equals("/register")) {
            request.getRequestDispatcher("/WEB-INF/views/user/register.jsp").forward(request, response);
        } else if (pathInfo.equals("/profile")) {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/user/login");
            } else {
                request.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(request, response);
            }
        } else if (pathInfo.equals("/logout")) {
            HttpSession session = request.getSession();
            session.invalidate();
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo.equals("/login")) {
            loginUser(request, response);
        } else if (pathInfo.equals("/register")) {
            registerUser(request, response);
        } else if (pathInfo.equals("/update")) {
            updateUser(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private void loginUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        User user = userService.login(username, password);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            
            // Check if user is admin
            if (user.getRole() == 1) {
                response.sendRedirect(request.getContextPath() + "/admin");
            } else {
            response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        } else {
            request.setAttribute("errorMessage", "用户名或密码错误");
            request.getRequestDispatcher("/WEB-INF/views/user/login.jsp").forward(request, response);
        }
    }
    
    private void registerUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        
        User user = userService.register(username, password, email);
        
        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", user);
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } else {
            request.setAttribute("errorMessage", "注册失败，用户名可能已存在");
            request.getRequestDispatcher("/WEB-INF/views/user/register.jsp").forward(request, response);
        }
    }
    
    private void updateUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }
        
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");
        
        user.setPassword(password);
        user.setEmail(email);
        user.setPhone(phone);
        user.setAddress(address);
        
        boolean success = userService.updateUser(user);
        
        if (success) {
            session.setAttribute("user", user);
            request.setAttribute("successMessage", "个人信息更新成功");
        } else {
            request.setAttribute("errorMessage", "个人信息更新失败");
        }
        
        request.getRequestDispatcher("/WEB-INF/views/user/profile.jsp").forward(request, response);
    }
}
