/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * AddStudentServlet - Handles student registration form submission and display.
 *
 * This servlet provides two main functions:
 * 1. GET requests - Forwards users to the student registration form page
 * 2. POST requests - Processes submitted student data, validates it, stores it in
 *    application scope, and redirects to the student list view
 *
 * Storage: Student objects are stored in a thread-safe List maintained in
 * ServletContext (application scope) under the key "students"
 *
 * @author nikla
 * @version 1.0
 * @since 2025-11-18
 */
public class AddStudentServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Forward to the form page so users can add a new student
        RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
        rd.forward(request, response);
    }

    /**
     * Handles HTTP POST requests by processing submitted student form data.
     *
     * This method performs the following steps:
     * 1. Extracts and sanitizes all form parameters (name, ID, program, email, hobbies, intro)
     * 2. Validates required fields (name, studentId, program, email)
     * 3. If validation fails, forwards back to form with error message and preserved input
     * 4. If validation passes, creates a new Student object
     * 5. Stores the Student in a thread-safe application-scoped list
     * 6. Redirects to the ListStudent servlet to display all students
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // retrive and trim html form parameters using helper methods to remove whitespace
        String name = safe(request.getParameter("name"));
        String studentId = safe(request.getParameter("studentId"));
        String program = safe(request.getParameter("program"));
        String email = safe(request.getParameter("email"));

        // Hobbies can be multiple values from a multi-select
        String[] hobbies = request.getParameterValues("hobbies");

        // Self-introduction is optional but still trim whitespace if user decides to make one
        String selfIntro = safe(request.getParameter("selfIntro"));

        // ===== VALIDATION PHASE =====
        // Check if any required fields are blank (null or empty after trimming)
        if (isBlank(name) || isBlank(studentId) || isBlank(program) || isBlank(email)) {
            // Validation failed - set error message
            request.setAttribute("error", "Please fill in all required fields.");

            // Preserve user input so they dont have to retype everything again
            request.setAttribute("name", name);
            request.setAttribute("studentId", studentId);
            request.setAttribute("program", program);
            request.setAttribute("email", email);
            request.setAttribute("hobbies", hobbies);
            request.setAttribute("selfIntro", selfIntro);

            // Forward back to form page to display error and preserved values
            RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
            rd.forward(request, response);
            return; // Stop processing here
        }

        // ===== DATA NORMALIZATION =====
        // If no hobbies were selected, init as empty array instead of null
        if (hobbies == null) {
            hobbies = new String[0];
        }

        // Trim whitespace from each hobby value to ensure clean data
        for (int i = 0; i < hobbies.length; i++) {
            hobbies[i] = hobbies[i] == null ? null : hobbies[i].trim();
        }

        // ===== create student object =====
        Student student = new Student(name, studentId, program, email, hobbies, selfIntro);

        // ===== store student objects =====
        ServletContext app = getServletContext();


        // Prevents multiple forms being submitted at the same time by the user
        synchronized (app) {
            // retrive existing / added students
            @SuppressWarnings("unchecked")
            List<Student> list = (List<Student>) app.getAttribute("students");

            // initialize a synced list if this is the first student added
            if (list == null) {
                list = Collections.synchronizedList(new ArrayList<Student>());
                app.setAttribute("students", list);
            }

            // Add the new student to the shared list
            list.add(student);
        }

        // ===== REDIRECT TO LIST VIEW =====
        
        response.sendRedirect(request.getContextPath() + "/ListStudent");
    }

    /**
     * Checks if a string is blank, null, empty, or only whitespace.
 
     */
    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    /**
     * Trims whitespace from string input
     */
    private static String safe(String s) {
        return s == null ? null : s.trim();
    }
}
