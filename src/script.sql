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

INSERT INTO users VALUES (000000, 'ADMIN', 'ADMIN', 'ADMIN', '000000');
INSERT INTO users VALUES (000001, 'INSTRUCTOR', 'Eduardo', 'eddie', '000000');
INSERT INTO users VALUES (000002, 'INSTRUCTOR', 'Maria', 'maria', '000000');
INSERT INTO users VALUES (904601, 'STUDENT', 'Simai Deng', 'simaid', '000000');

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
INSERT INTO exams VALUES (DEFAULT, 'SWEN90009', 'Final exam', 'the exam want you die', 'PUBLISHED');

--------------------------------------------------------------------------------
--                                 questions                                  --
--------------------------------------------------------------------------------
DROP TYPE question_types CASCADE;
CREATE TYPE question_types AS ENUM ('MULTIPLE_CHOICE', 'SHORT_ANSWER');

DROP TABLE questions CASCADE;
CREATE TABLE questions (
    exam_id SERIAL REFERENCES exams(exam_id),
    question_number SMALLINT NOT NULL,
    question_type question_types NOT NULL,
    title VARCHAR(45) NOT NULL,
    description VARCHAR(500) NOT NULL,
    marks FLOAT NOT NULL,
    PRIMARY KEY (exam_id, question_number)
);

INSERT INTO questions VALUES (1, 1, 'MULTIPLE_CHOICE', 'Maths Question 1', 'Select the biggest number.', 20);
INSERT INTO questions VALUES (1, 2, 'SHORT_ANSWER', 'Essay Question 2', 'Are cats cuter than dogs? Discuss.', 78);
INSERT INTO questions VALUES (1, 3, 'MULTIPLE_CHOICE', 'Maths Question 2', 'What does water turn into when temperature is below 0 degrees celsius?', 2);
INSERT INTO questions VALUES (2, 1, 'SHORT_ANSWER', 'Question 1', '1+1=?', 100);

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

INSERT INTO choices VALUES (1, 1, 1, '1');
INSERT INTO choices VALUES (1, 1, 2, '5');
INSERT INTO choices VALUES (1, 1, 3, '20');
INSERT INTO choices VALUES (1, 3, 1, 'Ice');
INSERT INTO choices VALUES (1, 3, 2, 'Cold water');

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

INSERT INTO submissions VALUES (1, 904601, '2001-09-28 01:00:00', DEFAULT, DEFAULT, DEFAULT);
INSERT INTO submissions VALUES (2, 904601, '2001-09-28 02:00:00', TRUE, 100, DEFAULT);
--------------------------------------------------------------------------------
--                            submitted questions                             --
--------------------------------------------------------------------------------
DROP TABLE submitted_questions CASCADE;
CREATE TABLE submitted_questions (
    exam_id SERIAL REFERENCES exams(exam_id),
    user_id INT REFERENCES users(user_id),
    question_number SMALLINT NOT NULL,
    question_type question_types NOT NULL,
    choice_number SMALLINT DEFAULT null,
    short_answer VARCHAR(500) DEFAULT null,
    is_marked BOOLEAN NOT NULL DEFAULT FALSE,
    marks FLOAT DEFAULT null,
    PRIMARY KEY (exam_id, user_id, question_number)
);

INSERT INTO submitted_questions VALUES (1, 904601, 1, 'MULTIPLE_CHOICE', 3, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO submitted_questions VALUES (1, 904601, 2, 'SHORT_ANSWER', DEFAULT, 'Probably.', DEFAULT, DEFAULT);
INSERT INTO submitted_questions VALUES (1, 904601, 3, 'MULTIPLE_CHOICE', 1, DEFAULT, DEFAULT, DEFAULT);
INSERT INTO submitted_questions VALUES (2, 904601, 1, 'SHORT_ANSWER', DEFAULT, '3', TRUE, 100);
