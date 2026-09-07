# 🎓 ZenSMS — Student Management System

> A full-stack role-based **Student Management System** built with Spring Boot 3 + Spring Security 6 + MySQL + Thymeleaf
+ Bootstrap 5. Features admin and student dashboards, CSV marks upload, attendance tracking, and deployed on Render using Docker.

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Project Structure](#-project-structure)
- [Screenshots](#-screenshots)
- [Getting Started](#-getting-started)
- [Database Setup](#-database-setup)
- [Default Login](#-default-login)
- [Deployment](#-deployment)
- [API Endpoints](#-api-endpoints)

---

## ✨ Features

### 👨‍💼 Admin
- Secure login with role-based access control
- Add and manage students with auto-created login credentials
- Add and manage departments
- Assign students to a department and semester
- Upload student marks in bulk via **CSV file**
- Add marks manually subject by subject
- View all students in a searchable dashboard

### 👨‍🎓 Student
- Secure login with credentials provided by admin
- View personal profile — department, semester, roll number
- View full **mark sheet** with percentage progress bar and auto grade (A+, A, B, C, F)
- Mark **daily attendance** as Present or Absent
- View complete attendance history with summary stats

### 🔒 Security
- Spring Security 6 with BCrypt password encoding (strength 12)
- Role-based routing — Admin → `/admin/**`, Student → `/student/**`
- Session management with single session per user
- Custom login success handler for role-based redirect
- Global exception handling with friendly error pages

---

## 🛠 Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 3.3.3 |
| Security | Spring Security 6 + BCrypt |
| ORM | Spring Data JPA + Hibernate |
| Database | MySQL 8.0 Railway |
| Frontend | Thymeleaf + Bootstrap 5.3 + Bootstrap Icons |
| Build Tool | Maven |
| IDE | Eclipse |
| Version Control | Git + GitHub |
| Containerization | Docker |
| Deployment | Render|

---

## 📁 Project Structure

```
ZenSMS/
├── Dockerfile
├── render.yaml
├── pom.xml
└── src/main/
    ├── java/com/example/sms/
    │   ├── controller/
    │   │   ├── AuthController.java
    │   │   ├── AdminController.java
    │   │   └── StudentController.java
    │   ├── model/
    │   │   ├── User.java
    │   │   ├── Student.java
    │   │   ├── Department.java
    │   │   ├── Marks.java
    │   │   └── Attendance.java
    │   ├── repository/
    │   │   ├── UserRepository.java
    │   │   ├── StudentRepository.java
    │   │   ├── DepartmentRepository.java
    │   │   ├── MarksRepository.java
    │   │   └── AttendanceRepository.java
    │   ├── service/
    │   │   ├── UserService.java
    │   │   ├── StudentService.java
    │   │   ├── MarksService.java
    │   │   └── AttendanceService.java
    │   ├── security/
    │   │   ├── SecurityConfig.java
    │   │   ├── CustomUserDetailsService.java
    │   │   └── AuthSuccessHandler.java
    │   └── config/
    │       ├── WebMvcConfig.java
    │       └── GlobalExceptionHandler.java
    └── resources/
        ├── templates/
        │   ├── login.html
        │   ├── fragments/navbar.html
        │   ├── admin/
        │   │   ├── dashboard.html
        │   │   ├── students.html
        │   │   ├── departments.html
        │   │   ├── assign.html
        │   │   └── marks.html
        │   └── student/
        │       ├── dashboard.html
        │       ├── marksheet.html
        │       └── attendance.html
        ├── static/css/style.css
        ├── application.properties
        └── application-prod.properties
```

---

## 🚀 Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+
- MySQL 8.0+
- Eclipse IDE (or IntelliJ)
- Git

### Clone the repository

```bash
git clone https://github.com/Nanditha1705/ZenSMS.git
cd ZenSMS
```

### Configure local database

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=yourpassword
```

### Run the application

```bash
mvn clean package -DskipTests
java -jar target/sms-0.0.1-SNAPSHOT.jar
```

Or in Eclipse: Right-click project → **Run As → Spring Boot App**

Visit: `http://localhost:8081/login`

---

## 🗄 Database Setup

Run this SQL in MySQL to create tables and seed data:

```sql
CREATE DATABASE IF NOT EXISTS student_db;
USE student_db;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL,
    enabled TINYINT(1) DEFAULT 1
);

CREATE TABLE departments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE students (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(150) NOT NULL,
    roll_number VARCHAR(50) NOT NULL UNIQUE,
    email VARCHAR(150),
    phone VARCHAR(20),
    semester INT DEFAULT 1,
    department_id BIGINT,
    user_id BIGINT UNIQUE,
    FOREIGN KEY (department_id) REFERENCES departments(id) ON DELETE SET NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE marks (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    subject_name VARCHAR(100) NOT NULL,
    marks_obtained INT NOT NULL,
    max_marks INT NOT NULL,
    semester INT NOT NULL,
    exam_type VARCHAR(50),
    student_id BIGINT,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE
);

CREATE TABLE attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    date DATE NOT NULL,
    status VARCHAR(20) NOT NULL,
    student_id BIGINT,
    FOREIGN KEY (student_id) REFERENCES students(id) ON DELETE CASCADE,
    UNIQUE KEY unique_attendance (student_id, date)
);

-- Seed admin user (password: admin123)
INSERT INTO users (username, password, role, enabled) VALUES (
    'admin',
    '$2a$12$eZQR1GnZFbGxqy7TqSFUXOA5HiBExT6Fy4B4sABCJ3PPoqGGZrx3K',
    'ROLE_ADMIN', 1
);

-- Sample departments
INSERT INTO departments (name)
VALUES ('Computer Science'), ('Electronics'), ('Mechanical'), ('Civil');
```

---

## 🔑 Default Login

| Role | Username | Password |
|---|---|---|
| Admin | `sms` | `admin123` |
| Student | created by admin | set by admin |

---

## 📦 CSV Marks Upload Format

When uploading marks as CSV, use this column order:

```
rollNumber, subjectName, marksObtained, maxMarks, semester, examType
```

Example:
```csv
rollNumber,subjectName,marksObtained,maxMarks,semester,examType
CS001,Mathematics,85,100,1,MID
CS001,Physics,90,100,1,FINAL
CS002,Mathematics,78,100,1,MID
```

---

## 🌐 Deployment

This project is deployed on **Render** using Docker.

### Docker build

```bash
docker build -t zensms .
docker run -p 8080:8080 zensms
```

### Environment variables required

```
SPRING_PROFILES_ACTIVE = prod
DATABASE_URL           = jdbc:mysql://host:port/railway?useSSL=false&allowPublicKeyRetrieval=true
DATABASE_USERNAME      = your_db_username
DATABASE_PASSWORD      = your_db_password
PORT                   = 8080
```

### Live URL

```
https://zensmss.onrender.com
```

---

## 🗺 API Endpoints

| Method | URL | Role | Description |
|---|---|---|---|
| GET | `/login` | All | Login page |
| POST | `/login` | All | Authenticate user |
| GET | `/admin/dashboard` | Admin | Admin home |
| GET | `/admin/students` | Admin | View all students |
| POST | `/admin/students/add` | Admin | Add new student |
| GET | `/admin/departments` | Admin | View departments |
| POST | `/admin/departments/add` | Admin | Add department |
| GET | `/admin/assign` | Admin | Assign dept & semester |
| POST | `/admin/assign` | Admin | Save assignment |
| GET | `/admin/marks` | Admin | Marks page |
| POST | `/admin/marks/upload` | Admin | Upload CSV marks |
| POST | `/admin/marks/add` | Admin | Add single mark |
| GET | `/student/dashboard` | Student | Student home |
| GET | `/student/marks` | Student | View mark sheet |
| GET | `/student/attendance` | Student | View attendance |
| POST | `/student/attendance/mark` | Student | Mark attendance |
| POST | `/logout` | All | Logout |

---

## 👩‍💻 Developer

**Nanditha**
- GitHub: [@Nanditha1705](https://github.com/Nanditha1705)

---

## 📄 License

This project is open source and available under the [MIT License](LICENSE).
