package com.enrollment.dao;

import com.enrollment.entity.Course;
import com.enrollment.service.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CourseDAO {
    
    public int addCourse(Course course) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            session.save(course);
            tx.commit();
            System.out.println("✓ Course added: " + course.getCourseName() + 
                             " (" + course.getCourseCode() + ")");
            return course.getCourseId();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return -1;
        } finally {
            session.close();
        }
    }
    
    public Course getCourseById(int courseId) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        
        try {
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                System.out.println("✓ Course found: " + course.getCourseName());
            } else {
                System.out.println("✗ Course not found with ID: " + courseId);
            }
            return course;
        } finally {
            session.close();
        }
    }
    
    public Course getCourseByCode(String courseCode) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        
        try {
            Query<Course> query = session.createQuery(
                "from Course where courseCode = :code", Course.class
            );
            query.setParameter("code", courseCode);
            Course course = query.uniqueResult();
            
            if (course != null) {
                System.out.println("✓ Course found: " + course.getCourseName());
            } else {
                System.out.println("✗ Course not found with code: " + courseCode);
            }
            return course;
        } finally {
            session.close();
        }
    }
    
    public List<Course> getAllCourses() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        
        try {
            Query<Course> query = session.createQuery("from Course", Course.class);
            List<Course> courses = query.getResultList();
            System.out.println("✓ Retrieved " + courses.size() + " courses");
            return courses;
        } finally {
            session.close();
        }
    }
    
    public List<Course> getCoursesByInstructor(String instructor) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        
        try {
            Query<Course> query = session.createQuery(
                "from Course where instructor = :instructor", Course.class
            );
            query.setParameter("instructor", instructor);
            List<Course> courses = query.getResultList();
            System.out.println("✓ Found " + courses.size() + " courses by " + instructor);
            return courses;
        } finally {
            session.close();
        }
    }
    
    public List<Course> getCoursesWithAvailableSeats() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        
        try {
            Query<Course> query = session.createQuery(
                "from Course where size(enrollments) < maxCapacity", Course.class
            );
            List<Course> courses = query.getResultList();
            System.out.println("✓ Found " + courses.size() + " courses with available seats");
            return courses;
        } finally {
            session.close();
        }
    }
    
    public void updateCourse(Course course) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            session.merge(course);
            tx.commit();
            System.out.println("✓ Course updated: " + course.getCourseName());
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public void updateCredits(int courseId, int newCredits) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                course.setCredits(newCredits);
                tx.commit();
                System.out.println("✓ Credits updated for " + course.getCourseName() + 
                                 " to " + newCredits);
            } else {
                System.out.println("✗ Course not found");
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public void deleteCourse(int courseId) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                session.delete(course);
                tx.commit();
                System.out.println("✓ Course deleted: " + course.getCourseName());
            } else {
                System.out.println("✗ Course not found");
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}