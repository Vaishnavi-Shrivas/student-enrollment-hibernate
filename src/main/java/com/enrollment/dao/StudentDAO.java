package com.enrollment.dao;

import com.enrollment.entity.Student;
import com.enrollment.service.HibernateUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class StudentDAO {
    
    public int addStudent(Student student) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            session.save(student);
            tx.commit();
            System.out.println("✓ Student added: " + student.getName());
            return student.getStudentId();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            return -1;
        } finally {
            session.close();
        }
    }
    
    public Student getStudentById(int studentId) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        
        try {
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                System.out.println("✓ Student found: " + student.getName());
            } else {
                System.out.println("✗ Student not found with ID: " + studentId);
            }
            return student;
        } finally {
            session.close();
        }
    }
    
    public List<Student> getAllStudents() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        
        try {
            Query<Student> query = session.createQuery("from Student", Student.class);
            List<Student> students = query.getResultList();
            System.out.println("✓ Retrieved " + students.size() + " students");
            return students;
        } finally {
            session.close();
        }
    }
    
    public List<Student> getStudentsByMinCGPA(double minCGPA) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        
        try {
            Query<Student> query = session.createQuery(
                "from Student where cgpa >= :minCGPA", Student.class
            );
            query.setParameter("minCGPA", minCGPA);
            List<Student> students = query.getResultList();
            System.out.println("✓ Found " + students.size() + " students with CGPA >= " + minCGPA);
            return students;
        } finally {
            session.close();
        }
    }
    
    public void updateStudent(Student student) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            session.merge(student);
            tx.commit();
            System.out.println("✓ Student updated: " + student.getName());
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public void updateCGPA(int studentId, double newCGPA) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                student.setCgpa(newCGPA);
                tx.commit();
                System.out.println("✓ CGPA updated for " + student.getName() + " to " + newCGPA);
            } else {
                System.out.println("✗ Student not found");
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public void deleteStudent(int studentId) {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                session.delete(student);
                tx.commit();
                System.out.println("✓ Student deleted: " + student.getName());
            } else {
                System.out.println("✗ Student not found");
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
    
    public void deleteAllStudents() {
        Session session = HibernateUtils.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        
        try {
            Query query = session.createQuery("delete from Student");
            int deletedCount = query.executeUpdate();
            tx.commit();
            System.out.println("✓ Deleted " + deletedCount + " students");
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}