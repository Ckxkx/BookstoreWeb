package com.bookstore.service;

import com.bookstore.dao.BookDAO;
import com.bookstore.model.Book;

import java.util.List;

/**
 * 图书服务类，处理图书相关业务逻辑
 */
public class BookService {
    private BookDAO bookDAO;
    
    public BookService() {
        this.bookDAO = new BookDAO();
    }
    
    /**
     * 添加图书
     * @param book 图书对象
     * @return 添加成功返回图书ID，失败返回-1
     */
    public int addBook(Book book) {
        return bookDAO.addBook(book);
    }
    
    /**
     * 获取图书信息
     * @param bookId 图书ID
     * @return 图书对象
     */
    public Book getBookById(int bookId) {
        return bookDAO.getBookById(bookId);
    }
    
    /**
     * 更新图书信息
     * @param book 图书对象
     * @return 更新成功返回true，失败返回false
     */
    public boolean updateBook(Book book) {
        return bookDAO.updateBook(book);
    }
    
    /**
     * 删除图书
     * @param bookId 图书ID
     * @return 删除成功返回true，失败返回false
     */
    public boolean deleteBook(int bookId) {
        System.out.println("Deleting book with ID: " + bookId);
        boolean result = bookDAO.deleteBook(bookId);
        System.out.println("Delete result: " + result);
        return result;
    }
    
    /**
     * 获取所有图书
     * @return 图书列表
     */
    public List<Book> getAllBooks() {
        return bookDAO.getAllBooks();
    }
    
    /**
     * 根据分类获取图书
     * @param category 图书分类
     * @return 图书列表
     */
    public List<Book> getBooksByCategory(String category) {
        return bookDAO.getBooksByCategory(category);
    }
    
    /**
     * 搜索图书
     * @param keyword 搜索关键词
     * @return 图书列表
     */
    public List<Book> searchBooks(String keyword) {
        return bookDAO.searchBooks(keyword);
    }
    
    /**
     * 更新图书库存
     * @param bookId 图书ID
     * @param quantity 变更数量（正数增加，负数减少）
     * @return 更新成功返回true，失败返回false
     */
    public boolean updateBookStock(int bookId, int quantity) {
        return bookDAO.updateBookStock(bookId, quantity);
    }
    
    /**
     * 分页获取所有图书
     * @param page 页码
     * @param pageSize 每页记录数
     * @return 图书列表
     */
    public List<Book> getAllBooksPaged(int page, int pageSize) {
        return bookDAO.getAllBooksPaged(page, pageSize);
    }
    
    /**
     * 获取图书总数
     * @return 图书总数
     */
    public int getTotalBookCount() {
        return bookDAO.getTotalBookCount();
    }
    
    /**
     * 获取总页数
     * @param pageSize 每页记录数
     * @return 总页数
     */
    public int getTotalPages(int pageSize) {
        int totalBooks = getTotalBookCount();
        return (int) Math.ceil((double) totalBooks / pageSize);
    }
    
    /**
     * 根据分类分页获取图书
     * @param category 分类
     * @param page 页码
     * @param pageSize 每页记录数
     * @return 图书列表
     */
    public List<Book> getBooksByCategoryPaged(String category, int page, int pageSize) {
        return bookDAO.getBooksByCategoryPaged(category, page, pageSize);
    }
    
    /**
     * 获取分类下的图书总数
     * @param category 分类
     * @return 图书总数
     */
    public int getTotalBookCountByCategory(String category) {
        return bookDAO.getTotalBookCountByCategory(category);
    }
    
    /**
     * 获取分类下的总页数
     * @param category 分类
     * @param pageSize 每页记录数
     * @return 总页数
     */
    public int getTotalPagesByCategory(String category, int pageSize) {
        int totalBooks = getTotalBookCountByCategory(category);
        return (int) Math.ceil((double) totalBooks / pageSize);
    }
    
    /**
     * 搜索图书（分页）
     * @param keyword 关键词
     * @param page 页码
     * @param pageSize 每页记录数
     * @return 图书列表
     */
    public List<Book> searchBooksPaged(String keyword, int page, int pageSize) {
        return bookDAO.searchBooksPaged(keyword, page, pageSize);
    }
    
    /**
     * 获取搜索结果总数
     * @param keyword 关键词
     * @return 搜索结果总数
     */
    public int getTotalSearchResultCount(String keyword) {
        return bookDAO.getTotalSearchResultCount(keyword);
    }
    
    /**
     * 获取搜索结果总页数
     * @param keyword 关键词
     * @param pageSize 每页记录数
     * @return 总页数
     */
    public int getTotalSearchResultPages(String keyword, int pageSize) {
        int totalBooks = getTotalSearchResultCount(keyword);
        return (int) Math.ceil((double) totalBooks / pageSize);
    }
}
