package com.IA.view;

import com.IA.model.ProfileBean;
import com.IA.model.ProfileDAO;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
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
public class ViewProfilesServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        List<ProfileBean> profiles;
        String searchType = request.getParameter("searchType");
        String searchQuery = request.getParameter("searchQuery");

        if (searchType != null && searchQuery != null && !searchQuery.trim().isEmpty()) {
            searchQuery = searchQuery.trim();

            if ("name".equalsIgnoreCase(searchType)) {
                profiles = ProfileDAO.searchByName(searchQuery);
                request.setAttribute("searchType", "name");
                request.setAttribute("searchQuery", searchQuery);
            } else if ("studentId".equalsIgnoreCase(searchType)) {
                try {
                    int studentId = Integer.parseInt(searchQuery);
                    ProfileBean profile = ProfileDAO.searchByStudentId(studentId);
                    profiles = new ArrayList<>();
                    if (profile != null) {
                        profiles.add(profile);
                    }
                    request.setAttribute("searchType", "studentId");
                    request.setAttribute("searchQuery", searchQuery);
                } catch (NumberFormatException e) {
                    profiles = ProfileDAO.getAllProfiles();
                    request.setAttribute("error", "Invalid Student ID format");
                }
            } else if ("major".equalsIgnoreCase(searchType)) {
                profiles = ProfileDAO.filterByMajor(searchQuery);
                request.setAttribute("searchType", "major");
                request.setAttribute("searchQuery", searchQuery);
            } else {
                profiles = ProfileDAO.getAllProfiles();
            }
        } else {
            profiles = ProfileDAO.getAllProfiles();
        }

        request.setAttribute("profiles", profiles);
        RequestDispatcher rd = request.getRequestDispatcher("/viewProfiles.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}

