<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Sugandha Sansaar — Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/aura.css"/>
</head>
<body>
<div class="auth-page">
    <div class="auth-left">
        <div class="auth-left-deco"><div class="deco-ring r1"></div><div class="deco-ring r2"></div><div class="deco-ring r3"></div></div>
        <div class="auth-brand">Sugandha<br/>Sansaar</div>
        <p class="auth-brand-sub">Premium Fragrance Collection</p>
    </div>
    <div class="auth-right">
        <div class="auth-form-wrap">
            <h2>Welcome Back</h2>
            <p class="auth-sub">Sign in to explore our exclusive fragrance collection.</p>
            <c:if test="${not empty error}"><div class="auth-alert err"><c:out value="${error}"/></div></c:if>
            <c:if test="${param.logout != null}"><div class="auth-alert ok">You have been logged out successfully.</div></c:if>
            <c:if test="${param.registered != null}"><div class="auth-alert ok">Registration successful! You can now log in.</div></c:if>
            <form action="${pageContext.request.contextPath}/login" method="post">
                <input class="auth-input" type="email" name="email" placeholder="Email Address" value="<c:out value='${param.email}' default=''/>" required/>
                <input class="auth-input" type="password" name="password" placeholder="Password" required/>
                <div class="auth-check"><input type="checkbox" name="remember" value="true"/> Remember me</div>
                <button type="submit" class="auth-submit">Sign In</button>
                <p class="auth-link">Don't have an account? <a href="${pageContext.request.contextPath}/register">Register free</a></p>
                <p class="auth-link"><a href="${pageContext.request.contextPath}/home">← Back to home</a></p>
            </form>
        </div>
    </div>
</div>
</body></html>
