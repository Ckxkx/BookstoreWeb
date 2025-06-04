<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>我的订单 - 网上书城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../common/header.jsp" />
        
        <div class="main-content">
            <div class="order-list-container">
                <h2>我的订单</h2>
                
                <c:if test="${empty orders}">
                    <div class="empty-orders">
                        <p>您还没有订单</p>
                        <a href="${pageContext.request.contextPath}/book" class="btn btn-primary">去购物</a>
                    </div>
                </c:if>
                
                <c:if test="${not empty orders}">
                    <div class="order-list">
                        <c:forEach var="order" items="${orders}">
                            <div class="order-item">
                                <div class="order-header">
                                    <div class="order-id">
                                        <span>订单号: </span>
                                        <a href="${pageContext.request.contextPath}/order/${order.id}">${order.id}</a>
                                    </div>
                                    <div class="order-date">
                                        <span>下单时间: </span>
                                        <fmt:formatDate value="${order.orderTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                    </div>
                                    <div class="order-status">
                                        <span>状态: </span>
                                        <span class="status-${order.status}">${order.status}</span>
                                    </div>
                                </div>
                                
                                <div class="order-summary">
                                    <div class="order-books">
                                        <div class="order-book-item">
                                            <c:set var="firstBook" value="${requestScope['firstBook_'.concat(order.id)]}" />
                                            <img src="${empty firstBook.coverImage ? pageContext.request.contextPath.concat('/images/default-cover.jpg') : pageContext.request.contextPath.concat('/images/').concat(firstBook.coverImage)}" alt="${empty firstBook ? '订单图书' : firstBook.title}">
                                        </div>
                                    </div>
                                    
                                    <div class="order-total">
                                        <span>总计: </span>
                                        <span class="total-price">￥${order.totalAmount}</span>
                                    </div>
                                    
                                    <div class="order-actions">
                                        <a href="${pageContext.request.contextPath}/order/${order.id}" class="btn btn-info">查看详情</a>
                                        
                                        <c:if test="${order.status == '待付款'}">
                                            <a href="${pageContext.request.contextPath}/order/pay?orderId=${order.id}" class="btn btn-primary">去支付</a>
                                            <form action="${pageContext.request.contextPath}/order/cancel" method="post" style="display: inline;">
                                                <input type="hidden" name="orderId" value="${order.id}">
                                                <button type="submit" class="btn btn-danger">取消订单</button>
                                            </form>
                                        </c:if>
                                        
                                        <c:if test="${order.status == '已发货'}">
                                            <form action="${pageContext.request.contextPath}/order/confirm" method="post" style="display: inline;">
                                                <input type="hidden" name="orderId" value="${order.id}">
                                                <button type="submit" class="btn btn-success">确认收货</button>
                                            </form>
                                        </c:if>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                </c:if>
            </div>
        </div>
        
        <jsp:include page="../common/footer.jsp" />
    </div>
</body>
</html>
