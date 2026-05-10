<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/head.jsp">
  <jsp:param name="title" value="Sugandha Sansaar — Register" />
  <jsp:param name="cssFile" value="register" />
</jsp:include>
<body>
<div class="register-page">

  <div class="login-panel-left" aria-hidden="true"></div>

  <div class="register-form">
    <form action="${pageContext.request.contextPath}/register" method="post">
      <h2>Create Account</h2>

      <c:if test="${not empty error}">
        <p class="error"><c:out value="${error}" /></p>
      </c:if>

      <input type="text" name="fullName" placeholder="Full Name"
             value="<c:out value='${param.fullName}' default='' />" required />

      <input type="email" name="email" placeholder="Email Address"
             value="<c:out value='${param.email}' default='' />" required />

      <input type="tel" name="phone" placeholder="Phone Number"
             value="<c:out value='${param.phone}' default='' />" required />

      <input type="password" name="password" placeholder="Password" required />

      <input type="password" name="confirmPassword" placeholder="Confirm Password" required />

      <button type="submit">Create Account</button>

      <p class="link">Already have an account?
        <a href="${pageContext.request.contextPath}/login">Log in</a>
      </p>
    </form>
  </div>

</div>
</body>
</html>
