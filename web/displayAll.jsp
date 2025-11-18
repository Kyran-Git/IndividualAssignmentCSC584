<%-- 
    Document   : displayAll
    Created on : Nov 18, 2025, 10:55:42 PM
    Author     : nikla
--%>

<%@page contentType="text/html" pageEncoding="UTF-8" import="java.util.*, com.IA.model.Student"%>

<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>All Students</title>
    <link rel="stylesheet" href="assets/styles.css" />
  </head>

  <body>

    <!-- Page Header -->
    <header class="header">
      <h1>Students</h1>
      <a class="btn-secondary" href="form.jsp">Add More</a>
    </header>

    <!-- Cards Container -->
    <div class="cards-wrapper">
      <%
        // Retrieve the list of students from request attribute
        List<Student> students = (List<Student>) request.getAttribute("students");

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
              String hobbies = (s.getHobbies() == null) ? "" : String.join(", ", s.getHobbies());
          %>

            <!-- Individual Student Card -->
            <article class="card">
              <h3><%= s.getName() %></h3>
              <div class="meta">ID: <%= s.getStudentId() %></div>
              <div class="meta">Program: <%= s.getProgram() %></div>
              <div class="meta">Email: <%= s.getEmail() %></div>

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
              <p style="margin-top:8px; color:#d1d5db; white-space: pre-wrap;"><%= s.getSelfIntro() == null ? "" : s.getSelfIntro() %></p>
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

  </body>

</html>
