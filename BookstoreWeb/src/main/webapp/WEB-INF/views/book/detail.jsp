<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${book.title} - 网上书城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../common/header.jsp" />
        
        <div class="main-content">
            <div class="book-detail">
                <div class="book-image">
                    <img src="${empty book.coverImage ? pageContext.request.contextPath.concat('/images/default-cover.jpg') : pageContext.request.contextPath.concat('/images/').concat(book.coverImage)}" alt="${book.title}">
                </div>
                
                <div class="book-info">
                    <h1>${book.title}</h1>
                    <p class="book-author">作者: ${book.author}</p>
                    <p class="book-publisher">出版社: ${book.publisher}</p>
                    <p class="book-isbn">ISBN: ${book.isbn}</p>
                    <p class="book-category">分类: ${book.category}</p>
                    <p class="book-price">价格: ￥${book.price}</p>
                    <p class="book-stock">库存: ${book.stock}</p>
                    
                    <div class="book-rating">
                        <span class="rating-stars">
                            <c:forEach begin="1" end="5" var="i">
                                <c:choose>
                                    <c:when test="${i <= averageRating}">
                                        <span class="star filled">★</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="star">☆</span>
                                    </c:otherwise>
                                </c:choose>
                            </c:forEach>
                        </span>
                        <span class="rating-value">${averageRating}/5</span>
                    </div>
                    
                    <div class="book-actions">
                        <form action="${pageContext.request.contextPath}/cart/add" method="post">
                            <input type="hidden" name="bookId" value="${book.id}">
                            <div class="quantity-control">
                                <label for="quantity">数量:</label>
                                <input type="number" id="quantity" name="quantity" value="1" min="1" max="${book.stock}">
                            </div>
                            <button type="submit" class="btn btn-primary">加入购物车</button>
                        </form>
                    </div>
                </div>
            </div>
            
            <div class="book-description">
                <h2>图书简介</h2>
                <p>${book.description}</p>
            </div>
            
            <div class="book-reviews">
                <h2>用户评论</h2>
                
                <c:if test="${not empty sessionScope.user}">
                    <div class="review-form">
                        <h3>发表评论</h3>
                        <form action="${pageContext.request.contextPath}/review/add" method="post">
                            <input type="hidden" name="bookId" value="${book.id}">
                            
                            <div class="form-group">
                                <label for="rating">评分:</label>
                                <select id="rating" name="rating" required>
                                    <option value="5">5星 - 非常好</option>
                                    <option value="4">4星 - 很好</option>
                                    <option value="3">3星 - 一般</option>
                                    <option value="2">2星 - 较差</option>
                                    <option value="1">1星 - 很差</option>
                                </select>
                            </div>
                            
                            <div class="form-group">
                                <label for="comment">评论内容:</label>
                                <textarea id="comment" name="comment" rows="4" required></textarea>
                            </div>
                            
                            <div class="form-group">
                                <button type="submit" class="btn btn-primary">提交评论</button>
                            </div>
                        </form>
                    </div>
                </c:if>
                
                <div class="review-list">
                    <c:if test="${empty review}">
                        <p class="no-reviews">暂无评论</p>
                    </c:if>
                    
                    <c:forEach var="review" items="${review}">
                        <div class="review-item">
                            <div class="review-header">
                                <span class="review-user">${review.username}</span>
                                <span class="review-date">
                                    <fmt:formatDate value="${review.reviewTime}" pattern="yyyy-MM-dd HH:mm" />
                                </span>
                                <span class="review-rating">
                                    <c:forEach begin="1" end="5" var="i">
                                        <c:choose>
                                            <c:when test="${i <= review.rating}">
                                                <span class="star filled">★</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="star">☆</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </c:forEach>
                                </span>
                            </div>
                            <div class="review-content">
                                <p>${review.comment}</p>
                            </div>
                            
                            <c:if test="${sessionScope.user.id == review.userId || sessionScope.user.role == 1}">
                                <div class="review-actions">
                                    <form action="${pageContext.request.contextPath}/review/delete" method="post">
                                        <input type="hidden" name="reviewId" value="${review.id}">
                                        <input type="hidden" name="bookId" value="${book.id}">
                                        <button type="submit" class="btn btn-danger btn-sm">删除</button>
                                    </form>
                                </div>
                            </c:if>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
        
        <jsp:include page="../common/footer.jsp" />
    </div>
</body>
</html>
