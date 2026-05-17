<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Sugandha Sansaar — Register</title>
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/aura.css"/>
</head>
<body>
<div class="auth-page">
  <div class="auth-left">
    <div class="auth-left-deco"><div class="deco-ring r1"></div><div class="deco-ring r2"></div><div class="deco-ring r3"></div></div>
    <div class="auth-brand">Join<br/>Sugandha<br/>Sansaar</div>
    <p class="auth-brand-sub">Your journey starts here</p>
  </div>
  <div class="auth-right">
    <div class="auth-form-wrap">
      <h2>Create Account</h2>
      <p class="auth-sub">Register to browse, shop and track your fragrance orders.</p>
      <c:if test="${not empty error}"><div class="auth-alert err"><c:out value="${error}"/></div></c:if>
      <form action="${pageContext.request.contextPath}/register" method="post">
        <input class="auth-input" type="text" name="fullName" placeholder="Full Name" value="<c:out value='${param.fullName}' default=''/>" required/>
        <input class="auth-input" type="email" name="email" placeholder="Email Address" value="<c:out value='${param.email}' default=''/>" required/>
        <input class="auth-input" type="tel" name="phone" placeholder="Phone Number" value="<c:out value='${param.phone}' default=''/>" required/>
        <input class="auth-input" type="password" name="password" placeholder="Password" required/>
        <input class="auth-input" type="password" name="confirmPassword" placeholder="Confirm Password" required/>
        <button type="submit" class="auth-submit">Create Account</button>
        <p class="auth-link">Already have an account? <a href="${pageContext.request.contextPath}/login">Sign in</a></p>
        <p class="auth-link"><a href="${pageContext.request.contextPath}/home">← Back to home</a></p>
      </form>
    </div>
  </div>
</div>
</body></html>
