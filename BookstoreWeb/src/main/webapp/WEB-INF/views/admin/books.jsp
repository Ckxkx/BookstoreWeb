<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>图书管理 - 网上书城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../common/header.jsp" />
        
        <div class="admin-container">
            <div class="admin-sidebar">
                <h3>管理菜单</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/admin">控制面板</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/admin/books">图书管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/orders">订单管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/users">用户管理</a></li>
                </ul>
            </div>
            
            <div class="admin-content">
                <h2>图书管理</h2>
                
                <div class="action-bar">
                    <button class="btn btn-primary" onclick="showAddBookForm()">添加新书</button>
                </div>
                
                <div id="addBookForm" class="modal" style="display: none;">
                    <div class="modal-content">
                        <h3>添加新书</h3>
                        <form action="${pageContext.request.contextPath}/admin/add-book" method="post">
                            <div class="form-group">
                                <label for="title">书名:</label>
                                <input type="text" id="title" name="title" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="author">作者:</label>
                                <input type="text" id="author" name="author" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="publisher">出版社:</label>
                                <input type="text" id="publisher" name="publisher">
                            </div>
                            
                            <div class="form-group">
                                <label for="isbn">ISBN:</label>
                                <input type="text" id="isbn" name="isbn" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="price">价格:</label>
                                <input type="number" id="price" name="price" step="0.01" required>
                            </div>
                            
                            <div class="form-group">
                                <label for="stock">库存:</label>
                                <input type="number" id="stock" name="stock" required>
                            </div>
                            
                            <div class="form-group">
                            <label for="category">分类:</label>
                                <input type="text" id="category" name="category">
                            </div>
                            
                            <div class="form-group">
                                <label for="description">描述:</label>
                                <textarea id="description" name="description" rows="3"></textarea>
                        </div>
                        
                            <div class="form-group">
                                <label for="publishDate">出版日期:</label>
                                <input type="date" id="publishDate" name="publishDate" required>
                        </div>
                        
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary">添加</button>
                                <button type="button" class="btn btn-secondary" onclick="hideAddBookForm()">取消</button>
                        </div>
                    </form>
                    </div>
                </div>
                
                <div class="book-list">
                    <table>
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>书名</th>
                                <th>作者</th>
                                <th>ISBN</th>
                                <th>价格</th>
                                <th>库存</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${books}" var="book">
                                <tr>
                                    <td>${book.id}</td>
                                    <td>${book.title}</td>
                                    <td>${book.author}</td>
                                    <td>${book.isbn}</td>
                                    <td>￥${book.price}</td>
                                    <td>${book.stock}</td>
                                    <td>
                                        <form action="${pageContext.request.contextPath}/admin/delete-book" method="post" style="display: inline;">
                                            <input type="hidden" name="bookId" value="${book.id}">
                                            <button type="submit" class="btn btn-danger" onclick="return confirm('确定要删除该图书吗？')">删除</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        
        <jsp:include page="../common/footer.jsp" />
    </div>
    
    <script>
        function showAddBookForm() {
            document.getElementById('addBookForm').style.display = 'block';
        }
        
        function hideAddBookForm() {
            document.getElementById('addBookForm').style.display = 'none';
        }
    </script>
</body>
</html>
