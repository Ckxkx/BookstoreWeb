<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>用户管理 - 网上书城</title>
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
                    <li><a href="${pageContext.request.contextPath}/admin/orders">订单管理</a></li>
                    <li class="active"><a href="${pageContext.request.contextPath}/admin/users">用户管理</a></li>
                </ul>
            </div>
            
            <div class="admin-content">
                <h2>用户管理</h2>
                
                <div class="admin-filter">
                    <form action="${pageContext.request.contextPath}/admin/users" method="get">
                        <div class="filter-group">
                            <label for="role">用户角色:</label>
                            <select id="role" name="role">
                                <option value="">全部</option>
                                <option value="0" ${param.role == '0' ? 'selected' : ''}>普通用户</option>
                                <option value="1" ${param.role == '1' ? 'selected' : ''}>管理员</option>
                            </select>
                        </div>
                        
                        <div class="filter-group">
                            <label for="keyword">关键词:</label>
                            <input type="text" id="keyword" name="keyword" value="${param.keyword}" placeholder="用户名/邮箱">
                        </div>
                        
                        <div class="filter-group">
                            <button type="submit" class="btn btn-primary">筛选</button>
                            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">重置</a>
                        </div>
                    </form>
                </div>
                
                <div class="admin-table-container">
                    <table class="admin-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>用户名</th>
                                <th>邮箱</th>
                                <th>电话</th>
                                <th>注册时间</th>
                                <th>角色</th>
                                <th>操作</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach var="user" items="${users}">
                                <tr>
                                    <td>${user.id}</td>
                                    <td>${user.username}</td>
                                    <td>${user.email}</td>
                                    <td>${user.phone}</td>
                                    <td><fmt:formatDate value="${user.registerTime}" pattern="yyyy-MM-dd HH:mm:ss" /></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${user.role == 1}">
                                                <span class="role-admin">管理员</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="role-user">普通用户</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>
                                        <a href="javascript:void(0);" class="btn btn-sm btn-warning btn-edit-role" data-userid="${user.id}" data-username="${user.username}" data-role="${user.role}">编辑</a>
                                        <c:if test="${sessionScope.user.id != user.id}">
                                            <form action="${pageContext.request.contextPath}/admin/delete-user" method="post" style="display: inline;" onsubmit="return confirm('确定要删除此用户吗？');">
                                                <input type="hidden" name="userId" value="${user.id}">
                                                <button type="submit" class="btn btn-sm btn-danger">删除</button>
                                            </form>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                
                <div class="admin-pagination">
                    <c:if test="${currentPage > 1}">
                        <a href="${pageContext.request.contextPath}/admin/users?page=${currentPage - 1}&role=${param.role}&keyword=${param.keyword}" class="btn btn-sm btn-secondary">上一页</a>
                    </c:if>
                    
                    <span class="page-info">第 ${currentPage} 页，共 ${totalPages} 页</span>
                    
                    <c:if test="${currentPage < totalPages}">
                        <a href="${pageContext.request.contextPath}/admin/users?page=${currentPage + 1}&role=${param.role}&keyword=${param.keyword}" class="btn btn-sm btn-secondary">下一页</a>
                    </c:if>
                </div>
            </div>
        </div>
        
        <jsp:include page="../common/footer.jsp" />
    </div>
    <!-- 角色设置模态框 -->
    <div class="modal" id="roleModal" style="display:none;position:fixed;z-index:9999;left:0;top:0;width:100vw;height:100vh;background:rgba(0,0,0,0.3);align-items:center;justify-content:center;">
      <div style="background:#fff;padding:30px 20px;border-radius:8px;min-width:260px;max-width:90vw;">
        <h3 id="roleModalTitle">设置用户角色</h3>
        <p id="roleModalText"></p>
        <div style="margin-top:20px;text-align:right;">
          <button id="setRoleBtn" class="btn btn-primary"></button>
          <button type="button" class="btn btn-secondary" onclick="closeRoleModal()">取消</button>
        </div>
      </div>
    </div>
    <script>
    function closeRoleModal() {
      document.getElementById('roleModal').style.display = 'none';
    }
    document.querySelectorAll('.btn-edit-role').forEach(function(btn) {
      btn.addEventListener('click', function() {
        var userId = this.getAttribute('data-userid');
        var username = this.getAttribute('data-username');
        var role = this.getAttribute('data-role');
        var modal = document.getElementById('roleModal');
        var setRoleBtn = document.getElementById('setRoleBtn');
        var roleModalText = document.getElementById('roleModalText');
        if (role == '1') {
          setRoleBtn.textContent = '设为普通用户';
          roleModalText.textContent = '当前用户【' + username + '】是管理员，确定要设为普通用户吗？';
          setRoleBtn.onclick = function() { updateRole(userId, 0); };
        } else {
          setRoleBtn.textContent = '设为管理员';
          roleModalText.textContent = '当前用户【' + username + '】是普通用户，确定要设为管理员吗？';
          setRoleBtn.onclick = function() { updateRole(userId, 1); };
        }
        modal.style.display = 'flex';
      });
    });
    function updateRole(userId, newRole) {
      var xhr = new XMLHttpRequest();
      xhr.open('POST', '${pageContext.request.contextPath}/admin/update-role', true);
      xhr.setRequestHeader('Content-Type', 'application/x-www-form-urlencoded');
      xhr.onreadystatechange = function() {
        if (xhr.readyState === 4) {
          if (xhr.status === 200) {
            var res = JSON.parse(xhr.responseText);
            if (res.success) {
              location.reload();
            } else {
              alert('操作失败：' + (res.message || '未知错误'));
            }
          } else {
            alert('请求失败');
          }
        }
      };
      xhr.send('userId=' + encodeURIComponent(userId) + '&role=' + encodeURIComponent(newRole));
      closeRoleModal();
    }
    // 点击模态框外部关闭
    window.onclick = function(event) {
      var modal = document.getElementById('roleModal');
      if (event.target === modal) {
        closeRoleModal();
      }
    };
    </script>
</body>
</html>
