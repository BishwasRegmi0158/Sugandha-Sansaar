<%--
  Register page for Sugandha Sansaar.
  Collects full name, email, phone and password.
  Values (except passwords) are preserved on validation failure.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
  <title>Sugandha Sansaar — Register</title>
  <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
  <link rel="stylesheet" href="${ctx}/static/css/register.css"/>
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
         src="${ctx}/static/images/register-bg.jpg"
         alt="Glass perfume bottle against a black background"/>
    <div class="auth-bokeh" aria-hidden="true">
      <span></span><span></span><span></span><span></span>
    </div>
    <div class="auth-visual-content">
      <p class="auth-eyebrow">Join the Collection</p>
      <h1 class="auth-visual-title">Begin your <em>fragrance</em> story</h1>
      <p class="auth-visual-desc">
        Create a free account to browse our full collection, save your favourites,
        and order authentic luxury scents delivered across Nepal.
      </p>
      <div class="auth-stats">
        <div class="auth-stat"><strong>25+</strong><span>Products</span></div>
        <div class="auth-stat"><strong>10+</strong><span>Brands</span></div>
        <div class="auth-stat"><strong>100%</strong><span>Authentic</span></div>
      </div>
    </div>
  </section>

  <%-- Right: register form --%>
  <section class="auth-panel">
    <form class="auth-card" action="${ctx}/register" method="post">
      <p class="auth-eyebrow">New Member</p>
      <h2 class="auth-card-title">Create <em>Account</em></h2>
      <p class="auth-card-sub">It takes less than a minute to join.</p>

      <%-- Display error message if present --%>
      <c:if test="${not empty error}">
        <p class="auth-msg error"><c:out value="${error}"/></p>
      </c:if>

      <%-- Full name — value preserved on validation failure --%>
      <div class="auth-field">
        <input type="text" id="fullName" name="fullName" placeholder=" " autocomplete="name"
               value="<c:out value='${param.fullName}' default=''/>" required/>
        <label for="fullName">Full Name</label>
      </div>

      <%-- Email + phone — values preserved on validation failure --%>
      <div class="auth-row">
        <div class="auth-field">
          <input type="email" id="email" name="email" placeholder=" " autocomplete="email"
                 value="<c:out value='${param.email}' default=''/>" required/>
          <label for="email">Email</label>
        </div>
        <div class="auth-field">
          <input type="tel" id="phone" name="phone" placeholder=" " autocomplete="tel"
                 value="<c:out value='${param.phone}' default=''/>" required/>
          <label for="phone">Phone Number</label>
        </div>
      </div>

      <%-- Password fields — never preserved for security --%>
      <div class="auth-field">
        <input type="password" id="password" name="password" placeholder=" "
               autocomplete="new-password" required/>
        <label for="password">Password</label>
        <button type="button" class="auth-toggle" aria-label="Show password">Show</button>
      </div>

      <div class="auth-strength" data-level="0" aria-live="polite">
        <div class="auth-strength-bars"><i></i><i></i><i></i><i></i></div>
        <span class="auth-strength-label">Strength</span>
      </div>

      <div class="auth-field">
        <input type="password" id="confirmPassword" name="confirmPassword" placeholder=" "
               autocomplete="new-password" required/>
        <label for="confirmPassword">Confirm Password</label>
        <button type="button" class="auth-toggle" aria-label="Show password">Show</button>
      </div>
      <p class="auth-match" aria-live="polite"></p>

      <button type="submit" class="auth-btn"><span>Register</span></button>

      <p class="auth-divider">Already a member?</p>
      <a href="${ctx}/login" class="auth-btn-outline">Log In Instead</a>
    </form>
  </section>

</main>

<%-- Show / Hide password, strength meter, confirm-match check --%>
<script>
  // Show / Hide buttons
  document.querySelectorAll('.auth-toggle').forEach(function (btn) {
    btn.addEventListener('click', function () {
      var input = btn.parentElement.querySelector('input');
      var show = input.type === 'password';
      input.type = show ? 'text' : 'password';
      btn.textContent = show ? 'Hide' : 'Show';
      btn.setAttribute('aria-label', show ? 'Hide password' : 'Show password');
    });
  });

  var pw = document.getElementById('password');
  var confirmPw = document.getElementById('confirmPassword');
  var meter = document.querySelector('.auth-strength');
  var match = document.querySelector('.auth-match');
  var labels = ['Strength', 'Weak', 'Fair', 'Good', 'Strong'];

  function strengthLevel(v) {
    if (!v) return 0;
    var s = 0;
    if (v.length >= 8) s++;
    if (/[A-Z]/.test(v) && /[a-z]/.test(v)) s++;
    if (/\d/.test(v)) s++;
    if (/[^A-Za-z0-9]/.test(v)) s++;
    return Math.max(1, s);
  }

  function update() {
    var l = strengthLevel(pw.value);
    meter.setAttribute('data-level', l);
    meter.querySelector('.auth-strength-label').textContent = labels[l];

    if (!confirmPw.value) {
      match.textContent = '';
      match.className = 'auth-match';
      return;
    }
    var ok = confirmPw.value === pw.value;
    match.textContent = ok ? 'Passwords match' : 'Passwords do not match';
    match.className = 'auth-match ' + (ok ? 'is-ok' : 'is-bad');
  }

  pw.addEventListener('input', update);
  confirmPw.addEventListener('input', update);
</script>

</body>
</html>