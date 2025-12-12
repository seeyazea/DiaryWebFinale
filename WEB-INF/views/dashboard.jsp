<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.tirmizee.mvc.model.User" %>

<%
    // Если открыли напрямую, перенаправляем на сервлет
    if (request.getAttribute("totalEntries") == null) {
        response.sendRedirect(request.getContextPath() + "/dashboard");
        return;
    }

    User loginUser = (User) session.getAttribute("loginUser");
    if (loginUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Dashboard</title>

<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/sidebar.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/dashboard.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/dark.css">

<script>
function openSidebar() {
    document.getElementById("sidebar").classList.add("open");
    document.getElementById("sidebarBackdrop").classList.add("show");
}
function closeSidebar() {
    document.getElementById("sidebar").classList.remove("open");
    document.getElementById("sidebarBackdrop").classList.remove("show");
}
</script>

</head>

<body>
<script>
    if (localStorage.getItem("dark") === "true") {
        document.body.classList.add("dark");
    }
</script>

<%@ include file="/WEB-INF/views/components/sidebar.jsp" %>

<!-- TOP BAR -->
<div class="topbar">
    <button class="menu-btn" onclick="openSidebar()">☰</button>
    <div class="topbar-title">Dashboard</div>
</div>

<!-- MAIN -->
<div class="page">
    <div class="container">

        <div class="user-hello">
            <div class="user-hello-title">
                Hello, <%= loginUser.getFullname() %> 👋
            </div>
            <div class="user-hello-sub">
                <%= loginUser.getEmail() %>
            </div>
        </div>

        <div class="cards">

            <a class="card" href="<%=request.getContextPath()%>/diary?action=list">
                <div class="card-title">📘 My Diary</div>
                <div class="card-sub">View all entries</div>
            </a>

            <a class="card" href="<%=request.getContextPath()%>/diary?action=write">
                <div class="card-title">📝 Write New Entry</div>
                <div class="card-sub">Create a new page</div>
            </a>

            <a class="card" href="<%=request.getContextPath()%>/profile">
                <div class="card-title">👤 Profile</div>
                <div class="card-sub">User information</div>
            </a>

            <a class="card" href="<%=request.getContextPath()%>/settings">
                <div class="card-title">⚙️ Settings</div>
                <div class="card-sub">Dark mode, notifications</div>
            </a>

        </div>

        <div class="stats">
            <div class="stat-card">
                <div class="stat-label">Total Entries</div>
                <div class="stat-value">${totalEntries}</div>
            </div>

            <div class="stat-card">
                <div class="stat-label">This Month</div>
                <div class="stat-value">${entriesThisMonth}</div>
            </div>

           <!--  <div class="stat-card">
                <div class="stat-label">Photo Entries</div>
                <div class="stat-value">${photosCount}</div>
            </div>  i close cuz after change the write.jsp summernote (photo insane note box do not count like photo)  -->

            <div class="stat-card">
                <div class="stat-label">Streak</div>
                <div class="stat-value">${streakDays} days</div>         
        </div>
        
<a class="card" href="<%=request.getContextPath()%>/calendar">
    <div class="card-icon">📅</div>
    <div class="card-title">Calendar View</div>
    <div class="card-subtitle">View monthly diary</div>
</a>
        <form action="<%=request.getContextPath()%>/auth" method="get">
            <input type="hidden" name="action" value="logout">
            <button class="logout-btn" type="submit">Logout</button>
        </form>

    </div>
</div>
</body>
</html>
