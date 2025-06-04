<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>个人信息 - 网上书城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../common/header.jsp" />
        
        <div class="main-content">
            <div class="profile-container">
                <h2>个人信息</h2>
                
                <c:if test="${not empty successMessage}">
                    <div class="success-message">
                        <p>${successMessage}</p>
                    </div>
                </c:if>
                
                <c:if test="${not empty errorMessage}">
                    <div class="error-message">
                        <p>${errorMessage}</p>
                    </div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/user/update" method="post">
                    <div class="form-group">
                        <label for="username">用户名:</label>
                        <input type="text" id="username" value="${user.username}" readonly>
                    </div>
                    
                    <div class="form-group">
                        <label for="password">密码:</label>
                        <input type="password" id="password" name="password" value="${user.password}" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="email">邮箱:</label>
                        <input type="email" id="email" name="email" value="${user.email}" required>
                        <c:if test="${empty user.emailVerified}">
                            <div class="verification-notice">
                                <p>邮箱未验证 <a href="${pageContext.request.contextPath}/user/verify-email">发送验证邮件</a></p>
                            </div>
                        </c:if>
                    </div>
                    
                    <div class="form-group">
                        <label for="phone">电话:</label>
                        <input type="text" id="phone" name="phone" value="${user.phone}">
                    </div>
                    
                    <div class="form-group">
                        <label for="address">地址:</label>
                        <textarea id="address" name="address" rows="3">${user.address}</textarea>
                    </div>
                    
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">更新信息</button>
                    </div>
                </form>
                
                <div class="user-orders">
                    <h3>我的订单</h3>
                    <a href="${pageContext.request.contextPath}/order" class="btn btn-secondary">查看所有订单</a>
                </div>
            </div>
        </div>
        
        <jsp:include page="../common/footer.jsp" />
    </div>
</body>
</html>
