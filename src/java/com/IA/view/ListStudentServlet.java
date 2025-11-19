
package com.IA.view;

import com.IA.model.Student;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ListStudentServlet - Displays all registered students in a formatted view.

 * URL Mapping: /ListStudent (configured in web.xml)
 * View: displayAll.jsp
 *
 * @author nikla
 * @version 1.0
 * @since 2025-11-18
 */
public class ListStudentServlet extends HttpServlet {

    /**
     * Handles HTTP GET requests to display the list of all students.
     *
     * This method performs the following operations:
     * 1. Retrieves the ServletContext to access shared data
     * 2. Attempts to retrieve the existing students list from application scope
     * 3. If no list exists (first access), creates a new thread-safe empty list
     * 4. Stores the list in request scope so the JSP can access it
     * 5. Forwards the request to displayAll.jsp for rendering
     *
     * The servlet ensures a list always exists in application scope, even if empty,
     * so the JSP can safely iterate over it without null checks.

     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // ===== RETRIEVE STUDENT LIST FROM APPLICATION SCOPE =====
        // Get the ServletContext which holds app-wide shared data
        ServletContext app = getServletContext();

        // Attempt to retrieve the students list from servlet context
        @SuppressWarnings("unchecked")
        List<Student> list = (List<Student>) app.getAttribute("students");

        // ===== INITIALIZE LIST IF IT DOESN'T EXIST =====
        // If this is the first time accessing the list create a new thread-safe synchronized list
        if (list == null) {
            // This prevents concurrent (same time) modification issues when multiple users access it
            list = Collections.synchronizedList(new ArrayList<Student>());

            // Store the new list in servlet context for future requests
            app.setAttribute("students", list);
        }

        // ===== FORWARD TO JSP FOR RENDERING =====
        // Place the list in request scope/servlet context so the JSP can access it
        // The JSP will read over this list to display student cards
        request.setAttribute("students", list);

        // Forward to the JSP page that renders the student list
        // Using forward (not redirect) preserves the request attributes
        RequestDispatcher rd = request.getRequestDispatcher("/displayAll.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * Returns a brief description of this servlet.
     */
    @Override
    public String getServletInfo() {
        return "ListStudentServlet - Retrieves and displays all registered students";
    }
}
