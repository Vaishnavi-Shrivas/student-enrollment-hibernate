package com.enrollment.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private int courseId;
    
    @Column(name = "course_code", unique = true, nullable = false, length = 20)
    private String courseCode;
    
    @Column(name = "course_name", nullable = false, length = 100)
    private String courseName;
    
    @Column(name = "credits")
    private int credits;
    
    @Column(name = "instructor", length = 100)
    private String instructor;
    
    @Column(name = "max_capacity")
    private int maxCapacity;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();
    
    public Course() {}
    
    public Course(String courseCode, String courseName, int credits, 
                  String instructor, int maxCapacity) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.instructor = instructor;
        this.maxCapacity = maxCapacity;
    }
    
    public int getCourseId() {
        return courseId;
    }
    
    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }
    
    public String getCourseCode() {
        return courseCode;
    }
    
    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }
    
    public String getCourseName() {
        return courseName;
    }
    
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }
    
    public int getCredits() {
        return credits;
    }
    
    public void setCredits(int credits) {
        this.credits = credits;
    }
    
    public String getInstructor() {
        return instructor;
    }
    
    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
    
    public int getMaxCapacity() {
        return maxCapacity;
    }
    
    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }
    
    public List<Enrollment> getEnrollments() {
        return enrollments;
    }
    
    public void setEnrollments(List<Enrollment> enrollments) {
        this.enrollments = enrollments;
    }
    
    @Override
    public String toString() {
        return "Course{" +
                "courseId=" + courseId +
                ", courseCode='" + courseCode + '\'' +
                ", courseName='" + courseName + '\'' +
                ", credits=" + credits +
                ", instructor='" + instructor + '\'' +
                ", maxCapacity=" + maxCapacity +
                '}';
    }
}