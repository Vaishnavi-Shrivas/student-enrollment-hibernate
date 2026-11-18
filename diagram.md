# UML and ER Diagrams: Student Course Enrollment System (Hibernate)


## 1. Class Diagram

```mermaid
classDiagram
    class Student {
        -Long studentId
        -String name
        -String email
        -String phone
        -Double cgpa
        -List~Enrollment~ enrollments
        +getters() setters()
    }
    class Course {
        -Long courseId
        -String courseCode
        -String courseName
        -Integer credits
        -String instructor
        -Integer maxCapacity
        -List~Enrollment~ enrollments
        +getters() setters()
    }
    class Enrollment {
        -Long enrollmentId
        -Student student
        -Course course
        -LocalDate enrollmentDate
        -String grade
        -String status
    }
    class StudentDAO {
        +addStudent(Student)
        +getStudentById(Long)
        +getAllStudents()
        +getStudentsByMinCGPA(Double)
        +updateCGPA(Long, Double)
        +deleteStudent(Long)
    }
    class CourseDAO {
        +addCourse(Course)
        +getCourseById(Long)
        +getCourseByCode(String)
        +getAllCourses()
        +getCoursesWithAvailableSeats()
        +updateCredits(Long, Integer)
        +deleteCourse(Long)
    }
    class EnrollmentDAO {
        +enrollStudent(Long, Long)
        +getEnrollmentsByStudent(Long)
        +getStudentsInCourse(Long)
        +updateGrade(Long, String)
        +updateStatus(Long, String)
        +dropCourse(Long)
    }
    class HibernateUtils {
        -SessionFactory sessionFactory$
        +getSessionFactory()$ SessionFactory
        +shutdown()$
    }
    class Main {
        +main(String[])$
    }

    Student "1" --> "0..*" Enrollment : enrollments
    Course "1" --> "0..*" Enrollment : enrollments
    Enrollment "0..*" --> "1" Student : student
    Enrollment "0..*" --> "1" Course : course
    StudentDAO ..> Student
    CourseDAO ..> Course
    EnrollmentDAO ..> Enrollment
    StudentDAO ..> HibernateUtils : uses
    CourseDAO ..> HibernateUtils : uses
    EnrollmentDAO ..> HibernateUtils : uses
    Main ..> StudentDAO
    Main ..> CourseDAO
    Main ..> EnrollmentDAO
```

 Entities hold data and mappings. DAOs hold the database operations. `HibernateUtils` is the shared SessionFactory.

## 2. Object Diagram

```mermaid
flowchart LR
    s1["<b>s1 : Student</b><br/>studentId = 1<br/>name = Vaishnavi<br/>cgpa = 8.5"]
    s2["<b>s2 : Student</b><br/>name = Arjun<br/>cgpa = 7.8"]
    s3["<b>s3 : Student</b><br/>name = Priya<br/>cgpa = 9.2"]
    c1["<b>c1 : Course</b><br/>courseCode = CSC101<br/>courseName = Data Structures"]
    c2["<b>c2 : Course</b><br/>courseCode = CSC201<br/>courseName = Database Management"]
    e1["<b>e1 : Enrollment</b><br/>grade = A<br/>status = ACTIVE"]
    e2["<b>e2 : Enrollment</b><br/>grade = B+<br/>status = ACTIVE"]
    e3["<b>e3 : Enrollment</b><br/>grade = null<br/>status = COMPLETED"]
    e4["<b>e4 : Enrollment</b><br/>grade = null<br/>status = ACTIVE"]

    e1 -->|student| s1
    e1 -->|course| c1
    e2 -->|student| s1
    e2 -->|course| c2
    e3 -->|student| s2
    e3 -->|course| c1
    e4 -->|student| s3
    e4 -->|course| c2
```
 Vaishnavi appears in two enrollments, so one student links to many courses. Data Structures has two students, which is the many-to-many relationship.

## 3. Use Case Diagram

```mermaid
flowchart LR
    Admin(["Admin / User"])

    subgraph System["Student Enrollment System"]
        UC1(["Register student"])
        UC2(["Update student CGPA"])
        UC3(["Delete student"])
        UC4(["Add course"])
        UC5(["Update course credits"])
        UC6(["Delete course"])
        UC7(["Enroll student in course"])
        UC8(["Assign grade"])
        UC9(["Update enrollment status"])
        UC10(["Drop course"])
        UC11(["View student's courses"])
        UC12(["View students in a course"])
        UC13(["Find students by minimum CGPA"])
    end

    Admin --- UC1
    Admin --- UC2
    Admin --- UC3
    Admin --- UC4
    Admin --- UC5
    Admin --- UC6
    Admin --- UC7
    Admin --- UC8
    Admin --- UC9
    Admin --- UC10
    Admin --- UC11
    Admin --- UC12
    Admin --- UC13
    UC3 -.->|includes| UC10
```

eleting a student also removes their enrollments through `CascadeType.ALL`, which is why the diagram links the two use cases.

## 4. Activity Diagram (Enroll a Student)

```mermaid
flowchart TD
    A((Start)) --> B[Open Session from SessionFactory]
    B --> C[Begin Transaction]
    C --> D[Load Student by ID]
    D --> E[Load Course by ID]
    E --> F{Student and Course found?}
    F -- No --> G[Throw not found exception]
    G --> H[Rollback Transaction]
    F -- Yes --> I[Create Enrollment<br/>status = ACTIVE, date = today]
    I --> J[session.save enrollment]
    J --> K{Save successful?}
    K -- No --> H
    K -- Yes --> L[Commit Transaction]
    L --> M[Print success message]
    H --> N[Close Session]
    M --> N
    N --> O((End))
```

 The transaction is all-or-nothing. A failure triggers a rollback, so no half-saved enrollment remains.

## 5. Sequence Diagram (Enroll a Student)

```mermaid
sequenceDiagram
    participant M as Main
    participant D as EnrollmentDAO
    participant H as HibernateUtils
    participant SF as SessionFactory
    participant S as Session
    participant T as Transaction
    participant DB as MySQL

    M->>D: enrollStudent(studentId, courseId)
    D->>H: getSessionFactory()
    H-->>D: sessionFactory
    D->>SF: openSession()
    SF-->>D: session
    D->>S: beginTransaction()
    S-->>D: tx
    D->>S: get(Student, studentId)
    S->>DB: SELECT from students
    DB-->>S: student row
    S-->>D: student
    D->>S: get(Course, courseId)
    S->>DB: SELECT from courses
    DB-->>S: course row
    S-->>D: course
    D->>S: save(new Enrollment)
    S->>DB: INSERT into enrollments
    D->>T: commit()
    T->>DB: COMMIT
    D->>S: close()
    D-->>M: success
```
 Every DAO method follows this pattern: open session, begin transaction, do the work, commit, close.

## 6. ER Diagram

```mermaid
erDiagram
    STUDENTS ||--o{ ENROLLMENTS : "enrolls in"
    COURSES ||--o{ ENROLLMENTS : "has"

    STUDENTS {
        int student_id PK
        varchar name
        varchar email UK
        varchar phone
        double cgpa
    }
    COURSES {
        int course_id PK
        varchar course_code UK
        varchar course_name
        int credits
        varchar instructor
        int max_capacity
    }
    ENROLLMENTS {
        int enrollment_id PK
        int student_id FK
        int course_id FK
        date enrollment_date
        varchar grade
        varchar status
    }
```

`ENROLLMENTS` is the junction table holding both foreign keys, which gives the many-to-many relationship.

## 7. Component Diagram

```mermaid
flowchart TB
    subgraph UI["Presentation"]
        Main["Main.java<br/>(console program)"]
    end
    subgraph DAO["Data Access Layer"]
        SD["StudentDAO"]
        CD["CourseDAO"]
        ED["EnrollmentDAO"]
    end
    subgraph ORM["ORM Layer (Hibernate 5.6)"]
        HU["HibernateUtils<br/>(singleton SessionFactory)"]
        EN["Entities:<br/>Student, Course, Enrollment"]
    end
    subgraph CFG["Configuration"]
        XML["hibernate.cfg.xml"]
    end
    subgraph DB["Database"]
        JDBC["MySQL JDBC Driver"]
        MYSQL[("MySQL: student_enrollment")]
    end

    Main --> SD
    Main --> CD
    Main --> ED
    SD --> HU
    CD --> HU
    ED --> HU
    SD --> EN
    CD --> EN
    ED --> EN
    HU --> XML
    HU --> JDBC
    JDBC --> MYSQL
```
 Each layer talks only to the layer below it. The UI never uses Hibernate directly.