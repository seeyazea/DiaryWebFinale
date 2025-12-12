<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입 | Registration</title>

    <link rel="stylesheet" href="<%=request.getContextPath()%>/styles/register.css">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/styles/dark.css">
</head>
<body>

<script>
    if (localStorage.getItem("dark") === "true") {
        document.body.classList.add("dark");
    }
</script>

<div class="form-container">
    <h2>회원가입</h2>
    <form action="<%=request.getContextPath()%>/auth" method="POST">
        <input type="hidden" name="action" value="register">
        <input type="text" name="fullname" placeholder="Full Name" required>
        <input type="email" name="email" placeholder="Email Address" required>
        <input type="password" name="password" placeholder="Password" required>
        <button type="submit">Create Account</button>
        <div class="link">
            <a href="<%=request.getContextPath()%>/login">Already have an account? Log in</a>
        </div>
    </form>

</div>

</body>
</html>
