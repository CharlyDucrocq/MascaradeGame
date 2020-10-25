package utils.choice;

public class YesOrNoQuestion extends QuestionAnswers {
    public YesOrNoQuestion(String question, Runnable ifYes, Runnable ifNo) {
        super(  question,
                new Answer() {
                    @Override
                    public String getDescription() {
                        return "Yes";
                    }

                    @Override
                    public void toDoIfChose() {
                        ifYes.run();
                    }
                },
                new Answer() {
                    @Override
                    public String getDescription() {
                        return "No";
                    }

                    @Override
                    public void toDoIfChose() {
                        ifNo.run();
                    }
                }
        );
    }

    public YesOrNoQuestion(String question, Runnable ifYes) {
        super(  question,
                new Answer() {
                    @Override
                    public String getDescription() {
                        return "Yes";
                    }

                    @Override
                    public void toDoIfChose() {
                        ifYes.run();
                    }
                },
                new Answer() {
                    @Override
                    public String getDescription() {
                        return "No";
                    }

                    @Override
                    public void toDoIfChose() {
                    }
                }
        );
    }
}
