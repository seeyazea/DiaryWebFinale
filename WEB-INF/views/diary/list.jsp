<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="com.tirmizee.mvc.model.Diary" %>
<%
    List<Diary> diaryList = (List<Diary>) request.getAttribute("diaryList");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>My Diary</title>

<!-- Подключаем CSS -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/sidebar.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/list.css">
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

    function openDiary(id) {
        window.location.href = "<%=request.getContextPath()%>/diary?action=view&id=" + id;
    }
</script>

</head>
<body>
<!-- ===== SIDEBAR ===== -->
<%@ include file="/WEB-INF/views/components/sidebar.jsp" %>

<!-- ===== TOPBAR ===== -->
<div class="topbar">
    <button class="menu-btn" type="button" onclick="openSidebar()">☰</button>
    <div class="topbar-title">My Diary</div>
</div>
<!-- ===== PAGE CONTENT ===== -->
<div class="page">
<%
    String backDate = request.getParameter("date"); // null или YYYY-MM-DD
    String backYear = null;
    String backMonth = null;

    if (backDate != null && backDate.length() >= 7) {
        backYear = backDate.substring(0, 4);
        backMonth = backDate.substring(5, 7);
    }
%>

<% if (backDate != null) { %>
<div class="back-calendar">
    <a href="<%=request.getContextPath()%>/calendar?year=<%=backYear%>&month=<%=backMonth%>">
        ⬅ 달력으로 돌아가기
    </a>
</div>
<% } %>

<style>
.back-calendar {
    margin: 12px 0 18px 0;
}
.back-calendar a {
    display: inline-block;
    padding: 10px 16px;
    background: #E8F0FF;
    color: #1A3A70;
    text-decoration: none;
    border-radius: 8px;
    font-weight: 600;
    box-shadow: 0 2px 4px rgba(0,0,0,0.15);
    transition: 0.2s;
}
.back-calendar a:hover {
    background: #D0E2FF;
}
</style>

    <div class="container">

        <div class="header">
            <div class="title">📘 My Diary</div>
            <a class="add-btn" href="<%=request.getContextPath()%>/diary?action=write">+ Create New Entry</a>
        </div>

        <!-- GRID -->
        <div class="grid">
            <%
                if (diaryList != null) {
                    for (Diary d : diaryList) {
            %>
            <div class="card" ondblclick="openDiary(<%=d.getId()%>)">

                <%-- Если у записи есть фото (BLOB) --%>
             <% if (d.getFirstImage() != null) { %>
    <div class="card-img">
        <img src="<%= d.getFirstImage() %>" alt="">
    </div>

<% } else if (d.hasPhoto()) { %>
    <div class="card-img">
        <img src="<%=request.getContextPath()%>/photo?id=<%= d.getId() %>" alt="">
    </div>

<% } else { %>
    <div class="card-img placeholder"></div>
<% } %>


                <div class="card-body">
                    <div class="card-title"><%= d.getTitle() %></div>
                    <div class="card-date"><%= d.getDiaryDate() %></div>
                </div>
            </div>
            <%
                    }
                }
            %>
        </div>
        <!-- PAGINATION -->
        <%
            Integer currentPage = (Integer) request.getAttribute("page");
            Integer totalPages = (Integer) request.getAttribute("totalPages");

            if (currentPage == null) currentPage = 1;
            if (totalPages == null) totalPages = 1;
        %>

        <div style="margin: 30px 0; text-align: center; font-size: 15px;">

            <% if (currentPage > 1) { %>
                <a href="<%=request.getContextPath()%>/diary?action=list&page=<%=currentPage - 1%>"
                   style="margin-right: 15px; text-decoration:none;">
                    ⟨ Previous
                </a>
            <% } %>

            <% for (int p = 1; p <= totalPages; p++) {
                    if (p == currentPage) {
            %>
                <span style="font-weight: bold; margin: 0 5px; color:#333;">[<%=p%>]</span>
            <% } else { %>
                <a href="<%=request.getContextPath()%>/diary?action=list&page=<%=p%>"
                   style="margin: 0 5px; text-decoration:none;">
                    <%=p%>
                </a>
            <% }} %>

            <% if (currentPage < totalPages) { %>
                <a href="<%=request.getContextPath()%>/diary?action=list&page=<%=currentPage + 1%>"
                   style="margin-left: 15px; text-decoration:none;">
                    Next ⟩
                </a>
            <% } %>

      </div>
     <!--  <div class="back-wrap">
            <a class="back-btn" href="<%=request.getContextPath()%>/dashboard.jsp">⬅ Back</a>
        </div>  -->

    </div>
</div>
</body>
</html>
