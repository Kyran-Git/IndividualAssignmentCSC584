<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%
  // Retrieve validation error and form values from request attributes
  String err = (String) request.getAttribute("error");
  String nameVal = (String) request.getAttribute("name");
  String idVal = (String) request.getAttribute("studentId");
  String programVal = (String) request.getAttribute("program");
  String emailVal = (String) request.getAttribute("email");
  String introVal = (String) request.getAttribute("selfIntro");
  String[] hobbiesVal = (String[]) request.getAttribute("hobbies");
%>

<!DOCTYPE html>
<html lang="en">

  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Add Student</title>
    <link rel="stylesheet" href="assets/styles.css" />
    <script defer src="assets/app.js"></script>
  </head>

  <body>
    <main class="page">
      <section class="form-card" role="region" aria-labelledby="formTitle">
        <h1 id="formTitle" style="margin:0 0 12px">Add a Student</h1>

        <% if (err != null) { %>
          <div class="error"><%= err %></div>
        <% } %>

        <form method="post" action="AddStudentServlet" accept-charset="UTF-8">
          <div class="form-grid">

            <!-- Name Field -->
            <div>
              <label class="label" for="name">Full Name</label>
              <input class="input" id="name" name="name" type="text" value="<%= nameVal == null ? "" : nameVal %>" required />
            </div>

            <!-- Student ID Field -->
            <div>
              <label class="label" for="studentId">Student ID</label>
              <input class="input" id="studentId" name="studentId" type="text" value="<%= idVal == null ? "" : idVal %>" required />
            </div>

            <!-- Program Field -->
            <div>
              <label class="label" for="program">Program</label>
              <input class="input" id="program" name="program" type="text" value="<%= programVal == null ? "" : programVal %>" required />
            </div>

            <!-- Email Field -->
            <div>
              <label class="label" for="email">Email</label>
              <input class="input" id="email" name="email" type="email" value="<%= emailVal == null ? "" : emailVal %>" required />
            </div>

            <!-- Hobbies Multi-select -->
            <div class="full">
              <label class="label" for="hobbies">Hobbies (Ctrl/Cmd+Click to select multiple)</label>
              <select class="input" id="hobbies" name="hobbies" multiple size="4">
                <%
                  String[] options = {"Reading","Gaming","Music","Sports","Travel","Cooking","Art"};
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
            <div class="full">
              <label class="label" for="selfIntro">Short Self-Introduction</label>
              <textarea class="textarea" id="selfIntro" name="selfIntro" maxlength="600" placeholder="Who are you, what do you like, what are your goals?"><%= introVal == null ? "" : introVal %></textarea>
            </div>

          </div>

          <!-- Action Buttons -->
          <div class="row-actions">
            <button class="pill-btn show" type="submit">Save Student</button>
            <a class="btn-secondary" href="ListStudent">Display All</a>
          </div>
        </form>

      </section>
    </main>

  </body>

</html>
