package com.IA.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * StudentDAO - Data Access Object for managing Student database operations.
 * @author nikla
 * @version 1.0
 * @since 2025-12-25
 */
public class StudentDAO {

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
     * Insert a new student into the database
     */
    public static boolean insertStudent(Student student) {
        String sql = "INSERT INTO STUDENT (FIRST_NAME, LAST_NAME, STUDENT_ID, PROGRAM, EMAIL, " +
                     "PHONE, DATE_OF_BIRTH, ADDRESS, GPA, HOBBIES, SELF_INTRO) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

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

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Get student by ID (primary key)
     */
    public static Student getStudentById(int id) {
        String sql = "SELECT * FROM STUDENT WHERE ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get student by Student ID (unique identifier like S12345)
     */
    public static Student getStudentByStudentId(String studentId) {
        String sql = "SELECT * FROM STUDENT WHERE STUDENT_ID = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return mapResultSetToStudent(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Get all students from the database
     */
    public static List<Student> getAllStudents() {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM STUDENT ORDER BY ID";

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Search students by name (searches both first and last name)
     */
    public static List<Student> searchByName(String name) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM STUDENT WHERE UPPER(FIRST_NAME) LIKE ? OR UPPER(LAST_NAME) LIKE ? ORDER BY LAST_NAME, FIRST_NAME";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + name.toUpperCase() + "%";
            pstmt.setString(1, searchTerm);
            pstmt.setString(2, searchTerm);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Search students by student ID
     */
    public static List<Student> searchByStudentId(String studentId) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM STUDENT WHERE UPPER(STUDENT_ID) LIKE ? ORDER BY STUDENT_ID";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String searchTerm = "%" + studentId.toUpperCase() + "%";
            pstmt.setString(1, searchTerm);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Filter students by program
     */
    public static List<Student> filterByProgram(String program) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM STUDENT WHERE UPPER(PROGRAM) LIKE ? ORDER BY PROGRAM, LAST_NAME, FIRST_NAME";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + program.toUpperCase() + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Filter students by hobby
     */
    public static List<Student> filterByHobby(String hobby) {
        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM STUDENT WHERE UPPER(HOBBIES) LIKE ? ORDER BY LAST_NAME, FIRST_NAME";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + hobby.toUpperCase() + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                students.add(mapResultSetToStudent(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

    /**
     * Update a student
     */
    public static boolean updateStudent(int id, Student student) {
        String sql = "UPDATE STUDENT SET FIRST_NAME=?, LAST_NAME=?, STUDENT_ID=?, PROGRAM=?, EMAIL=?, " +
                     "PHONE=?, DATE_OF_BIRTH=?, ADDRESS=?, GPA=?, HOBBIES=?, SELF_INTRO=? WHERE ID=?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

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
            pstmt.setInt(12, id);

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Delete a student
     */
    public static boolean deleteStudent(int id) {
        String sql = "DELETE FROM STUDENT WHERE ID = ?";

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
     * Helper method to map ResultSet to Student object
     */
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

    /**
     * Helper method to convert array to comma-separated string
     */
    private static String arrayToString(String[] array) {
        if (array == null || array.length == 0) {
            return "";
        }
        return String.join(",", array);
    }

    /**
     * Helper method to convert comma-separated string to array
     */
    private static String[] stringToArray(String str) {
        if (str == null || str.trim().isEmpty()) {
            return new String[0];
        }
        return str.split(",");
    }
}

