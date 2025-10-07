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

    @Override
    public String toString()
    {
        return name;
    }
}
