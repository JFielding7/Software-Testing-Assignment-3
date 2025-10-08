import java.util.ArrayList;
import java.util.List;


public enum Cell
{

    EMPTY(" "),
    RED("●"),
    BLACK("○"),
    RED_KING("♛"),
    BLACK_KING("♕");

    private final String symbol;

    Cell(String symbol)
    {
        this.symbol = symbol;
    }

    private static final int RIGHT_DOWN = 7;
    private static final int LEFT_DOWN = 9;
    private static final int RIGHT_UP = -7;
    private static final int LEFT_UP = -9;

    private static final int RED_KING_START = 56;
    private static final int RED_KING_END = 63;
    private static final int BLACK_KING_START = 0;
    private static final int BLACK_KING_END = 7;

    public ArrayList<Integer> movement() {
        switch (this) {
            case RED:
                return new ArrayList<>(List.of(RIGHT_DOWN, LEFT_DOWN));
            case BLACK:
                return new ArrayList<>(List.of(RIGHT_UP, LEFT_UP));
            case RED_KING, BLACK_KING:
                return new ArrayList<>(List.of(RIGHT_DOWN, LEFT_DOWN, RIGHT_UP, LEFT_UP));
        }

        return new ArrayList<>();
    }

    public boolean isRedPiece() {
        return this.equals(RED) || this.equals(RED_KING);
    }

    public boolean isBlackPiece() {
        return this.equals(BLACK) || this.equals(BLACK_KING);
    }

    public boolean isOpponent(Cell other)  {
        switch (this) {
            case RED, RED_KING:
                return other.equals(Cell.BLACK) || other.equals(Cell.BLACK_KING);
            case BLACK, BLACK_KING:
                return other.equals(Cell.RED) || other.equals(Cell.RED_KING);
        }

        return false;
    }

    public Cell promoteIfReachedEnd(int cell_index) {
        switch (this) {
            case RED:
                if (RED_KING_START <= cell_index && cell_index <= RED_KING_END)
                    return RED_KING;
                break;
            case BLACK:
                if (BLACK_KING_START <= cell_index && cell_index <= BLACK_KING_END)
                    return BLACK_KING;
        }

        return this;
    }

    public boolean isEmpty() {
        return this.equals(Cell.EMPTY);
    }

    @Override
    public String toString()
    {
        return symbol;
    }
}
