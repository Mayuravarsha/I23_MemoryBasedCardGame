import java.awt.Dimension;
import javax.swing.JFrame;

public class Game {
    public static void main(String[] args) {
        Board b = new Board();
        b.setPreferredSize(new Dimension(1030, 1030)); // need to use this instead of setSize
        b.setLocation(500, 250);
        b.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        b.instructions();
        b.pack();
        b.setVisible(true);
    }
}
