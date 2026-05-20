<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>

<!DOCTYPE html>
<html lang="en">

<jsp:include page="/WEB-INF/templates/head.jsp">
    <jsp:param name="title"   value="Sugandha Sansaar — My Profile" />
    <jsp:param name="cssFile" value="profile" />
</jsp:include>

<body>
<div class="profile-wrapper">
    <div class="profile-card">

        <%-- ── Header ── --%>
        <div class="profile-header">
            <h1>My Profile</h1>
            <p>Manage your personal information and preferences.</p>
        </div>
        <hr class="divider" />

        <form method="post" enctype="multipart/form-data" id="profileForm">

            <%-- ── Avatar ── --%>
            <div class="avatar-section">
                <div class="avatar-wrapper">
                    <c:choose>
                        <c:when test="${not empty user.profilePic}">
                            <img src="${pageContext.request.contextPath}/image/profiles/${user.profilePic}"
                                 class="avatar-img" id="preview" alt="Profile" />
                            <button type="button" class="avatar-remove" id="removeBtn"
                                    onclick="removeImage()" title="Remove photo">✕</button>
                        </c:when>
                        <c:otherwise>
                            <div class="avatar-initials" id="avatarInitials">
                                <c:if test="${not empty user.fullName}">
                                    ${fn:toUpperCase(fn:substring(user.fullName, 0, 1))}
                                </c:if>
                            </div>
                            <img src="" class="avatar-img" id="preview"
                                 alt="Profile" style="display:none;" />
                            <button type="button" class="avatar-remove" id="removeBtn"
                                    onclick="removeImage()" title="Remove photo"
                                    style="display:none;">✕</button>
                        </c:otherwise>
                    </c:choose>
                </div>

                <input type="file" name="profilePic" id="fileInput"
                       accept=".jpg,.jpeg,.png,.webp"
                       onchange="previewImage(this)"
                       style="display:none;" />
                <input type="hidden" name="removeProfilePic"
                       id="removeProfilePic" value="false" />

                <button type="button" class="btn-change-image"
                        onclick="document.getElementById('fileInput').click()">
                    CHANGE IMAGE
                </button>
            </div>

            <hr class="divider" />

            <%-- ── Personal Info ── --%>
            <div class="form-section">

                <div class="form-group">
                    <label>FULL NAME</label>
                    <input type="text" name="fullName"
                           value="<c:out value='${user.fullName}' />"
                           placeholder="Your full name" required />
                </div>

                <div class="form-group">
                    <label>EMAIL ADDRESS</label>
                    <input type="email"
                           value="<c:out value='${user.email}' />"
                           disabled />
                    <span class="field-hint">
                        Email cannot be changed directly. Contact support.
                    </span>
                </div>

                <div class="form-group">
                    <label>PHONE NUMBER</label>
                    <input type="text" name="phone"
                           value="<c:out value='${user.phone}' />"
                           placeholder="98XXXXXXXX" required />
                    <span class="field-hint">NTC or Ncell number</span>
                </div>

            </div>

            <hr class="divider" />

            <%-- ── Change Password ── --%>
            <div class="form-section">
                <h2 class="section-title">Change Password</h2>

                <div class="form-group">
                    <label>CURRENT PASSWORD</label>
                    <div class="input-eye-wrap">
                        <input type="password" name="currentPassword"
                               id="currentPassword" placeholder="••••••••" />
                        <button type="button" class="eye-btn"
                                onclick="togglePwd('currentPassword', this)">👁</button>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-group">
                        <label>NEW PASSWORD</label>
                        <div class="input-eye-wrap">
                            <input type="password" name="newPassword"
                                   id="newPassword" placeholder="••••••••" />
                            <button type="button" class="eye-btn"
                                    onclick="togglePwd('newPassword', this)">👁</button>
                        </div>
                    </div>
                    <div class="form-group">
                        <label>CONFIRM PASSWORD</label>
                        <div class="input-eye-wrap">
                            <input type="password" name="confirmPassword"
                                   id="confirmPassword" placeholder="••••••••" />
                            <button type="button" class="eye-btn"
                                    onclick="togglePwd('confirmPassword', this)">👁</button>
                        </div>
                    </div>
                </div>

                <div class="password-rules" id="passwordRules" style="display:none;">
                    <span id="rule-length" class="rule">✗ At least 8 characters</span>
                    <span id="rule-upper"  class="rule">✗ One uppercase letter</span>
                    <span id="rule-number" class="rule">✗ One number</span>
                    <span id="rule-symbol" class="rule">✗ One special character</span>
                </div>
            </div>

            <%-- ── Alerts ── --%>
            <c:if test="${not empty success}">
                <div class="alert alert-success">
                    ✓ <c:out value="${success}" />
                </div>
            </c:if>
            <c:if test="${not empty error}">
                <div class="alert alert-error">
                    ✗ <c:out value="${error}" />
                </div>
            </c:if>

            <div class="form-actions">
                <button type="submit" class="btn-save">SAVE CHANGES</button>
            </div>

        </form>
    </div>
</div>

<script>
    /* ── Image Preview ── */
    function previewImage(input) {
        if (!input.files || !input.files[0]) return;
        const reader = new FileReader();
        reader.onload = function (e) {
            const preview  = document.getElementById('preview');
            const initials = document.getElementById('avatarInitials');
            const removeBtn= document.getElementById('removeBtn');
            preview.src = e.target.result;
            preview.style.display = 'block';
            if (initials)   initials.style.display  = 'none';
            if (removeBtn)  removeBtn.style.display  = 'flex';
            document.getElementById('removeProfilePic').value = 'false';
        };
        reader.readAsDataURL(input.files[0]);
    }

    /* ── Remove Image ── */
    function removeImage() {
        const preview  = document.getElementById('preview');
        const initials = document.getElementById('avatarInitials');
        const removeBtn= document.getElementById('removeBtn');
        const fileInput= document.getElementById('fileInput');
        preview.src = '';
        preview.style.display = 'none';
        if (initials)  initials.style.display  = 'flex';
        if (removeBtn) removeBtn.style.display  = 'none';
        fileInput.value = '';
        document.getElementById('removeProfilePic').value = 'true';
    }

    /* ── Toggle Password Visibility ── */
    function togglePwd(id, btn) {
        const input = document.getElementById(id);
        if (input.type === 'password') {
            input.type = 'text';
            btn.textContent = '🙈';
        } else {
            input.type = 'password';
            btn.textContent = '👁';
        }
    }

    /* ── Live Password Rules ── */
    document.getElementById('newPassword').addEventListener('input', function () {
        const val   = this.value;
        const rules = document.getElementById('passwordRules');
        rules.style.display = val.length > 0 ? 'flex' : 'none';
        setRule('rule-length', val.length >= 8,        ' At least 8 characters');
        setRule('rule-upper',  /[A-Z]/.test(val),      ' One uppercase letter');
        setRule('rule-number', /[0-9]/.test(val),      ' One number');
        setRule('rule-symbol', /[^A-Za-z0-9]/.test(val),' One special character');
    });

    function setRule(id, passed, text) {
        const el = document.getElementById(id);
        if (!el) return;
        el.textContent = (passed ? '✓' : '✗') + text;
        el.className   = 'rule' + (passed ? ' rule-pass' : '');
    }

    /* ── Client-side validation before submit ── */
    document.getElementById('profileForm').addEventListener('submit', function (e) {
        const current = document.getElementById('currentPassword').value;
        const newPwd  = document.getElementById('newPassword').value;
        const confirm = document.getElementById('confirmPassword').value;

        // Only validate password fields if any password field is filled
        if (current || newPwd || confirm) {
            if (!current) {
                showError('Please enter your current password.'); e.preventDefault(); return;
            }
            if (!newPwd) {
                showError('Please enter a new password.'); e.preventDefault(); return;
            }
            if (newPwd.length < 8 || !/[A-Z]/.test(newPwd) ||
                !/[0-9]/.test(newPwd) || !/[^A-Za-z0-9]/.test(newPwd)) {
                showError('New password must be 8+ characters with uppercase, number, and symbol.');
                e.preventDefault(); return;
            }
            if (newPwd !== confirm) {
                showError('New password and confirm password do not match.');
                e.preventDefault(); return;
            }
        }
    });

    function showError(msg) {
        let el = document.querySelector('.alert-error');
        if (!el) {
            el = document.createElement('div');
            el.className = 'alert alert-error';
            document.querySelector('.form-actions').before(el);
        }
        el.textContent = '✗ ' + msg;
        el.scrollIntoView({ behavior: 'smooth', block: 'center' });
    }
    <jsp:include page="/WEB-INF/templates/footer.jsp"/>
</script>

</body>
</html>
