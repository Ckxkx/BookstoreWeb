package com.bookstore.dao;

import com.bookstore.model.Book;
import com.bookstore.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 图书数据访问对象
 */
public class BookDAO {
    
    /**
     * 添加图书
     * @param book 图书对象
     * @return 新增图书的ID，失败返回-1
     */
    public int addBook(Book book) {
        String sql = "INSERT INTO books (title, author, publisher, isbn, price, stock, category, " +
                     "description, cover_image, publish_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int bookId = -1;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setString(4, book.getIsbn());
            pstmt.setDouble(5, book.getPrice());
            pstmt.setInt(6, book.getStock());
            pstmt.setString(7, book.getCategory());
            pstmt.setString(8, book.getDescription());
            pstmt.setString(9, book.getCoverImage());
            pstmt.setTimestamp(10, new Timestamp(book.getPublishDate().getTime()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                rs = pstmt.getGeneratedKeys();
                if (rs.next()) {
                    bookId = rs.getInt(1);
                    book.setId(bookId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return bookId;
    }
    
    /**
     * 根据ID获取图书
     * @param bookId 图书ID
     * @return 图书对象，未找到返回null
     */
    public Book getBookById(int bookId) {
        String sql = "SELECT * FROM books WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Book book = null;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, bookId);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                book = mapResultSetToBook(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return book;
    }
    
    /**
     * 更新图书信息
     * @param book 图书对象
     * @return 是否更新成功
     */
    public boolean updateBook(Book book) {
        String sql = "UPDATE books SET title = ?, author = ?, publisher = ?, isbn = ?, " +
                     "price = ?, stock = ?, category = ?, description = ?, cover_image = ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setString(4, book.getIsbn());
            pstmt.setDouble(5, book.getPrice());
            pstmt.setInt(6, book.getStock());
            pstmt.setString(7, book.getCategory());
            pstmt.setString(8, book.getDescription());
            pstmt.setString(9, book.getCoverImage());
            pstmt.setInt(10, book.getId());
            
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
     * 删除图书
     * @param bookId 图书ID
     * @return 是否删除成功
     */
    public boolean deleteBook(int bookId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // 开启事务
            
            // 先删除相关的订单项
            String deleteOrderItemsSql = "DELETE FROM order_items WHERE book_id = ?";
            pstmt = conn.prepareStatement(deleteOrderItemsSql);
            pstmt.setInt(1, bookId);
            pstmt.executeUpdate();
            
            // 再删除图书
            String deleteBookSql = "DELETE FROM books WHERE id = ?";
            pstmt = conn.prepareStatement(deleteBookSql);
            pstmt.setInt(1, bookId);
            
            System.out.println("Executing SQL: " + deleteBookSql + " with bookId: " + bookId);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("Affected rows: " + affectedRows);
            
            success = (affectedRows > 0);
            
            if (success) {
                conn.commit(); // 提交事务
            } else {
                conn.rollback(); // 回滚事务
            }
        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            e.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback(); // 发生异常时回滚事务
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true); // 恢复自动提交
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            closeResources(conn, pstmt, null);
        }
        
        return success;
    }
    
    /**
     * 获取所有图书
     * @return 图书列表
     */
    public List<Book> getAllBooks() {
        String sql = "SELECT * FROM books";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return books;
    }
    
    /**
     * 根据分类获取图书
     * @param category 图书分类
     * @return 图书列表
     */
    public List<Book> getBooksByCategory(String category) {
        String sql = "SELECT * FROM books WHERE category = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return books;
    }
    
    /**
     * 搜索图书
     * @param keyword 搜索关键词
     * @return 图书列表
     */
    public List<Book> searchBooks(String keyword) {
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR publisher LIKE ? OR description LIKE ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return books;
    }
    
    /**
     * 更新图书库存
     * @param bookId 图书ID
     * @param quantity 变更数量（正数增加，负数减少）
     * @return 是否更新成功
     */
    public boolean updateBookStock(int bookId, int quantity) {
        String sql = "UPDATE books SET stock = stock + ? WHERE id = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        boolean success = false;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, quantity);
            pstmt.setInt(2, bookId);
            
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
     * 将ResultSet映射为Book对象
     * @param rs ResultSet结果集
     * @return Book对象
     * @throws SQLException SQL异常
     */
    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        Book book = new Book();
        book.setId(rs.getInt("id"));
        book.setTitle(rs.getString("title"));
        book.setAuthor(rs.getString("author"));
        book.setPublisher(rs.getString("publisher"));
        book.setIsbn(rs.getString("isbn"));
        book.setPrice(rs.getDouble("price"));
        book.setStock(rs.getInt("stock"));
        book.setCategory(rs.getString("category"));
        book.setDescription(rs.getString("description"));
        book.setCoverImage(rs.getString("cover_image"));
        book.setPublishDate(rs.getTimestamp("publish_date"));
        return book;
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
    
    /**
     * 分页获取所有图书
     * @param page 页码（从1开始）
     * @param pageSize 每页记录数
     * @return 图书列表
     */
    public List<Book> getAllBooksPaged(int page, int pageSize) {
        String sql = "SELECT * FROM books LIMIT ? OFFSET ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, (page - 1) * pageSize);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return books;
    }
    
    /**
     * 获取图书总数
     * @return 图书总数
     */
    public int getTotalBookCount() {
        String sql = "SELECT COUNT(*) FROM books";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return count;
    }
    
    /**
     * 根据分类分页获取图书
     * @param category 图书分类
     * @param page 页码（从1开始）
     * @param pageSize 每页记录数
     * @return 图书列表
     */
    public List<Book> getBooksByCategoryPaged(String category, int page, int pageSize) {
        String sql = "SELECT * FROM books WHERE category = ? LIMIT ? OFFSET ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category);
            pstmt.setInt(2, pageSize);
            pstmt.setInt(3, (page - 1) * pageSize);
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return books;
    }
    
    /**
     * 获取指定分类的图书总数
     * @param category 图书分类
     * @return 图书总数
     */
    public int getTotalBookCountByCategory(String category) {
        String sql = "SELECT COUNT(*) FROM books WHERE category = ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, category);
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return count;
    }
    
    /**
     * 搜索图书（分页）
     * @param keyword 搜索关键词
     * @param page 页码（从1开始）
     * @param pageSize 每页记录数
     * @return 图书列表
     */
    public List<Book> searchBooksPaged(String keyword, int page, int pageSize) {
        String sql = "SELECT * FROM books WHERE title LIKE ? OR author LIKE ? OR publisher LIKE ? OR description LIKE ? LIMIT ? OFFSET ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        List<Book> books = new ArrayList<>();
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            pstmt.setInt(5, pageSize);
            pstmt.setInt(6, (page - 1) * pageSize);
            
            rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Book book = mapResultSetToBook(rs);
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return books;
    }
    
    /**
     * 获取搜索结果的总数
     * @param keyword 搜索关键词
     * @return 搜索结果总数
     */
    public int getTotalSearchResultCount(String keyword) {
        String sql = "SELECT COUNT(*) FROM books WHERE title LIKE ? OR author LIKE ? OR publisher LIKE ? OR description LIKE ?";
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;
        
        try {
            conn = DBConnection.getConnection();
            pstmt = conn.prepareStatement(sql);
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            
            rs = pstmt.executeQuery();
            
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }
        
        return count;
    }
}
