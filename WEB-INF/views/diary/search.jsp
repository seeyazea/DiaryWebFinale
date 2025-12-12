<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Search Diary</title>

<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/sidebar.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/search.css">
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

<!-- SIDEBAR -->
<%@ include file="/WEB-INF/views/components/sidebar.jsp" %>


<!-- TOPBAR -->
<div class="topbar">
    <button class="menu-btn" type="button" onclick="openSidebar()">☰</button>
    <div class="topbar-title">Search</div>
</div>

<!-- PAGE CONTENT -->
<div class="page">
    <div class="container">

        <h2>🔍 Search Diary</h2>

        <form action="<%=request.getContextPath()%>/diary" method="get" class="search-box">
            <input type="hidden" name="action" value="search">
            <input type="text" name="keyword" placeholder="Enter keyword..."
                   value="${keyword}" required>
            <button type="submit">Search</button>
        </form>

        <hr>

        <!-- No results -->
        <c:if test="${empty results}">
            <div class="no-results">No entries found.</div>
        </c:if>

        <!-- Results -->
        <c:forEach var="d" items="${results}">
            <div class="result-item">
                <a href="<%=request.getContextPath()%>/diary?action=view&id=${d.id}">
                    ${d.title}
                </a>
                <div style="font-size: 13px; color: gray;">
                    ${d.diaryDate}
                </div>
            </div>
        </c:forEach>

    </div>
</div>

</body>
</html>
