package com.IA.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * ProfileDAO - Data Access Object for managing profile database operations.
 * @author nikla
 * @version 1.0
 * @since 2025-12-25
 */
public class ProfileDAO {

    private static final String DB_URL = "jdbc:derby://localhost:1527/studentProfiles";
    private static final String DB_USER = "APP";
    private static final String DB_PASSWORD = "APP";
    private static final String DRIVER = "org.apache.derby.jdbc.ClientDriver";

    static {
        try {
            Class.forName(DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get a database connection
     */
    private static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }

    /**
     * Insert a new profile into the database
     */
    public static boolean insertProfile(ProfileBean profile) {
        String sql = "INSERT INTO profile (STUDENT_ID, FIRST_NAME, LAST_NAME, EMAIL, PHONE, " +
                     "DATE_OF_BIRTH, ADDRESS, MAJOR, GPA) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, profile.getStudentId());
            pstmt.setString(2, profile.getFirstName());
            pstmt.setString(3, profile.getLastName());
            pstmt.setString(4, profile.getEmail());
            pstmt.setString(5, profile.getPhone());
            pstmt.setDate(6, profile.getDateOfBirth());
            pstmt.setString(7, profile.getAddress());
            pstmt.setString(8, profile.getMajor());
            pstmt.setDouble(9, profile.getGpa());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get profile by ID
     */
    public static ProfileBean getProfileById(int id) {
        String sql = "SELECT * FROM profile WHERE ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProfile(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all profiles from the database
     */
    public static List<ProfileBean> getAllProfiles() {
        List<ProfileBean> profiles = new ArrayList<>();
        String sql = "SELECT * FROM profile";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                profiles.add(mapResultSetToProfile(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profiles;
    }

    /**
     * Search profiles by name (first or last name)
     */
    public static List<ProfileBean> searchByName(String name) {
        List<ProfileBean> profiles = new ArrayList<>();
        String sql = "SELECT * FROM profile WHERE UPPER(FIRST_NAME) LIKE ? OR UPPER(LAST_NAME) LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + name.toUpperCase() + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                profiles.add(mapResultSetToProfile(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profiles;
    }

    /**
     * Search profiles by student ID
     */
    public static ProfileBean searchByStudentId(int studentId) {
        String sql = "SELECT * FROM profile WHERE STUDENT_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToProfile(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Filter profiles by major
     */
    public static List<ProfileBean> filterByMajor(String major) {
        List<ProfileBean> profiles = new ArrayList<>();
        String sql = "SELECT * FROM profile WHERE UPPER(MAJOR) LIKE ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + major.toUpperCase() + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                profiles.add(mapResultSetToProfile(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profiles;
    }

    /**
     * Update a profile
     */
    public static boolean updateProfile(ProfileBean profile) {
        String sql = "UPDATE profile SET FIRST_NAME=?, LAST_NAME=?, EMAIL=?, PHONE=?, " +
                     "DATE_OF_BIRTH=?, ADDRESS=?, MAJOR=?, GPA=? WHERE ID=?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, profile.getFirstName());
            pstmt.setString(2, profile.getLastName());
            pstmt.setString(3, profile.getEmail());
            pstmt.setString(4, profile.getPhone());
            pstmt.setDate(5, profile.getDateOfBirth());
            pstmt.setString(6, profile.getAddress());
            pstmt.setString(7, profile.getMajor());
            pstmt.setDouble(8, profile.getGpa());
            pstmt.setInt(9, profile.getId());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a profile
     */
    public static boolean deleteProfile(int id) {
        String sql = "DELETE FROM profile WHERE ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Helper method to map ResultSet to ProfileBean object
     */
    private static ProfileBean mapResultSetToProfile(ResultSet rs) throws SQLException {
        return new ProfileBean(
            rs.getInt("ID"),
            rs.getInt("STUDENT_ID"),
            rs.getString("FIRST_NAME"),
            rs.getString("LAST_NAME"),
            rs.getString("EMAIL"),
            rs.getString("PHONE"),
            rs.getDate("DATE_OF_BIRTH"),
            rs.getString("ADDRESS"),
            rs.getString("MAJOR"),
            rs.getDouble("GPA")
        );
    }
}

