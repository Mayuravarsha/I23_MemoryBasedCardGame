package memorygame.model;

/** Board sizes. Easy mode also allows undo, as described in the project brief. */
public enum Difficulty {
    EASY(4, 4, true),
    HARD(6, 6, false);

    private final int rows;
    private final int columns;
    private final boolean undoAllowed;

    Difficulty(int rows, int columns, boolean undoAllowed) {
        this.rows = rows;
        this.columns = columns;
        this.undoAllowed = undoAllowed;
    }

    public int rows() { return rows; }
    public int columns() { return columns; }
    public int pairs() { return rows * columns / 2; }
    public boolean undoAllowed() { return undoAllowed; }
}
