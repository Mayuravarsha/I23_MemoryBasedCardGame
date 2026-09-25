import javax.swing.ImageIcon;
import javax.swing.JButton;

@SuppressWarnings("serial")
public class Card extends JButton {

    private int id;
    private boolean matched = false;

    public void set_front_icon(String file) {

        this.setIcon(new ImageIcon(this.getClass().getResource(file)));
    }

    public void set_back_icon() {
        this.setIcon(new ImageIcon(this.getClass().getResource("cardback1.jpg")));
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }

    public boolean getMatched() {
        return this.matched;
    }
}