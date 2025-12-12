<%@ page contentType="text/html; charset=UTF-8" language="java" %>

<%
    com.tirmizee.mvc.model.User sidebarUser =
        (com.tirmizee.mvc.model.User) session.getAttribute("loginUser");
%>

<div id="sidebar" class="sidebar">

    <!-- Header -->
    <div class="sidebar-header">
        <span>Menu</span>
        <button class="sidebar-close" onclick="closeSidebar()">×</button>
    </div>

    <!-- User Info -->
    <div class="sidebar-user">
        <div class="sidebar-avatar">
            <%= sidebarUser != null
                ? sidebarUser.getFullname().toUpperCase().charAt(0)
                : "?" %>
        </div>

        <div>
            <div class="sidebar-user-name">
                <%= sidebarUser != null ? sidebarUser.getFullname() : "Unknown User" %>
            </div>

            <div class="sidebar-user-email">
                <%= sidebarUser != null ? sidebarUser.getEmail() : "" %>
            </div>
        </div>
    </div>

    <!-- Menu links -->
   <a class="sidebar-item" href="<%=request.getContextPath()%>/dashboard">
    <span class="sidebar-item-emoji">🏠</span> Dashboard
</a>

<a class="sidebar-item" href="<%=request.getContextPath()%>/diary?action=searchPage">
    <span class="sidebar-item-emoji">🔍</span> Search
</a>

<a class="sidebar-item" href="<%=request.getContextPath()%>/diary?action=list">
    <span class="sidebar-item-emoji">📘</span> My Entries
</a>

<a class="sidebar-item" href="<%=request.getContextPath()%>/trash">
    <span class="sidebar-item-emoji">🗑️</span> Trash
</a>

<a class="sidebar-item" href="<%=request.getContextPath()%>/profile">
    <span class="sidebar-item-emoji">👤</span> Profile
</a>

<a class="sidebar-item" href="<%=request.getContextPath()%>/settings">
    <span class="sidebar-item-emoji">⚙️</span> Settings
</a>


    <!-- Logout Button -->
    <div class="sidebar-logout">
        <form action="<%=request.getContextPath()%>/auth" method="get">
            <input type="hidden" name="action" value="logout">
            <button class="sidebar-logout-btn" type="submit">Log out</button>
        </form>
    </div>
</div>

<!-- Backdrop -->
<div id="sidebarBackdrop" class="sidebar-backdrop" onclick="closeSidebar()"></div>

<!-- Sidebar JS -->
<script>
function openSidebar() {
    document.getElementById("sidebar").classList.add("open");
    document.getElementById("sidebarBackdrop").classList.add("show");
}

function closeSidebar() {
    document.getElementById("sidebar").classList.remove("open");
    document.getElementById("sidebarBackdrop").classList.remove("show");
}

// Dark mode apply for sidebar component
if (localStorage.getItem("dark") === "true") {
    document.getElementById("sidebar").classList.add("dark");
}
</script>
