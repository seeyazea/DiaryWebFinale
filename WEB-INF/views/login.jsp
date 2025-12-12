<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>로그인 | Login</title>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/styles/login.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/styles/dark.css">
</head>
<body>

<script>
    if (localStorage.getItem("dark") === "true") {
        document.body.classList.add("dark");
    }
</script>

<div class="form-container">
    <h2>로그인</h2>
    <% if ("1".equals(request.getParameter("error"))) { %>
        <div class="error">❌ Incorrect email or password</div>
    <% } %>
    <% if ("1".equals(request.getParameter("success"))) { %>
        <div class="success">✅ Account created successfully!</div>
    <% } %>
    <form action="<%=request.getContextPath()%>/auth" method="POST">
        <input type="hidden" name="action" value="login">
        <input type="email" name="email" placeholder="Email Address" required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit">Log In</button>
        <div class="link">
            <a href="<%=request.getContextPath()%>/register">Create new account</a>
        </div>
    </form>
</div>
</body>
</html>
