<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>订单详情 - 网上书城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../common/header.jsp" />
        
        <div class="main-content">
            <div class="order-detail-container">
                <h2>订单详情</h2>
                
                <div class="order-info">
                    <div class="order-info-item">
                        <span class="label">订单号:</span>
                        <span class="value">${order.id}</span>
                    </div>
                    <div class="order-info-item">
                        <span class="label">下单时间:</span>
                        <span class="value"><fmt:formatDate value="${order.orderTime}" pattern="yyyy-MM-dd HH:mm:ss" /></span>
                    </div>
                    <div class="order-info-item">
                        <span class="label">订单状态:</span>
                        <span class="value status-${order.status}">${order.status}</span>
                    </div>
                    <div class="order-info-item">
                        <span class="label">收货地址:</span>
                        <span class="value">${order.shippingAddress}</span>
                    </div>
                    <div class="order-info-item">
                        <span class="label">订单总额:</span>
                        <span class="value price">￥${order.totalAmount}</span>
                    </div>
                </div>
                
                <div class="order-timeline">
                    <div class="timeline-item ${order.orderTime != null ? 'active' : ''}">
                        <div class="timeline-point"></div>
                        <div class="timeline-content">
                            <h4>下单</h4>
                            <p><fmt:formatDate value="${order.orderTime}" pattern="yyyy-MM-dd HH:mm:ss" /></p>
                        </div>
                    </div>
                    <div class="timeline-item ${order.paymentTime != null ? 'active' : ''}">
                        <div class="timeline-point"></div>
                        <div class="timeline-content">
                            <h4>付款</h4>
                            <p>
                                <c:choose>
                                    <c:when test="${order.paymentTime != null}">
                                        <fmt:formatDate value="${order.paymentTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                    </c:when>
                                    <c:otherwise>
                                        待付款
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                    <div class="timeline-item ${order.shippingTime != null ? 'active' : ''}">
                        <div class="timeline-point"></div>
                        <div class="timeline-content">
                            <h4>发货</h4>
                            <p>
                                <c:choose>
                                    <c:when test="${order.shippingTime != null}">
                                        <fmt:formatDate value="${order.shippingTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                    </c:when>
                                    <c:otherwise>
                                        待发货
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                    <div class="timeline-item ${order.completionTime != null ? 'active' : ''}">
                        <div class="timeline-point"></div>
                        <div class="timeline-content">
                            <h4>完成</h4>
                            <p>
                                <c:choose>
                                    <c:when test="${order.completionTime != null}">
                                        <fmt:formatDate value="${order.completionTime}" pattern="yyyy-MM-dd HH:mm:ss" />
                                    </c:when>
                                    <c:otherwise>
                                        待完成
                                    </c:otherwise>
                                </c:choose>
                            </p>
                        </div>
                    </div>
                </div>
                
                <div class="order-items">
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
                            <c:forEach var="item" items="${orderItems}">
                                <c:set var="book" value="${requestScope['book_'.concat(item.bookId)]}" />
                                <tr>
                                    <td class="order-item-info">
                                        <div class="order-item-image">
                                            <img src="${empty book.coverImage ? pageContext.request.contextPath.concat('/images/default-cover.jpg') : pageContext.request.contextPath.concat('/images/').concat(book.coverImage)}" alt="${book.title}">
                                        </div>
                                        <div class="order-item-details">
                                            <h4><a href="${pageContext.request.contextPath}/book/${book.id}">${book.title}</a></h4>
                                            <p>作者: ${book.author}</p>
                                        </div>
                                    </td>
                                    <td class="order-item-price">￥${item.price}</td>
                                    <td class="order-item-quantity">${item.quantity}</td>
                                    <td class="order-item-subtotal">￥${item.price * item.quantity}</td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <div class="order-actions">
                    <a href="${pageContext.request.contextPath}/order" class="btn btn-secondary">返回订单列表</a>
                    
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
                    
                    <c:if test="${order.status == '已完成' && not empty sessionScope.user && sessionScope.user.role == 1}">
                        <form action="${pageContext.request.contextPath}/order/update-status" method="post" style="display: inline;">
                            <input type="hidden" name="orderId" value="${order.id}">
                            <select name="status">
                                <option value="待付款">待付款</option>
                                <option value="待发货">待发货</option>
                                <option value="已发货">已发货</option>
                                <option value="已完成">已完成</option>
                                <option value="已取消">已取消</option>
                            </select>
                            <button type="submit" class="btn btn-warning">更新状态</button>
                        </form>
                    </c:if>
                </div>
            </div>
        </div>
        
        <jsp:include page="../common/footer.jsp" />
    </div>
</body>
</html>
