package domain;

import java.util.List;

public class MultipleChoiceQuestion extends Question {

    private List<Choice> choices;

    public MultipleChoiceQuestion(int examID, int questionNumber, String title, String description, int marks) {
        super(examID, questionNumber, title, description, marks);
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }
}
