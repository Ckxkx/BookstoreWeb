<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>管理员控制面板 - 网上书城</title>
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
                <h2>管理员控制面板</h2>
                
                <div class="dashboard-stats" style="display: flex; justify-content: center; gap: 30px; margin-bottom: 30px;">
                    <div class="stat-card" style="flex: 1; min-width: 180px; max-width: 220px; text-align: center;">
                        <h3>用户总数</h3>
                        <p>${userCount}</p>
                    </div>
                    <div class="stat-card" style="flex: 1; min-width: 180px; max-width: 220px; text-align: center;">
                        <h3>图书总数</h3>
                        <p>${bookCount}</p>
                    </div>
                    <div class="stat-card" style="flex: 1; min-width: 180px; max-width: 220px; text-align: center;">
                        <h3>订单总数</h3>
                        <p>${orderCount}</p>
                    </div>
                    <div class="stat-card" style="flex: 1; min-width: 180px; max-width: 220px; text-align: center;">
                        <h3>今日销售额</h3>
                        <p>￥${todaySales}</p>
                    </div>
                </div>
                
                <div class="quick-actions">
                    <h3>快捷操作</h3>
                    <div class="action-buttons">
                        <a href="${pageContext.request.contextPath}/admin/books?action=add" class="btn btn-primary">添加新书</a>
                        <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-primary">查看订单</a>
                        <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-primary">管理用户</a>
                    </div>
                </div>
            </div>
        </div>
        
        <jsp:include page="../common/footer.jsp" />
    </div>
</body>
</html> 