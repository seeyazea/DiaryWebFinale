<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>🗑️ 휴지통</title>
<link rel="stylesheet" href="<%=request.getContextPath()%>/styles/trash.css">
</head>

<body>
<div class="container">
<a class="btn-back" href="<%=request.getContextPath()%>/dashboard">
    ⬅ 뒤로가기
</a>
    <div class="page-title">🗑️ 휴지통</div>

    <!-- 휴지통이 비어있는 경우 -->
    <c:if test="${empty trashList}">
        <div class="empty-box">
            휴지통이 비어 있습니다 😊<br>
            삭제한 일기는 이곳에 표시됩니다.
        </div>
    </c:if>

    <!-- 삭제된 일기 목록 -->
    <c:forEach var="d" items="${trashList}">
        <div class="card">

            <div class="card-title">${d.title}</div>
            <div class="card-date">${d.diaryDate}</div>

            <div class="btn-area">

                <!-- 복원 -->
                <form action="${pageContext.request.contextPath}/restoreDiary" method="post">
                    <input type="hidden" name="id" value="${d.id}">
                    <button class="btn btn-restore">♻️ 복원하기</button>
                </form>

                <!-- 영구 삭제 -->
                <form action="${pageContext.request.contextPath}/deleteForever" method="post"
                      onsubmit="return confirm('정말 영구적으로 삭제하시겠습니까?\n삭제 후에는 복구할 수 없습니다.');">
                    <input type="hidden" name="id" value="${d.id}">
                    <button class="btn btn-delete">❌ 영구 삭제</button>
                </form>

            </div>
        </div>
    </c:forEach>

</div>

</body>
</html>
