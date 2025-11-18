package com.IA.model;

import java.io.Serializable;
import java.util.Arrays;


public class Student implements Serializable {
    private static final long serialVersionUID = 1L;


    private String name;
    private String studentId;
    private String program;
    private String email;
    private String[] hobbies; // multiple hobbies
    private String selfIntro;

    // --- Constructors ---
    // default constructor
    public Student() {
    }

    // Normal constructor with parameters
    public Student(String name, String studentId, String program, String email, String[] hobbies, String selfIntro) {
        this.name = name;
        this.studentId = studentId;
        this.program = program;
        this.email = email;
        this.hobbies = hobbies;
        this.selfIntro = selfIntro;
    }

    // --- Getter methods and Setter methods ---

        public String getName(){return name;}
    public void setName(String name){this.name = name;}
    
    public String getStudentId() {return studentId;}
    public void setStudentId(String studentId) {this.studentId = studentId;}

    public String getProgram() {return program;}
    public void setProgram(String program) {this.program = program;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String[] getHobbies() {return hobbies;}
    public void setHobbies(String[] hobbies) {this.hobbies = hobbies;}

    public String getSelfIntro() {return selfIntro;}
    public void setSelfIntro(String selfIntro) {this.selfIntro = selfIntro;}

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", studentId='" + studentId + '\'' +
                ", program='" + program + '\'' +
                ", email='" + email + '\'' +
                ", hobbies=" + Arrays.toString(hobbies) +
                ", selfIntroduction='" + selfIntro + '\'' +
                '}';
    }
}