<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>${param.title}</title>

  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap"
        rel="stylesheet" />

  <%-- Base design system — ALWAYS loaded --%>
  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/static/css/aura.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/footer.css" />

  <%-- Page-specific CSS — loaded only if provided --%>
  <c:if test="${not empty param.cssFile}">
    <link rel="stylesheet"
          href="${pageContext.request.contextPath}/static/css/${param.cssFile}.css" />
  </c:if>

  <link rel="icon"
        href="${pageContext.request.contextPath}/static/images/favicon.ico" />
</head>