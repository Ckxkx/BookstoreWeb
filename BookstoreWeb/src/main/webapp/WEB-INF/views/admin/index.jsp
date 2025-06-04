<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>管理员首页 - 网上书城</title>
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
                    <li class="active"><a href="${pageContext.request.contextPath}/admin">控制面板</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/books">图书管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/orders">订单管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/users">用户管理</a></li>
                </ul>
            </div>
            
            <div class="admin-content">
                <h2>控制面板</h2>
                
                <div class="dashboard-stats">
                    <div class="stat-card">
                        <div class="stat-icon orders-icon">📦</div>
                        <div class="stat-info">
                            <h3>订单总数</h3>
                            <p class="stat-value">${orderCount}</p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon users-icon">👤</div>
                        <div class="stat-info">
                            <h3>用户总数</h3>
                            <p class="stat-value">${userCount}</p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon books-icon">📚</div>
                        <div class="stat-info">
                            <h3>图书总数</h3>
                            <p class="stat-value">${bookCount}</p>
                        </div>
                    </div>
                    
                    <div class="stat-card">
                        <div class="stat-icon sales-icon">💰</div>
                        <div class="stat-info">
                            <h3>销售总额</h3>
                            <p class="stat-value">￥${totalSales}</p>
                        </div>
                    </div>
                </div>
                
                <div class="recent-orders">
                    <h3>最近订单</h3>
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>订单ID</th>
                                <th>用户</th>
                                <th>金额</th>
                                <th>状态</th>
                                <th>时间</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${recentOrders}">
                                <tr>
                                    <td>${order.id}</td>
                                    <td>${order.username}</td>
                                    <td>￥${order.totalAmount}</td>
                                    <td><span class="status-${order.status}">${order.status}</span></td>
                                    <td><fmt:formatDate value="${order.orderTime}" pattern="yyyy-MM-dd HH:mm" /></td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/order/${order.id}" class="btn btn-sm btn-info">查看</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                    <a href="${pageContext.request.contextPath}/admin/orders" class="view-all">查看所有订单</a>
                </div>
            </div>
        </div>
        
        <jsp:include page="../common/footer.jsp" />
    </div>
</body>
</html>
