<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Add Product — Admin</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/aura.css"/>
</head>
<body>
<div class="admin-wrap">
    <aside class="admin-sidebar">
        <div class="admin-sidebar-logo"><span>Sugandha Admin</span></div>
        <nav class="admin-sidebar-nav">
            <a href="${pageContext.request.contextPath}/dashboard" class="admin-nav-link">📊 Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/products" class="admin-nav-link">📦 Products</a>
            <a href="${pageContext.request.contextPath}/admin/products?action=add" class="admin-nav-link active">➕ Add Product</a>
            <a href="${pageContext.request.contextPath}/logout" class="admin-nav-link logout">🚪 Logout</a>
        </nav>
    </aside>
    <main class="admin-main">
        <div class="admin-page-header">
            <div><h1>Add New Product</h1></div>
            <a href="${pageContext.request.contextPath}/admin/products" class="btn-outline">← Back</a>
        </div>
        <c:if test="${not empty errorMessage}"><div class="admin-alert err">${errorMessage}</div></c:if>

        <div class="admin-form-card">
            <form method="post" action="${pageContext.request.contextPath}/admin/products" onsubmit="return validateForm()">
                <input type="hidden" name="action" value="add"/>
                <div class="form-grid">
                    <div class="form-group"><label>Product Name *</label><input type="text" name="name" required maxlength="150" placeholder="e.g. Rose Bloom EDP" value="${not empty product ? product.name : ''}"/></div>
                    <div class="form-group"><label>Brand *</label><input type="text" name="brand" required maxlength="100" placeholder="e.g. Versace" value="${not empty product ? product.brand : ''}"/></div>
                    <div class="form-group"><label>Category *</label>
                        <select name="categoryId" required>
                            <option value="">-- Select --</option>
                            <c:forEach var="cat" items="${categories}"><option value="${cat.id}" ${not empty product && product.categoryId == cat.id ? 'selected' : ''}>${cat.name}</option></c:forEach>
                        </select>
                    </div>
                    <div class="form-group"><label>Gender</label>
                        <select name="gender">
                            <option value="">-- Not specified --</option>
                            <option value="male" ${not empty product && product.gender == 'male' ? 'selected' : ''}>Men</option>
                            <option value="female" ${not empty product && product.gender == 'female' ? 'selected' : ''}>Women</option>
                        </select>
                    </div>
                    <div class="form-group"><label>Price (Rs) *</label><input type="number" name="price" step="0.01" min="0.01" required placeholder="e.g. 1500.00" value="${not empty product ? product.price : ''}"/></div>
                    <div class="form-group"><label>Stock Qty *</label><input type="number" name="stock" min="0" required placeholder="e.g. 50" value="${not empty product ? product.stock : ''}"/></div>
                    <div class="form-group"><label>Volume (ml)</label><input type="number" name="volume" step="0.01" min="0" placeholder="e.g. 100" value="${not empty product ? product.volume : ''}"/></div>
                    <div class="form-group"><label>Image Filename</label><input type="text" name="imageUrl" placeholder="e.g. fogg_xpressio.jpg" value="${not empty product ? product.imageUrl : ''}"/></div>
                    <div class="form-group form-full"><label>Description</label><textarea name="description" rows="4" placeholder="Describe the fragrance...">${not empty product ? product.description : ''}</textarea></div>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn-gold">Add Product</button>
                    <a href="${pageContext.request.contextPath}/admin/products" class="btn-outline">Cancel</a>
                </div>
            </form>
        </div>
    </main>
</div>
<script>
    function validateForm() {
        const name = document.querySelector('[name=name]').value.trim();
        const brand = document.querySelector('[name=brand]').value.trim();
        const price = parseFloat(document.querySelector('[name=price]').value);
        if (!name || !brand) { alert('Name and brand are required.'); return false; }
        if (isNaN(price) || price <= 0) { alert('Enter a valid price.'); return false; }
        return true;
    }
</script>
</body></html>
