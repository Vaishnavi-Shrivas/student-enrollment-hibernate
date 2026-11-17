# Student Course Enrollment System (Hibernate)

A console application that manages students, courses, and enrollments using
Hibernate ORM 5.6 with MySQL and Maven.

## Features
- CRUD operations for students, courses, and enrollments through DAO classes
- Many-to-many relationship between students and courses via an enrollment entity
- Cascade delete, lazy/eager fetching, and HQL/JOIN queries
- Singleton SessionFactory managed by HibernateUtils

## Setup
1. Create a MySQL database named `student_enrollment`.
2. Set your MySQL password in `src/main/resources/hibernate.cfg.xml`.
3. Run:
   mvn exec:java "-Dexec.mainClass=com.enrollment.Main"