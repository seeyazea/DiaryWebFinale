<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="com.tirmizee.mvc.model.User" %>

<%
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
<title>Edit Profile</title>

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

<%@ include file="/WEB-INF/views/components/sidebar.jsp" %>
<div class="topbar">
    <button class="menu-btn" onclick="openSidebar()">☰</button>
    <div class="topbar-title">Edit Profile</div>
</div>
<div class="container">
    <h2>👤 프로필 수정</h2>
    <form action="<%=request.getContextPath()%>/editProfile" method="post">
        <div class="label">이름</div>
        <input class="input" type="text" name="fullname" value="<%= user.getFullname() %>" required>
        <div class="label">이메일</div>
        <input class="input" type="email" name="email" value="<%= user.getEmail() %>" required>
        <button class="btn-save" type="submit">저장하기</button>
        <a href="<%=request.getContextPath()%>/profile" class="btn-back">⬅ 취소</a>
    </form>
</div>
</body>
</html>
