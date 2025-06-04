package com.bookstore.controller;

import com.bookstore.model.User;
import com.bookstore.model.Book;
import com.bookstore.model.Order;
import com.bookstore.service.UserService;
import com.bookstore.service.BookService;
import com.bookstore.service.OrderService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
import java.text.ParseException;

/**
 * 管理员控制器，处理后台管理相关请求
 */
@WebServlet("/admin/*")
public class AdminController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private UserService userService;
    private BookService bookService;
    private OrderService orderService;
    
    public AdminController() {
        this.userService = new UserService();
        this.bookService = new BookService();
        this.orderService = new OrderService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is logged in and is admin
        if (user == null || user.getRole() != 1) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // Admin dashboard
            int userCount = userService.getAllUsers().size();
            int bookCount = bookService.getAllBooks().size();
            int orderCount = orderService.getAllOrders().size();
            double todaySales = orderService.getTodaySales();
            request.setAttribute("userCount", userCount);
            request.setAttribute("bookCount", bookCount);
            request.setAttribute("orderCount", orderCount);
            request.setAttribute("todaySales", todaySales);
            request.getRequestDispatcher("/WEB-INF/views/admin/dashboard.jsp").forward(request, response);
        } else if (pathInfo.equals("/users")) {
            // User management
            List<User> users = userService.getAllUsers();
            request.setAttribute("users", users);
            request.getRequestDispatcher("/WEB-INF/views/admin/users.jsp").forward(request, response);
        } else if (pathInfo.equals("/books")) {
            // Book management
            List<Book> books = bookService.getAllBooks();
            request.setAttribute("books", books);
            request.getRequestDispatcher("/WEB-INF/views/admin/books.jsp").forward(request, response);
        } else if (pathInfo.equals("/orders")) {
            // Order management
            List<Order> orders = orderService.getAllOrders();
            // 构建订单ID->用户名映射
            java.util.Map<Integer, String> orderUserMap = new java.util.HashMap<>();
            for (Order order : orders) {
                User u = userService.getUserById(order.getUserId());
                orderUserMap.put(order.getId(), u != null ? u.getUsername() : ("ID:" + order.getUserId()));
            }
            request.setAttribute("orders", orders);
            request.setAttribute("orderUserMap", orderUserMap);
            request.getRequestDispatcher("/WEB-INF/views/admin/orders.jsp").forward(request, response);
        } else if (pathInfo.equals("/delete-order")) {
            int orderId = Integer.parseInt(request.getParameter("orderId"));
            orderService.deleteOrderByAdmin(orderId);
            response.sendRedirect(request.getContextPath() + "/admin/orders");
        } else if (pathInfo.equals("/add-order")) {
            try {
                int userId = Integer.parseInt(request.getParameter("userId"));
                String shippingAddress = request.getParameter("shippingAddress");
                double totalAmount = Double.parseDouble(request.getParameter("totalAmount"));
                String status = request.getParameter("status");

                User userObj = userService.getUserById(userId);
                if (userObj == null) {
                    // 用户不存在，重定向并带错误参数
                    response.sendRedirect(request.getContextPath() + "/admin/orders?error=nouser");
                    return;
                }

                Order order = new Order();
                order.setUserId(userId);
                order.setShippingAddress(shippingAddress);
                order.setTotalAmount(totalAmount);
                order.setStatus(status);
                order.setOrderTime(new java.util.Date());

                orderService.createOrderByAdmin(order);
                response.sendRedirect(request.getContextPath() + "/admin/orders");
            } catch (Exception e) {
                // 参数异常等
                response.sendRedirect(request.getContextPath() + "/admin/orders?error=param");
            }
        } else if (pathInfo.equals("/order-detail")) {
            int orderId = Integer.parseInt(request.getParameter("id"));
            Order order = orderService.getOrderById(orderId);
            List<com.bookstore.model.OrderItem> items = orderService.getOrderItemsByOrderId(orderId);
            response.setContentType("text/html;charset=UTF-8");
            StringBuilder sb = new StringBuilder();
            if (order == null) {
                sb.append("<p>订单不存在</p>");
            } else {
                sb.append("<p><b>订单ID：</b>" + order.getId() + "</p>");
                sb.append("<p><b>用户ID：</b>" + order.getUserId() + "</p>");
                sb.append("<p><b>总金额：</b>￥" + order.getTotalAmount() + "</p>");
                sb.append("<p><b>收货地址：</b>" + order.getShippingAddress() + "</p>");
                sb.append("<p><b>订单状态：</b>" + order.getStatus() + "</p>");
                sb.append("<p><b>下单时间：</b>" + new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(order.getOrderTime()) + "</p>");
                sb.append("<hr><b>订单项：</b><ul>");
                for (com.bookstore.model.OrderItem item : items) {
                    sb.append("<li>图书ID: " + item.getBookId() + ", 数量: " + item.getQuantity() + ", 单价: ￥" + item.getPrice() + ", 小计: ￥" + item.getSubtotal() + "</li>");
                }
                sb.append("</ul>");
            }
            response.getWriter().write(sb.toString());
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        
        // Check if user is logged in and is admin
        if (user == null || user.getRole() != 1) {
            response.sendRedirect(request.getContextPath() + "/user/login");
            return;
        }
        
        String pathInfo = request.getPathInfo();
        
        if (pathInfo.equals("/delete-user")) {
            int userId = Integer.parseInt(request.getParameter("userId"));
            userService.deleteUser(userId);
            response.sendRedirect(request.getContextPath() + "/admin/users");
        } else if (pathInfo.equals("/add-book")) {
            // Add new book
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String publisher = request.getParameter("publisher");
            String isbn = request.getParameter("isbn");
            double price = Double.parseDouble(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            String category = request.getParameter("category");
            String description = request.getParameter("description");
            String publishDateStr = request.getParameter("publishDate");
            
            Book book = new Book();
            book.setTitle(title);
            book.setAuthor(author);
            book.setPublisher(publisher);
            book.setIsbn(isbn);
            book.setPrice(price);
            book.setStock(stock);
            book.setCategory(category);
            book.setDescription(description);
            
            try {
                // 将字符串日期转换为Date对象
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date publishDate = dateFormat.parse(publishDateStr);
                book.setPublishDate(publishDate);
            } catch (ParseException e) {
                // 如果日期解析失败，使用当前日期
                book.setPublishDate(new Date());
            }
            
            bookService.addBook(book);
            response.sendRedirect(request.getContextPath() + "/admin/books");
        } else if (pathInfo.equals("/delete-book")) {
            int bookId = Integer.parseInt(request.getParameter("bookId"));
            bookService.deleteBook(bookId);
            response.sendRedirect(request.getContextPath() + "/admin/books");
        } else if (pathInfo.equals("/update-role")) {
            // 处理用户角色变更
            int userId = Integer.parseInt(request.getParameter("userId"));
            int role = Integer.parseInt(request.getParameter("role"));
            User targetUser = userService.getUserById(userId);
            boolean success = false;
            String message = null;
            if (targetUser != null) {
                targetUser.setRole(role);
                success = userService.updateUser(targetUser);
                if (!success) message = "数据库更新失败";
            } else {
                message = "用户不存在";
            }
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"success\":" + success + (message != null ? ",\"message\":\"" + message + "\"" : "") + "}");
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
