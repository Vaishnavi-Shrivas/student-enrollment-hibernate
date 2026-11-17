package com.enrollment.dao;

import com.enrollment.entity.Course;
import com.enrollment.entity.Enrollment;
import com.enrollment.entity.Student;
import com.enrollment.service.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class EnrollmentDAO {
    
    public int enrollStudent(int studentId, int courseId, LocalDate enrollmentDate) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            
            if (student == null || course == null) {
                System.out.println("✗ Student or Course not found");
                return -1;
            }
            
            Enrollment enrollment = new Enrollment(student, course, enrollmentDate, "ACTIVE");
            session.save(enrollment);
            tx.commit();
            
            System.out.println("✓ Enrolled " + student.getName() + " in " + 
                             course.getCourseName());
            return enrollment.getEnrollmentId();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return -1;
        } finally {
            session.close();
        }
    }
    
    public Enrollment getEnrollmentById(int enrollmentId) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        
        try {
            Enrollment enrollment = session.get(Enrollment.class, enrollmentId);
            if (enrollment != null) {
                System.out.println("✓ Enrollment found: " + enrollment.getStudent().getName() + 
                                 " in " + enrollment.getCourse().getCourseName());
            } else {
                System.out.println("✗ Enrollment not found");
            }
            return enrollment;
        } finally {
            session.close();
        }
    }
    
    public List<Enrollment> getEnrollmentsByStudent(int studentId) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        
        try {
            Query<Enrollment> query = session.createQuery(
                "from Enrollment where student.studentId = :studentId", Enrollment.class
            );
            query.setParameter("studentId", studentId);
            List<Enrollment> enrollments = query.getResultList();
            System.out.println("✓ Found " + enrollments.size() + " enrollments for student ID " + studentId);
            return enrollments;
        } finally {
            session.close();
        }
    }
    
    public List<Student> getStudentsInCourse(int courseId) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        
        try {
            Query<Student> query = session.createQuery(
                "select e.student from Enrollment e where e.course.courseId = :courseId", 
                Student.class
            );
            query.setParameter("courseId", courseId);
            List<Student> students = query.getResultList();
            System.out.println("✓ Found " + students.size() + " students in course ID " + courseId);
            return students;
        } finally {
            session.close();
        }
    }
    
    public List<Enrollment> getAllEnrollments() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        
        try {
            Query<Enrollment> query = session.createQuery("from Enrollment", Enrollment.class);
            List<Enrollment> enrollments = query.getResultList();
            System.out.println("✓ Retrieved " + enrollments.size() + " total enrollments");
            return enrollments;
        } finally {
            session.close();
        }
    }
    
    public List<Course> getCoursesForStudent(int studentId) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        
        try {
            Query<Course> query = session.createQuery(
                "select e.course from Enrollment e where e.student.studentId = :studentId", 
                Course.class
            );
            query.setParameter("studentId", studentId);
            List<Course> courses = query.getResultList();
            System.out.println("✓ Found " + courses.size() + " courses for student ID " + studentId);
            return courses;
        } finally {
            session.close();
        }
    }
    
    public void updateGrade(int enrollmentId, String grade) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            Enrollment enrollment = session.get(Enrollment.class, enrollmentId);
            if (enrollment != null) {
                enrollment.setGrade(grade);
                tx.commit();
                System.out.println("✓ Grade updated to " + grade + " for enrollment ID " + enrollmentId);
            } else {
                System.out.println("✗ Enrollment not found");
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public void updateStatus(int enrollmentId, String status) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            Enrollment enrollment = session.get(Enrollment.class, enrollmentId);
            if (enrollment != null) {
                enrollment.setStatus(status);
                tx.commit();
                System.out.println("✓ Status updated to " + status + " for enrollment ID " + enrollmentId);
            } else {
                System.out.println("✗ Enrollment not found");
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public void dropCourse(int enrollmentId) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            Enrollment enrollment = session.get(Enrollment.class, enrollmentId);
            if (enrollment != null) {
                System.out.println("✓ Dropped " + enrollment.getStudent().getName() + 
                                 " from " + enrollment.getCourse().getCourseName());
                session.delete(enrollment);
                tx.commit();
            } else {
                System.out.println("✗ Enrollment not found");
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public void deleteEnrollmentsForCourse(int courseId) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            Query query = session.createQuery("delete from Enrollment where course.courseId = :courseId");
            query.setParameter("courseId", courseId);
            int deletedCount = query.executeUpdate();
            tx.commit();
            System.out.println("✓ Deleted " + deletedCount + " enrollments for course ID " + courseId);
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}