import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String stateFileName = "c";
        String cell = "A1";
        boolean isBlackTurn = false;

        GameState state = GameState.fromFile(stateFileName, isBlackTurn);

        for (GameState nextState : state.nextMovesFromCell(cell)) {
            System.out.println(nextState);
        }
    }
}
