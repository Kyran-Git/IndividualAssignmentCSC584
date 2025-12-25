
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.IA.model.Student"%>

<%
    Student profile = (Student) request.getAttribute("profile");
    String message = (String) request.getAttribute("message");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Student Profile Details</title>
    <link rel="stylesheet" href="assets/styles.css">
    <style>
        .profile-container {
            max-width: 800px;
            margin: 2rem auto;
            background: white;
            border-radius: 0.5rem;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
            padding: 2rem;
        }
        .profile-header {
            text-align: center;
            padding-bottom: 1.5rem;
            border-bottom: 2px solid #e5e7eb;
            margin-bottom: 2rem;
        }
        .profile-title {
            font-size: 2rem;
            font-weight: 700;
            color: #1f2937;
            margin-bottom: 0.5rem;
        }
        .profile-subtitle {
            color: #6b7280;
            font-size: 1.125rem;
        }
        .section {
            margin-bottom: 2rem;
        }
        .section-title {
            font-size: 1.25rem;
            font-weight: 600;
            color: #374151;
            margin-bottom: 1rem;
            padding-bottom: 0.5rem;
            border-bottom: 1px solid #e5e7eb;
        }
        .detail-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 1rem;
        }
        .detail-item {
            padding: 0.75rem;
            background: #f9fafb;
            border-radius: 0.375rem;
        }
        .detail-item.full {
            grid-column: 1 / -1;
        }
        .detail-label {
            font-weight: 600;
            color: #6b7280;
            font-size: 0.875rem;
            margin-bottom: 0.25rem;
        }
        .detail-value {
            color: #1f2937;
            font-size: 1rem;
        }
        .hobbies-tags {
            display: flex;
            flex-wrap: wrap;
            gap: 0.5rem;
            margin-top: 0.5rem;
        }
        .hobby-tag {
            background: #3b82f6;
            color: white;
            padding: 0.375rem 0.75rem;
            border-radius: 9999px;
            font-size: 0.875rem;
            font-weight: 500;
        }
        .success-badge {
            display: inline-block;
            background: #dcfce7;
            color: #166534;
            padding: 0.5rem 1rem;
            border-radius: 0.375rem;
            margin-bottom: 1.5rem;
            font-weight: 600;
        }
        .error-badge {
            display: inline-block;
            background: #fee2e2;
            color: #991b1b;
            padding: 0.5rem 1rem;
            border-radius: 0.375rem;
            margin-bottom: 1.5rem;
            font-weight: 600;
        }
        .actions {
            display: flex;
            gap: 1rem;
            justify-content: center;
            margin-top: 2rem;
            padding-top: 2rem;
            border-top: 2px solid #e5e7eb;
        }
        .btn {
            padding: 0.75rem 1.5rem;
            border: none;
            border-radius: 0.375rem;
            cursor: pointer;
            font-weight: 600;
            text-decoration: none;
            display: inline-block;
        }
        .btn-primary {
            background: #3b82f6;
            color: white;
        }
        .btn-primary:hover {
            background: #2563eb;
        }
        .btn-secondary {
            background: #6b7280;
            color: white;
        }
        .btn-secondary:hover {
            background: #4b5563;
        }
        @media (max-width: 768px) {
            .detail-grid {
                grid-template-columns: 1fr;
            }
        }
    </style>
</head>
<body>
    <header class="header">
        <h1>Profile Saved Successfully</h1>
    </header>

    <div class="profile-container">
        <% if (message != null) { %>
            <div class="success-badge">✓ <%= message %></div>
        <% } %>

        <% if (error != null) { %>
            <div class="error-badge">✗ <%= error %></div>
        <% } %>

        <% if (profile != null) { %>
            <div class="profile-header">
                <div class="profile-title"><%= profile.getFullName() %></div>
                <div class="profile-subtitle">Student ID: <%= profile.getStudentId() %></div>
            </div>

            <!-- Personal Information Section -->
            <div class="section">
                <div class="section-title">📋 Personal Information</div>
                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">First Name</div>
                        <div class="detail-value"><%= profile.getFirstName() %></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Last Name</div>
                        <div class="detail-value"><%= profile.getLastName() %></div>
                    </div>
                    <% if (profile.getDateOfBirth() != null) { %>
                    <div class="detail-item">
                        <div class="detail-label">Date of Birth</div>
                        <div class="detail-value"><%= profile.getDateOfBirth() %></div>
                    </div>
                    <% } %>
                    <% if (profile.getPhone() != null && !profile.getPhone().isEmpty()) { %>
                    <div class="detail-item">
                        <div class="detail-label">Phone</div>
                        <div class="detail-value"><%= profile.getPhone() %></div>
                    </div>
                    <% } %>
                    <% if (profile.getAddress() != null && !profile.getAddress().isEmpty()) { %>
                    <div class="detail-item full">
                        <div class="detail-label">Address</div>
                        <div class="detail-value"><%= profile.getAddress() %></div>
                    </div>
                    <% } %>
                </div>
            </div>

            <!-- Academic Information Section -->
            <div class="section">
                <div class="section-title">🎓 Academic Information</div>
                <div class="detail-grid">
                    <div class="detail-item">
                        <div class="detail-label">Student ID</div>
                        <div class="detail-value"><%= profile.getStudentId() %></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Program/Major</div>
                        <div class="detail-value"><%= profile.getProgram() %></div>
                    </div>
                    <div class="detail-item">
                        <div class="detail-label">Email</div>
                        <div class="detail-value"><%= profile.getEmail() %></div>
                    </div>
                    <% if (profile.getGpa() > 0) { %>
                    <div class="detail-item">
                        <div class="detail-label">GPA</div>
                        <div class="detail-value"><%= String.format("%.2f", profile.getGpa()) %>/4.0</div>
                    </div>
                    <% } %>
                </div>
            </div>

            <!-- Interests & Activities Section -->
            <div class="section">
                <div class="section-title">🎯 Interests & Activities</div>

                <% if (profile.getHobbies() != null && profile.getHobbies().length > 0) { %>
                <div class="detail-item full">
                    <div class="detail-label">Hobbies</div>
                    <div class="hobbies-tags">
                        <% for (String hobby : profile.getHobbies()) { %>
                            <span class="hobby-tag"><%= hobby %></span>
                        <% } %>
                    </div>
                </div>
                <% } %>

                <% if (profile.getSelfIntro() != null && !profile.getSelfIntro().isEmpty()) { %>
                <div class="detail-item full" style="margin-top: 1rem;">
                    <div class="detail-label">Self-Introduction</div>
                    <div class="detail-value" style="white-space: pre-wrap; line-height: 1.6;"><%= profile.getSelfIntro() %></div>
                </div>
                <% } %>
            </div>

            <div class="actions">
                <a href="form.jsp" class="btn btn-primary">Add Another Student</a>
                <a href="viewProfiles.jsp" class="btn btn-secondary">View All Profiles</a>
                <a href="index.html" class="btn btn-secondary">Home</a>
            </div>
        <% } else { %>
            <div class="error-badge">No profile data available to display.</div>
            <div class="actions">
                <a href="form.jsp" class="btn btn-primary">Add Student</a>
                <a href="index.html" class="btn btn-secondary">Home</a>
            </div>
        <% } %>
    </div>

    <footer class="footer-links" style="margin-top: 3rem;">
        <a href="index.html">Home</a> |
        <a href="form.jsp">Add Student</a> |
        <a href="viewProfiles.jsp">View All Profiles</a>
    </footer>
</body>
</html>

