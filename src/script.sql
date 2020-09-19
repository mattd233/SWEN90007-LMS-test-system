CREATE TABLE subjects(
    subject_code VARCHAR(20) NOT NULL UNIQUE ,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (subject_code)
);

-- CREATE TYPE user_type AS ENUM ('admin', 'student', 'instructor');

CREATE TABLE users(
    user_id INT NOT NULL UNIQUE,
    type user_type NOT NULL,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL,
    password VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id)
);

CREATE TABLE users_has_subjects(
    user_id INT NOT NULL REFERENCES users(user_id),
    subject_code VARCHAR(20) NOT NULL REFERENCES subjects(subject_code)
);

-- Dummy data for testing
INSERT INTO users VALUES (000000, 'admin', 'admin', 'admin', '000000');
INSERT INTO users VALUES (000001, 'instructor', 'Edurado', 'eddie', '000000');
INSERT INTO users VALUES (000002, 'instructor', 'Maria', 'maria', '000000');
INSERT INTO users VALUES (904601, 'student', 'Simai Deng', 'simaid', '000000');


INSERT INTO subjects VALUES ('SWEN90007', 'Software Design and Architecture');
INSERT INTO subjects VALUES ('SWEN90009', 'Software Requirement Analysis');

INSERT INTO users_has_subjects VALUES(000001, 'SWEN90007');
INSERT INTO users_has_subjects VALUES(000001, 'SWEN90009');
INSERT INTO users_has_subjects VALUES(000002, 'SWEN90007');
INSERT INTO users_has_subjects VALUES(904601, 'SWEN90007');
