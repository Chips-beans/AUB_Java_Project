use master 
create Database SchoolMGT
go 

use SchoolMGT
-- 0. Users Table (The Credential Store)
CREATE TABLE Users (
    User_ID INT PRIMARY KEY identity(1,1),
    Username VARCHAR(50) UNIQUE NOT NULL,
    Password_Hash VARCHAR(255) NOT NULL, -- Store hashed passwords only
    Role VARCHAR(20) Not Null Check (Role IN ('Admin', 'Staff')),
);


-- 1. Subject List
CREATE TABLE Subjects (
    Subject_ID INT PRIMARY KEY identity(1,1),
    Subject_Name VARCHAR(100) NOT NULL,
    Subject_Code VARCHAR(10) UNIQUE
);

-- 2. Teachers Table
CREATE TABLE Teachers (
    Teacher_ID INT PRIMARY KEY identity(1,1),
    First_Name VARCHAR(50),
    Last_Name VARCHAR(50),
    Email VARCHAR(100) UNIQUE,
    Phone VARCHAR(20),
    Hire_Date DATE
);

-- 3. Students Table
CREATE TABLE Students (
    Student_ID INT PRIMARY KEY identity(1,1),
    First_Name VARCHAR(50),
    Last_Name VARCHAR(50),
    DOB DATE,
    Gender varchar(7) not null check (Gender in ('Male', 'Female', 'Other')),
    Address TEXT,
    Guardian_Contact VARCHAR(20),
    Admission_Date DATE DEFAULT GETDATE()
);

-- 4. Classes/Sections (Links a room and teacher to a group)
CREATE TABLE Classes (
    Class_ID INT PRIMARY KEY identity(1,1),
    Class_Name VARCHAR(50), -- e.g., 'Grade 10-A'
    Teacher_ID INT,
    Room_Number VARCHAR(10),
    FOREIGN KEY (Teacher_ID) REFERENCES Teachers(Teacher_ID)
);

-- 5. Enrollments (Links Students to Classes)
CREATE TABLE Enrollments (
    Enrollment_ID INT PRIMARY KEY identity(1,1),
    Student_ID INT,
    Class_ID INT,
    Enrollment_Date DATE,
    Status Varchar(10) not null check (Status in ('Active', 'Graduated', 'Dropped')),
    FOREIGN KEY (Student_ID) REFERENCES Students(Student_ID),
    FOREIGN KEY (Class_ID) REFERENCES Classes(Class_ID)
);

-- 6. Attendance
CREATE TABLE Attendance (
    Attendance_ID INT PRIMARY KEY identity(1,1),
    Student_ID INT,
    Class_ID INT,
    Attendance_Date DATE,
    Status Varchar(10) not null Check (Status in ('Present', 'Absent', 'Late', 'Excused')),
    FOREIGN KEY (Student_ID) REFERENCES Students(Student_ID),
    FOREIGN KEY (Class_ID) REFERENCES Classes(Class_ID)
);
-- 7. Grades/Results
CREATE TABLE Grades (
    Grade_ID INT PRIMARY KEY identity(1,1),
    Student_ID INT,
    Subject_ID INT,
    Marks_Obtained DECIMAL(5,2),
    Exam_Date DATE,
    Exam_Type VARCHAR(50), -- e.g., Mid-term, Final
    FOREIGN KEY (Student_ID) REFERENCES Students(Student_ID),
    FOREIGN KEY (Subject_ID) REFERENCES Subjects(Subject_ID)
);

-- 8. Fees Management
CREATE TABLE Fees (
    Fee_ID INT PRIMARY KEY identity(1,1),
    Student_ID INT,
    Amount_Due DECIMAL(10,2),
    Amount_Paid DECIMAL(10,2),
    Due_Date DATE,
    Status varchar(10) not null Check (Status in ('Paid', 'Unpaid', 'Partial')),
    FOREIGN KEY (Student_ID) REFERENCES Students(Student_ID)
);


