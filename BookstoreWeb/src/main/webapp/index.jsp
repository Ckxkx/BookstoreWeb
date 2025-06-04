<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>网上书城 - 首页</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <jsp:include page="WEB-INF/views/common/header.jsp" />
        
        <div class="main-content">
            <div class="banner">
                <div class="banner-content">
                    <h1>欢迎来到网上书城</h1>
                    <p>发现知识的海洋，开启阅读的旅程</p>
                    <a href="${pageContext.request.contextPath}/book" class="btn btn-primary">浏览图书</a>
                </div>
            </div>
            
            <div class="featured-books">
                <h2>精选图书</h2>
                <div class="book-grid">
                    <c:forEach var="book" items="${featuredBooks}">
                        <div class="book-item">
                            <div class="book-cover">
                                <a href="${pageContext.request.contextPath}/book/${book.id}">
                                    <img src="${empty book.coverImage ? '/images/default-cover.jpg' : book.coverImage}" alt="${book.title}">
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
            </div>
            
            <div class="categories">
                <h2>图书分类</h2>
                <div class="category-grid">
                    <a href="${pageContext.request.contextPath}/book/category?category=文学" class="category-item">
                        <div class="category-icon">📚</div>
                        <h3>文学</h3>
                    </a>
                    <a href="${pageContext.request.contextPath}/book/category?category=计算机" class="category-item">
                        <div class="category-icon">💻</div>
                        <h3>计算机</h3>
                    </a>
                    <a href="${pageContext.request.contextPath}/book/category?category=经济管理" class="category-item">
                        <div class="category-icon">📊</div>
                        <h3>经济管理</h3>
                    </a>
                    <a href="${pageContext.request.contextPath}/book/category?category=科学技术" class="category-item">
                        <div class="category-icon">🔬</div>
                        <h3>科学技术</h3>
                    </a>
                    <a href="${pageContext.request.contextPath}/book/category?category=教育" class="category-item">
                        <div class="category-icon">🎓</div>
                        <h3>教育</h3>
                    </a>
                    <a href="${pageContext.request.contextPath}/book/category?category=艺术" class="category-item">
                        <div class="category-icon">🎨</div>
                        <h3>艺术</h3>
                    </a>
                    <a href="${pageContext.request.contextPath}/book/category?category=生活" class="category-item">
                        <div class="category-icon">🏠</div>
                        <h3>生活</h3>
                    </a>
                    <a href="${pageContext.request.contextPath}/book/category?category=童书" class="category-item">
                        <div class="category-icon">🧸</div>
                        <h3>童书</h3>
                    </a>
                </div>
            </div>
            
            <div class="new-arrivals">
                <h2>新书上架</h2>
                <div class="book-grid">
                    <c:forEach var="book" items="${newArrivals}">
                        <div class="book-item">
                            <div class="book-cover">
                                <a href="${pageContext.request.contextPath}/book/${book.id}">
                                    <img src="${empty book.coverImage ? '/images/default-cover.jpg' : book.coverImage}" alt="${book.title}">
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
            </div>
        </div>
        
        <jsp:include page="WEB-INF/views/common/footer.jsp" />
    </div>
</body>
</html>
