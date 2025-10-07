import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        GameState state = GameState.fromFile("c", false);

        System.out.println(state);

        System.out.println(state.nextMovesFromCell("A1"));
    }
}
