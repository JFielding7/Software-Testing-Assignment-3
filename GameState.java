import java.util.ArrayList;

public class GameState
{
    private static final int ROWS = 8;
    private static final int COLS = 8;

    private ArrayList<Cell> cells;

    public GameState() {

    }

    public ArrayList<GameState> nextGameStates() {
        ArrayList<GameState> gameStates = new ArrayList<>();

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {

            }
        }

        return gameStates;
    }
}
