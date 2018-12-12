package group23.pacman.model;

public class TemporaryGhost extends Ghost{

    // storing the question and answer to check for collision between the pacman and the temp ghost
    private Question question;
    private int answer;

    public TemporaryGhost(int x, int y, Board board, int ghost, Question question, int answer) {

        // sending to super with AI number 4 for Shy Ghost -> run away from pacman
        super(x, y, board, 4, ghost);
        this.question = question;
        this.answer = answer;
    }

    public boolean isRightGhost() {
        return question.getCorrect_ans() == answer;
    }
}
