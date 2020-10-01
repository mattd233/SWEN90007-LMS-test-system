--------------------------------------------------------------------------------
--                                 subjects                                   --
--------------------------------------------------------------------------------
DROP TABLE subjects CASCADE;
CREATE TABLE subjects (
    subject_code VARCHAR(20) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    PRIMARY KEY (subject_code)
);

INSERT INTO subjects VALUES ('SWEN90007', 'Software Design and Architecture');
INSERT INTO subjects VALUES ('SWEN90009', 'Software Requirement Analysis');

--------------------------------------------------------------------------------
--                                   users                                    --
--------------------------------------------------------------------------------
DROP TYPE user_type CASCADE;
CREATE TYPE user_type AS ENUM ('ADMIN', 'STUDENT', 'INSTRUCTOR');

DROP TABLE users CASCADE;
CREATE TABLE users (
    user_id INT NOT NULL UNIQUE,
    type user_type NOT NULL,
    name VARCHAR(50) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(50) NOT NULL,
    PRIMARY KEY (user_id)
);

INSERT INTO users VALUES (000000, 'ADMIN', 'Admin', 'Admin', 'admin');
INSERT INTO users VALUES (000001, 'INSTRUCTOR', 'Eduardo', 'eduardo', '000000');
INSERT INTO users VALUES (000002, 'INSTRUCTOR', 'Maria', 'maria', '000000');
INSERT INTO users VALUES (904601, 'STUDENT', 'Simai Deng', 'simaid', '111111');
INSERT INTO users VALUES (713551, 'STUDENT', 'Jiayu Li', 'jiayul3', '222222');
INSERT INTO users VALUES (1049166, 'STUDENT', 'Yiran Wei', 'yirwei', '333333');

--------------------------------------------------------------------------------
--                            users_has_subjects                              --
--------------------------------------------------------------------------------
DROP TABLE users_has_subjects CASCADE;
CREATE TABLE users_has_subjects (
    user_id INT NOT NULL REFERENCES users(user_id),
    subject_code VARCHAR(20) NOT NULL REFERENCES subjects(subject_code),
    fudge_points FLOAT DEFAULT 0
);

INSERT INTO users_has_subjects VALUES(000001, 'SWEN90007', DEFAULT);
INSERT INTO users_has_subjects VALUES(000001, 'SWEN90009', DEFAULT);
INSERT INTO users_has_subjects VALUES(000002, 'SWEN90007', DEFAULT);
INSERT INTO users_has_subjects VALUES(904601, 'SWEN90007', DEFAULT);
INSERT INTO users_has_subjects VALUES(904601, 'SWEN90009', DEFAULT);
INSERT INTO users_has_subjects VALUES(713551, 'SWEN90007', DEFAULT);
INSERT INTO users_has_subjects VALUES(1049166, 'SWEN90007', DEFAULT);

--------------------------------------------------------------------------------
--                                  exams                                     --
--------------------------------------------------------------------------------
DROP TYPE exam_status CASCADE;
CREATE TYPE exam_status AS ENUM ('UNPUBLISHED', 'PUBLISHED', 'CLOSED');

DROP TABLE exams CASCADE;
CREATE TABLE exams (
    exam_id SERIAL NOT NULL UNIQUE,
    subject_code VARCHAR(20) REFERENCES subjects(subject_code),
    title VARCHAR(100) NOT NULL,
    description VARCHAR(200) NOT NULL,
    status exam_status NOT NULL DEFAULT 'UNPUBLISHED',
    PRIMARY KEY (exam_id)
);

INSERT INTO exams VALUES (DEFAULT, 'SWEN90007', 'Mid-Sem exam', 'very good exam', DEFAULT);
INSERT INTO exams VALUES (DEFAULT, 'SWEN90007', 'Final exam', 'very hard exam', DEFAULT);
INSERT INTO exams VALUES (DEFAULT, 'SWEN90007', 'Final exam', 'the exam', 'CLOSED');
INSERT INTO exams VALUES (DEFAULT, 'SWEN90009', 'Final exam', 'the exam', 'PUBLISHED');
INSERT INTO exams VALUES (DEFAULT, 'SWEN90007', 'Week 3 Quiz', 'A quiz about data source layer', 'CLOSED');
INSERT INTO exams VALUES (DEFAULT, 'SWEN90007', 'Week 5 Quiz', 'A quiz about object to relational structural patterns', 'PUBLISHED');
INSERT INTO exams VALUES (DEFAULT, 'SWEN90007', 'Final exam', 'Final exam of Software Design and Architecture', DEFAULT);
INSERT INTO exams VALUES (DEFAULT, 'SWEN90009', 'Final exam', 'Final exam of Software Requirement Analysis', DEFAULT);


--------------------------------------------------------------------------------
--                                 questions                                  --
--------------------------------------------------------------------------------
DROP TYPE question_type CASCADE;
CREATE TYPE question_type AS ENUM ('MULTIPLE_CHOICE', 'SHORT_ANSWER');

DROP TABLE questions CASCADE;
CREATE TABLE questions (
    exam_id SERIAL REFERENCES exams(exam_id),
    question_number SMALLINT NOT NULL,
    question_type question_type NOT NULL,
    title VARCHAR(45) NOT NULL,
    description VARCHAR(500) NOT NULL,
    marks FLOAT NOT NULL,
    PRIMARY KEY (exam_id, question_number)
);

INSERT INTO questions VALUES (1, 1, 'SHORT_ANSWER', 'Question 1', 'What is the object that wraps a row in a DB table or view, encapsulates the DB access, and adds domain logic on that data?', 50);
INSERT INTO questions VALUES (1, 2, 'MULTIPLE_CHOICE', 'Question 2', 'What is the layer of software that separates the in-memory objects from the database?', 50);

INSERT INTO questions VALUES (4, 1, 'SHORT_ANSWER', 'Essay Question 1', 'What is software engineering?', 20);
INSERT INTO questions VALUES (4, 2, 'SHORT_ANSWER', 'Essay Question 2', 'What is software requirements analysis?', 50);
INSERT INTO questions VALUES (4, 3, 'MULTIPLE_CHOICE', 'Multiple Choice 1', 'Choose the WRONG statements.', 30);

--------------------------------------------------------------------------------
--                                  choices                                   --
--------------------------------------------------------------------------------
DROP TABLE choices CASCADE;
CREATE TABLE choices (
    exam_id SERIAL REFERENCES exams(exam_id),
    question_number SMALLINT NOT NULL,
    choice_number SMALLINT NOT NULL,
    choice_description VARCHAR(200) NOT NULL,
    PRIMARY KEY (exam_id, question_number, choice_number)
);

INSERT INTO choices VALUES (1, 2, 1, 'Table data gateway');
INSERT INTO choices VALUES (1, 2, 2, 'Row data gateway');
INSERT INTO choices VALUES (1, 2, 3, 'Active record');
INSERT INTO choices VALUES (1, 2, 4, 'Data mapper');

INSERT INTO choices VALUES (4, 3, 1, 'Software engineering is meaningless.');
INSERT INTO choices VALUES (4, 3, 2, 'Software engineering is the systematic application of engineering approaches to the development of software.');

--------------------------------------------------------------------------------
--                                submissions                                 --
--------------------------------------------------------------------------------
DROP TABLE submissions CASCADE;
CREATE TABLE submissions (
    exam_id SERIAL REFERENCES exams(exam_id),
    user_id INT REFERENCES users(user_id),
    submission_time TIMESTAMP NOT NULL,
    is_marked BOOLEAN NOT NULL DEFAULT FALSE,
    marks FLOAT DEFAULT null,
    fudge_points FLOAT DEFAULT 0,
    PRIMARY KEY (exam_id, user_id)
);

INSERT INTO submissions VALUES (1, 904601, '2020-09-28 01:00:00', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO submissions VALUES (2, 904601, '2020-09-28 02:00:00', TRUE, 30, DEFAULT);
INSERT INTO submissions VALUES (3, 904601, '2020-09-28 02:00:00', TRUE, 50, DEFAULT);
INSERT INTO submissions VALUES (1, 713551, '2020-09-28 01:00:00', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO submissions VALUES (2, 713551, '2020-09-28 02:00:00', TRUE, 30, DEFAULT);
INSERT INTO submissions VALUES (3, 713551, '2020-09-28 02:00:00', TRUE, 50, DEFAULT);
INSERT INTO submissions VALUES (1, 1049166, '2020-09-28 01:00:00', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO submissions VALUES (2, 1049166, '2020-09-28 02:00:00', TRUE, 30, DEFAULT);
INSERT INTO submissions VALUES (3, 1049166, '2020-09-28 02:00:00', TRUE, 50, DEFAULT);

--------------------------------------------------------------------------------
--                            submitted questions                             --
--------------------------------------------------------------------------------
DROP TABLE submitted_questions CASCADE;
CREATE TABLE submitted_questions (
    exam_id SERIAL REFERENCES exams(exam_id),
    user_id INT REFERENCES users(user_id),
    question_number SMALLINT NOT NULL,
    question_type question_type NOT NULL,
    choice_number SMALLINT DEFAULT null,
    short_answer VARCHAR(500) DEFAULT null,
    is_marked BOOLEAN NOT NULL DEFAULT FALSE,
    marks FLOAT DEFAULT null,
    PRIMARY KEY (exam_id, user_id, question_number)
);

INSERT INTO submitted_questions VALUES (1, 904601, 1, 'SHORT_ANSWER', DEFAULT, 'Active record', DEFAULT, DEFAULT);
INSERT INTO submitted_questions VALUES (1, 904601, 2, 'MULTIPLE_CHOICE', 3, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO submitted_questions VALUES (1, 713551, 1, 'SHORT_ANSWER', DEFAULT, 'Active record', DEFAULT, DEFAULT);
INSERT INTO submitted_questions VALUES (1, 713551, 2, 'MULTIPLE_CHOICE', 2, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO submitted_questions VALUES (1, 1049166, 1, 'SHORT_ANSWER', DEFAULT, 'Data mapper', DEFAULT, DEFAULT);
INSERT INTO submitted_questions VALUES (1, 1049166, 2, 'MULTIPLE_CHOICE', 4, DEFAULT, DEFAULT, DEFAULT);
