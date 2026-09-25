package memorygame.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.Test;

class GameTest {
    private final AtomicLong now = new AtomicLong(1_000);

    private Game game(Difficulty d) {
        return new Game(d, new Random(42), now::get);
    }

    /** Index pairs [a, b] with the same face, in board order. */
    private static List<int[]> pairs(Game g) {
        Map<Integer, Integer> seen = new HashMap<>();
        List<int[]> out = new ArrayList<>();
        for (int i = 0; i < g.cards().size(); i++) {
            Integer j = seen.put(g.cards().get(i).face(), i);
            if (j != null) out.add(new int[] {j, i});
        }
        return out;
    }

    private static int[] mismatch(Game g) {
        List<int[]> p = pairs(g);
        return new int[] {p.get(0)[0], p.get(1)[0]};
    }

    @Test
    void deckHasEachFaceExactlyTwice() {
        for (Difficulty d : Difficulty.values()) {
            Game g = game(d);
            assertEquals(d.rows() * d.columns(), g.cards().size());
            assertEquals(d.pairs(), pairs(g).size());
        }
    }

    @Test
    void sameSeedGivesSameLayoutDifferentSeedsDiffer() {
        List<Integer> a = new Game(Difficulty.HARD, 7).cards().stream().map(Card::face).toList();
        List<Integer> b = new Game(Difficulty.HARD, 7).cards().stream().map(Card::face).toList();
        List<Integer> c = new Game(Difficulty.HARD, 8).cards().stream().map(Card::face).toList();
        assertEquals(a, b);
        assertFalse(a.equals(c));
    }

    @Test
    void matchingPairStaysFaceUp() {
        Game g = game(Difficulty.EASY);
        int[] p = pairs(g).get(0);
        assertTrue(g.flip(p[0]));
        assertTrue(g.flip(p[1]));
        assertTrue(g.hasPendingPair());
        assertTrue(g.resolvePending());
        assertTrue(g.cards().get(p[0]).isMatched() && g.cards().get(p[1]).isFaceUp());
        assertEquals(1, g.moves());
        assertEquals(1, g.pairsFound());
    }

    @Test
    void mismatchTurnsBothBack() {
        Game g = game(Difficulty.EASY);
        int[] m = mismatch(g);
        g.flip(m[0]);
        g.flip(m[1]);
        assertFalse(g.resolvePending());
        assertFalse(g.cards().get(m[0]).isFaceUp());
        assertFalse(g.cards().get(m[1]).isFaceUp());
        assertEquals(1, g.moves());
    }

    @Test
    void clicksAreIgnoredWhilePairPendingOrOnFaceUpCards() {
        Game g = game(Difficulty.EASY);
        int[] m = mismatch(g);
        assertTrue(g.flip(m[0]));
        assertFalse(g.flip(m[0]), "same card twice");
        assertTrue(g.flip(m[1]));
        int third = pairs(g).get(2)[0];
        assertFalse(g.flip(third), "third card while a pair is showing");
        assertFalse(g.cards().get(third).isFaceUp());
    }

    @Test
    void matchedCardsCannotBeFlippedAgain() {
        Game g = game(Difficulty.EASY);
        int[] p = pairs(g).get(0);
        g.flip(p[0]);
        g.flip(p[1]);
        g.resolvePending();
        assertFalse(g.flip(p[0]));
    }

    @Test
    void perfectGameWinsWithOneMovePerPairAndReportsTime() {
        Game g = game(Difficulty.HARD);
        List<Object> won = new ArrayList<>();
        g.addListener(new GameListener() {
            @Override public void cardChanged(int index) {}
            @Override public void gameWon(int moves, long elapsedMillis) {
                won.add(moves);
                won.add(elapsedMillis);
            }
        });
        for (int[] p : pairs(g)) {
            g.flip(p[0]);
            now.addAndGet(500);
            g.flip(p[1]);
            g.resolvePending();
        }
        assertTrue(g.isWon());
        assertEquals(List.of(18, 18 * 500L), won);
        assertFalse(g.flip(0));
    }

    @Test
    void timerStartsOnFirstFlip() {
        Game g = game(Difficulty.EASY);
        now.addAndGet(10_000);
        assertEquals(0, g.elapsedMillis());
        g.flip(0);
        now.addAndGet(3_000);
        assertEquals(3_000, g.elapsedMillis());
    }

    @Test
    void undoRevertsMatchAndMoveCount() {
        Game g = game(Difficulty.EASY);
        int[] p = pairs(g).get(0);
        int[] m = mismatch(g);
        g.flip(p[0]); g.flip(p[1]); g.resolvePending();
        g.flip(m[1]); g.flip(pairs(g).get(3)[0]); g.resolvePending();
        assertEquals(2, g.moves());

        assertTrue(g.undo());                   // undo the mismatch
        assertEquals(1, g.moves());
        assertEquals(1, g.pairsFound());
        assertTrue(g.undo());                   // undo the match
        assertEquals(0, g.pairsFound());
        assertFalse(g.cards().get(p[0]).isFaceUp());
        assertFalse(g.undo(), "nothing left to undo");
    }

    @Test
    void undoIsDisabledInHardModeAndMidMove() {
        Game hard = game(Difficulty.HARD);
        int[] p = pairs(hard).get(0);
        hard.flip(p[0]); hard.flip(p[1]); hard.resolvePending();
        assertFalse(hard.canUndo());

        Game easy = game(Difficulty.EASY);
        int[] q = pairs(easy).get(0);
        easy.flip(q[0]); easy.flip(q[1]); easy.resolvePending();
        easy.flip(pairs(easy).get(1)[0]);
        assertFalse(easy.undo(), "cannot undo with one card turned over");
    }

    @Test
    void listenersSeeEveryCardChange() {
        Game g = game(Difficulty.EASY);
        List<Integer> changed = new ArrayList<>();
        g.addListener(changed::add);
        int[] m = mismatch(g);
        g.flip(m[0]);
        g.flip(m[1]);
        g.resolvePending();
        assertEquals(List.of(m[0], m[1], m[0], m[1]), changed);
    }
}
