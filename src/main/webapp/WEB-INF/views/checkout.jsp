<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<jsp:include page="/WEB-INF/templates/head.jsp">
  <jsp:param name="title"   value="Sugandha Sansaar — Checkout" />
  <jsp:param name="cssFile" value="checkout" />
</jsp:include>
<body>

<%-- ── Navbar ── --%>
<nav class="ss-nav">
  <div class="ss-nav-logo">Sugandha Sansaar</div>
  <div class="ss-nav-search">
    <form action="${pageContext.request.contextPath}/products" method="GET">
      <input type="text" name="search" placeholder="Search fragrances, brands…" />
      <button type="submit">⌕</button>
    </form>
  </div>
  <div class="ss-nav-links">
    <a href="${pageContext.request.contextPath}/home">Home</a>
    <a href="${pageContext.request.contextPath}/products">Product</a>
    <a href="${pageContext.request.contextPath}/about">About</a>
    <a href="${pageContext.request.contextPath}/user/cart">
      Cart<c:if test="${cartCount > 0}"> (${cartCount})</c:if>
    </a>
    <a href="${pageContext.request.contextPath}/user/order">Orders</a>
    <a href="${pageContext.request.contextPath}/user/profile">Profile</a>
    <a href="${pageContext.request.contextPath}/logout" class="nav-cta">Logout</a>
  </div>
</nav>

<div class="page-body">
  <div class="checkout-page">

    <%-- ── Breadcrumb ── --%>
    <div class="checkout-breadcrumb">
      <a href="${pageContext.request.contextPath}/user/cart">Cart</a>
      <span class="crumb-sep">›</span>
      <span class="crumb-active">Checkout</span>
    </div>

    <h1 class="checkout-title">Complete Your Order</h1>

    <%-- ── Error banner ── --%>
    <c:if test="${not empty error}">
      <div class="checkout-error">
        <span class="error-icon">⚠</span>
        <c:out value="${error}" />
      </div>
    </c:if>

    <div class="checkout-layout">

      <%-- ══════════════════════════════════════════════════════
           LEFT — Delivery + Payment form
           ══════════════════════════════════════════════════════ --%>
      <form class="checkout-form" id="checkoutForm" action="${pageContext.request.contextPath}/user/checkout" method="post">

        <%-- ── Section: Delivery ── --%>
        <div class="form-section">
          <h2 class="section-heading">
            <span class="section-num">01</span> Delivery Details
          </h2>

          <div class="field-row two-col">
            <div class="field-group">
              <label for="deliveryName">Full Name</label>
              <input type="text" id="deliveryName" name="deliveryName"
                     placeholder="Recipient's full name"
                     value="<c:out value='${not empty prefillName ? prefillName : ""}' />"
                     required />
            </div>
            <div class="field-group">
              <label for="deliveryPhone">Phone Number</label>
              <input type="tel" id="deliveryPhone" name="deliveryPhone"
                     placeholder="98XXXXXXXX"
                     value="<c:out value='${not empty prefillPhone ? prefillPhone : ""}' />"
                     required />
            </div>
          </div>

          <div class="field-group">
            <label for="deliveryStreet">Street Address</label>
            <input type="text" id="deliveryStreet" name="deliveryStreet"
                   placeholder="House no., street, locality"
                   value="<c:out value='${not empty prefillStreet ? prefillStreet : ""}' />"
                   required />
          </div>

          <div class="field-row three-col">
            <div class="field-group">
              <label for="deliveryCity">City</label>
              <input type="text" id="deliveryCity" name="deliveryCity"
                     placeholder="e.g. Kathmandu"
                     value="<c:out value='${not empty prefillCity ? prefillCity : ""}' />"
                     required />
            </div>

            <%-- ── Province dropdown (replaces free-text input) ── --%>
            <div class="field-group">
              <label for="deliveryState">Province</label>
              <c:set var="ps" value="${not empty prefillState ? prefillState : ''}" />
              <select id="deliveryState" name="deliveryState" required>
                <option value="" disabled ${empty prefillState ? 'selected' : ''}>— Select Province —</option>
                <option value="Koshi"         ${ps == 'Koshi'         ? 'selected' : ''}>Koshi (Province 1)</option>
                <option value="Madhesh"       ${ps == 'Madhesh'       ? 'selected' : ''}>Madhesh (Province 2)</option>
                <option value="Bagmati"       ${ps == 'Bagmati'       ? 'selected' : ''}>Bagmati (Province 3)</option>
                <option value="Gandaki"       ${ps == 'Gandaki'       ? 'selected' : ''}>Gandaki (Province 4)</option>
                <option value="Lumbini"       ${ps == 'Lumbini'       ? 'selected' : ''}>Lumbini (Province 5)</option>
                <option value="Karnali"       ${ps == 'Karnali'       ? 'selected' : ''}>Karnali (Province 6)</option>
                <option value="Sudurpashchim" ${ps == 'Sudurpashchim' ? 'selected' : ''}>Sudurpashchim (Province 7)</option>
              </select>
            </div>

            <div class="field-group">
              <label for="deliveryPinCode">PIN Code</label>
              <input type="text" id="deliveryPinCode" name="deliveryPinCode"
                     placeholder="44600"
                     maxlength="5"
                     value="<c:out value='${not empty prefillPin ? prefillPin : ""}' />"
                     required />
            </div>
          </div>
        </div>

        <%-- ── Section: Payment ── --%>
        <div class="form-section">
          <h2 class="section-heading">
            <span class="section-num">02</span> Payment Method
          </h2>

          <div class="payment-options">

            <label class="payment-card">
              <input type="radio" name="paymentMethod" value="cash_on_delivery"
              ${prefillMethod == 'cash_on_delivery' || empty prefillMethod ? 'checked' : ''} />
              <span class="pay-icon">💵</span>
              <span class="pay-label">Cash on Delivery</span>
              <span class="pay-note">Pay when your order arrives</span>
            </label>

            <label class="payment-card">
              <input type="radio" name="paymentMethod" value="esewa"
              ${prefillMethod == 'esewa' ? 'checked' : ''} />
              <span class="pay-icon">📱</span>
              <span class="pay-label">eSewa</span>
              <span class="pay-note">Digital wallet payment</span>
            </label>

            <label class="payment-card">
              <input type="radio" name="paymentMethod" value="khalti"
              ${prefillMethod == 'khalti' ? 'checked' : ''} />
              <span class="pay-icon">🔵</span>
              <span class="pay-label">Khalti</span>
              <span class="pay-note">Digital wallet payment</span>
            </label>

            <label class="payment-card">
              <input type="radio" name="paymentMethod" value="bank_transfer"
              ${prefillMethod == 'bank_transfer' ? 'checked' : ''} />
              <span class="pay-icon">🏦</span>
              <span class="pay-label">Bank Transfer</span>
              <span class="pay-note">Direct bank deposit</span>
            </label>

          </div>
        </div>

        <button type="submit" class="btn-place-order" id="btnPlaceOrder">
          Place Order &rarr;
        </button>

      </form>

      <%-- ══════════════════════════════════════════════════════
           RIGHT — Order summary
           ══════════════════════════════════════════════════════ --%>
      <aside class="checkout-summary">
        <h2 class="summary-title">Order Summary</h2>

        <div class="summary-items">
          <c:forEach var="item" items="${cartItems}">
            <div class="summary-item">
              <div class="summary-item-img">
                <img src="${pageContext.request.contextPath}/static/images/product_images/${item.productImageUrl}"
                     alt="<c:out value='${item.productName}' />"
                     onerror="this.style.visibility='hidden'" />
                <span class="summary-qty-badge">${item.quantity}</span>
              </div>
              <div class="summary-item-info">
                <p class="summary-item-name"><c:out value="${item.productName}" /></p>
                <p class="summary-item-brand"><c:out value="${item.productBrand}" /></p>
              </div>
              <p class="summary-item-price">Rs. <c:out value="${item.lineTotal}" /></p>
            </div>
          </c:forEach>
        </div>

        <div class="summary-divider"></div>

        <div class="summary-row">
          <span>Subtotal</span>
          <span>Rs. <c:out value="${subtotal}" /></span>
        </div>
        <div class="summary-row">
          <span>Shipping</span>
          <span>Rs. <c:out value="${shipping}" /></span>
        </div>
        <div class="summary-divider"></div>
        <div class="summary-row summary-total">
          <span>Total</span>
          <span>Rs. <c:out value="${total}" /></span>
        </div>

        <p class="summary-secure">🔒 Secure checkout</p>
      </aside>

    </div><%-- /.checkout-layout --%>
  </div><%-- /.checkout-page --%>

  <footer class="ss-footer">
    <div class="ss-footer-inner">
      <div class="ss-footer-brand">Sugandha Sansaar</div>
      <p class="ss-footer-copy">© 2025 Sugandha Sansaar · Premium Fragrance Destination</p>
      <div class="ss-footer-links">
        <a href="${pageContext.request.contextPath}/about">About</a>
        <a href="${pageContext.request.contextPath}/products">Shop</a>
      </div>
    </div>
  </footer>
</div>

<script>
  /* ─────────────────────────────────────────────────────────────
     FIX 1: Sync .selected class on page load AND on every change.
     The old code only ran on 'change', so the pre-checked
     Cash on Delivery card was never highlighted on first render.
  ───────────────────────────────────────────────────────────── */
  function syncPaymentCards() {
    document.querySelectorAll('.payment-card').forEach(function(card) {
      var radio = card.querySelector('input[type="radio"]');
      card.classList.toggle('selected', radio.checked);
    });
  }

  // Run immediately so COD (or whichever is pre-checked) is highlighted on load
  syncPaymentCards();

  // Also update whenever the user clicks a different option
  document.querySelectorAll('.payment-card input[type="radio"]').forEach(function(radio) {
    radio.addEventListener('change', syncPaymentCards);
  });

  /* ─────────────────────────────────────────────────────────────
     FIX 2: Prevent duplicate order submissions.
     Once the form is submitted, disable the button and block
     any further submit events until the page navigates away.
  ───────────────────────────────────────────────────────────── */
  var form    = document.getElementById('checkoutForm');
  var btnPlace = document.getElementById('btnPlaceOrder');

  form.addEventListener('submit', function(e) {
    if (form.dataset.submitted === 'true') {
      // Already submitted — block this extra click
      e.preventDefault();
      return;
    }
    // Mark as submitted and disable the button
    form.dataset.submitted = 'true';
    btnPlace.disabled      = true;
    btnPlace.textContent   = 'Placing Order…';
  });
</script>

</body>
</html>
