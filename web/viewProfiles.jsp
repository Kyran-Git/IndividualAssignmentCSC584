
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.*, com.IA.model.Student, com.IA.model.StudentDAO"%>

<%
    // Retrieve all students from database
    List<Student> profiles = StudentDAO.getAllStudents();
    String error = request.getParameter("error");
    String success = request.getParameter("success");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>All Student Profiles</title>
    <link rel="stylesheet" href="assets/styles.css">
    <style>
        .alert {
            padding: 1rem;
            border-radius: 0.5rem;
            margin-bottom: 1.5rem;
            font-weight: 600;
        }
        .alert-success {
            background: #dcfce7;
            color: #166534;
            border: 1px solid #86efac;
        }
        .alert-error {
            background: #fee2e2;
            color: #991b1b;
            border: 1px solid #fca5a5;
        }
        .profiles-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 2rem;
            flex-wrap: wrap;
            gap: 1rem;
        }
        .profile-count {
            color: #6b7280;
            font-size: 1rem;
        }
        .profiles-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(350px, 1fr));
            gap: 1.5rem;
            margin-bottom: 2rem;
        }
        .profile-card {
            background: white;
            border: 1px solid #e5e7eb;
            border-radius: 0.5rem;
            padding: 1.5rem;
            transition: all 0.3s;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }
        .profile-card:hover {
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.15);
            transform: translateY(-2px);
        }
        .profile-card-header {
            border-bottom: 2px solid #e5e7eb;
            padding-bottom: 1rem;
            margin-bottom: 1rem;
        }
        .profile-name {
            font-size: 1.25rem;
            font-weight: 700;
            color: #1f2937;
            margin-bottom: 0.25rem;
        }
        .profile-id {
            color: #6b7280;
            font-size: 0.875rem;
        }
        .profile-detail {
            margin-bottom: 0.75rem;
            display: flex;
            align-items: start;
        }
        .detail-icon {
            margin-right: 0.5rem;
            color: #3b82f6;
        }
        .detail-text {
            color: #4b5563;
            font-size: 0.9rem;
        }
        .detail-label {
            font-weight: 600;
            color: #374151;
            margin-right: 0.25rem;
        }
        .hobbies-section {
            margin-top: 1rem;
            padding-top: 1rem;
            border-top: 1px solid #e5e7eb;
        }
        .hobby-tags {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
            margin-top: 0.5rem;
        }
        .hobby-tag {
            background: #dbeafe;
            color: #1e40af;
            padding: 0.25rem 0.75rem;
            border-radius: 9999px;
            font-size: 0.75rem;
            font-weight: 500;
        }
        .intro-text {
            background: #f9fafb;
            padding: 0.75rem;
            border-radius: 0.375rem;
            margin-top: 1rem;
            color: #4b5563;
            font-size: 0.875rem;
            line-height: 1.5;
            max-height: 4.5em;
            overflow: hidden;
            text-overflow: ellipsis;
        }
        .card-actions {
            display: flex;
            gap: 0.5rem;
            margin-top: 1rem;
            padding-top: 1rem;
            border-top: 1px solid #e5e7eb;
        }
        .btn-small {
            padding: 0.5rem 1rem;
            font-size: 0.875rem;
            border: none;
            border-radius: 0.375rem;
            cursor: pointer;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
            text-align: center;
            flex: 1;
        }
        .btn-view {
            background: #3b82f6;
            color: white;
        }
        .btn-view:hover {
            background: #2563eb;
        }
        .btn-edit {
            background: #10b981;
            color: white;
        }
        .btn-edit:hover {
            background: #059669;
        }
        .empty-state {
            text-align: center;
            padding: 4rem 2rem;
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
        }
        .empty-state h2 {
            color: #6b7280;
            margin-bottom: 1rem;
        }
        .empty-state p {
            color: #9ca3af;
            margin-bottom: 2rem;
        }
    </style>
</head>
<body>
    <header class="header">
        <h1>All Student Profiles</h1>
        <a class="btn-secondary" href="form.jsp">Add New Student</a>
    </header>

    <% if (success != null) { %>
        <div class="alert alert-success">✓ <%= success %></div>
    <% } %>

    <% if (error != null) { %>
        <div class="alert alert-error">✗ <%= error %></div>
    <% } %>

    <div class="profiles-header">
        <h2 style="margin: 0; color: #1f2937;">
            <% if (profiles != null && !profiles.isEmpty()) { %>
                <%= profiles.size() %> Student<%= profiles.size() != 1 ? "s" : "" %> Found
            <% } else { %>
                No Students
            <% } %>
        </h2>
        <a href="ListStudent" class="btn btn-secondary" style="text-decoration: none;">Advanced Search & Filter</a>
    </div>

    <% if (profiles == null || profiles.isEmpty()) { %>
        <div class="empty-state">
            <h2>📋 No Student Profiles Found</h2>
            <p>Get started by adding your first student profile.</p>
            <a href="form.jsp" class="btn btn-primary">Add First Student</a>
        </div>
    <% } else { %>
        <div class="profiles-grid">
            <% for (Student profile : profiles) { %>
                <article class="profile-card">
                    <div class="profile-card-header">
                        <div class="profile-name"><%= profile.getFullName() %></div>
                        <div class="profile-id">ID: <%= profile.getStudentId() %></div>
                    </div>

                    <div class="profile-detail">
                        <span class="detail-icon">🎓</span>
                        <div class="detail-text">
                            <span class="detail-label">Program:</span>
                            <%= profile.getProgram() %>
                        </div>
                    </div>

                    <div class="profile-detail">
                        <span class="detail-icon">📧</span>
                        <div class="detail-text">
                            <span class="detail-label">Email:</span>
                            <%= profile.getEmail() %>
                        </div>
                    </div>

                    <% if (profile.getPhone() != null && !profile.getPhone().isEmpty()) { %>
                    <div class="profile-detail">
                        <span class="detail-icon">📱</span>
                        <div class="detail-text">
                            <span class="detail-label">Phone:</span>
                            <%= profile.getPhone() %>
                        </div>
                    </div>
                    <% } %>

                    <% if (profile.getGpa() > 0) { %>
                    <div class="profile-detail">
                        <span class="detail-icon">📊</span>
                        <div class="detail-text">
                            <span class="detail-label">GPA:</span>
                            <%= String.format("%.2f", profile.getGpa()) %>/4.0
                        </div>
                    </div>
                    <% } %>

                    <% if (profile.getDateOfBirth() != null) { %>
                    <div class="profile-detail">
                        <span class="detail-icon">🎂</span>
                        <div class="detail-text">
                            <span class="detail-label">DOB:</span>
                            <%= profile.getDateOfBirth() %>
                        </div>
                    </div>
                    <% } %>

                    <% if (profile.getAddress() != null && !profile.getAddress().isEmpty()) { %>
                    <div class="profile-detail">
                        <span class="detail-icon">📍</span>
                        <div class="detail-text">
                            <span class="detail-label">Address:</span>
                            <%= profile.getAddress() %>
                        </div>
                    </div>
                    <% } %>

                    <% if (profile.getHobbies() != null && profile.getHobbies().length > 0) { %>
                    <div class="hobbies-section">
                        <div class="detail-label" style="font-size: 0.875rem;">Hobbies:</div>
                        <div class="hobby-tags">
                            <% for (String hobby : profile.getHobbies()) { %>
                                <span class="hobby-tag"><%= hobby %></span>
                            <% } %>
                        </div>
                    </div>
                    <% } %>

                    <% if (profile.getSelfIntro() != null && !profile.getSelfIntro().isEmpty()) { %>
                    <div class="intro-text">
                        <%= profile.getSelfIntro() %>
                    </div>
                    <% } %>

                    <div class="card-actions">
                        <a href="EditDeleteStudentServlet?id=<%= profile.getId() %>" class="btn-small btn-edit">Edit</a>
                    </div>
                </article>
            <% } %>
        </div>
    <% } %>

    <footer class="footer-links" style="margin-top: 3rem;">
        <a href="index.html">Home</a> |
        <a href="form.jsp">Add Student</a> |
        <a href="ListStudent">Advanced Search</a>
    </footer>
</body>
</html>

