DROP TABLE  subjects CASCADE;

CREATE TABLE subjects(
    subject_code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (subject_code)
);

DROP TYPE user_type CASCADE;
CREATE TYPE user_type AS ENUM ('ADMIN', 'STUDENT', 'INSTRUCTOR');

DROP TABLE  users CASCADE;

CREATE TABLE users(
    user_id INT NOT NULL UNIQUE,
    type user_type NOT NULL,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id)
);

DROP TABLE  users_has_subjects CASCADE;

CREATE TABLE users_has_subjects(
    user_id INT NOT NULL REFERENCES users(user_id),
    subject_code VARCHAR(20) NOT NULL REFERENCES subjects(subject_code)
);

-- Dummy data for testing
INSERT INTO users VALUES (000000, 'ADMIN', 'ADMIN', 'ADMIN', '000000');
INSERT INTO users VALUES (000001, 'INSTRUCTOR', 'Edurado', 'eddie', '000000');
INSERT INTO users VALUES (000002, 'INSTRUCTOR', 'Maria', 'maria', '000000');
INSERT INTO users VALUES (904601, 'STUDENT', 'Simai Deng', 'simaid', '000000');


INSERT INTO subjects VALUES ('SWEN90007', 'Software Design and Architecture');
INSERT INTO subjects VALUES ('SWEN90009', 'Software Requirement Analysis');

INSERT INTO users_has_subjects VALUES(000001, 'SWEN90007');
INSERT INTO users_has_subjects VALUES(000001, 'SWEN90009');
INSERT INTO users_has_subjects VALUES(000002, 'SWEN90007');
INSERT INTO users_has_subjects VALUES(904601, 'SWEN90007');

-- Exams
DROP TYPE exam_status CASCADE;
CREATE TYPE exam_status AS ENUM ('PUBLISHED', 'UNPUBLISHED', 'CLOSED');

DROP TABLE exams CASCADE;

CREATE TABLE exams(
  exam_id SERIAL NOT NULL UNIQUE,
  subject_code VARCHAR(20) REFERENCES subjects(subject_code),
  title VARCHAR(100) NOT NULL,
  description VARCHAR(200) NOT NULL UNIQUE,
  status exam_status NOT NULL DEFAULT 'UNPUBLISHED',
  PRIMARY KEY (exam_id)
);

INSERT INTO exams VALUES (DEFAULT,'SWEN90007', 'Mid-Sem exam', 'very good exam', DEFAULT);
INSERT INTO exams VALUES (DEFAULT,'SWEN90007', 'Final exam', 'very hard exam', DEFAULT);
