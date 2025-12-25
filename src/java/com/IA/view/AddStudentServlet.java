/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.IA.view;

import com.IA.model.Student;
import com.IA.model.StudentDAO;
import java.io.IOException;
import java.sql.Date;
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

        // Personal Information
        String firstName = safe(request.getParameter("firstName"));
        String lastName = safe(request.getParameter("lastName"));
        String phone = safe(request.getParameter("phone"));
        String dobStr = request.getParameter("dateOfBirth");
        String address = safe(request.getParameter("address"));

        // Academic Information
        String studentId = safe(request.getParameter("studentId"));
        String program = safe(request.getParameter("program"));
        String email = safe(request.getParameter("email"));
        String gpaStr = request.getParameter("gpa");

        // Interests
        String[] hobbies = request.getParameterValues("hobbies");
        String selfIntro = safe(request.getParameter("selfIntro"));

        // Validation - Check required fields
        if (isBlank(firstName) || isBlank(lastName) || isBlank(studentId) ||
            isBlank(program) || isBlank(email)) {

            request.setAttribute("error", "Please fill in all required fields (marked with *).");
            preserveFormData(request, firstName, lastName, studentId, program, email,
                           phone, dobStr, address, gpaStr, hobbies, selfIntro);

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

        // Create Student object with unified model
        Student student = new Student(firstName, lastName, studentId, program, email,
                                    phone, dateOfBirth, address, gpa, hobbies, selfIntro);

        // Save to database
        try {
            if (StudentDAO.insertStudent(student)) {
                // Task 5: Redirect to profile.jsp to display submitted profile details after saving
                // Retrieve the saved student (to get the generated ID)
                Student savedStudent = StudentDAO.getStudentByStudentId(studentId);
                if (savedStudent != null) {
                    request.setAttribute("profile", savedStudent);
                    request.setAttribute("message", "Student profile added successfully!");
                    RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
                    rd.forward(request, response);
                } else {
                    // Fallback if we can't retrieve the saved student
                    response.sendRedirect("viewProfiles.jsp?success=Student profile added successfully!");
                }
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
}
