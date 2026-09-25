package memorygame.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class LeaderboardTest {
    @TempDir
    Path dir;

    @Test
    void ranksByMovesThenTimeAndPersists() throws IOException {
        Path file = dir.resolve("sub/board.tsv");
        Leaderboard b = new Leaderboard(file);
        assertEquals(1, b.record(Difficulty.EASY, "Asha", 12, 40));
        assertEquals(1, b.record(Difficulty.EASY, "Ravi", 10, 90));
        assertEquals(2, b.record(Difficulty.EASY, "Mayur", 12, 30));
        b.record(Difficulty.HARD, "Asha", 30, 200);

        Leaderboard reloaded = new Leaderboard(file);
        List<String> easy = reloaded.top(Difficulty.EASY).stream().map(Leaderboard.Entry::name).toList();
        assertEquals(List.of("Ravi", "Mayur", "Asha"), easy);
        assertEquals(1, reloaded.top(Difficulty.HARD).size());
    }

    @Test
    void keepsOnlyTopTen() throws IOException {
        Leaderboard b = new Leaderboard(dir.resolve("b.tsv"));
        for (int i = 0; i < 12; i++) {
            b.record(Difficulty.HARD, "p" + i, 20 + i, 60);
        }
        assertEquals(Leaderboard.SIZE, b.top(Difficulty.HARD).size());
        assertEquals(0, b.record(Difficulty.HARD, "slow", 99, 999));
        assertEquals(Leaderboard.SIZE, Files.readAllLines(dir.resolve("b.tsv")).size());
    }

    @Test
    void sanitisesNamesAndSkipsCorruptLines() throws IOException {
        Path file = dir.resolve("b.tsv");
        Files.writeString(file, "EASY\tok\t8\t20\nnot a valid line\nMEDIUM\tx\t1\t1\nEASY\tbad\tNaN\t3\n");
        Leaderboard b = new Leaderboard(file);
        assertEquals(1, b.top(Difficulty.EASY).size());
        b.record(Difficulty.EASY, "tab\tname\n", 9, 9);
        b.record(Difficulty.EASY, "   ", 10, 9);
        List<String> names = new Leaderboard(file).top(Difficulty.EASY).stream()
                .map(Leaderboard.Entry::name).toList();
        assertEquals(List.of("ok", "tab name", "Player"), names);
        assertTrue(Files.readString(file).lines().allMatch(l -> l.split("\t").length == 4));
    }
}
