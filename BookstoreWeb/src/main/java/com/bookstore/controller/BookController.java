package com.bookstore.controller;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import com.bookstore.service.ReviewService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 图书控制器，处理图书相关请求
 */
@WebServlet("/book/*")
public class BookController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private BookService bookService;
    private ReviewService reviewService;
    
    public BookController() {
        this.bookService = new BookService();
        this.reviewService = new ReviewService();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null || pathInfo.equals("/")) {
            // 显示所有图书（分页）
            int page = 1;
            int pageSize = 6; // 每页显示6条记录
            
            try {
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) {
                        page = 1;
                    }
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
            
            List<Book> books = bookService.getAllBooksPaged(page, pageSize);
            int totalBooks = bookService.getTotalBookCount();
            int totalPages = bookService.getTotalPages(pageSize);
            
            request.setAttribute("books", books);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalBooks", totalBooks);
            request.getRequestDispatcher("/WEB-INF/views/book/list.jsp").forward(request, response);
        } else if (pathInfo.equals("/search")) {
            // 搜索图书（分页）
            String keyword = request.getParameter("keyword");
            int page = 1;
            int pageSize = 6; // 每页显示6条记录
            
            try {
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) {
                        page = 1;
                    }
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
            
            List<Book> books = bookService.searchBooksPaged(keyword, page, pageSize);
            int totalBooks = bookService.getTotalSearchResultCount(keyword);
            int totalPages = bookService.getTotalSearchResultPages(keyword, pageSize);
            
            request.setAttribute("books", books);
            request.setAttribute("keyword", keyword);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalBooks", totalBooks);
            request.getRequestDispatcher("/WEB-INF/views/book/list.jsp").forward(request, response);
        } else if (pathInfo.equals("/category")) {
            // 按分类显示图书（分页）
            String category = request.getParameter("category");
            int page = 1;
            int pageSize = 6; // 每页显示6条记录
            
            try {
                String pageParam = request.getParameter("page");
                if (pageParam != null && !pageParam.isEmpty()) {
                    page = Integer.parseInt(pageParam);
                    if (page < 1) {
                        page = 1;
                    }
                }
            } catch (NumberFormatException e) {
                page = 1;
            }
            
            List<Book> books = bookService.getBooksByCategoryPaged(category, page, pageSize);
            int totalBooks = bookService.getTotalBookCountByCategory(category);
            int totalPages = bookService.getTotalPagesByCategory(category, pageSize);
            
            request.setAttribute("books", books);
            request.setAttribute("category", category);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalBooks", totalBooks);
            request.getRequestDispatcher("/WEB-INF/views/book/list.jsp").forward(request, response);
        } else {
            try {
                // 显示图书详情
                int bookId = Integer.parseInt(pathInfo.substring(1));
                Book book = bookService.getBookById(bookId);
                
                if (book != null) {
                    request.setAttribute("book", book);
                    request.setAttribute("reviews", reviewService.getReviewsByBookId(bookId));
                    request.setAttribute("averageRating", reviewService.getAverageRatingByBookId(bookId));
                    request.getRequestDispatcher("/WEB-INF/views/book/detail.jsp").forward(request, response);
                } else {
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                }
            } catch (NumberFormatException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        
        if (pathInfo.equals("/add")) {
            // 添加图书（管理员功能）
            addBook(request, response);
        } else if (pathInfo.equals("/update")) {
            // 更新图书（管理员功能）
            updateBook(request, response);
        } else if (pathInfo.equals("/delete")) {
            // 删除图书（管理员功能）
            deleteBook(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
    
    private void addBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 检查管理员权限
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String publisher = request.getParameter("publisher");
        String isbn = request.getParameter("isbn");
        double price = Double.parseDouble(request.getParameter("price"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        String category = request.getParameter("category");
        String description = request.getParameter("description");
        String coverImage = request.getParameter("coverImage");
        
        Book book = new Book(title, author, publisher, isbn, price, stock, category);
        book.setDescription(description);
        book.setCoverImage(coverImage);
        
        int bookId = bookService.addBook(book);
        
        if (bookId > 0) {
            response.sendRedirect(request.getContextPath() + "/book/" + bookId);
        } else {
            request.setAttribute("errorMessage", "添加图书失败");
            request.getRequestDispatcher("/WEB-INF/views/admin/book/add.jsp").forward(request, response);
        }
    }
    
    private void updateBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 检查管理员权限
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        int bookId = Integer.parseInt(request.getParameter("id"));
        Book book = bookService.getBookById(bookId);
        
        if (book == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        
        book.setTitle(request.getParameter("title"));
        book.setAuthor(request.getParameter("author"));
        book.setPublisher(request.getParameter("publisher"));
        book.setIsbn(request.getParameter("isbn"));
        book.setPrice(Double.parseDouble(request.getParameter("price")));
        book.setStock(Integer.parseInt(request.getParameter("stock")));
        book.setCategory(request.getParameter("category"));
        book.setDescription(request.getParameter("description"));
        book.setCoverImage(request.getParameter("coverImage"));
        
        boolean success = bookService.updateBook(book);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/book/" + bookId);
        } else {
            request.setAttribute("errorMessage", "更新图书失败");
            request.setAttribute("book", book);
            request.getRequestDispatcher("/WEB-INF/views/admin/book/edit.jsp").forward(request, response);
        }
    }
    
    private void deleteBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 检查管理员权限
        if (!isAdmin(request)) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        int bookId = Integer.parseInt(request.getParameter("id"));
        boolean success = bookService.deleteBook(bookId);
        
        if (success) {
            response.sendRedirect(request.getContextPath() + "/book");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "删除图书失败");
        }
    }
    
    private boolean isAdmin(HttpServletRequest request) {
        return request.getSession().getAttribute("user") != null && 
               ((com.bookstore.model.User) request.getSession().getAttribute("user")).getRole() == 1;
    }
}
