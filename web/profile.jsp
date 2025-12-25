<%@page contentType="text/html" pageEncoding="UTF-8" import="com.IA.model.ProfileBean"%>

<%
    ProfileBean profile = (ProfileBean) request.getAttribute("profile");
    String error = (String) request.getAttribute("error");
    String success = (String) request.getAttribute("success");
    boolean isEdit = profile != null && profile.getId() > 0;
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><%= isEdit ? "Edit Profile" : "Add Profile" %></title>
    <link rel="stylesheet" href="assets/styles.css">
    <style>
        .form-card { max-width: 600px; margin: 2rem auto; }
        .form-grid { display: grid; gap: 1rem; }
        .form-group { display: flex; flex-direction: column; }
        .label { font-weight: 600; margin-bottom: 0.5rem; color: #374151; }
        .input, .textarea { padding: 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem; font-size: 1rem; }
        .input:focus, .textarea:focus { outline: none; border-color: #3b82f6; box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }
        .error { background: #fee2e2; color: #991b1b; padding: 0.75rem; border-radius: 0.375rem; margin-bottom: 1rem; }
        .success { background: #dcfce7; color: #166534; padding: 0.75rem; border-radius: 0.375rem; margin-bottom: 1rem; }
        .btn { padding: 0.75rem 1.5rem; border: none; border-radius: 0.375rem; cursor: pointer; font-weight: 600; }
        .btn-primary { background: #3b82f6; color: white; }
        .btn-primary:hover { background: #2563eb; }
        .btn-secondary { background: #6b7280; color: white; }
        .btn-secondary:hover { background: #4b5563; }
        .btn-danger { background: #ef4444; color: white; }
        .btn-danger:hover { background: #dc2626; }
        .actions { display: flex; gap: 1rem; margin-top: 2rem; }
    </style>
    <script>
        const successMessage = "<%= success != null ? success : "" %>";
        if (successMessage) {
            alert(successMessage);
        }
    </script>
</head>
<body>
    <header class="header">
        <h1><%= isEdit ? "Edit Profile" : "Add Profile" %></h1>
        <a class="btn-secondary" href="ViewProfiles">Back to Profiles</a>
    </header>

    <div class="form-card">
        <% if (error != null) { %>
            <div class="error"><%= error %></div>
        <% } %>

        <form method="post" action="<%= isEdit ? "EditDeleteProfile" : "Profile" %>" accept-charset="UTF-8">
            <% if (isEdit) { %>
                <input type="hidden" name="id" value="<%= profile.getId() %>">
                <input type="hidden" name="action" value="update">
            <% } %>

            <div class="form-grid">
                <!-- Student ID -->
                <div class="form-group">
                    <label class="label" for="studentId">Student ID<span style="color:red;">*</span></label>
                    <input class="input" id="studentId" name="studentId" type="number"
                           value="<%= isEdit ? profile.getStudentId() : "" %>"
                           <%= isEdit ? "readonly" : "required" %>>
                </div>

                <!-- First Name -->
                <div class="form-group">
                    <label class="label" for="firstName">First Name<span style="color:red;">*</span></label>
                    <input class="input" id="firstName" name="firstName" type="text"
                           value="<%= isEdit ? profile.getFirstName() : "" %>" required>
                </div>

                <!-- Last Name -->
                <div class="form-group">
                    <label class="label" for="lastName">Last Name<span style="color:red;">*</span></label>
                    <input class="input" id="lastName" name="lastName" type="text"
                           value="<%= isEdit ? profile.getLastName() : "" %>" required>
                </div>

                <!-- Email -->
                <div class="form-group">
                    <label class="label" for="email">Email<span style="color:red;">*</span></label>
                    <input class="input" id="email" name="email" type="email"
                           value="<%= isEdit ? profile.getEmail() : "" %>" required>
                </div>

                <!-- Phone -->
                <div class="form-group">
                    <label class="label" for="phone">Phone</label>
                    <input class="input" id="phone" name="phone" type="tel"
                           value="<%= isEdit && profile.getPhone() != null ? profile.getPhone() : "" %>">
                </div>

                <!-- Date of Birth -->
                <div class="form-group">
                    <label class="label" for="dateOfBirth">Date of Birth</label>
                    <input class="input" id="dateOfBirth" name="dateOfBirth" type="date"
                           value="<%= isEdit && profile.getDateOfBirth() != null ? profile.getDateOfBirth() : "" %>">
                </div>

                <!-- Address -->
                <div class="form-group">
                    <label class="label" for="address">Address</label>
                    <input class="input" id="address" name="address" type="text"
                           value="<%= isEdit && profile.getAddress() != null ? profile.getAddress() : "" %>">
                </div>

                <!-- Major -->
                <div class="form-group">
                    <label class="label" for="major">Major<span style="color:red;">*</span></label>
                    <input class="input" id="major" name="major" type="text"
                           value="<%= isEdit ? profile.getMajor() : "" %>" required>
                </div>

                <!-- GPA -->
                <div class="form-group">
                    <label class="label" for="gpa">GPA</label>
                    <input class="input" id="gpa" name="gpa" type="number" step="0.01" min="0" max="4"
                           value="<%= isEdit && profile.getGpa() > 0 ? profile.getGpa() : "" %>">
                </div>
            </div>

            <div class="actions">
                <button class="btn btn-primary" type="submit"><%= isEdit ? "Update Profile" : "Save Profile" %></button>
                <a class="btn btn-secondary" href="ViewProfiles">Cancel</a>
                <% if (isEdit) { %>
                    <button class="btn btn-danger" type="button" onclick="deleteProfile()">Delete Profile</button>
                <% } %>
            </div>
        </form>
    </div>

    <script>
        function deleteProfile() {
            if (confirm("Are you sure you want to delete this profile?")) {
                const form = document.querySelector('form');
                const input = document.createElement('input');
                input.type = 'hidden';
                input.name = 'action';
                input.value = 'delete';
                form.appendChild(input);

                const submitBtn = document.querySelector('button[type="submit"]');
                submitBtn.click();
            }
        }
    </script>
</body>
</html>
