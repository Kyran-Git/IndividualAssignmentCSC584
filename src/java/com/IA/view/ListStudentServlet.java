
package com.IA.view;

import com.IA.model.Student;
import java.io.IOException;
import java.sql.*;
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
            Connection conn = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/studentProfiles", "app", "app");

            // Perform search/filter based on parameters
            if (searchType != null && searchQuery != null &&
                !searchType.trim().isEmpty() && !searchQuery.trim().isEmpty()) {

                searchQuery = searchQuery.trim();
                PreparedStatement pstmt;

                switch (searchType.toLowerCase()) {
                    case "name":
                        String sql = "SELECT * FROM STUDENT WHERE UPPER(FIRST_NAME) LIKE ? OR UPPER(LAST_NAME) LIKE ? ORDER BY LAST_NAME, FIRST_NAME";
                        pstmt = conn.prepareStatement(sql);
                        String searchTerm = "%" + searchQuery.toUpperCase() + "%";
                        pstmt.setString(1, searchTerm);
                        pstmt.setString(2, searchTerm);
                        ResultSet rs = pstmt.executeQuery();
                        while (rs.next()) {
                            students.add(mapResultSetToStudent(rs));
                        }
                        break;
                    case "studentid":
                        sql = "SELECT * FROM STUDENT WHERE UPPER(STUDENT_ID) LIKE ? ORDER BY STUDENT_ID";
                        pstmt = conn.prepareStatement(sql);
                        searchTerm = "%" + searchQuery.toUpperCase() + "%";
                        pstmt.setString(1, searchTerm);
                        rs = pstmt.executeQuery();
                        while (rs.next()) {
                            students.add(mapResultSetToStudent(rs));
                        }
                        break;
                    case "program":
                        sql = "SELECT * FROM STUDENT WHERE UPPER(PROGRAM) LIKE ? ORDER BY PROGRAM, LAST_NAME, FIRST_NAME";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, "%" + searchQuery.toUpperCase() + "%");
                        rs = pstmt.executeQuery();
                        while (rs.next()) {
                            students.add(mapResultSetToStudent(rs));
                        }
                        break;
                    case "hobby":
                        sql = "SELECT * FROM STUDENT WHERE UPPER(HOBBIES) LIKE ? ORDER BY LAST_NAME, FIRST_NAME";
                        pstmt = conn.prepareStatement(sql);
                        pstmt.setString(1, "%" + searchQuery.toUpperCase() + "%");
                        rs = pstmt.executeQuery();
                        while (rs.next()) {
                            students.add(mapResultSetToStudent(rs));
                        }
                        break;
                    default:
                        sql = "SELECT * FROM STUDENT ORDER BY ID";
                        Statement stmt = conn.createStatement();
                        rs = stmt.executeQuery(sql);
                        while (rs.next()) {
                            students.add(mapResultSetToStudent(rs));
                        }
                }

                // Store search parameters for display
                request.setAttribute("searchType", searchType);
                request.setAttribute("searchQuery", searchQuery);
            } else {
                // No search - get all students
                String sql = "SELECT * FROM STUDENT ORDER BY ID";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    students.add(mapResultSetToStudent(rs));
                }
            }

            conn.close();
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

    /** Helper method to map ResultSet to Student object */
    private static Student mapResultSetToStudent(ResultSet rs) throws SQLException {
        Student student = new Student();
        student.setId(rs.getInt("ID"));
        student.setFirstName(rs.getString("FIRST_NAME"));
        student.setLastName(rs.getString("LAST_NAME"));
        student.setStudentId(rs.getString("STUDENT_ID"));
        student.setProgram(rs.getString("PROGRAM"));
        student.setEmail(rs.getString("EMAIL"));
        student.setPhone(rs.getString("PHONE"));
        student.setDateOfBirth(rs.getDate("DATE_OF_BIRTH"));
        student.setAddress(rs.getString("ADDRESS"));
        student.setGpa(rs.getDouble("GPA"));
        student.setHobbies(stringToArray(rs.getString("HOBBIES")));
        student.setSelfIntro(rs.getString("SELF_INTRO"));
        return student;
    }

    /** Helper method to convert comma-separated string to array */
    private static String[] stringToArray(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new String[0];
        }
        return str.split(",");
    }
}
