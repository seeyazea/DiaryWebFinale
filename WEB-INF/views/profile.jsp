w<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.tirmizee.mvc.model.User" %>
<%
    // Проверка логина
    User user = (User) session.getAttribute("loginUser");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>프로필</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/sidebar.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/profile.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/dark.css">

</head>

<body>
<script>
    if (localStorage.getItem("dark") === "true") {
        document.body.classList.add("dark");
    }
</script>
<!-- Подключаем Sidebar -->
<%@ include file="/WEB-INF/views/components/sidebar.jsp" %>

<!-- TOPBAR -->
<div class="topbar">
    <button class="menu-btn" onclick="openSidebar()">☰</button>
    <div class="topbar-title">Profile</div>
</div>

<div class="container">
    <div class="avatar"><%= user.getFullname().toUpperCase().charAt(0) %></div>
    <div class="username"><%= user.getFullname() %></div>
    <div class="email"><%= user.getEmail() %></div>
    <div class="section-title">프로필 수정</div>
    <form>
        <input class="input" type="text" value="<%= user.getFullname() %>" readonly>
        <input class="input" type="email" value="<%= user.getEmail() %>" readonly>
    <a class="btn-save" href="<%=request.getContextPath()%>/editProfile">수정하기</a>
    </form>

     <a class="btn-back" href="<%=request.getContextPath()%>/dashboard">
        ⬅ 뒤로가기</a>
    

</div>

</body>
</html>
