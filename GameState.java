import java.util.ArrayList;

public class GameState
{
    private static final int ROWS = 8;
    private static final int COLS = 8;

    private ArrayList<Cell> cells;
    private boolean black_turn;

    public GameState(
            int moved_cell_start_index,
            int moved_cell_end_index,
            ArrayList<Integer> jumped_cells,
            GameState prevState
    ) {
        this.cells = new ArrayList<>(prevState.cells);
        this.black_turn = !prevState.black_turn;

        Cell moved_cell = prevState.cells.get(moved_cell_start_index);
        this.cells.set(moved_cell_start_index, Cell.EMPTY);
        this.cells.set(moved_cell_end_index, moved_cell);

        for (int cell_index : jumped_cells) {
            this.cells.set(cell_index, Cell.EMPTY);
        }
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

        if ((black_turn && !(curr.equals(Cell.BLACK) || curr.equals(Cell.BLACK_KING))) ||
            (!black_turn && !(curr.equals(Cell.RED) || curr.equals(Cell.RED_KING)))) {
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
}
