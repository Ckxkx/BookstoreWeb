package com.bookstore.service;

import com.bookstore.dao.OrderDAO;
import com.bookstore.dao.OrderItemDAO;
import com.bookstore.dao.BookDAO;
import com.bookstore.dao.CartItemDAO;
import com.bookstore.model.Order;
import com.bookstore.model.OrderItem;
import com.bookstore.model.CartItem;
import com.bookstore.model.Book;

import java.util.List;
import java.util.ArrayList;

/**
 * 订单服务类，处理订单相关业务逻辑
 */
public class OrderService {
    private OrderDAO orderDAO;
    private OrderItemDAO orderItemDAO;
    private BookDAO bookDAO;
    private CartItemDAO cartItemDAO;
    
    public OrderService() {
        this.orderDAO = new OrderDAO();
        this.orderItemDAO = new OrderItemDAO();
        this.bookDAO = new BookDAO();
        this.cartItemDAO = new CartItemDAO();
    }
    
    /**
     * 创建订单
     * @param userId 用户ID
     * @param shippingAddress 收货地址
     * @return 创建成功返回订单ID，失败返回-1
     */
    public int createOrder(int userId, String shippingAddress) {
        // 获取用户购物车中的商品
        List<CartItem> cartItems = cartItemDAO.getCartItemsByUserId(userId);
        if (cartItems.isEmpty()) {
            return -1;
        }
        
        // 计算订单总金额
        double totalAmount = 0;
        for (CartItem cartItem : cartItems) {
            Book book = bookDAO.getBookById(cartItem.getBookId());
            if (book != null) {
                totalAmount += book.getPrice() * cartItem.getQuantity();
            }
        }
        
        // 创建订单
        Order order = new Order(userId, totalAmount, shippingAddress);
        int orderId = orderDAO.createOrder(order);
        
        if (orderId > 0) {
            // 创建订单项
            for (CartItem cartItem : cartItems) {
                Book book = bookDAO.getBookById(cartItem.getBookId());
                if (book != null) {
                    OrderItem orderItem = new OrderItem(orderId, cartItem.getBookId(), cartItem.getQuantity(), book.getPrice());
                    orderItemDAO.addOrderItem(orderItem);
                    
                    // 更新图书库存
                    bookDAO.updateBookStock(cartItem.getBookId(), -cartItem.getQuantity());
                }
            }
            
            // 清空购物车
            cartItemDAO.clearCart(userId);
            
            return orderId;
        }
        
        return -1;
    }
    
    /**
     * 获取订单信息
     * @param orderId 订单ID
     * @return 订单对象
     */
    public Order getOrderById(int orderId) {
        return orderDAO.getOrderById(orderId);
    }
    
    /**
     * 获取用户的所有订单
     * @param userId 用户ID
     * @return 订单列表
     */
    public List<Order> getOrdersByUserId(int userId) {
        return orderDAO.getOrdersByUserId(userId);
    }
    
    /**
     * 获取订单的所有订单项
     * @param orderId 订单ID
     * @return 订单项列表
     */
    public List<OrderItem> getOrderItemsByOrderId(int orderId) {
        return orderItemDAO.getOrderItemsByOrderId(orderId);
    }
    
    /**
     * 更新订单状态
     * @param orderId 订单ID
     * @param status 新状态
     * @return 更新成功返回true，失败返回false
     */
    public boolean updateOrderStatus(int orderId, String status) {
        return orderDAO.updateOrderStatus(orderId, status);
    }
    
    /**
     * 取消订单
     * @param orderId 订单ID
     * @return 取消成功返回true，失败返回false
     */
    public boolean cancelOrder(int orderId) {
        Order order = orderDAO.getOrderById(orderId);
        if (order != null && "待付款".equals(order.getStatus())) {
            // 获取订单项
            List<OrderItem> orderItems = orderItemDAO.getOrderItemsByOrderId(orderId);
            
            // 恢复图书库存
            for (OrderItem orderItem : orderItems) {
                bookDAO.updateBookStock(orderItem.getBookId(), orderItem.getQuantity());
            }
            
            // 更新订单状态为已取消
            return orderDAO.updateOrderStatus(orderId, "已取消");
        }
        
        return false;
    }
    
    /**
     * 获取所有订单
     * @return 订单列表
     */
    public List<Order> getAllOrders() {
        return orderDAO.getAllOrders();
    }
    
    /**
     * 统计今日销售额
     * @return double 今日销售额
     */
    public double getTodaySales() {
        List<Order> orders = orderDAO.getAllOrders();
        double total = 0;
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
        String today = sdf.format(new java.util.Date());
        for (Order order : orders) {
            if (order.getOrderTime() != null && today.equals(sdf.format(order.getOrderTime()))) {
                total += order.getTotalAmount();
            }
        }
        return total;
    }
    
    /**
     * 管理员直接创建订单
     */
    public int createOrderByAdmin(Order order) {
        return orderDAO.createOrder(order);
    }
    
    /**
     * 管理员直接删除订单（含订单项）
     */
    public boolean deleteOrderByAdmin(int orderId) {
        orderItemDAO.deleteOrderItemsByOrderId(orderId);
        return orderDAO.deleteOrder(orderId);
    }
}
