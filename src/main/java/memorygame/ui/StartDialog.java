package memorygame.ui;

import java.awt.GridLayout;
import java.util.Optional;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import memorygame.model.Difficulty;

/** Asks for the player's name and a difficulty. Empty if the player cancels. */
final class StartDialog {
    record Choice(String name, Difficulty difficulty) {}

    private StartDialog() {}

    static Optional<Choice> ask(String lastName) {
        JTextField name = new JTextField(lastName, 16);
        JComboBox<Difficulty> level = new JComboBox<>(Difficulty.values());
        JPanel panel = new JPanel(new GridLayout(0, 1, 4, 4));
        panel.add(new JLabel("Your name"));
        panel.add(name);
        panel.add(new JLabel("Difficulty (Easy: 4x4 with undo, Hard: 6x6)"));
        panel.add(level);
        int answer = JOptionPane.showConfirmDialog(null, panel, "Memory Match",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (answer != JOptionPane.OK_OPTION) {
            return Optional.empty();
        }
        String n = name.getText().strip();
        return Optional.of(new Choice(n.isEmpty() ? "Player" : n, (Difficulty) level.getSelectedItem()));
    }
}
