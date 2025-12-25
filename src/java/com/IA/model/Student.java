package com.IA.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Arrays;
import java.util.Objects;

/**
 * Student - Unified JavaBean class representing a comprehensive student profile
 * Combines personal info, academic details, contact info, and interests
 *
 * @author nikla
 * @version 2.0
 * @since 2025-12-25
 */
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    // Database primary key
    private int id;

    // Personal Information
    private String firstName;
    private String lastName;
    private Date dateOfBirth;
    private String phone;
    private String address;

    // Academic Information
    private String studentId;  // Student ID number (e.g., S12345)
    private String program;    // Major/Programme
    private double gpa;

    // Contact & Social
    private String email;
    private String[] hobbies;
    private String selfIntro;

    // Constructors
    public Student() {
    }

    /**
     * Constructor for new student (without database ID)
     */
    public Student(String firstName, String lastName, String studentId, String program,
                   String email, String phone, Date dateOfBirth, String address,
                   double gpa, String[] hobbies, String selfIntro) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;
        this.program = program;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.gpa = gpa;
        this.hobbies = hobbies;
        this.selfIntro = selfIntro;
    }

    /**
     * Constructor with database ID (for existing records)
     */
    public Student(int id, String firstName, String lastName, String studentId, String program,
                   String email, String phone, Date dateOfBirth, String address,
                   double gpa, String[] hobbies, String selfIntro) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.studentId = studentId;
        this.program = program;
        this.email = email;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.gpa = gpa;
        this.hobbies = hobbies;
        this.selfIntro = selfIntro;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Convenience method to get full name
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String[] getHobbies() {
        return hobbies;
    }

    public void setHobbies(String[] hobbies) {
        this.hobbies = hobbies;
    }

    public String getSelfIntro() {
        return selfIntro;
    }

    public void setSelfIntro(String selfIntro) {
        this.selfIntro = selfIntro;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id && Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, studentId);
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", studentId='" + studentId + '\'' +
                ", program='" + program + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", address='" + address + '\'' +
                ", gpa=" + gpa +
                ", hobbies=" + Arrays.toString(hobbies) +
                ", selfIntro='" + selfIntro + '\'' +
                '}';
    }
}