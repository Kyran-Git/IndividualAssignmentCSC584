/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * AddStudentServlet - Handles comprehensive student profile form submission.
 * Manages personal, academic, and contact information for the unified Student model.
 *
 * @author nikla
 * @version 3.0
 * @since 2025-12-25
 */
public class AddStudentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String firstName = safe(request.getParameter("firstName"));
        String lastName = safe(request.getParameter("lastName"));
        String phone = safe(request.getParameter("phone"));
        String dobStr = request.getParameter("dateOfBirth");
        String address = safe(request.getParameter("address"));

        String studentId = safe(request.getParameter("studentId"));
        String program = safe(request.getParameter("program"));
        String email = safe(request.getParameter("email"));
        String gpaStr = request.getParameter("gpa");

        String[] hobbies = request.getParameterValues("hobbies");
        String selfIntro = safe(request.getParameter("selfIntro"));

        if (isBlank(firstName) || isBlank(lastName) || isBlank(studentId) ||
            isBlank(program) || isBlank(email)) {

            request.setAttribute("error", "Please fill in all required fields (marked with *).");
            preserveFormData(request, firstName, lastName, studentId, program, email,
                           phone, dobStr, address, gpaStr, hobbies, selfIntro);

            RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
            rd.forward(request, response);
            return;
        }

        if (hobbies == null) {
            hobbies = new String[0];
        }
        for (int i = 0; i < hobbies.length; i++) {
            hobbies[i] = hobbies[i] == null ? null : hobbies[i].trim();
        }

        Date dateOfBirth = null;
        if (dobStr != null && !dobStr.isEmpty()) {
            try {
                dateOfBirth = Date.valueOf(dobStr);
            } catch (IllegalArgumentException e) {
                request.setAttribute("error", "Invalid date format for Date of Birth.");
                preserveFormData(request, firstName, lastName, studentId, program, email,
                               phone, dobStr, address, gpaStr, hobbies, selfIntro);
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
                    preserveFormData(request, firstName, lastName, studentId, program, email,
                                   phone, dobStr, address, gpaStr, hobbies, selfIntro);
                    RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
                    rd.forward(request, response);
                    return;
                }
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid GPA format.");
                preserveFormData(request, firstName, lastName, studentId, program, email,
                               phone, dobStr, address, gpaStr, hobbies, selfIntro);
                RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
                rd.forward(request, response);
                return;
            }
        }

        Student student = new Student(firstName, lastName, studentId, program, email,
                                    phone, dateOfBirth, address, gpa, hobbies, selfIntro);

 
        try {
            Connection conn = DriverManager.getConnection(
                "jdbc:derby://localhost:1527/studentProfiles", "app", "app");

            String sql = "INSERT INTO STUDENT (FIRST_NAME, LAST_NAME, STUDENT_ID, PROGRAM, EMAIL, " +
                         "PHONE, DATE_OF_BIRTH, ADDRESS, GPA, HOBBIES, SELF_INTRO) " +
                         "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, student.getFirstName());
            stmt.setString(2, student.getLastName());
            stmt.setString(3, student.getStudentId());
            stmt.setString(4, student.getProgram());
            stmt.setString(5, student.getEmail());
            stmt.setString(6, student.getPhone());
            stmt.setDate(7, student.getDateOfBirth());
            stmt.setString(8, student.getAddress());
            stmt.setDouble(9, student.getGpa());
            stmt.setString(10, arrayToString(student.getHobbies()));
            stmt.setString(11, student.getSelfIntro());

            int result = stmt.executeUpdate();
            conn.close();

            if (result > 0) {
                // Retrieve the saved student to display
                Connection conn2 = DriverManager.getConnection(
                    "jdbc:derby://localhost:1527/studentProfiles", "app", "app");
                String selectSql = "SELECT * FROM STUDENT WHERE STUDENT_ID = ?";
                PreparedStatement selectStmt = conn2.prepareStatement(selectSql);
                selectStmt.setString(1, studentId);
                ResultSet rs = selectStmt.executeQuery();

                if (rs.next()) {
                    Student savedStudent = new Student();
                    savedStudent.setId(rs.getInt("ID"));
                    savedStudent.setFirstName(rs.getString("FIRST_NAME"));
                    savedStudent.setLastName(rs.getString("LAST_NAME"));
                    savedStudent.setStudentId(rs.getString("STUDENT_ID"));
                    savedStudent.setProgram(rs.getString("PROGRAM"));
                    savedStudent.setEmail(rs.getString("EMAIL"));
                    savedStudent.setPhone(rs.getString("PHONE"));
                    savedStudent.setDateOfBirth(rs.getDate("DATE_OF_BIRTH"));
                    savedStudent.setAddress(rs.getString("ADDRESS"));
                    savedStudent.setGpa(rs.getDouble("GPA"));
                    savedStudent.setHobbies(stringToArray(rs.getString("HOBBIES")));
                    savedStudent.setSelfIntro(rs.getString("SELF_INTRO"));

                    request.setAttribute("profile", savedStudent);
                    request.setAttribute("message", "Student profile added successfully!");
                    RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
                    rd.forward(request, response);
                } else {
                    response.sendRedirect("viewProfiles.jsp?success=Student profile added successfully!");
                }
                conn2.close();
            } else {
                request.setAttribute("error", "Failed to save student to database. Please try again.");
                preserveFormData(request, firstName, lastName, studentId, program, email,
                               phone, dobStr, address, gpaStr, hobbies, selfIntro);
                RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
                rd.forward(request, response);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error: " + e.getMessage());
            preserveFormData(request, firstName, lastName, studentId, program, email,
                           phone, dobStr, address, gpaStr, hobbies, selfIntro);
            RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
            rd.forward(request, response);
        }
    }

    private void preserveFormData(HttpServletRequest request, String firstName, String lastName,
                                 String studentId, String program, String email, String phone,
                                 String dob, String address, String gpa, String[] hobbies, String intro) {
        request.setAttribute("firstName", firstName);
        request.setAttribute("lastName", lastName);
        request.setAttribute("studentId", studentId);
        request.setAttribute("program", program);
        request.setAttribute("email", email);
        request.setAttribute("phone", phone);
        request.setAttribute("dateOfBirth", dob);
        request.setAttribute("address", address);
        request.setAttribute("gpa", gpa);
        request.setAttribute("hobbies", hobbies);
        request.setAttribute("selfIntro", intro);
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
