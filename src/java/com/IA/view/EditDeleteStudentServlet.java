package com.IA.view;

import com.IA.model.Student;
import java.io.IOException;
import java.sql.*;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * EditDeleteStudentServlet - Handles edit and delete operations for students.
 * @author nikla
 * @version 1.0
 * @since 2025-12-25
 */
public class EditDeleteStudentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String studentIdStr = request.getParameter("id");

        if (studentIdStr == null || studentIdStr.isEmpty()) {
            response.sendRedirect("ListStudent?error=Invalid student ID");
            return;
        }

        try {
            int id = Integer.parseInt(studentIdStr);

            // Get student by ID from database
            Connection conn = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/studentProfiles", "app", "app");
            String sql = "SELECT * FROM STUDENT WHERE ID = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
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

                request.setAttribute("student", student);
                request.setAttribute("isEdit", true);
                RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
                rd.forward(request, response);
            } else {
                response.sendRedirect("ListStudent?error=Student not found");
            }
            conn.close();
        } catch (NumberFormatException e) {
            response.sendRedirect("ListStudent?error=Invalid student ID");
        } catch (SQLException e) {
            e.printStackTrace();
            response.sendRedirect("ListStudent?error=Database error: " + e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String studentIdStr = request.getParameter("id");

        if (studentIdStr == null || studentIdStr.isEmpty()) {
            response.sendRedirect("ListStudent");
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdStr);

            if ("delete".equalsIgnoreCase(action)) {
                // Delete student from database
                try {
                    Connection conn = DriverManager.getConnection(
                        "jdbc:derby://localhost:1527/studentProfiles", "app", "app");
                    String sql = "DELETE FROM STUDENT WHERE ID = ?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);
                    pstmt.setInt(1, studentId);
                    int result = pstmt.executeUpdate();
                    conn.close();

                    if (result > 0) {
                        response.sendRedirect("ListStudent?success=Student profile deleted successfully!");
                    } else {
                        response.sendRedirect("ListStudent?error=Failed to delete student");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.sendRedirect("ListStudent?error=Database error: " + e.getMessage());
                }
            } else if ("update".equalsIgnoreCase(action)) {
                // Personal Information
                String firstName = safe(request.getParameter("firstName"));
                String lastName = safe(request.getParameter("lastName"));
                String phone = safe(request.getParameter("phone"));
                String dobStr = request.getParameter("dateOfBirth");
                String address = safe(request.getParameter("address"));

                // Academic Information
                String studentIdValue = safe(request.getParameter("studentId"));
                String program = safe(request.getParameter("program"));
                String email = safe(request.getParameter("email"));
                String gpaStr = request.getParameter("gpa");

                // Interests
                String[] hobbies = request.getParameterValues("hobbies");
                String selfIntro = safe(request.getParameter("selfIntro"));

                // Validation
                if (isBlank(firstName) || isBlank(lastName) || isBlank(studentIdValue) ||
                    isBlank(program) || isBlank(email)) {
                    request.setAttribute("error", "Please fill in all required fields (marked with *)");

                    // Get student by ID from database
                    try {
                        Connection conn = DriverManager.getConnection(
                            "jdbc:derby://localhost:1527/studentProfiles", "app", "app");
                        String sql = "SELECT * FROM STUDENT WHERE ID = ?";
                        PreparedStatement pstmt = conn.prepareStatement(sql);
                        pstmt.setInt(1, studentId);
                        ResultSet rs = pstmt.executeQuery();

                        if (rs.next()) {
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
                            request.setAttribute("student", student);
                        }
                        conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }

                    request.setAttribute("isEdit", true);
                    RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
                    rd.forward(request, response);
                    return;
                }

                // Data normalization
                if (hobbies == null) {
                    hobbies = new String[0];
                }
                for (int i = 0; i < hobbies.length; i++) {
                    hobbies[i] = hobbies[i] == null ? null : hobbies[i].trim();
                }

                // Parse optional fields
                java.sql.Date dateOfBirth = null;
                if (dobStr != null && !dobStr.isEmpty()) {
                    try {
                        dateOfBirth = java.sql.Date.valueOf(dobStr);
                    } catch (IllegalArgumentException e) {
                        request.setAttribute("error", "Invalid date format for Date of Birth.");

                        // Get student by ID from database
                        try {
                            Connection conn = DriverManager.getConnection(
                                "jdbc:derby://localhost:1527/studentProfiles", "app", "app");
                            String sql = "SELECT * FROM STUDENT WHERE ID = ?";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            pstmt.setInt(1, studentId);
                            ResultSet rs = pstmt.executeQuery();

                            if (rs.next()) {
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
                                request.setAttribute("student", student);
                            }
                            conn.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                        request.setAttribute("isEdit", true);
                        RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
                        rd.forward(request, response);
                        return;
                    }
                }

                double gpa = 0.0;
                if (gpaStr != null && !gpaStr.isEmpty()) {
                    try {
                        gpa = Double.parseDouble(gpaStr);
                        if (gpa < 0 || gpa > 4.0) {
                            request.setAttribute("error", "GPA must be between 0.0 and 4.0.");

                            // Get student by ID from database
                            try {
                                Connection conn = DriverManager.getConnection(
                                    "jdbc:derby://localhost:1527/studentProfiles", "app", "app");
                                String sql = "SELECT * FROM STUDENT WHERE ID = ?";
                                PreparedStatement pstmt = conn.prepareStatement(sql);
                                pstmt.setInt(1, studentId);
                                ResultSet rs = pstmt.executeQuery();

                                if (rs.next()) {
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
                                    request.setAttribute("student", student);
                                }
                                conn.close();
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }

                            request.setAttribute("isEdit", true);
                            RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
                            rd.forward(request, response);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("error", "Invalid GPA format.");

                        // Get student by ID from database
                        try {
                            Connection conn = DriverManager.getConnection(
                                "jdbc:derby://localhost:1527/studentProfiles", "app", "app");
                            String sql = "SELECT * FROM STUDENT WHERE ID = ?";
                            PreparedStatement pstmt = conn.prepareStatement(sql);
                            pstmt.setInt(1, studentId);
                            ResultSet rs = pstmt.executeQuery();

                            if (rs.next()) {
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
                                request.setAttribute("student", student);
                            }
                            conn.close();
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        }

                        request.setAttribute("isEdit", true);
                        RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
                        rd.forward(request, response);
                        return;
                    }
                }

                Student student = new Student(studentId, firstName, lastName, studentIdValue, program,
                                            email, phone, dateOfBirth, address, gpa, hobbies, selfIntro);

                // Update student in database
                try {
                    Connection conn = DriverManager.getConnection(
                        "jdbc:derby://localhost:1527/studentProfiles", "app", "app");
                    String sql = "UPDATE STUDENT SET FIRST_NAME=?, LAST_NAME=?, STUDENT_ID=?, PROGRAM=?, EMAIL=?, " +
                                 "PHONE=?, DATE_OF_BIRTH=?, ADDRESS=?, GPA=?, HOBBIES=?, SELF_INTRO=? WHERE ID=?";
                    PreparedStatement pstmt = conn.prepareStatement(sql);

                    pstmt.setString(1, student.getFirstName());
                    pstmt.setString(2, student.getLastName());
                    pstmt.setString(3, student.getStudentId());
                    pstmt.setString(4, student.getProgram());
                    pstmt.setString(5, student.getEmail());
                    pstmt.setString(6, student.getPhone());
                    pstmt.setDate(7, student.getDateOfBirth());
                    pstmt.setString(8, student.getAddress());
                    pstmt.setDouble(9, student.getGpa());
                    pstmt.setString(10, arrayToString(student.getHobbies()));
                    pstmt.setString(11, student.getSelfIntro());
                    pstmt.setInt(12, studentId);

                    int result = pstmt.executeUpdate();
                    conn.close();

                    if (result > 0) {
                        response.sendRedirect("ListStudent?success=Student profile updated successfully!");
                    } else {
                        response.sendRedirect("ListStudent?error=Failed to update student");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    response.sendRedirect("ListStudent?error=Database error: " + e.getMessage());
                }
            } else {
                response.sendRedirect("ListStudent");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("ListStudent?error=Invalid student ID");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String safe(String s) {
        return s == null ? null : s.trim();
    }

    /** Helper method to convert array to comma-separated string */
    private static String arrayToString(String[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            if (i > 0) sb.append(',');
            sb.append(array[i]);
        }
        return sb.toString();
    }

    /** Helper method to convert comma-separated string to array */
    private static String[] stringToArray(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new String[0];
        }
        return str.split(",");
    }
}

