<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page import="java.time.*" %>
<%@ page import="java.util.*" %>

<%
    int year = (int) request.getAttribute("year");
    int month = (int) request.getAttribute("month");
    List<String> diaryDates = (List<String>) request.getAttribute("diaryDates");

    LocalDate firstDay = LocalDate.of(year, month, 1);
    int length = firstDay.lengthOfMonth();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>📅 Calendar</title>

<style>
body { 
    font-family: 'Segoe UI', sans-serif; 
    background:#f7f7f7; 
}

/* CALENDAR MAIN BLOCK */
.calendar {
    width: 95%;
    max-width: 1100px;
    margin: 30px auto;
    background: white;
    padding: 30px 35px;
    border-radius: 16px;
    box-shadow: 0 4px 14px rgba(0,0,0,0.12);
}
.month-title {
    text-align: center;
    font-size: 26px;
    margin-bottom: 18px;
    font-weight: 700;
    color: #333;
}
/* MONTH NAVIGATION */
.month-nav {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
}
.nav-btn {
    background: #e8e8ff;
    padding: 10px 20px;
    border-radius: 8px;
    font-size: 15px;
    color: #333;
    text-decoration: none;
    font-weight: 600;
    transition: 0.2s;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
.nav-btn:hover {
    background: #cdd0ff;
}
/* TABLE (DAYS) */
table {
    width: 100%;
    border-collapse: collapse;
    text-align: center;
    font-size: 17px;
}

th {
    padding: 10px 0;
    background: #f3f3f7;
    border-radius: 8px;
    color: #666;
    font-weight: 600;
}

td {
    padding: 14px 0;
    height: 60px;
    font-size: 17px;
    position: relative;
    cursor: pointer;
    transition: 0.2s;
    border-radius: 10px;
}

td:hover {
    background: #f4f6ff;
}
/* EMPTY CELLS */
.empty {
    background: none !important;
    cursor: default;
}
/* DAYS WITH DIARY (SMALL CIRCLE + DOT) */
.has-diary {
    position: relative;
    font-weight: 700;
    color: #2d2d2d;
}
/* small circle */
.has-diary::before {
    content: "";
    width: 28px;
    height: 28px;
    background: #d9e0ff;
    border-radius: 50%;
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    z-index: -1;
}
/* small bottom dot */
.has-diary::after {
    content: '';
    width: 6px;
    height: 6px;
    background: #4a5cff;
    border-radius: 50%;
    position: absolute;
    bottom: 6px;
    left: 50%;
    transform: translateX(-50%);
}
/* BACK BUTTON */
.back-button {
    margin: 15px;
}
.back-button a {
    display: inline-block;
    padding: 10px 16px;
    background: #e8e8ff;
    color: #333;
    text-decoration: none;
    border-radius: 8px;
    font-weight: 600;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    transition: 0.2s;
}
.back-button a:hover {
    background: #d0d0ff;
}
</style>

</head>
<body>

<div class="back-button">
    <a href="<%=request.getContextPath()%>/dashboard">⬅ 대시보드로 돌아가기</a>
</div>

<div class="calendar">

    <div class="month-title">
        <%= year %>년 <%= month %>월
    </div>

    <div class="month-nav">
        <a href="calendar?year=<%= (month == 1 ? year - 1 : year) %>&month=<%= (month == 1 ? 12 : month - 1) %>" 
           class="nav-btn">◀ 이전달</a>

        <a href="calendar?year=<%= (month == 12 ? year + 1 : year) %>&month=<%= (month == 12 ? 1 : month + 1) %>" 
           class="nav-btn">다음달 ▶</a>
    </div>

    <table>
        <tr>
            <th>일</th><th>월</th><th>화</th><th>수</th>
            <th>목</th><th>금</th><th>토</th>
        </tr>

        <%
            int dayOfWeek = firstDay.getDayOfWeek().getValue() % 7;
            int day = 1;
            for (int i = 0; i < 6; i++) { %>
                <tr>
                <% for (int j = 0; j < 7; j++) {
                       if (i == 0 && j < dayOfWeek) { %>
                           <td class="empty"></td>
                       <% } else if (day > length) { %>
                           <td class="empty"></td>
                       <% } else {
                           String dateStr = year + "-" + 
                                            String.format("%02d", month) + "-" +
                                            String.format("%02d", day);
                           boolean hasDiary = diaryDates.contains(dateStr);
                       %>
                        <td class="<%= hasDiary ? "has-diary" : "" %>"
                            onclick="if(<%= hasDiary %>) location.href='diary?action=list&date=<%=dateStr%>'">
                            <%= day %>
                        </td>
                       <% day++;
                       } %>
                <% } %>
                </tr>
        <% } %>
    </table>
</div>
</body>
</html>