<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title"   value="Sugandha Sansaar — My Profile" />
    <jsp:param name="cssFile" value="profile" />
</jsp:include>


<body>

<div class="profile-page">

    <h1>My Profile</h1>

    <form method="post"
          enctype="multipart/form-data">

        <c:choose>

            <c:when test="${not empty user.profilePic}">
                <img
                        src="${pageContext.request.contextPath}/image/profiles/${user.profilePic}"
                        class="profile-pic"
                        id="preview"
                        alt="Profile Picture"/>
            </c:when>

            <c:otherwise>
                <img
                        src="${pageContext.request.contextPath}/static/images/profiles/default.png"
                        class="profile-pic"
                        id="preview"
                        alt="Default Picture"/>
            </c:otherwise>

        </c:choose>

        <br><br>

        <input type="file"
               name="profilePic"
               accept=".jpg,.jpeg,.png,.webp"
               onchange="previewImage(this)">

        <br><br>

        <input type="text"
               name="fullName"
               value="${user.fullName}"
               required>

        <br><br>

        <input type="email"
               value="${user.email}"
               disabled>

        <br><br>

        <input type="text"
               name="phone"
               value="${user.phone}"
               required>

        <br><br>

        <input type="password"
               name="newPassword"
               placeholder="New Password">

        <br><br>

        <button type="submit">
            Save Changes
        </button>

        <c:if test="${not empty success}">
            <p>${success}</p>
        </c:if>

        <c:if test="${not empty error}">
            <p>${error}</p>
        </c:if>

    </form>
</div>

<script>

    function previewImage(input){

        if(input.files
            && input.files[0]){

            const reader =
                new FileReader();

            reader.onload =
                function(e){

                    document
                        .getElementById(
                            "preview"
                        ).src =
                        e.target.result;
                }

            reader.readAsDataURL(
                input.files[0]
            );
        }
    }

</script>

</body>
</html>