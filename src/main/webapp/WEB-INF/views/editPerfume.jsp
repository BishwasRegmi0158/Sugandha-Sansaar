<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Perfume – Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css">
</head>
<body>

<div class="admin-wrapper">
    <aside class="sidebar">
        <div class="sidebar-logo">
            <span class="logo-icon"></span>
            <span class="logo-text">Essence Admin</span>
        </div>
        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/admin/dashboard" class="nav-link">📊 Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/perfumes" class="nav-link active">🧴 Manage Perfumes</a>
            <a href="${pageContext.request.contextPath}/admin/perfumes?action=add" class="nav-link">➕ Add Perfume</a>
            <a href="${pageContext.request.contextPath}/logout" class="nav-link nav-logout">🚪 Logout</a>
        </nav>
    </aside>

    <main class="main-content">
        <header class="page-header">
            <h1>Edit Perfume: <em>${perfume.name}</em></h1>
            <a href="${pageContext.request.contextPath}/admin/perfumes" class="btn-secondary">← Back to List</a>
        </header>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <div class="form-card">
            <form method="post" action="${pageContext.request.contextPath}/admin/perfumes"
                  onsubmit="return validateForm();">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id"     value="${perfume.id}">

                <div class="form-grid">

                    <div class="form-group">
                        <label for="name">Perfume Name <span class="required">*</span></label>
                        <input type="text" id="name" name="name" value="${perfume.name}"
                               required maxlength="100">
                    </div>

                    <div class="form-group">
                        <label for="brand">Brand <span class="required">*</span></label>
                        <input type="text" id="brand" name="brand" value="${perfume.brand}"
                               required maxlength="100" list="brandSuggestions">
                        <datalist id="brandSuggestions">
                            <c:forEach var="b" items="${brands}">
                                <option value="${b}"/>
                            </c:forEach>
                        </datalist>
                    </div>

                    <div class="form-group">
                        <label for="category">Category <span class="required">*</span></label>
                        <input type="text" id="category" name="category" value="${perfume.category}"
                               required maxlength="50" list="categorySuggestions">
                        <datalist id="categorySuggestions">
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat}"/>
                            </c:forEach>
                        </datalist>
                    </div>

                    <div class="form-group">
                        <label for="gender">Gender <span class="required">*</span></label>
                        <select id="gender" name="gender" required>
                            <option value="Male"   ${perfume.gender == 'Male'   ? 'selected' : ''}>Male</option>
                            <option value="Female" ${perfume.gender == 'Female' ? 'selected' : ''}>Female</option>
                            <option value="Unisex" ${perfume.gender == 'Unisex' ? 'selected' : ''}>Unisex</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="price">Price ($) <span class="required">*</span></label>
                        <input type="number" id="price" name="price" step="0.01" min="0.01"
                               value="${perfume.price}" required>
                    </div>

                    <div class="form-group">
                        <label for="volume">Volume (ml) <span class="required">*</span></label>
                        <input type="number" id="volume" name="volume" step="0.1" min="1"
                               value="${perfume.volume}" required>
                    </div>

                    <div class="form-group">
                        <label for="stock">Stock Quantity <span class="required">*</span></label>
                        <input type="number" id="stock" name="stock" min="0"
                               value="${perfume.stock}" required>
                    </div>

                    <div class="form-group">
                        <label for="imageUrl">Image Path</label>
                        <input type="text" id="imageUrl" name="imageUrl" value="${perfume.imageUrl}"
                               placeholder="images/perfumes/rose.jpg">
                        <small>Relative path from webapp root</small>
                    </div>

                    <div class="form-group form-group-full">
                        <label class="checkbox-label">
                            <input type="checkbox" name="active" value="true" ${perfume.active ? 'checked' : ''}>
                            Mark as Active (visible to customers)
                        </label>
                    </div>

                    <div class="form-group form-group-full">
                        <label for="description">Description</label>
                        <textarea id="description" name="description" rows="4">${perfume.description}</textarea>
                    </div>

                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-primary">Save Changes</button>
                    <a href="${pageContext.request.contextPath}/admin/perfumes" class="btn-secondary">Cancel</a>
                </div>

            </form>
        </div>
    </main>
</div>

<script>
    function validateForm() {
        const name   = document.getElementById('name').value.trim();
        const brand  = document.getElementById('brand').value.trim();
        const price  = parseFloat(document.getElementById('price').value);
        const stock  = parseInt(document.getElementById('stock').value);
        const volume = parseFloat(document.getElementById('volume').value);

        if (!name)        { alert('Perfume name is required.'); return false; }
        if (!brand)       { alert('Brand is required.'); return false; }
        if (isNaN(price)  || price <= 0)  { alert('Please enter a valid price.'); return false; }
        if (isNaN(stock)  || stock < 0)   { alert('Stock must be 0 or more.'); return false; }
        if (isNaN(volume) || volume <= 0) { alert('Please enter a valid volume.'); return false; }
        return true;
    }
</script>

</body>
</html>