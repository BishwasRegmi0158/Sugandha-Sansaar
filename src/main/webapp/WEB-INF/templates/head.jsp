

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>${param.title}</title>

  <%-- Google Fonts — load BEFORE your CSS --%>
  <link rel="preconnect" href="https://fonts.googleapis.com" />
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin />
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap"
        rel="stylesheet" />

  <link rel="stylesheet"
        href="${pageContext.request.contextPath}/static/css/${param.cssFile}.css" />
  <link rel="icon"
        href="${pageContext.request.contextPath}/static/images/favicon.ico" />
</head>