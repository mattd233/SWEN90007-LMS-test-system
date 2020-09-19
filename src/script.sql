CREATE TABLE students(
    student_id INT NOT NULL UNIQUE ,
    name VARCHAR(50) NOT NULL ,
    username VARCHAR(50) NOT NULL ,
    password VARCHAR(50) NOT NULL ,
    PRIMARY KEY (student_id)
);

CREATE TABLE subjects(
    subject_code VARCHAR(20) NOT NULL UNIQUE ,
    name VARCHAR(100) NOT NULL ,
    PRIMARY KEY (subject_code)
);

CREATE TABLE coordinators(
    staff_id INT NOT NULL UNIQUE ,
    name VARCHAR(50) NOT NULL ,
    username VARCHAR(50) NOT NULL ,
    password VARCHAR(50) NOT NULL ,
    PRIMARY KEY (staff_id)
);

CREATE TABLE students_has_subjects(
    student_id INT NOT NULL REFERENCES students(student_id),
    subject_code VARCHAR(20) NOT NULL REFERENCES subjects(subject_code)
);

CREATE TABLE coordinators_has_subjects(
    staff_id INT NOT NULL REFERENCES coordinators(staff_id),
    subject_code VARCHAR(20) NOT NULL REFERENCES subjects(subject_code)
);