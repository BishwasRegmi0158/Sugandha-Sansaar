<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Edit Product — Admin</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/aura.css"/>
</head>
<body>
<div class="admin-wrap">
    <aside class="admin-sidebar">
        <div class="admin-sidebar-logo"><span>Sugandha Admin</span></div>
        <nav class="admin-sidebar-nav">
            <a href="${pageContext.request.contextPath}/dashboard" class="admin-nav-link">📊 Dashboard</a>
            <a href="${pageContext.request.contextPath}/admin/products" class="admin-nav-link active">📦 Products</a>
            <a href="${pageContext.request.contextPath}/admin/products?action=add" class="admin-nav-link">➕ Add Product</a>
            <a href="${pageContext.request.contextPath}/logout" class="admin-nav-link logout">🚪 Logout</a>
        </nav>
    </aside>
    <main class="admin-main">
        <div class="admin-page-header">
            <div><h1>Edit — ${product.name}</h1></div>
            <a href="${pageContext.request.contextPath}/admin/products" class="btn-outline">← Back</a>
        </div>
        <c:if test="${not empty errorMessage}"><div class="admin-alert err">${errorMessage}</div></c:if>

        <div class="admin-form-card">
            <form method="post" action="${pageContext.request.contextPath}/admin/products" onsubmit="return validateForm()">
                <input type="hidden" name="action" value="edit"/>
                <input type="hidden" name="id" value="${product.id}"/>
                <div class="form-grid">
                    <div class="form-group"><label>Product Name *</label><input type="text" name="name" required maxlength="150" value="${product.name}"/></div>
                    <div class="form-group"><label>Brand *</label><input type="text" name="brand" required maxlength="100" value="${product.brand}"/></div>
                    <div class="form-group"><label>Category *</label>
                        <select name="categoryId" required>
                            <option value="">-- Select --</option>
                            <c:forEach var="cat" items="${categories}"><option value="${cat.id}" ${product.categoryId == cat.id ? 'selected' : ''}>${cat.name}</option></c:forEach>
                        </select>
                    </div>
                    <div class="form-group"><label>Gender</label>
                        <select name="gender">
                            <option value="">-- Not specified --</option>
                            <option value="male" ${product.gender == 'male' ? 'selected' : ''}>Men</option>
                            <option value="female" ${product.gender == 'female' ? 'selected' : ''}>Women</option>
                        </select>
                    </div>
                    <div class="form-group"><label>Price (Rs) *</label><input type="number" name="price" step="0.01" min="0.01" required value="${product.price}"/></div>
                    <div class="form-group"><label>Stock Qty *</label><input type="number" name="stock" min="0" required value="${product.stock}"/></div>
                    <div class="form-group"><label>Volume (ml)</label><input type="number" name="volume" step="0.01" min="0" value="${product.volume}"/></div>
                    <div class="form-group"><label>Image Filename</label><input type="text" name="imageUrl" value="${product.imageUrl}" placeholder="e.g. fogg_xpressio.jpg"/>
                        <c:if test="${not empty product.imageUrl}">
                            <img src="${pageContext.request.contextPath}/static/images/product_images/${product.imageUrl}" style="width:64px;height:64px;object-fit:cover;border-radius:3px;margin-top:8px;border:1px solid rgba(255,255,255,0.07);" alt="preview"/>
                        </c:if>
                    </div>
                    <div class="form-group"><label>Active</label>
                        <select name="active">
                            <option value="true" ${product.active ? 'selected' : ''}>Active</option>
                            <option value="false" ${not product.active ? 'selected' : ''}>Inactive</option>
                        </select>
                    </div>
                    <div class="form-group form-full"><label>Description</label><textarea name="description" rows="4">${product.description}</textarea></div>
                </div>
                <div class="form-actions">
                    <button type="submit" class="btn-gold">Save Changes</button>
                    <a href="${pageContext.request.contextPath}/admin/products" class="btn-outline">Cancel</a>
                </div>
            </form>
        </div>
    </main>
</div>
<script>
    function validateForm() {
        const name = document.querySelector('[name=name]').value.trim();
        const price = parseFloat(document.querySelector('[name=price]').value);
        if (!name) { alert('Product name is required.'); return false; }
        if (isNaN(price) || price <= 0) { alert('Enter a valid price.'); return false; }
        return true;
    }
</script>
</body></html>
