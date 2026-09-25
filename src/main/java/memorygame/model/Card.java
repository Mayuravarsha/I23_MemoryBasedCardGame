package memorygame.model;

/** One card on the board. {@code face} identifies the picture; two cards share each face. */
public final class Card {
    private final int face;
    private boolean faceUp;
    private boolean matched;

    Card(int face) {
        this.face = face;
    }

    public int face() { return face; }
    public boolean isFaceUp() { return faceUp || matched; }
    public boolean isMatched() { return matched; }

    void setFaceUp(boolean faceUp) { this.faceUp = faceUp; }
    void setMatched(boolean matched) { this.matched = matched; }
}
