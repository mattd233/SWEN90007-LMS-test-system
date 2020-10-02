package main.java.domain;

import java.util.List;

public class MultipleChoiceQuestion extends Question {

    private List<Choice> choices;

    public MultipleChoiceQuestion(int examID, int questionID, String title, String description, int marks) {
        super(examID, questionID, QuestionType.MULTIPLE_CHOICE, title, description, marks);
    }

    public MultipleChoiceQuestion(int examID, String title, String description, int marks) {
        super(examID, QuestionType.MULTIPLE_CHOICE, title, description, marks);
    }



    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}
