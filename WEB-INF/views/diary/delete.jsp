<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.tirmizee.mvc.model.Diary" %>
<%
    Diary d = (Diary) request.getAttribute("diary");
    if (d == null) {
        response.sendRedirect("list.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>일기 삭제</title>

<!-- Подключаем Sidebar CSS -->
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/sidebar.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/delete.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/dark.css">

</head>

<body>
<!-- ⭐ Sidebar подключение -->
<%@ include file="/WEB-INF/views/components/sidebar.jsp" %>

<!-- ⭐ Topbar -->
<div class="topbar">
    <button class="menu-btn" onclick="openSidebar()">☰</button>
    <div class="topbar-title">Delete Entry</div>
</div>

<!-- Main Content -->
<div class="page">
    <div class="container">

        <div class="title">🗑️ 일기 삭제</div>

        <div class="subtitle">
            아래 일기를 정말 삭제하시겠습니까?<br>
            삭제 후에는 되돌릴 수 없습니다.
        </div>

        <div class="info-box">
            <b>제목:</b> <%= d.getTitle() %><br>
            <b>날짜:</b> <%= d.getDiaryDate() %><br>
        </div>

        <div class="buttons">
            <a class="btn btn-cancel"
               href="<%=request.getContextPath()%>/diary?action=view&id=<%=d.getId()%>">취소</a>

            <a class="btn btn-delete"
               href="<%=request.getContextPath()%>/diary?action=delete&id=<%=d.getId()%>">삭제하기</a>
        </div>

    </div>
</div>

</body>
</html>
