package com.enrollment;

import com.enrollment.dao.CourseDAO;
import com.enrollment.dao.EnrollmentDAO;
import com.enrollment.dao.StudentDAO;
import com.enrollment.entity.Course;
import com.enrollment.entity.Enrollment;
import com.enrollment.entity.Student;
import com.enrollment.service.HibernateUtils;

import java.time.LocalDate;
import java.util.List;

public class Main {
    
    public static void main(String[] args) {
        try {
            System.out.println("\n===== STUDENT COURSE ENROLLMENT SYSTEM =====\n");
            
            StudentDAO studentDAO = new StudentDAO();
            CourseDAO courseDAO = new CourseDAO();
            EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
            
            System.out.println("\n--- CREATE OPERATIONS ---\n");
            
            Student student1 = new Student("Vaishnavi", "vaishnavi@example.com", "9876543210", 8.5);
            Student student2 = new Student("Arjun", "arjun@example.com", "9876543211", 7.8);
            Student student3 = new Student("Priya", "priya@example.com", "9876543212", 9.2);
            
            int s1 = studentDAO.addStudent(student1);
            int s2 = studentDAO.addStudent(student2);
            int s3 = studentDAO.addStudent(student3);
            
            Course course1 = new Course("CSC101", "Data Structures", 4, "Dr. Smith", 50);
            Course course2 = new Course("CSC201", "Database Management", 3, "Dr. Johnson", 45);
            Course course3 = new Course("CSC301", "Web Development", 3, "Dr. Smith", 40);
            
            int c1 = courseDAO.addCourse(course1);
            int c2 = courseDAO.addCourse(course2);
            int c3 = courseDAO.addCourse(course3);
            
            System.out.println("\n--- READ OPERATIONS ---\n");
            
            studentDAO.getStudentById(s1);
            courseDAO.getCourseById(c1);
            
            System.out.println("\nAll Students:");
            List<Student> allStudents = studentDAO.getAllStudents();
            allStudents.forEach(System.out::println);
            
            System.out.println("\nAll Courses:");
            List<Course> allCourses = courseDAO.getAllCourses();
            allCourses.forEach(System.out::println);
            
            System.out.println("\nStudents with CGPA >= 8.0:");
            studentDAO.getStudentsByMinCGPA(8.0).forEach(System.out::println);
            
            System.out.println("\nCourses by Dr. Smith:");
            courseDAO.getCoursesByInstructor("Dr. Smith").forEach(System.out::println);
            
            System.out.println("\n--- ENROLLMENT OPERATIONS (Many-to-Many) ---\n");
            
            int e1 = enrollmentDAO.enrollStudent(s1, c1, LocalDate.now());
            int e2 = enrollmentDAO.enrollStudent(s1, c2, LocalDate.now());
            int e3 = enrollmentDAO.enrollStudent(s2, c1, LocalDate.now());
            int e4 = enrollmentDAO.enrollStudent(s3, c2, LocalDate.now());
            int e5 = enrollmentDAO.enrollStudent(s3, c3, LocalDate.now());
            
            System.out.println("\nEnrollments for Student 1 (Vaishnavi):");
            List<Enrollment> enrollmentsS1 = enrollmentDAO.getEnrollmentsByStudent(s1);
            enrollmentsS1.forEach(System.out::println);
            
            System.out.println("\nStudents enrolled in Course 1 (Data Structures):");
            List<Student> studentsInCourse = enrollmentDAO.getStudentsInCourse(c1);
            studentsInCourse.forEach(System.out::println);
            
            System.out.println("\nCourses for Student 3 (Priya):");
            List<Course> coursesForStudent = enrollmentDAO.getCoursesForStudent(s3);
            coursesForStudent.forEach(System.out::println);
            
            System.out.println("\nAll Enrollments:");
            List<Enrollment> allEnrollments = enrollmentDAO.getAllEnrollments();
            allEnrollments.forEach(System.out::println);
            
            System.out.println("\n--- UPDATE OPERATIONS (Dirty Checking) ---\n");
            
            studentDAO.updateCGPA(s1, 9.0);
            courseDAO.updateCredits(c1, 5);
            enrollmentDAO.updateGrade(e1, "A");
            enrollmentDAO.updateGrade(e2, "B+");
            enrollmentDAO.updateStatus(e3, "COMPLETED");
            
            System.out.println("\nVerifying updates:");
            enrollmentDAO.getEnrollmentById(e1);
            studentDAO.getStudentById(s1);
            
            System.out.println("\n--- DELETION OPERATIONS (with CASCADE) ---\n");
            
            enrollmentDAO.dropCourse(e5);
            
            System.out.println("\n--- ADVANCED QUERIES ---\n");
            
            System.out.println("\nFinal Enrollment Summary:");
            List<Enrollment> finalEnrollments = enrollmentDAO.getAllEnrollments();
            System.out.println("Total Enrollments: " + finalEnrollments.size());
            finalEnrollments.forEach(e -> {
                System.out.println("  - " + e.getStudent().getName() + " -> " + 
                                 e.getCourse().getCourseName() + " (Grade: " + e.getGrade() + 
                                 ", Status: " + e.getStatus() + ")");
            });
            
            System.out.println("\n===== APPLICATION ENDED SUCCESSFULLY =====\n");
            
        } catch (Exception e) {
            System.err.println("Error occurred:");
            e.printStackTrace();
        } finally {
            HibernateUtils.shutdown();
        }
    }
}
// mvn exec:java "-Dexec.mainClass=com.enrollment.Main"
