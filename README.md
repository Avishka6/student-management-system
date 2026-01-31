# 🎓 Student Management System (SMS Admin)

A comprehensive web-based Student Management System built with Spring Boot and Java. This system helps educational institutions manage students, courses, and enrollments efficiently.

## 📋 Features

### Dashboard
- 📊 Real-time statistics (Total Students, Courses, Enrollments)
- 🏆 Most Popular Course tracking
- 📝 Recent Enrollments with pagination
- 📈 Visual data representation

### Student Management
- ➕ Add new students with detailed information
- ✏️ Edit student profiles
- 👁️ View complete student details
- 📄 Paginated student lists
- ✅ Activate/Deactivate student accounts

### Course Management
- ➕ Create new courses
- ✏️ Edit course information
- 👁️ View course details
- 💰 Manage course fees and duration
- 📄 Paginated course lists
- ✅ Activate/Deactivate courses
- 📊 Track enrolled students per course

### Enrollment Management
- 📝 Enroll students in courses
- 📋 View enrollment details
- 🔄 Manage enrollment status
- 📊 Track enrollment history
- 📄 Paginated enrollment lists

### Security
- 🔐 User authentication and authorization
- 🛡️ Role-based access control
- 🔒 Secure password encryption

## 🛠️ Technologies Used

### Backend
- **Java 17**
- **Spring Boot 3.5.10**
- **Spring Security** - Authentication and Authorization
- **Spring Data JPA** - Database operations
- **Hibernate** - ORM framework
- **Maven** - Dependency management

### Frontend
- **Thymeleaf** - Template engine
- **HTML5 & CSS3**
- **Bootstrap 5** - Responsive UI framework
- **Bootstrap Icons** - Icon library

### Database
- **MySQL** - Relational database

### Tools
- **Spring DevTools** - Hot reload for development
- **Lombok** - Reduce boilerplate code
- **ModelMapper** - Object mapping

## 📦 Prerequisites

Before running this project, make sure you have:

- Java 17 or higher installed
- MySQL 8.0 or higher installed
- Maven 3.6 or higher installed
- Git (optional, for cloning)

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone https://github.com/Avishka6/student-management-system.git
cd student-management-system
```

### 2. Configure Database
Create a MySQL database:
```sql
CREATE DATABASE student_management_db;
```

Update database configuration in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/student_management_db
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Build the Project
```bash
mvn clean install
```

### 4. Run the Application
```bash
mvn spring-boot:run
```

Or run the JAR file:
```bash
java -jar target/studentManagementSystem-0.0.1-SNAPSHOT.jar
```

### 5. Access the Application
Open your browser and navigate to:
```
http://localhost:8081
```

Default login credentials (if configured):
- Username: `admin`
- Password: `admin123`

## 📁 Project Structure

```
studentManagementSystem/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/studentManagementSystem/
│   │   │       ├── config/              # Configuration classes
│   │   │       ├── controller/          # REST & Web controllers
│   │   │       ├── dto/                 # Data Transfer Objects
│   │   │       │   └── paginated/       # Pagination DTOs
│   │   │       ├── entity/              # JPA Entities
│   │   │       ├── exception/           # Custom exceptions
│   │   │       ├── repo/                # JPA Repositories
│   │   │       ├── service/             # Service interfaces
│   │   │       │   └── impl/            # Service implementations
│   │   │       └── util/                # Utility classes
│   │   └── resources/
│   │       ├── static/                  # CSS, JS files
│   │       ├── templates/               # Thymeleaf templates
│   │       └── application.properties   # Application config
│   └── test/                            # Test files
├── target/                              # Compiled files
├── pom.xml                              # Maven dependencies
└── README.md                            # Project documentation
```

## 🎯 Key Functionalities

### Dashboard
- View total students, courses, and enrollments
- See most popular course
- Browse recent enrollments with pagination (2 per page)

### Student Operations
- **Add Student**: First name, last name, email, phone, address, date of birth
- **Edit Student**: Update any student information
- **View Student**: See complete profile and enrollment history
- **List Students**: Paginated list with active/inactive filter
- **Toggle Status**: Activate or deactivate student accounts

### Course Operations
- **Add Course**: Course name, code, description, duration, fees
- **Edit Course**: Update course information
- **View Course**: See details and enrolled students
- **List Courses**: Paginated list with active/inactive filter
- **Toggle Status**: Activate or deactivate courses

### Enrollment Operations
- **Enroll Student**: Link students to courses
- **View Enrollments**: See all enrollments by student or course
- **Enrollment Details**: View complete enrollment information

## 🔐 Security Features

- Password encryption using BCrypt
- Session-based authentication
- CSRF protection enabled
- Role-based access control
- Secure endpoints

## 📊 Database Schema

### Main Tables
- **students** - Student information
- **course** - Course details
- **enrollment** - Student-Course relationships
- **users** - Authentication data


## 👨‍💻 Author

- GitHub: https://github.com/Avishka6
- LinkedIn: https://www.linkedin.com/in/avishka-udayanga-35a3b5387


