import java.util.ArrayList;
import java.util.List;


public enum Cell
{

    EMPTY(" "),
    RED("●"),
    BLACK("○"),
    RED_KING("♛"),
    BLACK_KING("♕");

    private final String name;

    Cell(String name)
    {
        this.name = name;
    }

    public ArrayList<Integer> movement() {
        switch (this) {
            case RED:
                return new ArrayList<>(List.of(1));
            case BLACK:
                return new ArrayList<>(List.of(-1));
            case RED_KING, BLACK_KING:
                return new ArrayList<>(List.of(-1, 1));
        }

        return new ArrayList<>();
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

    public Cell toKing() {
        switch (this) {
            case RED:
                return RED_KING;
            case BLACK:
                return BLACK_KING;
        }

        return null;
    }

    @Override
    public String toString()
    {
        return name;
    }
}
