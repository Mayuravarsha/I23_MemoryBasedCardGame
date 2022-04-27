import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

@SuppressWarnings("serial")
public class Board extends JFrame {

    private List<Card> cards;
    private Card selected_card;
    private Card c1;
    private Card c2;
    private Timer t;
    private int Mode = 0;
    private int turns_taken = 0;
    private int pairs[] = { 8, 18 };
    private int rows[] = { 4, 6 };
    public List<Integer> card_vals = new ArrayList<Integer>();
    public List<Card> cardsList = new ArrayList<Card>();
    public List<Card> Sorted_cardsList = new ArrayList<Card>();

    public Board() {

        List<Card> cardsList = new ArrayList<Card>();

        for (int i = 0; i < pairs[Mode]; i++) {
            card_vals.add(i);
            card_vals.add(i);
        }
        Collections.shuffle(card_vals);
        List<Integer> shuffled = card_vals;
        System.out.println(shuffled);

        for (int val : shuffled) {
            Card c = new Card();
            c.setId(val);
            c.set_back_icon();
            c.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    selected_card = c;
                    doTurn();
                }
            });
            cardsList.add(c);
        }
        this.cards = cardsList;
        // set up the timer
        t = new Timer(750, new ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                checkCards();
            }
        });

        t.setRepeats(false);

        // set up the board itself
        Container pane = getContentPane();
        pane.setLayout(new GridLayout(rows[Mode], 5));

        for (Card c : cards) {
            pane.add(c);
        }

        setTitle("Memory Match");

    }

    public void instructions() {
        JOptionPane.showMessageDialog(this, "Try and match all cards in least turns possible");

    }

    public void doTurn() {
        if (c1 == null && c2 == null) {
            c1 = selected_card;
            c1.set_front_icon("card" + c1.getId() + ".jpg");
        }

        if (c1 != null && c1 != selected_card && c2 == null) {
            c2 = selected_card;
            c2.set_front_icon("card" + c2.getId() + ".jpg");
            t.start();

        }
    }

    public void solution(List<Integer> Cardval, List<Card> Sorted_CardList) {
        for (int val : Cardval) {
            Card c = new Card();

            c.set_front_icon("card" + val + ".jpg");

            Sorted_CardList.add(c);
        }
        this.cards = Sorted_CardList;

        // set up the board itself
        this.getContentPane().removeAll();
        Container pane2 = getContentPane();
        pane2.setLayout(new GridLayout(this.rows[Mode], 5));
        for (Card c : cards) {

            pane2.add(c);
            pane2.setVisible(true);
            this.pack();

        }
        setTitle("Memory Match");

    }

    public List<Integer> sorted(List<Integer> list) {
        if (list.size() < 2) {
            return list;
        }
        int mid = list.size() / 2;
        return merged(
                sorted(list.subList(0, mid)),
                sorted(list.subList(mid, list.size())));
    }

    private static List<Integer> merged(List<Integer> left, List<Integer> right) {
        int left_index = 0;
        int right_index = 0;
        List<Integer> merged = new ArrayList<Integer>();

        while (left_index < left.size() && right_index < right.size()) {
            if (left.get(left_index) < right.get(right_index)) {
                merged.add(left.get(left_index++));
            } else {
                merged.add(right.get(right_index++));
            }
        }
        merged.addAll(left.subList(left_index, left.size()));
        merged.addAll(right.subList(right_index, right.size()));
        return merged;
    }

    public void checkCards() {

        if (c1.getId() == c2.getId()) {// match condition
            c1.setEnabled(false); // disables the button
            c2.setEnabled(false);
            c1.setMatched(true); // flags the button as having been matched
            c2.setMatched(true);
            if (isGameWon()) {
                List<Integer> Sorted_list = sorted(card_vals);
                System.out.println(Sorted_list);
                solution(Sorted_list, Sorted_cardsList);

                JOptionPane.showMessageDialog(this, "You win! " + "Turns Taken=" + turns_taken);
                System.exit(0);
            }
        }

        else {
            c1.set_back_icon();
            c2.set_back_icon();
        }
        c1 = null; // reset c1 and c2
        c2 = null;
        turns_taken += 1;
    }

    public boolean isGameWon() {
        for (Card c : this.cards) {
            if (c.getMatched() == false) {
                return false;
            }
        }
        return true;
    }

}