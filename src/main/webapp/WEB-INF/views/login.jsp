<%--
  Login page for Sugandha Sansaar.
  Allows users to enter email and password.
  Displays error or success messages based on query parameters.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Sugandha Sansaar — Login</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="${ctx}/static/css/login.css"/>
</head>
<body>

<header class="auth-topbar">
    <a href="${ctx}/home" class="auth-logo">Sugandha Sansaar</a>
    <nav>
        <a href="${ctx}/home">Home</a>
        <a href="${ctx}/about">About Us</a>
        <a href="${ctx}/products">Product</a>
    </nav>
</header>

<main class="auth-page">

    <%-- Left: image panel --%>
    <section class="auth-visual">
        <img class="auth-visual-img"
             src="${ctx}/static/images/login-bg.jpg"
             alt="Perfume bottle under dramatic low light"/>
        <div class="auth-bokeh" aria-hidden="true">
            <span></span><span></span><span></span><span></span>
        </div>
        <div class="auth-visual-content">
            <p class="auth-eyebrow">Premium Fragrance Collection</p>
            <h1 class="auth-visual-title">Return to your <em>signature</em> scent</h1>
            <p class="auth-visual-desc">
                Your saved favourites, orders and cart are waiting. Step back into a world of
                luxury perfumes crafted for elegance and allure.
            </p>
            <blockquote class="auth-quote">
                &ldquo;A fragrance is an invisible garment that speaks before you do.&rdquo;
                <cite>The Sugandha Sansaar Philosophy</cite>
            </blockquote>
        </div>
    </section>

    <%-- Right: login form --%>
    <section class="auth-panel">
        <form class="auth-card" action="${ctx}/login" method="post">
            <p class="auth-eyebrow">Member Login</p>
            <h2 class="auth-card-title">Welcome <em>Back</em></h2>
            <p class="auth-card-sub">Sign in to continue your fragrance journey.</p>

            <%-- Error message --%>
            <c:if test="${not empty error}">
                <p class="auth-msg error"><c:out value="${error}"/></p>
            </c:if>

            <%-- Logout success message --%>
            <c:if test="${param.logout != null}">
                <p class="auth-msg success">You have been logged out.</p>
            </c:if>

            <%-- Registration success message --%>
            <c:if test="${param.registered != null}">
                <p class="auth-msg success">Registration successful! You can now log in.</p>
            </c:if>

            <div class="auth-field">
                <input type="email" id="email" name="email" placeholder=" " autocomplete="email"
                       value="<c:out value='${not empty savedEmail ? savedEmail : param.email}' default=''/>"
                       required/>
                <label for="email">Email Address</label>
            </div>

            <div class="auth-field">
                <input type="password" id="password" name="password" placeholder=" "
                       autocomplete="current-password" required/>
                <label for="password">Password</label>
                <button type="button" class="auth-toggle" aria-label="Show password">Show</button>
            </div>

            <div class="auth-options">
                <label class="auth-check">
                    <input type="checkbox" name="remember" value="true"/>
                    Remember me
                </label>
            </div>

            <button type="submit" class="auth-btn"><span>Log In</span></button>

            <p class="auth-divider">New to Sugandha Sansaar?</p>
            <a href="${ctx}/register" class="auth-btn-outline">Create an Account — It's Free</a>
        </form>
    </section>

</main>

<%-- Show / Hide password --%>
<script>
    document.querySelectorAll('.auth-toggle').forEach(function (btn) {
        btn.addEventListener('click', function () {
            var input = btn.parentElement.querySelector('input');
            var show = input.type === 'password';
            input.type = show ? 'text' : 'password';
            btn.textContent = show ? 'Hide' : 'Show';
            btn.setAttribute('aria-label', show ? 'Hide password' : 'Show password');
        });
    });
</script>

</body>
</html>