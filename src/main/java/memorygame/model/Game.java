package memorygame.model;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.LongSupplier;

/**
 * Game rules, independent of Swing so they can be unit tested.
 *
 * <p>A move turns two cards over. When the second card is flipped the pair
 * is <em>pending</em> so the player can see both faces; the UI then calls
 * {@link #resolvePending()} (after a short delay) to either keep them face up
 * as a match or turn them back. Clicks while a pair is pending are ignored.
 *
 * <p>Undo (Easy mode only) is a Command-style history: each resolved move is
 * pushed on a stack and can be reverted, which also reverts the move count.
 */
public final class Game {
    private final Difficulty difficulty;
    private final List<Card> cards;
    private final List<GameListener> listeners = new CopyOnWriteArrayList<>();
    private final Deque<Move> history = new ArrayDeque<>();
    private final LongSupplier clock;

    private int first = -1;
    private int second = -1;
    private int moves;
    private int pairsFound;
    private long startedAt = -1;
    private long finishedAt = -1;

    private record Move(int first, int second, boolean matched) {}

    public Game(Difficulty difficulty, long seed) {
        this(difficulty, new Random(seed), System::currentTimeMillis);
    }

    public Game(Difficulty difficulty, Random random, LongSupplier clock) {
        this.difficulty = difficulty;
        this.clock = clock;
        List<Integer> faces = new ArrayList<>();
        for (int face = 0; face < difficulty.pairs(); face++) {
            faces.add(face);
            faces.add(face);
        }
        Collections.shuffle(faces, random);
        List<Card> deck = new ArrayList<>();
        for (int face : faces) {
            deck.add(new Card(face));
        }
        this.cards = Collections.unmodifiableList(deck);
    }

    public void addListener(GameListener listener) { listeners.add(listener); }

    public Difficulty difficulty() { return difficulty; }
    public List<Card> cards() { return cards; }
    public int moves() { return moves; }
    public int pairsFound() { return pairsFound; }
    public boolean isWon() { return pairsFound == difficulty.pairs(); }
    public boolean hasPendingPair() { return second >= 0; }
    public boolean canUndo() { return difficulty.undoAllowed() && !history.isEmpty() && first < 0; }

    public long elapsedMillis() {
        if (startedAt < 0) return 0;
        return (finishedAt >= 0 ? finishedAt : clock.getAsLong()) - startedAt;
    }

    /**
     * Player clicked card {@code index}. Returns true if it was turned over;
     * false if the click was ignored (already face up, pair pending, game over).
     */
    public boolean flip(int index) {
        Card card = cards.get(index);
        if (isWon() || hasPendingPair() || card.isFaceUp()) {
            return false;
        }
        if (startedAt < 0) {
            startedAt = clock.getAsLong();
        }
        card.setFaceUp(true);
        if (first < 0) {
            first = index;
        } else {
            second = index;
        }
        fire(l -> l.cardChanged(index));
        return true;
    }

    /** Compare the two face-up cards. Returns true for a match. No-op without a pending pair. */
    public boolean resolvePending() {
        if (!hasPendingPair()) {
            return false;
        }
        Card a = cards.get(first);
        Card b = cards.get(second);
        boolean matched = a.face() == b.face();
        moves++;
        if (matched) {
            a.setMatched(true);
            b.setMatched(true);
            pairsFound++;
        } else {
            a.setFaceUp(false);
            b.setFaceUp(false);
        }
        history.push(new Move(first, second, matched));
        int i = first, j = second;
        first = second = -1;
        fire(l -> {
            l.pairResolved(i, j, matched);
            l.cardChanged(i);
            l.cardChanged(j);
        });
        if (isWon()) {
            finishedAt = clock.getAsLong();
            fire(l -> l.gameWon(moves, elapsedMillis()));
        }
        return matched;
    }

    /** Easy mode: take back the last move (a found pair is turned face down again). */
    public boolean undo() {
        if (!canUndo()) {
            return false;
        }
        Move last = history.pop();
        moves--;
        if (last.matched()) {
            cards.get(last.first()).setMatched(false);
            cards.get(last.second()).setMatched(false);
            cards.get(last.first()).setFaceUp(false);
            cards.get(last.second()).setFaceUp(false);
            pairsFound--;
            finishedAt = -1;
        }
        fire(l -> {
            l.cardChanged(last.first());
            l.cardChanged(last.second());
        });
        return true;
    }

    private void fire(java.util.function.Consumer<GameListener> event) {
        for (GameListener l : listeners) {
            event.accept(l);
        }
    }
}
