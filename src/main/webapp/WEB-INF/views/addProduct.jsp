<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add Product – Admin</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/admin.css">
</head>
<body>

<div class="admin-wrapper">
    <aside class="sidebar">
        <div class="sidebar-logo"><span class="logo-icon">🌸</span><span class="logo-text">Sugandha Admin</span></div>
        <nav class="sidebar-nav">
            <a href="${pageContext.request.contextPath}/admin/dashboard"            class="nav-link">📊 Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/products"             class="nav-link">📦 Manage Products</a>
            <a href="${pageContext.request.contextPath}/admin/products?action=add"  class="nav-link active">➕ Add Product</a>
            <a href="${pageContext.request.contextPath}/logout"                     class="nav-link nav-logout">🚪 Logout</a>
        </nav>
    </aside>

    <main class="main-content">
        <header class="page-header">
            <h1>Add New Product</h1>
            <a href="${pageContext.request.contextPath}/admin/products" class="btn-secondary">← Back</a>
        </header>

        <c:if test="${not empty errorMessage}">
            <div class="alert alert-error">${errorMessage}</div>
        </c:if>

        <div class="form-card">
            <form method="post" action="${pageContext.request.contextPath}/admin/products"
                  onsubmit="return validateForm();">
                <input type="hidden" name="action" value="add">

                <div class="form-grid">

                    <div class="form-group">
                        <label for="name">Product Name <span class="required">*</span></label>
                        <input type="text" id="name" name="name" maxlength="150" required
                               value="${not empty product ? product.name : ''}"
                               placeholder="e.g. Rose Bloom EDP">
                    </div>

                    <div class="form-group">
                        <label for="brand">Brand <span class="required">*</span></label>
                        <input type="text" id="brand" name="brand" maxlength="100" required
                               value="${not empty product ? product.brand : ''}"
                               placeholder="e.g. Versace">
                    </div>

                    <div class="form-group">
                        <label for="categoryId">Category <span class="required">*</span></label>
                        <select id="categoryId" name="categoryId" required>
                            <option value="">-- Select --</option>
                            <c:forEach var="cat" items="${categories}">
                                <option value="${cat.id}"
                                    ${not empty product && product.categoryId == cat.id ? 'selected' : ''}>
                                        ${cat.name}
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="gender">Gender</label>
                        <select id="gender" name="gender">
                            <option value="">-- Not specified --</option>
                            <option value="male"   ${not empty product && product.gender == 'male'   ? 'selected' : ''}>Men</option>
                            <option value="female" ${not empty product && product.gender == 'female' ? 'selected' : ''}>Women</option>
                        </select>
                    </div>

                    <div class="form-group">
                        <label for="price">Price (Rs) <span class="required">*</span></label>
                        <input type="number" id="price" name="price" step="0.01" min="0.01" required
                               value="${not empty product ? product.price : ''}"
                               placeholder="1299.00">
                    </div>

                    <div class="form-group">
                        <label for="stock">Stock Qty <span class="required">*</span></label>
                        <input type="number" id="stock" name="stock" min="0" required
                               value="${not empty product ? product.stock : '0'}">
                    </div>

                    <div class="form-group">
                        <label for="volume">Volume (ml)</label>
                        <input type="number" id="volume" name="volume" step="0.01" min="0.01"
                               value="${not empty product && product.volume != null ? product.volume : ''}"
                               placeholder="50.00">
                    </div>

                    <div class="form-group">
                        <label for="imageUrl">Image Filename</label>
                        <input type="text" id="imageUrl" name="imageUrl" maxlength="500"
                               value="${not empty product ? product.imageUrl : ''}"
                               placeholder="rose-bloom.jpg">
                        <small>Place file in /static/images/</small>
                    </div>

                    <div class="form-group form-group-full">
                        <label for="description">Description</label>
                        <textarea id="description" name="description" rows="4"
                                  placeholder="Describe the product...">${not empty product ? product.description : ''}</textarea>
                    </div>

                    <div class="form-group">
                        <label>Status</label>
                        <select name="active">
                            <option value="1" ${empty product || product.active ? 'selected' : ''}>Active</option>
                            <option value="0" ${not empty product && !product.active ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>

                </div>

                <div class="form-actions">
                    <button type="submit" class="btn-primary">Add Product</button>
                    <a href="${pageContext.request.contextPath}/admin/products" class="btn-secondary">Cancel</a>
                </div>
            </form>
        </div>
    </main>
</div>

<script>
    function validateForm() {
        const name  = document.getElementById('name').value.trim();
        const brand = document.getElementById('brand').value.trim();
        const cat   = document.getElementById('categoryId').value;
        const price = parseFloat(document.getElementById('price').value);
        const stock = parseInt(document.getElementById('stock').value);
        if (!name)                        { alert('Product name is required.');        return false; }
        if (!brand)                       { alert('Brand is required.');               return false; }
        if (!cat)                         { alert('Please select a category.');        return false; }
        if (isNaN(price) || price <= 0)   { alert('Price must be a positive number.'); return false; }
        if (isNaN(stock) || stock < 0)    { alert('Stock must be zero or positive.');  return false; }
        const vol = document.getElementById('volume').value.trim();
        if (vol !== '' && (isNaN(parseFloat(vol)) || parseFloat(vol) <= 0)) {
            alert('Volume must be a positive number.'); return false;
        }
        return true;
    }
</script>
</body>
</html>
