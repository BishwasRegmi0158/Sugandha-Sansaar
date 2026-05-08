<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title" value="Sugandha Sansaar — My Profile" />
    <jsp:param name="cssFile" value="profile" />
</jsp:include>

<body>
<div class="profile-page">

    <div class="profile-header">
        <h1>My Profile</h1>
        <a href="${pageContext.request.contextPath}/user/dashboard">← Back to Dashboard</a>
    </div>

    <div class="profile-container">

        <%-- Profile Picture --%>
        <div class="profile-pic-section">
            <c:choose>
                <c:when test="${not empty user.profilePic}">
                    <img src="${pageContext.request.contextPath}/static/images/profiles/${user.profilePic}"
                         alt="Profile Picture"
                         class="profile-pic"
                         id="profilePicPreview" />
                </c:when>
                <c:otherwise>
                    <img src="${pageContext.request.contextPath}/static/images/profiles/default.png"
                         alt="Default Profile"
                         class="profile-pic"
                         id="profilePicPreview" />
                </c:otherwise>
            </c:choose>
        </div>

        <div class="profile-form">
            <%-- enctype MUST be multipart for file upload --%>
            <form action="${pageContext.request.contextPath}/user/profile"
                  method="post"
                  enctype="multipart/form-data">

                <h2>Personal Information</h2>

                <c:if test="${not empty success}">
                    <p class="msg-success"><c:out value="${success}" /></p>
                </c:if>
                <c:if test="${not empty error}">
                    <p class="msg-error"><c:out value="${error}" /></p>
                </c:if>

                <%-- Profile picture upload --%>
                <div class="form-group">
                    <label for="profilePic">Profile Picture
                        <span class="hint">(JPG, PNG or WEBP — max 3 MB)</span>
                    </label>
                    <input type="file"
                           id="profilePicInput"
                           name="profilePic"
                           accept=".jpg,.jpeg,.png,.webp"
                           onchange="previewImage(this)" />
                </div>

                <div class="form-group">
                    <label for="fullName">Full Name</label>
                    <input type="text"
                           id="fullName"
                           name="fullName"
                           value="<c:out value='${user.fullName}' />"
                           required minlength="3" />
                </div>

                <div class="form-group">
                    <label>Email Address</label>
                    <input type="email"
                           value="<c:out value='${user.email}' />"
                           disabled />
                    <small class="hint">Email cannot be changed.</small>
                </div>

                <div class="form-group">
                    <label for="phone">Phone Number</label>
                    <input type="tel"
                           id="phone"
                           name="phone"
                           value="<c:out value='${user.phone}' />"
                           required />
                </div>

                <h2>Change Password</h2>
                <p class="hint">Leave blank to keep your current password.</p>

                <div class="form-group">
                    <label for="newPassword">New Password</label>
                    <input type="password"
                           id="newPassword"
                           name="newPassword"
                           placeholder="Min 8 chars, uppercase, number, symbol" />
                </div>

                <button type="submit" class="btn-save">Save Changes</button>
            </form>
        </div>
    </div>

</div>

<script>
    function previewImage(input) {
        if (input.files && input.files[0]) {
            const reader = new FileReader();
            reader.onload = function(e) {
                document.getElementById('profilePicPreview').src = e.target.result;
            };
            reader.readAsDataURL(input.files[0]);
        }
    }
</script>
</body>
</html>