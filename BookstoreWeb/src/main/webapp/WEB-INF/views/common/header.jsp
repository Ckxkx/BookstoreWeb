<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<header class="site-header">
    <div class="header-top">
        <div class="logo">
            <a href="${pageContext.request.contextPath}/">
                <h1>网上书城</h1>
            </a>
        </div>
        
        <div class="search-box">
            <form action="${pageContext.request.contextPath}/book/search" method="get">
                <input type="text" name="keyword" placeholder="搜索图书..." value="${keyword}">
                <button type="submit" class="search-btn">搜索</button>
            </form>
        </div>
        
        <div class="user-actions">
            <c:choose>
                <c:when test="${empty sessionScope.user}">
                    <a href="${pageContext.request.contextPath}/user/login" class="btn btn-outline">登录</a>
                    <a href="${pageContext.request.contextPath}/user/register" class="btn btn-primary">注册</a>
                </c:when>
                <c:otherwise>
                    <div class="user-menu" id="userMenu">
                        <button id="userMenuBtn" class="user-greeting">你好，${sessionScope.user.username}</button>
                        <div class="user-dropdown" id="userDropdownMenu">
                            <a href="${pageContext.request.contextPath}/user/profile">个人中心</a>
                            <a href="${pageContext.request.contextPath}/order">我的订单</a>
                            <c:if test="${sessionScope.user.role == 1}">
                                <a href="${pageContext.request.contextPath}/admin">后台管理</a>
                            </c:if>
                            <a href="${pageContext.request.contextPath}/user/logout">退出登录</a>
                        </div>
                    </div>
                </c:otherwise>
            </c:choose>
            
            <a href="${pageContext.request.contextPath}/cart" class="cart-icon">
                <span class="icon">🛒</span>
                <c:if test="${not empty sessionScope.cartItemCount && sessionScope.cartItemCount > 0}">
                    <span class="cart-count">${sessionScope.cartItemCount}</span>
                </c:if>
            </a>
        </div>
    </div>
    
    <nav class="main-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/">首页</a></li>
            <!-- <li><a href="${pageContext.request.contextPath}/book">全部图书</a></li> -->
            <li><a href="${pageContext.request.contextPath}/book/category?category=文学">文学</a></li>
            <li><a href="${pageContext.request.contextPath}/book/category?category=计算机">计算机</a></li>
            <li><a href="${pageContext.request.contextPath}/book/category?category=经济管理">经济管理</a></li>
            <li><a href="${pageContext.request.contextPath}/book/category?category=科学技术">科学技术</a></li>
            <li><a href="${pageContext.request.contextPath}/book/category?category=教育">教育</a></li>
            <li><a href="${pageContext.request.contextPath}/book/category?category=艺术">艺术</a></li>
            <li><a href="${pageContext.request.contextPath}/book/category?category=生活">生活</a></li>
            <li><a href="${pageContext.request.contextPath}/book/category?category=童书">童书</a></li>
        </ul>
    </nav>
</header>
<style>
.user-dropdown { display: none; position: absolute; top: 100%; right: 0; background-color: white; box-shadow: 0 2px 5px rgba(0,0,0,0.2); border-radius: 4px; width: 150px; z-index: 100; }
.user-menu.open .user-dropdown { display: block; }
.user-greeting { background: none; border: none; font: inherit; cursor: pointer; padding: 5px; }
</style>
<script>
document.addEventListener('DOMContentLoaded', function() {
    var menuBtn = document.getElementById('userMenuBtn');
    var menu = document.getElementById('userMenu');
    document.body.addEventListener('click', function() {
        menu.classList.remove('open');
    });
    menuBtn.addEventListener('click', function(e) {
        e.stopPropagation();
        menu.classList.toggle('open');
    });
    // 防止点击菜单内容时关闭
    document.getElementById('userDropdownMenu').addEventListener('click', function(e) {
        e.stopPropagation();
    });
});
</script>
