<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>购物车 - 网上书城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../common/header.jsp" />
        
        <div class="main-content">
            <div class="cart-container">
                <h2>我的购物车</h2>
                
                <c:if test="${not empty errorMessage}">
                    <div class="error-message">
                        <p>${errorMessage}</p>
                    </div>
                </c:if>
                
                <c:if test="${empty cartItems}">
                    <div class="empty-cart">
                        <p>购物车为空</p>
                        <a href="${pageContext.request.contextPath}/book" class="btn btn-primary">去购物</a>
                    </div>
                </c:if>
                
                <c:if test="${not empty cartItems}">
                    <div class="cart-items">
                        <table class="cart-table">
                            <thead>
                                <tr>
                                    <th>图书</th>
                                    <th>单价</th>
                                    <th>数量</th>
                                    <th>小计</th>
                                    <th>操作</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="item" items="${cartItems}">
                                    <c:set var="book" value="${requestScope['book_'.concat(item.bookId)]}" />
                                    <tr>
                                        <td class="cart-item-info">
                                            <div class="cart-item-image">
                                                <img src="${empty book.coverImage ? pageContext.request.contextPath.concat('/images/default-cover.jpg') : pageContext.request.contextPath.concat('/images/').concat(book.coverImage)}" alt="${book.title}">
                                            </div>
                                            <div class="cart-item-details">
                                                <h3><a href="${pageContext.request.contextPath}/book/${book.id}">${book.title}</a></h3>
                                                <p>作者: ${book.author}</p>
                                            </div>
                                        </td>
                                        <td class="cart-item-price">￥${book.price}</td>
                                        <td class="cart-item-quantity">
                                            <form action="${pageContext.request.contextPath}/cart/update" method="post">
                                                <input type="hidden" name="cartItemId" value="${item.id}">
                                                <div class="quantity-control">
                                                    <button type="button" class="quantity-btn minus" onclick="decrementQuantity(this)">-</button>
                                                    <input type="number" name="quantity" value="${item.quantity}" min="1" max="${book.stock}" onchange="this.form.submit()">
                                                    <button type="button" class="quantity-btn plus" onclick="incrementQuantity(this)">+</button>
                                                </div>
                                            </form>
                                        </td>
                                        <td class="cart-item-subtotal">￥${book.price * item.quantity}</td>
                                        <td class="cart-item-actions">
                                            <form action="${pageContext.request.contextPath}/cart/remove" method="post">
                                                <input type="hidden" name="cartItemId" value="${item.id}">
                                                <button type="submit" class="btn btn-danger btn-sm">删除</button>
                                            </form>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                    
                    <div class="cart-summary">
                        <div class="cart-total">
                            <span>总计:</span>
                            <span class="total-price">￥${cartTotal}</span>
                        </div>
                        
                        <div class="cart-actions">
                            <a href="${pageContext.request.contextPath}/book" class="btn btn-secondary">继续购物</a>
                            <a href="${pageContext.request.contextPath}/cart/clear" class="btn btn-danger">清空购物车</a>
                            <a href="${pageContext.request.contextPath}/order/checkout" class="btn btn-primary">结算</a>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>
        
        <jsp:include page="../common/footer.jsp" />
    </div>
    
    <script>
        function decrementQuantity(button) {
            const input = button.nextElementSibling;
            if (input.value > 1) {
                input.value = parseInt(input.value) - 1;
                input.form.submit();
            }
        }
        
        function incrementQuantity(button) {
            const input = button.previousElementSibling;
            const max = parseInt(input.getAttribute('max'));
            if (parseInt(input.value) < max) {
                input.value = parseInt(input.value) + 1;
                input.form.submit();
            }
        }
    </script>
</body>
</html>
