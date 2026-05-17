<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Add Perfume – Admin</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css">
</head>
<body>

<div class="admin-wrapper">
  <aside class="sidebar">
    <div class="sidebar-logo">
      <span class="logo-icon"></span>
      <span class="logo-text">Sugandha Sansaar</span>
    </div>
    <nav class="sidebar-nav">
      <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">
        📊 Dashboard
      </a>
      <a href="${pageContext.request.contextPath}/admin/perfumes" class="nav-link">
        🧴 Manage Perfumes
      </a>
      <a href="${pageContext.request.contextPath}/admin/perfumes?action=add" class="nav-link active">
        ➕ Add Perfume
      </a>
      <a href="${pageContext.request.contextPath}/admin/users" class="nav-link">
        👤 Manage Users
      </a>
      <a href="${pageContext.request.contextPath}/logout" class="nav-link nav-logout">
        🚪 Logout
      </a>
    </nav>
  </aside>

  <main class="main-content">
    <header class="page-header">
      <h1>Add New Perfume</h1>
      <a href="${pageContext.request.contextPath}/admin/perfumes" class="btn-secondary">← Back to List</a>
    </header>

    <c:if test="${not empty errorMessage}">
      <div class="alert alert-error">${errorMessage}</div>
    </c:if>

    <div class="form-card">
      <form method="post" action="${pageContext.request.contextPath}/admin/perfumes"
            onsubmit="return validateForm();">
        <input type="hidden" name="action" value="add">

        <div class="form-grid">

          <%-- Name --%>
          <div class="form-group">
            <label for="name">Perfume Name <span class="required">*</span></label>
            <input type="text" id="name" name="name"
                   value="${not empty perfume ? perfume.name : ''}"
                   placeholder="e.g. Midnight Rose" required maxlength="100">
          </div>

          <%-- Brand --%>
          <div class="form-group">
            <label for="brand">Brand <span class="required">*</span></label>
            <input type="text" id="brand" name="brand"
                   value="${not empty perfume ? perfume.brand : ''}"
                   placeholder="e.g. Chanel" required maxlength="100"
                   list="brandSuggestions">
            <datalist id="brandSuggestions">
              <c:forEach var="b" items="${brands}">
                <option value="${b}"/>
              </c:forEach>
            </datalist>
          </div>

          <%-- Category --%>
          <div class="form-group">
            <label for="category">Category <span class="required">*</span></label>
            <input type="text" id="category" name="category"
                   value="${not empty perfume ? perfume.category : ''}"
                   placeholder="e.g. Floral" required maxlength="50"
                   list="categorySuggestions">
            <datalist id="categorySuggestions">
              <c:forEach var="c" items="${categories}">
                <option value="${c}"/>
              </c:forEach>
              <option value="Floral"/>
              <option value="Woody"/>
              <option value="Oriental"/>
              <option value="Fresh"/>
              <option value="Aquatic"/>
              <option value="Citrus"/>
            </datalist>
          </div>

          <%-- Gender --%>
          <div class="form-group">
            <label for="gender">Gender <span class="required">*</span></label>
            <select id="gender" name="gender" required>
              <option value="">-- Select --</option>
              <option value="Male"   ${perfume.gender == 'Male'   ? 'selected' : ''}>Male</option>
              <option value="Female" ${perfume.gender == 'Female' ? 'selected' : ''}>Female</option>
              <option value="Unisex" ${perfume.gender == 'Unisex' ? 'selected' : ''}>Unisex</option>
            </select>
          </div>

          <%-- Price --%>
          <div class="form-group">
            <label for="price">Price (Rs.) <span class="required">*</span></label>
            <input type="number" id="price" name="price" step="1.00" min="1.00"
                   value="${not empty perfume ? perfume.price : ''}"
                   placeholder="0.00" required>
          </div>

          <%-- Volume --%>
          <div class="form-group">
            <label for="volume">Volume (ml) <span class="required">*</span></label>
            <input type="number" id="volume" name="volume" step="0.1" min="1"
                   value="${not empty perfume ? perfume.volume : ''}"
                   placeholder="50" required>
          </div>

          <%-- Stock --%>
          <div class="form-group">
            <label for="stock">Stock Quantity <span class="required">*</span></label>
            <input type="number" id="stock" name="stock" min="0"
                   value="${not empty perfume ? perfume.stock : '0'}"
                   placeholder="0" required>
          </div>

          <%-- Image URL --%>
          <div class="form-group">
            <label for="imageUrl">Image Path</label>
            <input type="text" id="imageUrl" name="imageUrl"
                   value="${not empty perfume ? perfume.imageUrl : ''}"
                   placeholder="images/perfumes/rose.jpg">
            <small>Relative path from webapp root</small>
          </div>

          <%-- Active Status --%>
          <div class="form-group form-group-full">
            <label class="checkbox-label">
              <input type="checkbox" name="active" value="true"
              ${empty perfume || perfume.active ? 'checked' : ''}>
              Mark as Active (visible to customers)
            </label>
          </div>

          <%-- Description --%>
          <div class="form-group form-group-full">
            <label for="description">Description</label>
            <textarea id="description" name="description" rows="4"
                      placeholder="Describe this perfume...">${not empty perfume ? perfume.description : ''}</textarea>
          </div>

        </div><%-- end form-grid --%>

        <div class="form-actions">
          <button type="submit" class="btn-primary">Add Perfume</button>
          <a href="${pageContext.request.contextPath}/admin/perfumes" class="btn-secondary">Cancel</a>
        </div>

      </form>
    </div>
  </main>
</div>

<script>
  /**
   * Client-side validation before form submission.
   * Server-side validation also runs in ValidationUtil.java.
   */
  function validateForm() {
    const name   = document.getElementById('name').value.trim();
    const brand  = document.getElementById('brand').value.trim();
    const price  = parseFloat(document.getElementById('price').value);
    const stock  = parseInt(document.getElementById('stock').value);
    const volume = parseFloat(document.getElementById('volume').value);

    if (!name)       { alert('Perfume name is required.'); return false; }
    if (!brand)      { alert('Brand is required.'); return false; }
    if (isNaN(price) || price <= 0) { alert('Please enter a valid price greater than 0.'); return false; }
    if (isNaN(stock) || stock < 0)  { alert('Stock must be 0 or more.'); return false; }
    if (isNaN(volume) || volume <= 0) { alert('Please enter a valid volume.'); return false; }
    return true;
  }
</script>

</body>
</html>
