package main.java.domain;

public class ShortAnswerQuestion extends Question {
    public ShortAnswerQuestion(int examID, int questionNumber, String title, String description, int marks) {
        super(examID, questionNumber, QuestionType.SHORT_ANSWER, title, description, marks);
    }
}
