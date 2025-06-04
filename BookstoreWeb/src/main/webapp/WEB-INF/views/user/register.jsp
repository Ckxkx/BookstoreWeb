<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户注册 - 网上书城</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <div class="container">
        <jsp:include page="../common/header.jsp" />
        
        <div class="main-content">
            <div class="register-form">
                <h2>用户注册</h2>
                
                <c:if test="${not empty errorMessage}">
                    <div class="error-message">
                        <p>${errorMessage}</p>
                    </div>
                </c:if>
                
                <form action="${pageContext.request.contextPath}/user/register" method="post">
                    <div class="form-group">
                        <label for="username">用户名:</label>
                        <input type="text" id="username" name="username" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="password">密码:</label>
                        <input type="password" id="password" name="password" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="email">邮箱:</label>
                        <input type="email" id="email" name="email" required>
                    </div>
                    
                    <div class="form-group">
                        <label for="phone">电话:</label>
                        <input type="text" id="phone" name="phone">
                    </div>
                    
                    <div class="form-group">
                        <label for="address">地址:</label>
                        <textarea id="address" name="address" rows="3"></textarea>
                    </div>
                    
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary">注册</button>
                    </div>
                    
                    <div class="form-links">
                        <p>已有账号? <a href="${pageContext.request.contextPath}/user/login">立即登录</a></p>
                    </div>
                </form>
            </div>
        </div>
        
        <jsp:include page="../common/footer.jsp" />
    </div>
</body>
</html>
