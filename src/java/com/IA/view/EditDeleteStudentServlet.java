package com.IA.view;

import com.IA.model.Student;
import com.IA.model.StudentDAO;
import java.io.IOException;
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
            Student student = StudentDAO.getStudentById(id);

            if (student != null) {
                request.setAttribute("student", student);
                request.setAttribute("isEdit", true);
                RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
                rd.forward(request, response);
            } else {
                response.sendRedirect("ListStudent?error=Student not found");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("ListStudent?error=Invalid student ID");
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
                if (StudentDAO.deleteStudent(studentId)) {
                    response.sendRedirect("ListStudent?success=Student profile deleted successfully!");
                } else {
                    response.sendRedirect("ListStudent?error=Failed to delete student");
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
                    Student student = StudentDAO.getStudentById(studentId);
                    request.setAttribute("student", student);
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
                        Student student = StudentDAO.getStudentById(studentId);
                        request.setAttribute("student", student);
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
                            Student student = StudentDAO.getStudentById(studentId);
                            request.setAttribute("student", student);
                            request.setAttribute("isEdit", true);
                            RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
                            rd.forward(request, response);
                            return;
                        }
                    } catch (NumberFormatException e) {
                        request.setAttribute("error", "Invalid GPA format.");
                        Student student = StudentDAO.getStudentById(studentId);
                        request.setAttribute("student", student);
                        request.setAttribute("isEdit", true);
                        RequestDispatcher rd = request.getRequestDispatcher("/form.jsp");
                        rd.forward(request, response);
                        return;
                    }
                }

                Student student = new Student(studentId, firstName, lastName, studentIdValue, program,
                                            email, phone, dateOfBirth, address, gpa, hobbies, selfIntro);

                if (StudentDAO.updateStudent(studentId, student)) {
                    response.sendRedirect("ListStudent?success=Student profile updated successfully!");
                } else {
                    response.sendRedirect("ListStudent?error=Failed to update student");
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
}

