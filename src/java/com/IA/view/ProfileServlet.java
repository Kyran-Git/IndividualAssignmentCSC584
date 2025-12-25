package com.IA.view;

import com.IA.model.ProfileBean;
import com.IA.model.ProfileDAO;
import java.io.IOException;
import java.sql.Date;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author nikla
 * @version 1.0
 * @since 2025-12-25
 */
public class ProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Get profile ID from request if editing
        String profileId = request.getParameter("id");

        if (profileId != null && !profileId.isEmpty()) {
            try {
                int id = Integer.parseInt(profileId);
                ProfileBean profile = ProfileDAO.getProfileById(id);
                request.setAttribute("profile", profile);
            } catch (NumberFormatException e) {
                request.setAttribute("error", "Invalid profile ID");
            }
        }

        RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        // Extract form parameters
        String studentIdStr = safe(request.getParameter("studentId"));
        String firstName = safe(request.getParameter("firstName"));
        String lastName = safe(request.getParameter("lastName"));
        String email = safe(request.getParameter("email"));
        String phone = safe(request.getParameter("phone"));
        String dob = request.getParameter("dateOfBirth");
        String address = safe(request.getParameter("address"));
        String major = safe(request.getParameter("major"));
        String gpaStr = request.getParameter("gpa");

        // Validate required fields
        if (isBlank(studentIdStr) || isBlank(firstName) || isBlank(lastName) ||
            isBlank(email) || isBlank(major)) {
            request.setAttribute("error", "Please fill in all required fields");
            request.setAttribute("studentId", studentIdStr);
            request.setAttribute("firstName", firstName);
            request.setAttribute("lastName", lastName);
            request.setAttribute("email", email);
            request.setAttribute("phone", phone);
            request.setAttribute("dateOfBirth", dob);
            request.setAttribute("address", address);
            request.setAttribute("major", major);
            request.setAttribute("gpa", gpaStr);

            RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
            rd.forward(request, response);
            return;
        }

        try {
            int studentId = Integer.parseInt(studentIdStr);
            Date dateOfBirth = dob != null && !dob.isEmpty() ? Date.valueOf(dob) : null;
            double gpa = gpaStr != null && !gpaStr.isEmpty() ? Double.parseDouble(gpaStr) : 0.0;

            ProfileBean profile = new ProfileBean(studentId, firstName, lastName, email,
                                                  phone, dateOfBirth, address, major, gpa);

            if (ProfileDAO.insertProfile(profile)) {
                request.setAttribute("success", "Profile saved successfully!");

                // Retrieve the newly created profile to display
                ProfileBean savedProfile = ProfileDAO.searchByStudentId(studentId);
                request.setAttribute("profile", savedProfile);

                RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
                rd.forward(request, response);
            } else {
                request.setAttribute("error", "Failed to save profile. Please try again.");
                RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
                rd.forward(request, response);
            }
        } catch (NumberFormatException e) {
            request.setAttribute("error", "Invalid input. Please check your data.");
            RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
            rd.forward(request, response);
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String safe(String s) {
        return s == null ? null : s.trim();
    }
}

