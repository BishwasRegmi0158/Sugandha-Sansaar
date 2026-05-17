<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8"/><meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Sugandha Sansaar — My Profile</title>
    <link href="https://fonts.googleapis.com/css2?family=Cormorant+Garamond:ital,wght@0,300;0,400;0,600;1,300;1,400&family=Jost:wght@300;400;500;600&display=swap" rel="stylesheet"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/aura.css"/>
</head>
<body>
<nav class="ss-nav">
    <div class="ss-nav-logo">Sugandha Sansaar</div>
    <div class="ss-nav-links">
        <a href="${pageContext.request.contextPath}/user/dashboard">MY DASHBOARD</a>
        <a href="${pageContext.request.contextPath}/products">SHOP</a>
        <a href="${pageContext.request.contextPath}/user/profile" class="active">PROFILE</a>
        <a href="${pageContext.request.contextPath}/logout">LOGOUT</a>
    </div>
</nav>

<div class="page-body">
    <div class="profile-wrap">
        <h1>My Profile</h1>
        <div class="profile-grid">
            <div class="profile-avatar-col">
                <c:choose>
                    <c:when test="${not empty user.profilePic}">
                        <img src="${pageContext.request.contextPath}/static/images/profiles/${user.profilePic}" class="profile-pic" id="profilePicPreview" alt="Profile"/>
                    </c:when>
                    <c:otherwise>
                        <img src="${pageContext.request.contextPath}/static/images/profiles/default.png" class="profile-pic" id="profilePicPreview" alt="Profile" onerror="this.style.display='none'"/>
                    </c:otherwise>
                </c:choose>
            </div>
            <div class="profile-form-card">
                <form action="${pageContext.request.contextPath}/user/profile" method="post" enctype="multipart/form-data">
                    <h2>Personal Information</h2>
                    <c:if test="${not empty success}"><p class="msg-ok"><c:out value="${success}"/></p></c:if>
                    <c:if test="${not empty error}"><p class="msg-err"><c:out value="${error}"/></p></c:if>

                    <div class="form-group">
                        <label>Profile Picture <small>(JPG, PNG — max 3MB)</small></label>
                        <input type="file" name="profilePic" accept=".jpg,.jpeg,.png,.webp" onchange="previewImage(this)" style="color:var(--muted);font-size:0.82rem;"/>
                    </div>
                    <div class="form-group">
                        <label>Full Name</label>
                        <input type="text" name="fullName" value="<c:out value='${user.fullName}'/>" required minlength="3"/>
                    </div>
                    <div class="form-group">
                        <label>Email Address</label>
                        <input type="email" value="<c:out value='${user.email}'/>" disabled/>
                        <small>Email cannot be changed.</small>
                    </div>
                    <div class="form-group">
                        <label>Phone Number</label>
                        <input type="tel" name="phone" value="<c:out value='${user.phone}'/>" required/>
                    </div>

                    <h2>Change Password</h2>
                    <div class="form-group">
                        <label>New Password</label>
                        <input type="password" name="newPassword" placeholder="Leave blank to keep current password"/>
                    </div>

                    <button type="submit" class="btn-gold" style="margin-top:8px;width:100%;padding:13px;">Save Changes</button>
                </form>
            </div>
        </div>
    </div>

    <footer class="ss-footer">
        <div class="ss-footer-inner">
            <div class="ss-footer-brand">Sugandha Sansaar</div>
            <p class="ss-footer-copy">© 2025 Sugandha Sansaar</p>
        </div>
    </footer>
</div>
<script>
    function previewImage(input) {
        if (input.files && input.files[0]) {
            const r = new FileReader();
            r.onload = e => document.getElementById('profilePicPreview').src = e.target.result;
            r.readAsDataURL(input.files[0]);
        }
    }
</script>
</body></html>
