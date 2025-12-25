<%-- 
    Document   : displayAll
    Created on : Nov 18, 2025, 10:55:42 PM
    Author     : nikla
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*, com.IA.model.Student"%>

<%
    // Retrieve the list of students from request attribute
    List<Student> students = (List<Student>) request.getAttribute("students");
    String searchType = (String) request.getAttribute("searchType");
    String searchQuery = (String) request.getAttribute("searchQuery");
    String error = request.getParameter("error");
    String success = request.getParameter("success");

    if (students == null) {
        // Avoid diamond operator to support older JSP compiler (source 1.5)
        students = new ArrayList<Student>();
    }
%>

<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Student Profile Management</title>
    <link rel="stylesheet" href="assets/styles.css" />
    <style>
      .alert { padding: 1rem; border-radius: 0.5rem; margin-bottom: 1rem; }
      .alert-error { background: #fee2e2; color: #991b1b; border: 1px solid #fca5a5; }
      .alert-success { background: #dcfce7; color: #166534; border: 1px solid #86efac; }
      .search-container { background: white; padding: 1.5rem; border-radius: 0.5rem; margin-bottom: 1.5rem; box-shadow: 0 1px 3px rgba(0,0,0,0.1); }
      .search-form { display: flex; gap: 0.75rem; flex-wrap: wrap; align-items: end; }
      .search-group { display: flex; flex-direction: column; flex: 1; min-width: 150px; }
      .search-label { font-weight: 600; margin-bottom: 0.5rem; color: #374151; font-size: 0.875rem; }
      .search-select, .search-input { padding: 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem; font-size: 1rem; }
      .search-select:focus, .search-input:focus { outline: none; border-color: #3b82f6; box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }
      .search-btn { padding: 0.75rem 1.5rem; border: none; border-radius: 0.375rem; cursor: pointer; font-weight: 600; }
      .search-btn-primary { background: #3b82f6; color: white; }
      .search-btn-primary:hover { background: #2563eb; }
      .search-btn-secondary { background: #6b7280; color: white; text-decoration: none; display: inline-block; }
      .search-btn-secondary:hover { background: #4b5563; }
      .filter-info { color: #6b7280; font-style: italic; margin-bottom: 1rem; padding: 0.75rem; background: #f9fafb; border-radius: 0.375rem; }
      .card-actions { display: flex; gap: 0.5rem; margin-top: 1rem; }
      .btn-small { padding: 0.5rem 1rem; font-size: 0.875rem; border: none; border-radius: 0.375rem; cursor: pointer; text-decoration: none; font-weight: 600; }
      .btn-edit { background: #3b82f6; color: white; }
      .btn-edit:hover { background: #2563eb; }
      .btn-delete { background: #ef4444; color: white; }
      .btn-delete:hover { background: #dc2626; }
    </style>
  </head>

  <body>

    <!-- Page Header -->
    <header class="header">
      <h1>Student Profile Management</h1>
      <a class="btn-secondary" href="form.jsp">Add New Student</a>
    </header>

    <!-- Success/Error Messages -->
    <% if (success != null) { %>
      <div class="alert alert-success"><%= success %></div>
    <% } %>
    <% if (error != null) { %>
      <div class="alert alert-error"><%= error %></div>
    <% } %>

    <!-- Search Form -->
    <div class="search-container">
      <form method="GET" action="ListStudent" class="search-form">
        <div class="search-group">
          <label class="search-label" for="searchType">Search/Filter By</label>
          <select name="searchType" id="searchType" class="search-select">
            <option value="">-- Select Type --</option>
            <option value="name" <%= "name".equals(searchType) ? "selected" : "" %>>Name</option>
            <option value="studentId" <%= "studentId".equals(searchType) ? "selected" : "" %>>Student ID</option>
            <option value="program" <%= "program".equals(searchType) ? "selected" : "" %>>Program</option>
            <option value="hobby" <%= "hobby".equals(searchType) ? "selected" : "" %>>Hobby</option>
          </select>
        </div>
        <div class="search-group" style="flex: 2;">
          <label class="search-label" for="searchQuery">Search Term</label>
          <input type="text" name="searchQuery" id="searchQuery" class="search-input"
                 placeholder="Enter search term..." value="<%= searchQuery != null ? searchQuery : "" %>">
        </div>
        <button type="submit" class="search-btn search-btn-primary">Search</button>
        <a href="ListStudent" class="search-btn search-btn-secondary">Clear</a>
      </form>
    </div>

    <!-- Filter Info -->
    <% if (searchType != null && searchQuery != null && !searchType.isEmpty() && !searchQuery.isEmpty()) { %>
      <div class="filter-info">
        Filtering by <strong><%= searchType %></strong>: "<%= searchQuery %>"
        (<%= students.size() %> result<%= students.size() != 1 ? "s" : "" %> found)
      </div>
    <% } %>

    <!-- Cards Container -->
    <div class="cards-wrapper">
      <%

        // Check if list is empty or null - show empty state
        if (students == null || students.isEmpty()) {
      %>

        <!-- Empty State: No Students -->
        <section class="empty">
          <h2>There's nothing here…</h2>
          <p>Add your first student to get started.</p>
          <a class="pill-btn show" href="form.jsp">Add</a>
        </section>

      <%
        } else {
          // List has students - display them as cards
      %>

        <!-- Student Cards Grid -->
        <section class="cards">
          <%
            // Loop through each student and render a card
            for (Student s : students) {
          %>

            <!-- Individual Student Card -->
            <article class="card">
              <h3><%= s.getFullName() %></h3>
              <div class="meta">ID: <%= s.getStudentId() %></div>
              <div class="meta">Program: <%= s.getProgram() %></div>
              <% if (s.getGpa() > 0) { %>
                <div class="meta">GPA: <%= String.format("%.2f", s.getGpa()) %></div>
              <% } %>
              <div class="meta">Email: <%= s.getEmail() %></div>
              <% if (s.getPhone() != null && !s.getPhone().isEmpty()) { %>
                <div class="meta">Phone: <%= s.getPhone() %></div>
              <% } %>
              <% if (s.getDateOfBirth() != null) { %>
                <div class="meta">DOB: <%= s.getDateOfBirth() %></div>
              <% } %>
              <% if (s.getAddress() != null && !s.getAddress().isEmpty()) { %>
                <div class="meta">Address: <%= s.getAddress() %></div>
              <% } %>

              <!-- Hobbies Tags -->
              <div class="tags">
                <%
                  if (s.getHobbies() != null) {
                    for (String h : s.getHobbies()) {
                %>
                      <span class="tag"><%= h %></span>
                <%
                    }
                  }
                %>
              </div>

              <!-- Self Introduction -->
              <% if (s.getSelfIntro() != null && !s.getSelfIntro().isEmpty()) { %>
                <p style="margin-top:8px; color:#d1d5db; white-space: pre-wrap;"><%= s.getSelfIntro() %></p>
              <% } %>

              <!-- Action Buttons -->
              <div class="card-actions">
                <a href="EditDeleteStudentServlet?id=<%= s.getId() %>" class="btn-small btn-edit">Edit</a>
                <button onclick="deleteStudent(<%= s.getId() %>)" class="btn-small btn-delete">Delete</button>
              </div>
            </article>

          <%
            }
          %>
        </section>

      <%
        }
      %>
    </div>

    <!-- Footer Navigation -->
    <footer class="footer-links">
      <a href="index.html">Home</a>
    </footer>

    <script>
      function deleteStudent(studentId) {
        if (confirm("Are you sure you want to delete this student?")) {
          const form = document.createElement('form');
          form.method = 'POST';
          form.action = 'EditDeleteStudentServlet';

          const input1 = document.createElement('input');
          input1.type = 'hidden';
          input1.name = 'id';
          input1.value = studentId;

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
