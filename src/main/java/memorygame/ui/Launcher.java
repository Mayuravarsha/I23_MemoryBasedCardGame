package memorygame.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import memorygame.model.Leaderboard;

/** Shows the start dialog, then a game window; asks again when a window is closed. */
public final class Launcher {
    private final Leaderboard leaderboard;

    public Launcher(Leaderboard leaderboard) {
        this.leaderboard = leaderboard;
    }

    public void start(String lastName) {
        StartDialog.ask(lastName).ifPresent(choice -> {
            GameWindow window = new GameWindow(choice.name(), choice.difficulty(), leaderboard);
            window.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    start(choice.name());
                }
            });
            window.setVisible(true);
        });
    }
}
