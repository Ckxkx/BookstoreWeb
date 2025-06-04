package com.bookstore.controller;

import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;
import com.bookstore.model.User;
import com.bookstore.model.Book;
import com.bookstore.service.OrderService;
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
 * 订单控制器，处理订单相关请求
 */
@WebServlet("/order/*")
public class OrderController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private OrderService orderService;
    private BookService bookService;
    
    public OrderController() {
        this.orderService = new OrderService();
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
            // 显示用户的所有订单
            List<Order> orders = orderService.getOrdersByUserId(user.getId());
            
            // 为每个订单加载第一个订单项的图书信息，用于显示
            for (Order order : orders) {
                List<OrderItem> orderItems = orderService.getOrderItemsByOrderId(order.getId());
                if (!orderItems.isEmpty()) {
                    OrderItem firstItem = orderItems.get(0);
                    Book book = bookService.getBookById(firstItem.getBookId());
                    request.setAttribute("firstBook_" + order.getId(), book);
                }
            }
            
            request.setAttribute("orders", orders);
            request.getRequestDispatcher("/WEB-INF/views/order/list.jsp").forward(request, response);
        } else if (pathInfo.equals("/checkout")) {
            // 结算页面
            request.getRequestDispatcher("/WEB-INF/views/order/checkout.jsp").forward(request, response);
        } else {
            try {
                // 显示订单详情
                int orderId = Integer.parseInt(pathInfo.substring(1));
                Order order = orderService.getOrderById(orderId);
                
                // 检查订单是否属于当前用户或用户是否为管理员
                if (order != null && (order.getUserId() == user.getId() || user.getRole() == 1)) {
                    request.setAttribute("order", order);
                    
                    // 获取订单项
                    List<OrderItem> orderItems = orderService.getOrderItemsByOrderId(orderId);
                    request.setAttribute("orderItems", orderItems);
                    
                    // 获取每个订单项对应的图书信息
                    for (OrderItem item : orderItems) {
                        Book book = bookService.getBookById(item.getBookId());
                        request.setAttribute("book_" + item.getBookId(), book);
                    }
                    
                    request.getRequestDispatcher("/WEB-INF/views/order/detail.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
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
        
        if (pathInfo.equals("/create")) {
            // 创建订单
            String shippingAddress = request.getParameter("shippingAddress");
            
            int orderId = orderService.createOrder(user.getId(), shippingAddress);
            
            if (orderId > 0) {
                response.sendRedirect(request.getContextPath() + "/order/" + orderId);
            } else {
                request.setAttribute("errorMessage", "创建订单失败，请确保购物车不为空");
                response.sendRedirect(request.getContextPath() + "/cart");
            }
        } else if (pathInfo.equals("/cancel")) {
            // 取消订单
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            Order order = orderService.getOrderById(orderId);
            
            // 检查订单是否属于当前用户
            if (order != null && order.getUserId() == user.getId()) {
                boolean success = orderService.cancelOrder(orderId);
                
                if (success) {
                    response.sendRedirect(request.getContextPath() + "/order/" + orderId);
                } else {
                    request.setAttribute("errorMessage", "取消订单失败，只能取消待付款状态的订单");
                    response.sendRedirect(request.getContextPath() + "/order/" + orderId);
                }
            } else {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
            }
        } else if (pathInfo.equals("/update-status")) {
            // 更新订单状态（管理员功能）
            if (user.getRole() != 1) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            String status = request.getParameter("status");
            
            boolean success = orderService.updateOrderStatus(orderId, status);
            
            if (success) {
                response.sendRedirect(request.getContextPath() + "/order/" + orderId);
            } else {
                request.setAttribute("errorMessage", "更新订单状态失败");
                response.sendRedirect(request.getContextPath() + "/order/" + orderId);
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
