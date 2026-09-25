package memorygame.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.Border;
import memorygame.model.Card;
import memorygame.model.Difficulty;
import memorygame.model.Game;
import memorygame.model.GameListener;
import memorygame.model.Leaderboard;

/** The board: a grid of card buttons plus a status bar. Pure view/controller over {@link Game}. */
public final class GameWindow extends JFrame implements GameListener {
    private static final int REVEAL_MILLIS = 750;
    private static final Border MATCHED_BORDER = BorderFactory.createLineBorder(new Color(0x52b788), 4);

    private final String player;
    private final Leaderboard leaderboard;
    private final CardImages images = new CardImages();
    private final List<JButton> buttons = new ArrayList<>();
    private final JLabel status = new JLabel();
    private final JButton undo = new JButton("Undo");
    private final Timer reveal;
    private final Timer ticker;
    private final int cardSize;
    private Game game;

    public GameWindow(String player, Difficulty difficulty, Leaderboard leaderboard) {
        super("Memory Match");
        this.player = player;
        this.leaderboard = leaderboard;
        this.cardSize = difficulty == Difficulty.EASY ? 140 : 105;
        this.game = new Game(difficulty, System.nanoTime());
        game.addListener(this);

        reveal = new Timer(REVEAL_MILLIS, e -> game.resolvePending());
        reveal.setRepeats(false);
        ticker = new Timer(1000, e -> updateStatus());

        JPanel grid = new JPanel(new GridLayout(difficulty.rows(), difficulty.columns(), 6, 6));
        grid.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        grid.setBackground(new Color(0x2f3e46));
        for (int i = 0; i < game.cards().size(); i++) {
            int index = i;
            JButton b = new JButton(images.back(cardSize));
            b.setPreferredSize(new Dimension(cardSize, cardSize));
            b.setFocusPainted(false);
            b.setBorder(BorderFactory.createEmptyBorder());
            b.addActionListener(e -> onClick(index));
            buttons.add(b);
            grid.add(b);
        }

        undo.setEnabled(false);
        undo.setVisible(difficulty.undoAllowed());
        undo.addActionListener(e -> game.undo());
        JButton scores = new JButton("Leaderboard");
        scores.addActionListener(e -> LeaderboardDialog.show(this, leaderboard, difficulty));

        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 6));
        bar.add(status);
        bar.add(undo);
        bar.add(scores);

        getContentPane().add(bar, BorderLayout.NORTH);
        getContentPane().add(grid, BorderLayout.CENTER);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        updateStatus();
        pack();
        setLocationRelativeTo(null);
    }

    private void onClick(int index) {
        if (game.flip(index) && game.hasPendingPair()) {
            reveal.restart();
        }
        if (!ticker.isRunning() && !game.isWon()) {
            ticker.start();
        }
    }

    @Override
    public void cardChanged(int index) {
        Card c = game.cards().get(index);
        JButton b = buttons.get(index);
        b.setIcon(c.isFaceUp() ? images.face(c.face(), cardSize) : images.back(cardSize));
        b.setEnabled(!c.isMatched());
        b.setDisabledIcon(c.isMatched() ? images.face(c.face(), cardSize) : null);
        b.setBorder(c.isMatched() ? MATCHED_BORDER : BorderFactory.createEmptyBorder());
        updateStatus();
    }

    @Override
    public void gameWon(int moves, long elapsedMillis) {
        ticker.stop();
        updateStatus();
        long seconds = Math.round(elapsedMillis / 1000.0);
        int rank;
        try {
            rank = leaderboard.record(game.difficulty(), player, moves, seconds);
        } catch (IOException e) {
            rank = 0;
        }
        String msg = String.format("You found all %d pairs in %d moves and %d s.%s",
                game.difficulty().pairs(), moves, seconds,
                rank > 0 ? "\nThat's #" + rank + " on the " + game.difficulty() + " leaderboard!" : "");
        JOptionPane.showMessageDialog(this, msg, "You win!", JOptionPane.INFORMATION_MESSAGE);
        LeaderboardDialog.show(this, leaderboard, game.difficulty());
    }

    private void updateStatus() {
        status.setText(String.format("%s  |  %s  |  Moves: %d  |  Pairs: %d/%d  |  Time: %ds",
                player, game.difficulty(), game.moves(), game.pairsFound(),
                game.difficulty().pairs(), game.elapsedMillis() / 1000));
        undo.setEnabled(game.canUndo());
    }

    /** For screenshots and tests: the underlying model. */
    Game game() {
        return game;
    }
}
