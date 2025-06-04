<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<footer class="site-footer">
    <div class="footer-content">
        <div class="footer-section about">
            <h3>关于我们</h3>
            <p>网上书城是一个专注于提供优质图书的在线书店，致力于为读者提供丰富多样的阅读资源和便捷的购书体验。</p>
        </div>
        
        <div class="footer-section links">
            <h3>快速链接</h3>
            <ul>
                <li><a href="${pageContext.request.contextPath}/">首页</a></li>
                <li><a href="${pageContext.request.contextPath}/book">全部图书</a></li>
                <li><a href="${pageContext.request.contextPath}/user/register">注册</a></li>
                <li><a href="${pageContext.request.contextPath}/user/login">登录</a></li>
                <li><a href="${pageContext.request.contextPath}/cart">购物车</a></li>
            </ul>
        </div>
        
        <div class="footer-section contact">
            <h3>联系我们</h3>
            <p><i class="icon">📧</i> 邮箱: contact@bookstore.com</p>
            <p><i class="icon">📞</i> 电话: 400-123-4567</p>
            <p><i class="icon">🏢</i> 地址: 北京市海淀区中关村大街1号</p>
        </div>
    </div>
    
    <div class="footer-bottom">
        <p>&copy; 2025 网上书城 版权所有</p>
    </div>
</footer>
