package memorygame.model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Best scores per difficulty, stored as tab-separated lines:
 * {@code difficulty<TAB>name<TAB>moves<TAB>seconds}. Fewer moves rank higher;
 * ties go to the faster time.
 */
public final class Leaderboard {
    public record Entry(Difficulty difficulty, String name, int moves, long seconds) {}

    public static final int SIZE = 10;
    private static final Comparator<Entry> RANKING =
            Comparator.comparingInt(Entry::moves).thenComparingLong(Entry::seconds);

    private final Path file;
    private final List<Entry> entries = new ArrayList<>();

    public Leaderboard(Path file) {
        this.file = file;
        load();
    }

    public static Path defaultFile() {
        return Path.of(System.getProperty("user.home"), ".memorygame", "leaderboard.tsv");
    }

    /** Top entries for one difficulty, best first. */
    public List<Entry> top(Difficulty difficulty) {
        return entries.stream()
                .filter(e -> e.difficulty() == difficulty)
                .sorted(RANKING)
                .limit(SIZE)
                .toList();
    }

    /** Record a finished game. Returns its 1-based rank, or 0 if it did not make the list. */
    public int record(Difficulty difficulty, String name, int moves, long seconds) throws IOException {
        String clean = name == null ? "" : name.replaceAll("[\\t\\r\\n]", " ").strip();
        Entry entry = new Entry(difficulty, clean.isEmpty() ? "Player" : clean, moves, seconds);
        entries.add(entry);
        List<Entry> top = top(difficulty);
        entries.removeIf(e -> e.difficulty() == difficulty && !top.contains(e));
        save();
        int rank = top.indexOf(entry);
        return rank < 0 ? 0 : rank + 1;
    }

    private void load() {
        if (!Files.exists(file)) {
            return;
        }
        try {
            for (String line : Files.readAllLines(file, StandardCharsets.UTF_8)) {
                String[] p = line.split("\t");
                if (p.length != 4) continue;
                try {
                    entries.add(new Entry(Difficulty.valueOf(p[0]), p[1],
                            Integer.parseInt(p[2]), Long.parseLong(p[3])));
                } catch (IllegalArgumentException ignored) {
                    // skip corrupt lines rather than refusing to start
                }
            }
        } catch (IOException ignored) {
            // an unreadable leaderboard should not stop the game
        }
    }

    private void save() throws IOException {
        Files.createDirectories(file.toAbsolutePath().getParent());
        List<String> lines = entries.stream()
                .map(e -> e.difficulty() + "\t" + e.name() + "\t" + e.moves() + "\t" + e.seconds())
                .toList();
        Files.write(file, lines, StandardCharsets.UTF_8);
    }
}
