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
public class EditDeleteProfileServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String profileId = request.getParameter("id");

        if (profileId == null || profileId.isEmpty()) {
            response.sendRedirect("ViewProfiles");
            return;
        }

        try {
            int id = Integer.parseInt(profileId);
            ProfileBean profile = ProfileDAO.getProfileById(id);

            if (profile != null) {
                request.setAttribute("profile", profile);
                RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
                rd.forward(request, response);
            } else {
                response.sendRedirect("ViewProfiles?error=Profile not found");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("ViewProfiles?error=Invalid profile ID");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        String profileIdStr = request.getParameter("id");

        if (profileIdStr == null || profileIdStr.isEmpty()) {
            response.sendRedirect("ViewProfiles");
            return;
        }

        try {
            int profileId = Integer.parseInt(profileIdStr);

            if ("delete".equalsIgnoreCase(action)) {
                // Delete the profile
                if (ProfileDAO.deleteProfile(profileId)) {
                    response.sendRedirect("ViewProfiles?success=Profile deleted successfully!");
                } else {
                    response.sendRedirect("ViewProfiles?error=Failed to delete profile");
                }
            } else if ("update".equalsIgnoreCase(action)) {
                // Update the profile
                String firstName = safe(request.getParameter("firstName"));
                String lastName = safe(request.getParameter("lastName"));
                String email = safe(request.getParameter("email"));
                String phone = safe(request.getParameter("phone"));
                String dob = request.getParameter("dateOfBirth");
                String address = safe(request.getParameter("address"));
                String major = safe(request.getParameter("major"));
                String gpaStr = request.getParameter("gpa");

                if (isBlank(firstName) || isBlank(lastName) || isBlank(email) || isBlank(major)) {
                    request.setAttribute("error", "Please fill in all required fields");
                    ProfileBean profile = ProfileDAO.getProfileById(profileId);
                    request.setAttribute("profile", profile);
                    RequestDispatcher rd = request.getRequestDispatcher("/profile.jsp");
                    rd.forward(request, response);
                    return;
                }

                Date dateOfBirth = dob != null && !dob.isEmpty() ? Date.valueOf(dob) : null;
                double gpa = gpaStr != null && !gpaStr.isEmpty() ? Double.parseDouble(gpaStr) : 0.0;

                ProfileBean profile = new ProfileBean(profileId, 0, firstName, lastName, email,
                                                      phone, dateOfBirth, address, major, gpa);

                if (ProfileDAO.updateProfile(profile)) {
                    response.sendRedirect("ViewProfiles?success=Profile updated successfully!");
                } else {
                    response.sendRedirect("ViewProfiles?error=Failed to update profile");
                }
            } else {
                response.sendRedirect("ViewProfiles");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("ViewProfiles?error=Invalid profile ID");
        }
    }

    private static boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static String safe(String s) {
        return s == null ? null : s.trim();
    }
}

