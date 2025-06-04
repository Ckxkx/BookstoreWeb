<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>订单管理 - 网上书城</title>
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
                    <li><a href="${pageContext.request.contextPath}/admin">控制面板</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/books">图书管理</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/admin/orders">订单管理</a></li>
                    <li><a href="${pageContext.request.contextPath}/admin/users">用户管理</a></li>
                </ul>
            </div>
            
            <div class="admin-content">
                <h2>订单管理</h2>
                
                <div class="admin-actions" style="margin-bottom:20px;">
                    <button class="btn btn-primary" onclick="showAddOrderModal()">新增订单</button>
                </div>
                
                <div class="admin-filter">
                    <form action="${pageContext.request.contextPath}/admin/orders" method="get">
                        <div class="filter-group">
                            <label for="status">订单状态:</label>
                            <select id="status" name="status">
                                <option value="">全部</option>
                                <option value="待付款" ${param.status == '待付款' ? 'selected' : ''}>待付款</option>
                                <option value="待发货" ${param.status == '待发货' ? 'selected' : ''}>待发货</option>
                                <option value="已发货" ${param.status == '已发货' ? 'selected' : ''}>已发货</option>
                                <option value="已完成" ${param.status == '已完成' ? 'selected' : ''}>已完成</option>
                                <option value="已取消" ${param.status == '已取消' ? 'selected' : ''}>已取消</option>
                            </select>
                        </div>
                        
                        <div class="filter-group">
                            <label for="dateRange">日期范围:</label>
                            <select id="dateRange" name="dateRange">
                                <option value="">全部时间</option>
                                <option value="today" ${param.dateRange == 'today' ? 'selected' : ''}>今天</option>
                                <option value="yesterday" ${param.dateRange == 'yesterday' ? 'selected' : ''}>昨天</option>
                                <option value="thisWeek" ${param.dateRange == 'thisWeek' ? 'selected' : ''}>本周</option>
                                <option value="thisMonth" ${param.dateRange == 'thisMonth' ? 'selected' : ''}>本月</option>
                            </select>
                        </div>
                        
                        <div class="filter-group">
                            <button type="submit" class="btn btn-primary">筛选</button>
                            <a href="${pageContext.request.contextPath}/admin/orders" class="btn btn-secondary">重置</a>
                        </div>
                    </form>
                </div>
                
                <div class="admin-table-container">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>订单ID</th>
                                <th>用户</th>
                                <th>总金额</th>
                                <th>收货地址</th>
                                <th>订单状态</th>
                                <th>下单时间</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="order" items="${orders}">
                                <tr>
                                    <td>${order.id}</td>
                                    <td>${orderUserMap[order.id]}</td>
                                    <td>￥${order.totalAmount}</td>
                                    <td>${order.shippingAddress}</td>
                                    <td>${order.status}</td>
                                    <td><fmt:formatDate value="${order.orderTime}" pattern="yyyy-MM-dd HH:mm:ss"/></td>
                                    <td>
                                        <button class="btn btn-info" onclick="showOrderDetails('${order.id}')">查看详情</button>
                                        <form action="${pageContext.request.contextPath}/admin/delete-order" method="post" style="display:inline;" onsubmit="return confirm('确定要删除该订单吗？');">
                                            <input type="hidden" name="orderId" value="${order.id}">
                                            <button type="submit" class="btn btn-danger">删除</button>
                                        </form>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <div class="admin-pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/admin/orders?page=${currentPage - 1}&status=${param.status}&dateRange=${param.dateRange}" class="btn btn-sm btn-secondary">上一页</a>
                    </c:if>
                    
                    <span class="page-info">第 ${currentPage} 页，共 ${totalPages} 页</span>
                    
                    <c:if test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/admin/orders?page=${currentPage + 1}&status=${param.status}&dateRange=${param.dateRange}" class="btn btn-sm btn-secondary">下一页</a>
                    </c:if>
                </div>
            </div>
        </div>
        
        <jsp:include page="../common/footer.jsp" />
    </div>
    
    <div id="orderDetailsModal" class="modal" style="display: none;">
        <div class="modal-content">
            <h3>订单详情</h3>
            <div id="orderDetailsContent"></div>
            <button class="btn btn-secondary" onclick="hideOrderDetails()">关闭</button>
        </div>
    </div>
    
    <div id="addOrderModal" class="modal" style="display:none;position:fixed;z-index:9999;left:0;top:0;width:100vw;height:100vh;background:rgba(0,0,0,0.3);align-items:center;justify-content:center;">
      <div style="background:#fff;padding:30px 20px;border-radius:8px;min-width:260px;max-width:90vw;">
        <h3>新增订单</h3>
        <form id="addOrderForm" method="post" action="${pageContext.request.contextPath}/admin/add-order">
          <div class="form-group">
            <label>用户ID:</label>
            <input type="number" name="userId" required>
          </div>
          <div class="form-group">
            <label>收货地址:</label>
            <input type="text" name="shippingAddress" required>
          </div>
          <div class="form-group">
            <label>总金额:</label>
            <input type="number" name="totalAmount" step="0.01" required>
          </div>
          <div class="form-group">
            <label>订单状态:</label>
            <select name="status">
              <option value="待付款">待付款</option>
              <option value="待发货">待发货</option>
              <option value="已发货">已发货</option>
              <option value="已完成">已完成</option>
              <option value="已取消">已取消</option>
            </select>
          </div>
          <div style="text-align:right;">
            <button type="submit" class="btn btn-primary">提交</button>
            <button type="button" class="btn btn-secondary" onclick="closeAddOrderModal()">取消</button>
          </div>
        </form>
      </div>
    </div>
    
    <script>
        function showOrderDetails(orderId) {
            var modal = document.getElementById('orderDetailsModal');
            var content = document.getElementById('orderDetailsContent');
            content.innerHTML = '加载中...';
            modal.style.display = 'block';
            fetch('${pageContext.request.contextPath}/admin/order-detail?id=' + orderId)
                .then(res => res.text())
                .then(html => { content.innerHTML = html; })
                .catch(() => { content.innerHTML = '加载失败'; });
        }
        
        function hideOrderDetails() {
            document.getElementById('orderDetailsModal').style.display = 'none';
        }
        
        function showAddOrderModal() {
            document.getElementById('addOrderModal').style.display = 'flex';
        }
        
        function closeAddOrderModal() {
            document.getElementById('addOrderModal').style.display = 'none';
        }
    </script>
</body>
</html>
