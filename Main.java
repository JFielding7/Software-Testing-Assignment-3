import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        String stateFileName = args[0];
        String cell = args[1];
        boolean isBlackTurn = args[2].equals("1");

        GameState state = GameState.fromFile(stateFileName, isBlackTurn);

        for (GameState nextState : state.nextMovesFromCell(cell)) {
            System.out.println(nextState);
        }
    }
}
