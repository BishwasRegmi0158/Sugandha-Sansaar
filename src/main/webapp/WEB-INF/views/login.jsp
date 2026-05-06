<%--
  Login page for Sugandha Sansaar.
  Allows users to enter email and password.
  Displays error or success messages based on query parameters.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Sugandha Sansaar — Login" />
    <jsp:param name="cssFile" value="login" />
</jsp:include>

<body>
<div class="login-page">

    <div class="login-header">
        <img src="${pageContext.request.contextPath}/static/images/logo.png"
             alt="Sugandha Sansaar" />
        <h1>Sugandha Sansaar</h1>
    </div>

    <div class="login-form">
        <form action="${pageContext.request.contextPath}/login" method="post">
            <h2>Welcome Back</h2>

            <%-- Error message --%>
            <c:if test="${not empty error}">
                <p class="error"><c:out value="${error}" /></p>
            </c:if>

            <%-- Logout success message --%>
            <c:if test="${param.logout != null}">
                <p class="success">You have been logged out.</p>
            </c:if>

            <%-- Registration success message
                 Changed from "pending approval" to "you can now login" --%>
            <c:if test="${param.registered != null}">
                <p class="success">
                    Registration successful! You can now log in.
                </p>
            </c:if>

            <input type="email"
                   name="email"
                   placeholder="Email Address"
                   value="<c:out value='${param.email}' default='' />"
                   required />

            <input type="password"
                   name="password"
                   placeholder="Password"
                   required />

            <div class="checkbox-group">
                <label>
                    <input type="checkbox" name="remember" value="true" />
                    Remember me
                </label>
            </div>

            <button type="submit">Log In</button>

            <p class="link">Don't have an account?
                <a href="${pageContext.request.contextPath}/register">Register</a>
            </p>
        </form>
    </div>

</div>
</body>
</html>