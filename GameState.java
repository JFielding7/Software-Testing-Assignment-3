import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class GameState
{
    private static final int ROWS = 8;
    private static final int COLS = 8;

    private ArrayList<Cell> cells;
    private boolean blackTurn;

    public GameState(
            int moved_cell_start_index,
            int moved_cell_end_index,
            ArrayList<Integer> jumped_cells,
            GameState prevState
    ) {
        this.cells = new ArrayList<>(prevState.cells);
        this.blackTurn = !prevState.blackTurn;

        Cell moved_cell = prevState.cells.get(moved_cell_start_index);
        this.cells.set(moved_cell_start_index, Cell.EMPTY);
        this.cells.set(moved_cell_end_index, moved_cell);

        for (int cell_index : jumped_cells) {
            this.cells.set(cell_index, Cell.EMPTY);
        }
    }

    private GameState(ArrayList<Cell> cells,  boolean blackTurn) {
        this.cells = cells;
        this.blackTurn = blackTurn;
    }

    private int rowIndex(String move) {
        if (move.length() < 2) {
            return -1;
        }

        return move.charAt(1) - '1';
    }

    private int colIndex(String move) {
        if (move.length() < 2) {
            return -1;
        }

        return move.charAt(0) - 'A';
    }

    private void populateNextMoves(
            int start_index,
            int curr_index,
            ArrayList<Integer> jumped,
            ArrayList<GameState> nextMoves
    ) {
        Cell start_cell = cells.get(start_index);

        for (int move : start_cell.movement()) {
            int next_index = curr_index + move;

            if (!start_cell.isOpponent(cells.get(next_index))) {
                nextMoves.add(new GameState(start_index, next_index, jumped, this));

            } else if (cells.get(next_index).equals(Cell.EMPTY)) {
                ArrayList<Integer> new_jumped = new ArrayList<>(jumped);
                new_jumped.add(next_index);
                next_index += move;
                nextMoves.add(new GameState(start_index, next_index, new_jumped, this));

                populateNextMoves(start_index, next_index, new_jumped, nextMoves);
            }
        }
    }

    public ArrayList<GameState> nextGameStates(String move) {
        int r = rowIndex(move);
        int c = colIndex(move);

        if (r < 0 || c < 0) {
            return null;
        }

        int start_index = r * COLS + c;
        Cell curr = cells.get(start_index);

        if ((blackTurn && !(curr.equals(Cell.BLACK) || curr.equals(Cell.BLACK_KING))) ||
            (!blackTurn && !(curr.equals(Cell.RED) || curr.equals(Cell.RED_KING)))) {
            return null;
        }

        ArrayList<GameState> nextGameStates = new ArrayList<>();
        populateNextMoves(start_index, start_index, new ArrayList<>(), nextGameStates);

        return nextGameStates;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int horizontalSize = COLS * 2 + 1;
        sb.append("┌").append("─".repeat(horizontalSize)).append("┐\n");

        for (int i = 0; i < ROWS; i++) {
            sb.append("│ ");
            for (int j = 0; j < COLS; j++) {
                sb.append(cells.get(i * COLS + j).toString()).append(" ");
            }
            sb.append("│\n");
        }

        sb.append("└").append("─".repeat(horizontalSize)).append("┘");
        return sb.toString();

    }

    public static GameState fromFile(String filepath, boolean blackTurn) throws FileNotFoundException, NoSuchElementException {

        File file = new File(filepath);

        Scanner reader = new Scanner(file);

        ArrayList<Cell> cells = new ArrayList<>();
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                char data = (char) reader.nextByte();
                switch (data) {
                    case 'r':
                        cells.add(Cell.RED);
                        break;
                    case 'b':
                        cells.add(Cell.BLACK);
                        break;
                    case 'R':
                        cells.add(Cell.RED_KING);
                        break;
                    case 'B':
                        cells.add(Cell.BLACK_KING);
                        break;
                    case '-':
                        cells.add(Cell.EMPTY);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid file format");
                }
            }
        }

        return new GameState(cells, blackTurn);
    }
}
