<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="com.tirmizee.mvc.model.Diary" %>

<%
    Diary d = (Diary) request.getAttribute("diary");

    if (d == null) {
        response.sendRedirect(request.getContextPath() + "/diary?action=list");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title><%= d.getTitle() %></title>

<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/sidebar.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/view.css">
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/dark.css">
</head>

<body>

<!-- ⭐ SIDEBAR -->
<%@ include file="/WEB-INF/views/components/sidebar.jsp" %>

<!-- ⭐ TOP BAR -->
<div class="topbar">
    <button class="menu-btn" onclick="openSidebar()">☰</button>
    <div class="topbar-title">View Entry</div>
</div>

<!-- ⭐ MAIN CONTENT -->
<div class="page">
    <div class="container">

        <a class="back-btn" href="<%=request.getContextPath()%>/diary?action=list">⬅ 뒤로가기</a>

        <div class="title"><%= d.getTitle() %></div>
        <div class="date"><%= d.getDiaryDate() %></div>

        <% if (d.getPhoto() != null) { %>
        <div class="photo-box">
            <img src="<%=request.getContextPath()%>/image?id=<%= d.getId() %>"
                 style="max-width: 100%; border-radius: 12px;">
        </div>
        <% } %>

        <div class="content"><%= d.getContent() %></div>

        <div class="actions">
            <a class="btn btn-edit"
               href="<%=request.getContextPath()%>/diary?action=edit&id=<%= d.getId() %>">
               수정
            </a>

            <!-- ⭐ Открываем modal -->
            <button class="btn btn-delete" onclick="openDeleteModal(<%= d.getId() %>)">
                삭제
            </button>
        </div>
    </div>
</div>


<!-- ⭐ DELETE MODAL -->
<div id="deleteModal" class="modal-backdrop">
    <div class="modal-box">
        <div class="modal-title">일기 삭제</div>
        <div class="modal-text">
            정말로 이 일기를 삭제하시겠습니까?<br>
            되돌릴 수 없습니다.
        </div>

        <div class="modal-buttons">

            <!-- закрыть -->
            <button class="btn-cancel" onclick="closeDeleteModal()">취소</button>

            <!-- ⭐ УДАЛЕНИЕ ЧЕРЕЗ /diary?action=delete -->
            <form action="<%=request.getContextPath()%>/diary" method="get">
                <input type="hidden" name="action" value="delete">
                <input type="hidden" name="id" id="deleteId">
                <button type="submit" class="btn-delete2">삭제하기</button>
            </form>

        </div>
    </div>
</div>
<!-- ⭐ SCRIPT -->
<script>
function openDeleteModal(id) {
    document.getElementById("deleteId").value = id;
    document.getElementById("deleteModal").style.display = "flex";
}

function closeDeleteModal() {
    document.getElementById("deleteModal").style.display = "none";
}
</script>

</body>
</html>
