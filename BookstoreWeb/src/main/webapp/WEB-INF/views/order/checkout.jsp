<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>结算 - 网上书城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../common/header.jsp" />
        
        <div class="main-content">
            <div class="checkout-container">
                <h2>订单结算</h2>
                
                <div class="checkout-steps">
                    <div class="step active">1. 确认订单</div>
                    <div class="step">2. 支付</div>
                    <div class="step">3. 完成</div>
                </div>
                
                <div class="order-summary">
                    <h3>订单商品</h3>
                    <table class="order-table">
                        <thead>
                            <tr>
                                <th>图书</th>
                                <th>单价</th>
                                <th>数量</th>
                                <th>小计</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="item" items="${cartItems}">
                                <c:set var="book" value="${requestScope['book_'.concat(item.bookId)]}" />
                                <tr>
                                    <td class="order-item-info">
                                        <div class="order-item-image">
                                            <img src="${empty book.coverImage ? pageContext.request.contextPath.concat('/images/default-cover.jpg') : pageContext.request.contextPath.concat('/images/').concat(book.coverImage)}" alt="${book.title}">
                                        </div>
                                        <div class="order-item-details">
                                            <h4>${book.title}</h4>
                                            <p>作者: ${book.author}</p>
                                        </div>
                                    </td>
                                    <td class="order-item-price">￥${book.price}</td>
                                    <td class="order-item-quantity">${item.quantity}</td>
                                    <td class="order-item-subtotal">￥${book.price * item.quantity}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <div class="shipping-info">
                    <h3>收货信息</h3>
                    <form action="${pageContext.request.contextPath}/order/create" method="post" id="checkoutForm">
                        <div class="form-group">
                            <label for="shippingAddress">收货地址:</label>
                            <textarea id="shippingAddress" name="shippingAddress" rows="3" required>${user.address}</textarea>
                        </div>
                        
                        <div class="order-total">
                            <span>订单总计:</span>
                            <span class="total-price">￥${cartTotal}</span>
                        </div>
                        
                        <div class="checkout-actions">
                            <a href="${pageContext.request.contextPath}/cart" class="btn btn-secondary">返回购物车</a>
                            <button type="submit" class="btn btn-primary">提交订单</button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
        
        <jsp:include page="../common/footer.jsp" />
    </div>
</body>
</html>
