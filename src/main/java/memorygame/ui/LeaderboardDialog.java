package memorygame.ui;

import java.awt.Component;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import memorygame.model.Difficulty;
import memorygame.model.Leaderboard;

final class LeaderboardDialog {
    private LeaderboardDialog() {}

    static void show(Component parent, Leaderboard board, Difficulty difficulty) {
        List<Leaderboard.Entry> top = board.top(difficulty);
        if (top.isEmpty()) {
            JOptionPane.showMessageDialog(parent, "No scores yet - be the first!",
                    difficulty + " leaderboard", JOptionPane.PLAIN_MESSAGE);
            return;
        }
        Object[][] rows = new Object[top.size()][];
        for (int i = 0; i < top.size(); i++) {
            Leaderboard.Entry e = top.get(i);
            rows[i] = new Object[] {i + 1, e.name(), e.moves(), e.seconds() + " s"};
        }
        JTable table = new JTable(rows, new Object[] {"#", "Player", "Moves", "Time"});
        table.setEnabled(false);
        table.setPreferredScrollableViewportSize(table.getPreferredSize());
        JOptionPane.showMessageDialog(parent, new JScrollPane(table),
                difficulty + " leaderboard", JOptionPane.PLAIN_MESSAGE);
    }
}
