
package com.IA.view;

import com.IA.model.Student;
import com.IA.model.StudentDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * ListStudentServlet - Displays all registered students from database with search/filter support.
 *
 * URL Mapping: /ListStudent (configured in web.xml)
 * View: displayAll.jsp
 *
 * @author nikla
 * @version 2.0
 * @since 2025-12-25
 */
public class ListStudentServlet extends HttpServlet {

    /**
     * Handles HTTP GET requests to display the list of students.
     * Supports search and filter operations:
     * - Search by name
     * - Search by student ID
     * - Filter by program
     * - Filter by hobby
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Get search parameters
        String searchType = request.getParameter("searchType");
        String searchQuery = request.getParameter("searchQuery");

        List<Student> students = new ArrayList<>();

        try {
            // Perform search/filter based on parameters
            if (searchType != null && searchQuery != null &&
                !searchType.trim().isEmpty() && !searchQuery.trim().isEmpty()) {

                searchQuery = searchQuery.trim();

                switch (searchType.toLowerCase()) {
                    case "name":
                        students = StudentDAO.searchByName(searchQuery);
                        break;
                    case "studentid":
                        students = StudentDAO.searchByStudentId(searchQuery);
                        break;
                    case "program":
                        students = StudentDAO.filterByProgram(searchQuery);
                        break;
                    case "hobby":
                        students = StudentDAO.filterByHobby(searchQuery);
                        break;
                    default:
                        students = StudentDAO.getAllStudents();
                }

                // Store search parameters for display
                request.setAttribute("searchType", searchType);
                request.setAttribute("searchQuery", searchQuery);
            } else {
                // No search - get all students
                students = StudentDAO.getAllStudents();
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error retrieving students: " + e.getMessage());
        }

        // Place the list in request scope so the JSP can access it
        request.setAttribute("students", students);

        // Forward to the JSP page that renders the student list
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
        return "ListStudentServlet - Retrieves and displays students from database with search/filter support";
    }
}
