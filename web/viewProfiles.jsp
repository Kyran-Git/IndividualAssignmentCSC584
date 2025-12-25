<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*, com.IA.model.ProfileBean"%>

<%
    List<ProfileBean> profiles = (List<ProfileBean>) request.getAttribute("profiles");
    String searchType = (String) request.getAttribute("searchType");
    String searchQuery = (String) request.getAttribute("searchQuery");
    String error = request.getParameter("error");
    String success = request.getParameter("success");

    if (profiles == null) {
        profiles = new ArrayList<>();
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>View Profiles</title>
    <link rel="stylesheet" href="assets/styles.css">
    <style>
        .page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 2rem; flex-wrap: wrap; gap: 1rem; }
        .search-container { display: flex; gap: 0.5rem; flex-wrap: wrap; margin-bottom: 2rem; }
        .search-container select, .search-container input { padding: 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem; }
        .search-container button { padding: 0.75rem 1.5rem; background: #3b82f6; color: white; border: none; border-radius: 0.375rem; cursor: pointer; font-weight: 600; }
        .search-container button:hover { background: #2563eb; }
        .profile-card { background: white; border: 1px solid #e5e7eb; border-radius: 0.5rem; padding: 1.5rem; margin-bottom: 1rem; }
        .profile-header { display: flex; justify-content: space-between; align-items: start; margin-bottom: 1rem; }
        .profile-title { font-size: 1.25rem; font-weight: 600; color: #1f2937; }
        .profile-meta { color: #6b7280; font-size: 0.875rem; margin: 0.25rem 0; }
        .profile-actions { display: flex; gap: 0.5rem; }
        .btn-small { padding: 0.5rem 1rem; font-size: 0.875rem; border: none; border-radius: 0.375rem; cursor: pointer; text-decoration: none; }
        .btn-edit { background: #3b82f6; color: white; }
        .btn-edit:hover { background: #2563eb; }
        .btn-delete { background: #ef4444; color: white; }
        .btn-delete:hover { background: #dc2626; }
        .empty-state { text-align: center; padding: 3rem; color: #6b7280; }
        .alert { padding: 1rem; border-radius: 0.375rem; margin-bottom: 1rem; }
        .alert-error { background: #fee2e2; color: #991b1b; }
        .alert-success { background: #dcfce7; color: #166534; }
        .filter-info { color: #6b7280; font-style: italic; margin-bottom: 1rem; }
    </style>
</head>
<body>
    <header class="header">
        <h1>Student Profiles</h1>
        <a class="btn-secondary" href="Profile">Add New Profile</a>
    </header>

    <% if (success != null) { %>
        <div class="alert alert-success"><%= success %></div>
    <% } %>
    <% if (error != null) { %>
        <div class="alert alert-error"><%= error %></div>
    <% } %>

    <div class="search-container">
        <form method="GET" action="ViewProfiles" style="display: flex; gap: 0.5rem; flex-wrap: wrap; width: 100%;">
            <select name="searchType" style="padding: 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem;">
                <option value="">-- Select Search Type --</option>
                <option value="name" <%= "name".equals(searchType) ? "selected" : "" %>>Search by Name</option>
                <option value="studentId" <%= "studentId".equals(searchType) ? "selected" : "" %>>Search by Student ID</option>
                <option value="major" <%= "major".equals(searchType) ? "selected" : "" %>>Filter by Major</option>
            </select>
            <input type="text" name="searchQuery" placeholder="Enter search term..."
                   value="<%= searchQuery != null ? searchQuery : "" %>"
                   style="padding: 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem; flex: 1; min-width: 200px;">
            <button type="submit">Search</button>
            <a href="ViewProfiles" style="padding: 0.75rem 1.5rem; background: #6b7280; color: white; border-radius: 0.375rem; text-decoration: none; cursor: pointer;">Clear</a>
        </form>
    </div>

    <% if (searchType != null && searchQuery != null) { %>
        <div class="filter-info">
            Searching by <strong><%= searchType %></strong>: "<%= searchQuery %>"
            <% if (!profiles.isEmpty()) { %>
                (<%= profiles.size() %> result<%= profiles.size() != 1 ? "s" : "" %> found)
            <% } else { %>
                (No results found)
            <% } %>
        </div>
    <% } %>

    <div>
        <% if (profiles == null || profiles.isEmpty()) { %>
            <div class="empty-state">
                <h2>No profiles found</h2>
                <p>Start by adding a new profile or try a different search.</p>
                <a class="btn btn-primary" href="Profile" style="display: inline-block; margin-top: 1rem;">Add Profile</a>
            </div>
        <% } else { %>
            <% for (ProfileBean profile : profiles) { %>
                <div class="profile-card">
                    <div class="profile-header">
                        <div>
                            <div class="profile-title"><%= profile.getFirstName() %> <%= profile.getLastName() %></div>
                            <div class="profile-meta">Student ID: <%= profile.getStudentId() %></div>
                            <div class="profile-meta">Email: <%= profile.getEmail() %></div>
                            <% if (profile.getPhone() != null && !profile.getPhone().isEmpty()) { %>
                                <div class="profile-meta">Phone: <%= profile.getPhone() %></div>
                            <% } %>
                            <div class="profile-meta">Major: <%= profile.getMajor() %></div>
                            <% if (profile.getGpa() > 0) { %>
                                <div class="profile-meta">GPA: <%= String.format("%.2f", profile.getGpa()) %></div>
                            <% } %>
                            <% if (profile.getDateOfBirth() != null) { %>
                                <div class="profile-meta">DOB: <%= profile.getDateOfBirth() %></div>
                            <% } %>
                            <% if (profile.getAddress() != null && !profile.getAddress().isEmpty()) { %>
                                <div class="profile-meta">Address: <%= profile.getAddress() %></div>
                            <% } %>
                        </div>
                        <div class="profile-actions">
                            <a href="EditDeleteProfile?id=<%= profile.getId() %>" class="btn-small btn-edit">Edit</a>
                            <button onclick="deleteProfile(<%= profile.getId() %>)" class="btn-small btn-delete">Delete</button>
                        </div>
                    </div>
                </div>
            <% } %>
        <% } %>
    </div>

    <footer class="footer-links" style="margin-top: 3rem;">
        <a href="index.html">Home</a>
    </footer>

    <script>
        function deleteProfile(profileId) {
            if (confirm("Are you sure you want to delete this profile?")) {
                const form = document.createElement('form');
                form.method = 'POST';
                form.action = 'EditDeleteProfile';

                const input1 = document.createElement('input');
                input1.type = 'hidden';
                input1.name = 'id';
                input1.value = profileId;

                const input2 = document.createElement('input');
                input2.type = 'hidden';
                input2.name = 'action';
                input2.value = 'delete';

                form.appendChild(input1);
                form.appendChild(input2);
                document.body.appendChild(form);
                form.submit();
            }
        }
    </script>
</body>
</html>
