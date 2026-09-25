package memorygame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import memorygame.model.Leaderboard;
import memorygame.ui.Launcher;

public final class Main {
    private Main() {}

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // fall back to the default look and feel
            }
            new Launcher(new Leaderboard(Leaderboard.defaultFile())).start("");
        });
    }
}
