<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.IA.model.Student"%>

<%
  // Retrieve validation error and form values from request attributes
  String err = (String) request.getAttribute("error");
  String success = (String) request.getAttribute("success");

  // Check if we're in edit mode
  Boolean isEditObj = (Boolean) request.getAttribute("isEdit");
  boolean isEdit = isEditObj != null && isEditObj;

  // Get student object if in edit mode
  Student student = (Student) request.getAttribute("student");

  // Get form values - either from validation error or from student object
  String firstNameVal = (String) request.getAttribute("firstName");
  String lastNameVal = (String) request.getAttribute("lastName");
  String idVal = (String) request.getAttribute("studentId");
  String programVal = (String) request.getAttribute("program");
  String emailVal = (String) request.getAttribute("email");
  String phoneVal = (String) request.getAttribute("phone");
  String dobVal = (String) request.getAttribute("dateOfBirth");
  String addressVal = (String) request.getAttribute("address");
  String gpaVal = (String) request.getAttribute("gpa");
  String introVal = (String) request.getAttribute("selfIntro");
  String[] hobbiesVal = (String[]) request.getAttribute("hobbies");

  // If in edit mode and no validation errors, populate from student object
  if (isEdit && student != null) {
    if (firstNameVal == null) firstNameVal = student.getFirstName();
    if (lastNameVal == null) lastNameVal = student.getLastName();
    if (idVal == null) idVal = student.getStudentId();
    if (programVal == null) programVal = student.getProgram();
    if (emailVal == null) emailVal = student.getEmail();
    if (phoneVal == null) phoneVal = student.getPhone();
    if (dobVal == null && student.getDateOfBirth() != null) dobVal = student.getDateOfBirth().toString();
    if (addressVal == null) addressVal = student.getAddress();
    if (gpaVal == null && student.getGpa() > 0) gpaVal = String.valueOf(student.getGpa());
    if (introVal == null) introVal = student.getSelfIntro();
    if (hobbiesVal == null) hobbiesVal = student.getHobbies();
  }
%>

<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title><%= isEdit ? "Edit Student Profile" : "Add Student Profile" %></title>
    <link rel="stylesheet" href="assets/styles.css" />
    <style>
      .form-card { max-width: 800px; margin: 2rem auto; }
      .form-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 1rem; }
      .full { grid-column: 1 / -1; }
      .form-group { display: flex; flex-direction: column; }
      .label { font-weight: 600; margin-bottom: 0.5rem; color: #374151; font-size: 0.875rem; }
      .required::after { content: " *"; color: #ef4444; }
      .input, .textarea, .select { padding: 0.75rem; border: 1px solid #d1d5db; border-radius: 0.375rem; font-size: 1rem; }
      .input:focus, .textarea:focus, .select:focus { outline: none; border-color: #3b82f6; box-shadow: 0 0 0 3px rgba(59, 130, 246, 0.1); }
      .input:read-only { background-color: #f3f4f6; cursor: not-allowed; }
      .error-msg { background: #fee2e2; color: #991b1b; padding: 1rem; border-radius: 0.375rem; margin-bottom: 1rem; border: 1px solid #fca5a5; }
      .success-msg { background: #dcfce7; color: #166534; padding: 1rem; border-radius: 0.375rem; margin-bottom: 1rem; border: 1px solid #86efac; }
      .section-title { font-size: 1.125rem; font-weight: 600; color: #1f2937; margin: 1.5rem 0 0.75rem 0; padding-bottom: 0.5rem; border-bottom: 2px solid #e5e7eb; }
      .row-actions { display: flex; gap: 1rem; margin-top: 2rem; flex-wrap: wrap; }
      .btn { padding: 0.75rem 1.5rem; border: none; border-radius: 0.375rem; cursor: pointer; font-weight: 600; font-size: 1rem; transition: all 0.2s; }
      .btn-primary { background: #3b82f6; color: white; }
      .btn-primary:hover { background: #2563eb; transform: translateY(-1px); }
      .btn-secondary { background: #6b7280; color: white; text-decoration: none; display: inline-block; text-align: center; }
      .btn-secondary:hover { background: #4b5563; }
      .btn-danger { background: #ef4444; color: white; }
      .btn-danger:hover { background: #dc2626; }
      @media (max-width: 768px) {
        .form-grid { grid-template-columns: 1fr; }
        .full { grid-column: 1; }
      }
    </style>
    <script defer src="assets/app.js"></script>
  <script>
      const successMessage = "<%= success %>";
      if (successMessage && successMessage !== "null") {
        alert(successMessage);
      }
    </script>
  </head>

  <body>
    <main class="page">
      <section class="form-card" role="region" aria-labelledby="formTitle">
        <h1 id="formTitle" style="margin:0 0 12px"><%= isEdit ? "Edit Student" : "Add a Student" %></h1>

        <% if (err != null) { %>
          <div class="error-msg"><%= err %></div>
        <% } %>

        <form method="post" action="<%= isEdit ? "EditDeleteStudentServlet" : "AddStudentServlet" %>" accept-charset="UTF-8">
          <% if (isEdit && student != null) { %>
            <input type="hidden" name="id" value="<%= student.getId() %>">
            <input type="hidden" name="action" value="update">
          <% } %>

          <!-- Personal Information Section -->
          <div class="section-title full">Personal Information</div>

          <div class="form-grid">
            <!-- First Name -->
            <div class="form-group">
              <label class="label required" for="firstName">First Name</label>
              <input class="input" id="firstName" name="firstName" type="text"
                     value="<%= firstNameVal == null ? "" : firstNameVal %>" required />
            </div>

            <!-- Last Name -->
            <div class="form-group">
              <label class="label required" for="lastName">Last Name</label>
              <input class="input" id="lastName" name="lastName" type="text"
                     value="<%= lastNameVal == null ? "" : lastNameVal %>" required />
            </div>

            <!-- Date of Birth -->
            <div class="form-group">
              <label class="label" for="dateOfBirth">Date of Birth</label>
              <input class="input" id="dateOfBirth" name="dateOfBirth" type="date"
                     value="<%= dobVal == null ? "" : dobVal %>" />
            </div>

            <!-- Phone -->
            <div class="form-group">
              <label class="label" for="phone">Phone Number</label>
              <input class="input" id="phone" name="phone" type="tel"
                     value="<%= phoneVal == null ? "" : phoneVal %>"
                     placeholder="555-0123" />
            </div>

            <!-- Address -->
            <div class="form-group full">
              <label class="label" for="address">Address</label>
              <input class="input" id="address" name="address" type="text"
                     value="<%= addressVal == null ? "" : addressVal %>"
                     placeholder="123 Main Street, City, State" />
            </div>
          </div>

          <!-- Academic Information Section -->
          <div class="section-title full">Academic Information</div>

          <div class="form-grid">
            <!-- Student ID -->
            <div class="form-group">
              <label class="label required" for="studentId">Student ID</label>
              <input class="input" id="studentId" name="studentId" type="text"
                     value="<%= idVal == null ? "" : idVal %>"
                     <%= isEdit ? "readonly" : "required" %>
                     placeholder="S12345" />
            </div>

            <!-- Program -->
            <div class="form-group">
              <label class="label required" for="program">Program/Major</label>
              <input class="input" id="program" name="program" type="text"
                     value="<%= programVal == null ? "" : programVal %>" required
                     placeholder="Computer Science" />
            </div>

            <!-- GPA -->
            <div class="form-group">
              <label class="label" for="gpa">GPA</label>
              <input class="input" id="gpa" name="gpa" type="number"
                     step="0.01" min="0" max="4.0"
                     value="<%= gpaVal == null ? "" : gpaVal %>"
                     placeholder="3.75" />
            </div>

            <!-- Email -->
            <div class="form-group">
              <label class="label required" for="email">Email</label>
              <input class="input" id="email" name="email" type="email"
                     value="<%= emailVal == null ? "" : emailVal %>" required
                     placeholder="student@example.com" />
            </div>
          </div>

          <!-- Interests & Activities Section -->
          <div class="section-title full">Interests & Activities</div>

          <div class="form-grid">
            <!-- Hobbies Multi-select -->
            <div class="form-group full">
              <label class="label" for="hobbies">Hobbies (Ctrl/Cmd+Click to select multiple)</label>
              <select class="input" id="hobbies" name="hobbies" multiple size="5"
                      style="min-height: 120px;">
                <%
                  String[] options = {"Reading","Gaming","Music","Sports","Travel","Cooking","Art","Photography","Swimming","Dancing","Coding"};
                  for (String opt : options) {
                    boolean selected = false;
                    if (hobbiesVal != null) {
                      for (String hv : hobbiesVal) {
                        if (opt.equals(hv)) {
                          selected = true;
                          break;
                        }
                      }
                    }
                %>
                  <option <%= selected ? "selected" : "" %>><%= opt %></option>
                <%
                  }
                %>
              </select>
            </div>

            <!-- Self Introduction Textarea -->
            <div class="form-group full">
              <label class="label" for="selfIntro">Self-Introduction</label>
              <textarea class="textarea" id="selfIntro" name="selfIntro" rows="4"
                        maxlength="1000"
                        placeholder="Tell us about yourself, your goals, and what makes you unique..."><%= introVal == null ? "" : introVal %></textarea>
            </div>

          </div>

          <!-- Action Buttons -->
          <div class="row-actions">
            <button class="btn btn-primary" type="submit"><%= isEdit ? "Update Student" : "Save Student" %></button>
            <a class="btn btn-secondary" href="ListStudent">Display All</a>
            <% if (isEdit) { %>
              <button class="btn" type="button" onclick="deleteStudent()" style="background: #ef4444; color: white;">Delete</button>
            <% } %>
          </div>
        </form>

        <% if (isEdit && student != null) { %>
        <script>
          function deleteStudent() {
            if (confirm("Are you sure you want to delete this student?")) {
              const form = document.querySelector('form');
              const actionInput = form.querySelector('input[name="action"]');
              if (actionInput) {
                actionInput.value = 'delete';
              }
              form.submit();
            }
          }
        </script>
        <% } %>

      </section>
    </main>

  </body>

</html>
