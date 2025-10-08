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

    private GameState() {}

    private GameState(ArrayList<Cell> cells,  boolean blackTurn) {
        this.cells = cells;
        this.blackTurn = blackTurn;
    }

    private GameState removePiece(int piece_index) {
        GameState newState = new GameState();

        newState.cells = new ArrayList<>(this.cells);
        newState.blackTurn = this.blackTurn;
        newState.cells.set(piece_index, Cell.EMPTY);

        return newState;
    }

    private GameState movePieceFlipTurn(
        int movedCellStartIndex,
        int movedCellEndIndex
    ) {
        GameState newState = new GameState();

        newState.cells = new ArrayList<>(this.cells);
        newState.blackTurn = !this.blackTurn;

        Cell moved_cell = this.cells.get(movedCellStartIndex);
        newState.cells.set(movedCellStartIndex, Cell.EMPTY);
        newState.cells.set(movedCellEndIndex, moved_cell.promoteIfReachedEnd(movedCellEndIndex));

        return newState;
    }

    private int rowIndex(String cell) {
        if (cell.length() < 2) {
            return -1;
        }

        return cell.charAt(1) - '1';
    }

    private int colIndex(String cell) {
        if (cell.length() < 2) {
            return -1;
        }

        return cell.charAt(0) - 'A';
    }

    private boolean moveOffBoard(int startIndex, int endIndex) {
        int currRow = startIndex % ROWS;
        int nextRow = endIndex % ROWS;
        int rowDiff = Math.abs(currRow - nextRow);

        return (endIndex < 0 || endIndex >= ROWS * COLS || rowDiff != 1);
    }

    private void populateNextMoves(
            int startIndex,
            int currIndex,
            boolean hasJumped,
            ArrayList<GameState> nextMoves
    ) {
        Cell startCell = cells.get(startIndex);

        for (int move : startCell.movement()) {
            int nextIndex = currIndex + move;

            if (moveOffBoard(currIndex, nextIndex)) {
                continue;
            }

            if (cells.get(nextIndex).isEmpty() && !hasJumped) {
                nextMoves.add(this.movePieceFlipTurn(startIndex, nextIndex));

            } else if (startCell.isOpponent(cells.get(nextIndex))) {
                int opponentIndex = nextIndex;
                nextIndex += move;

                if (moveOffBoard(opponentIndex, nextIndex) || !cells.get(nextIndex).isEmpty()) {
                    continue;
                }

                GameState updated = this.removePiece(opponentIndex);
                nextMoves.add(updated.movePieceFlipTurn(startIndex, nextIndex));

                updated.populateNextMoves(startIndex, nextIndex, true, nextMoves);
            }
        }
    }

    public ArrayList<GameState> nextMovesFromCell(String cell) {
        int r = rowIndex(cell);
        int c = colIndex(cell);

        if (r < 0 || c < 0 || r >= ROWS || c >= COLS) {
            return new ArrayList<>();
        }

        int startIndex = r * COLS + c;
        Cell curr = cells.get(startIndex);

        if ((blackTurn && !curr.isBlackPiece()) ||
            (!blackTurn && !curr.isRedPiece())) {
            return new ArrayList<>();
        }

        ArrayList<GameState> nextGameStates = new ArrayList<>();
        populateNextMoves(startIndex, startIndex, false, nextGameStates);

        return nextGameStates;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        int horizontalSize = COLS * 2 - 1;
        sb.append("   A B C D E F G H\n");
        sb.append("  ┌").append("─".repeat(horizontalSize)).append("┐\n");

        for (int i = 0; i < ROWS; i++) {
            sb.append(i + 1).append(" │");
            for (int j = 0; j < COLS; j++) {
                sb.append(cells.get(i * COLS + j).toString()).append("│");
            }
            sb.append("\n");
        }

        sb.append("  └").append("─".repeat(horizontalSize)).append("┘\n");
        return sb.toString();
    }

    public static GameState fromFile(
        String filepath,
        boolean blackTurn
    ) throws FileNotFoundException, NoSuchElementException, IllegalArgumentException {

        File file = new File(filepath);

        Scanner reader = new Scanner(file);

        ArrayList<Cell> cells = new ArrayList<>();

        for (int i = 0; i < ROWS; i++) {
            String line = reader.nextLine();

            for (int j = 0; j < COLS; j++) {
                char data = line.charAt(j);
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

    public boolean isBlackTurn() {
        return this.blackTurn;
    }

    public ArrayList<Cell> getCells() {
        return this.cells;
    }
}
