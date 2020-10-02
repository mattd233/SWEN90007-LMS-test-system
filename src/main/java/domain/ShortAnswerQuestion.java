package main.java.domain;

public class ShortAnswerQuestion extends Question {
    public ShortAnswerQuestion(int examID, int questionID, String title, String description, int marks) {
        super(examID, questionID, QuestionType.SHORT_ANSWER, title, description, marks);
    }

    public ShortAnswerQuestion(int examID, String title, String description, int marks) {
        super(examID, QuestionType.SHORT_ANSWER, title, description, marks);
    }
}
