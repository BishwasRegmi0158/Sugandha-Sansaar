<%--
  Login page for Sugandha Sansaar.
  Allows users to enter email and password.
  Displays error or success messages based on query parameters.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<%-- Include common head template with dynamic title and CSS --%>
<jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Sugandha Sansaar — Login" />
    <jsp:param name="cssFile" value="login" />
</jsp:include>

<body>
<div class="login-page">

    <%-- Header section with logo and app name --%>
    <div class="login-header">
        <img src="${pageContext.request.contextPath}/static/images/logo.png" alt="Sugandha Sansaar" />
        <h1>Sugandha Sansaar</h1>
    </div>

    <%-- Login form container --%>
    <div class="login-form">
        <form action="${pageContext.request.contextPath}/login" method="post">
            <h2>Welcome Back</h2>

            <%-- Display error message if present (e.g., invalid credentials) --%>
            <c:if test="${not empty error}">
                <p class="error"><c:out value="${error}" /></p>
            </c:if>

            <%-- Display logout success message --%>
            <c:if test="${param.logout != null}">
                <p class="success">You have been logged out.</p>
            </c:if>

            <%-- Display registration success message --%>
            <c:if test="${param.registered != null}">
                <p class="success">Registration successful! Please log in.</p>
            </c:if>

            <%-- Email input field --%>
            <input type="email" name="email" placeholder="Email Address"
                   value="<c:out value='${param.email}' default='' />" required />

            <%-- Password input field --%>
            <input type="password" name="password" placeholder="Password" required />

            <%-- Optional "Remember me" checkbox --%>
            <div class="checkbox-group">
                <label>
                    <input type="checkbox" name="remember" value="true" /> Remember me
                </label>
            </div>

            <%-- Submit button --%>
            <button type="submit">Log In</button>

            <%-- Link to registration page for new users --%>
            <p class="link">Don't have an account?
                <a href="${pageContext.request.contextPath}/register">Register</a>
            </p>
        </form>
    </div>

</div>
</body>
</html>