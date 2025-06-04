<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>图书列表 - 网上书城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../common/header.jsp" />
        
        <div class="main-content">
            <div class="book-search">
                <form action="${pageContext.request.contextPath}/book/search" method="get">
                    <div class="search-box">
                        <label>
                            <input type="text" name="keyword" placeholder="搜索图书..." value="${keyword}">
                        </label>
                        <button type="submit" class="search-btn">搜索</button>
                    </div>
                </form>
            </div>
            
            <div class="category-nav">
                <h3>图书分类</h3>
                <ul>
                    <li><a href="${pageContext.request.contextPath}/book/category?category=文学">文学</a></li>
                    <li><a href="${pageContext.request.contextPath}/book/category?category=计算机">计算机</a></li>
                    <li><a href="${pageContext.request.contextPath}/book/category?category=经济管理">经济管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/book/category?category=科学技术">科学技术</a></li>
                    <li><a href="${pageContext.request.contextPath}/book/category?category=教育">教育</a></li>
                    <li><a href="${pageContext.request.contextPath}/book/category?category=艺术">艺术</a></li>
                    <li><a href="${pageContext.request.contextPath}/book/category?category=生活">生活</a></li>
                    <li><a href="${pageContext.request.contextPath}/book/category?category=童书">童书</a></li>
                </ul>
            </div>
            
            <div class="book-list">
                <c:if test="${not empty category}">
                    <h2>${category}类图书</h2>
                </c:if>
                
                <c:if test="${not empty keyword}">
                    <h2>搜索结果: "${keyword}"</h2>
                </c:if>
                
                <c:if test="${empty category && empty keyword}">
                    <h2>全部图书</h2>
                </c:if>
                
                <c:if test="${empty books}">
                    <div class="no-results">
                        <p>没有找到相关图书</p>
                    </div>
                </c:if>
                
                <div class="book-grid">
                    <c:forEach var="book" items="${books}">
                        <div class="book-item">
                            <div class="book-cover">
                                <a href="${pageContext.request.contextPath}/book/${book.id}">
                                    <img src="${empty book.coverImage ? pageContext.request.contextPath.concat('/images/default-cover.jpg') : pageContext.request.contextPath.concat('/images/').concat(book.coverImage)}" alt="${book.title}">
                                </a>
                            </div>
                            <div class="book-info">
                                <h3><a href="${pageContext.request.contextPath}/book/${book.id}">${book.title}</a></h3>
                                <p class="book-author">${book.author}</p>
                                <p class="book-price">￥${book.price}</p>
                                <div class="book-actions">
                                    <a href="${pageContext.request.contextPath}/book/${book.id}" class="btn btn-info">详情</a>
                                    <form action="${pageContext.request.contextPath}/cart/add" method="post">
                                        <input type="hidden" name="bookId" value="${book.id}">
                                        <input type="hidden" name="quantity" value="1">
                                        <button type="submit" class="btn btn-primary">加入购物车</button>
                                    </form>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
                
                <!-- 分页导航 -->
                <c:if test="${totalPages > 1}">
                    <div class="pagination">
                        <div class="page-info">
                            第 ${currentPage} 页，共 ${totalPages} 页，共 ${totalBooks} 条记录
                        </div>
                        <div class="page-nav">
                            <c:if test="${currentPage > 1}">
                                <a href="${pageContext.request.contextPath}/book<c:if test="${not empty category}">/category?category=${category}&</c:if><c:if test="${not empty keyword}">/search?keyword=${keyword}&</c:if><c:if test="${empty category && empty keyword}">?</c:if>page=${currentPage - 1}" class="btn btn-sm btn-secondary">上一页</a>
                            </c:if>
                            
                            <c:forEach begin="${Math.max(1, currentPage - 2)}" end="${Math.min(totalPages, currentPage + 2)}" var="i">
                                <c:choose>
                                    <c:when test="${i == currentPage}">
                                        <span class="current-page">${i}</span>
                                    </c:when>
                                    <c:otherwise>
                                        <a href="${pageContext.request.contextPath}/book<c:if test="${not empty category}">/category?category=${category}&</c:if><c:if test="${not empty keyword}">/search?keyword=${keyword}&</c:if><c:if test="${empty category && empty keyword}">?</c:if>page=${i}" class="page-link">${i}</a>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                            
                            <c:if test="${currentPage < totalPages}">
                                <a href="${pageContext.request.contextPath}/book<c:if test="${not empty category}">/category?category=${category}&</c:if><c:if test="${not empty keyword}">/search?keyword=${keyword}&</c:if><c:if test="${empty category && empty keyword}">?</c:if>page=${currentPage + 1}" class="btn btn-sm btn-secondary">下一页</a>
                            </c:if>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
        
        <jsp:include page="../common/footer.jsp" />
    </div>
</body>
</html>
