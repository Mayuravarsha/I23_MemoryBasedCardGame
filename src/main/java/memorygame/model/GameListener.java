package memorygame.model;

/** Observer interface: the UI is told about state changes and never polls the model. */
public interface GameListener {
    /** A card was turned face up or face down (including by undo). */
    void cardChanged(int index);

    /** Two cards were compared; {@code matched} tells whether they stay face up. */
    default void pairResolved(int first, int second, boolean matched) {}

    /** Every pair has been found. */
    default void gameWon(int moves, long elapsedMillis) {}
}
