<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.tirmizee.mvc.model.User" %>
<%
    // Проверка авторизации
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
<title>설정</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/sidebar.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/settings.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/dark.css">


</head>

<body>
<script>
    if (localStorage.getItem("dark") === "true") {
        document.body.classList.add("dark");
    }
</script>

<!-- Подключаем единый Sidebar -->
<%@ include file="/WEB-INF/views/components/sidebar.jsp" %>

<!-- TOPBAR -->
<div class="topbar">
    <button class="menu-btn" onclick="openSidebar()">☰</button>
    <div style="font-size:16px; font-weight:600;">Settings</div>
</div>

<!-- MAIN CONTENT -->
<div class="container">
    <h2>⚙️ 설정</h2>

<div class="item">
    <span class="label">다크모드</span>

    <label class="switch">
        <input type="checkbox" id="darkModeToggle">
        <span class="slider"></span>
    </label>
</div>


<div class="item">
        <span class="label">알림 설정</span>
        <span class="status">(준비중)</span>
    </div>
<div class="item">
        <span class="label">언어 설정</span>
        <span class="status">(준비중)</span>
        </div>

 <div class="item">
        <span class="label">비밀번호 변경</span>
        <span class="status">(준비중)</span>
        </div>
 <a class="btn-back" href="<%=request.getContextPath()%>/dashboard">
        ⬅ 뒤로가기</a>

</div>
<script>
    const toggle = document.getElementById("darkModeToggle");
    // Проверка сохранённого режима
    if (localStorage.getItem("dark") === "true") {
        document.body.classList.add("dark");
        toggle.checked = true;
    }
    // Переключение
    toggle.addEventListener("change", function() {
        if (toggle.checked) {
            document.body.classList.add("dark");
            localStorage.setItem("dark", "true");
        } else {
            document.body.classList.remove("dark");
            localStorage.setItem("dark", "false");
        }
    });
</script>

</body>
</html>
