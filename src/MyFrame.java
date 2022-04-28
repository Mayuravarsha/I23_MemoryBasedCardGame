
// Java program to implement
// a Simple Registration Form
// using Java Swing

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;

class MyFrame
                extends JFrame
                implements ActionListener {

        // Components of the Form
        private Container c;
        private JLabel title;
        private JLabel name;
        private JTextField tname;
        private JLabel gender;
        private JRadioButton male;
        private JRadioButton female;
        private ButtonGroup gengp;
        private JLabel dob;
        private JComboBox date;
        private JComboBox month;
        private JComboBox year;
        private JComboBox country;
        private JLabel add;
        private JCheckBox term;
        private JButton sub;
        private JButton reset;
        private JTextArea tout;
        private JLabel res;
        private JTextArea resadd;

        private String dates[] = { "1", "2", "3", "4", "5",
                        "6", "7", "8", "9", "10",
                        "11", "12", "13", "14", "15",
                        "16", "17", "18", "19", "20",
                        "21", "22", "23", "24", "25",
                        "26", "27", "28", "29", "30",
                        "31" };
        private String months[] = { "Jan", "Feb", "Mar", "Apr",
                        "May", "Jun", "July", "Aug",
                        "Sup", "Oct", "Nov", "Dec" };
        private String years[] = { "1979", "1980", "1981", "1982",
                        "1983", "1984", "1985", "1986",
                        "1987", "1988", "1989", "1990",
                        "1991", "1992", "1993", "1994",
                        "1995", "1996", "1997", "1998",
                        "1999", "2000", "2001", "2002",
                        "2003", "2004", "2005", "2006",
                        "2007", "2008", "2009", "2010",
                        "2011", "2012", "2013", "2014",
                        "2015", "2016", "2017", "2018",
                        "2019", "2020" };
        private String countries[] = { "India", "Pakistan", "Sri Lanka",
                        "Australia", "Bangladesh", "South Africa", "New Zealand",
                        "West Indies", "England", "Afghanistan", "Scotland",
                        "Nepal", "Bhutan", "Myanmar", "Kenya", "Zimbabwe", "Namibia" };
        /*
         * public MyFrame(Board b) {
         * b.setPreferredSize(new Dimension(1030, 1030)); // need to use this instead of
         * setSize
         * b.setLocation(200, 250);
         * b.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         * b.pack(); // Automatically adjusts size of cards based on change in panel
         * size
         * b.setVisible(true); // Displays Board panel
         * setVisible(true);
         * setBounds(300, 90, 900, 600);
         * setDefaultCloseOperation(EXIT_ON_CLOSE);
         * setResizable(false);
         * 
         * }
         */

        // constructor, to initialize the components
        // with default values.
        public MyFrame() {
                setTitle("Player Details");
                setBounds(300, 90, 900, 600);
                setDefaultCloseOperation(EXIT_ON_CLOSE);
                setResizable(false);

                c = getContentPane();
                c.setLayout(null);

                title = new JLabel("Player Details");
                title.setFont(new Font("Arial", Font.PLAIN, 30));
                title.setSize(300, 30);
                title.setLocation(300, 30);
                c.add(title);

                name = new JLabel("Name");
                name.setFont(new Font("Arial", Font.PLAIN, 20));
                name.setSize(100, 20);
                name.setLocation(100, 100);
                c.add(name);

                tname = new JTextField();
                tname.setFont(new Font("Arial", Font.PLAIN, 15));
                tname.setSize(200, 20);
                tname.setLocation(200, 100);
                c.add(tname);

                gender = new JLabel("Gender");
                gender.setFont(new Font("Arial", Font.PLAIN, 20));
                gender.setSize(100, 20);
                gender.setLocation(100, 150);
                c.add(gender);

                male = new JRadioButton("Male");
                male.setFont(new Font("Arial", Font.PLAIN, 15));
                male.setSelected(true);
                male.setSize(75, 20);
                male.setLocation(200, 150);
                c.add(male);

                female = new JRadioButton("Female");
                female.setFont(new Font("Arial", Font.PLAIN, 15));
                female.setSelected(false);
                female.setSize(80, 20);
                female.setLocation(275, 150);
                c.add(female);

                gengp = new ButtonGroup();
                gengp.add(male);
                gengp.add(female);

                dob = new JLabel("DOB");
                dob.setFont(new Font("Arial", Font.PLAIN, 20));
                dob.setSize(100, 20);
                dob.setLocation(100, 200);
                c.add(dob);

                date = new JComboBox(dates);
                date.setFont(new Font("Arial", Font.PLAIN, 15));
                date.setSize(50, 20);
                date.setLocation(200, 200);
                c.add(date);

                month = new JComboBox(months);
                month.setFont(new Font("Arial", Font.PLAIN, 15));
                month.setSize(60, 20);
                month.setLocation(250, 200);
                c.add(month);

                year = new JComboBox(years);
                year.setFont(new Font("Arial", Font.PLAIN, 15));
                year.setSize(60, 20);
                year.setLocation(320, 200);
                c.add(year);

                add = new JLabel("Location");
                add.setFont(new Font("Arial", Font.PLAIN, 20));
                add.setSize(100, 20);
                add.setLocation(100, 250);
                c.add(add);

                country = new JComboBox(countries);
                country.setFont(new Font("Arial", Font.PLAIN, 15));
                country.setSize(200, 20);
                country.setLocation(200, 250);
                c.add(country);

                term = new JCheckBox("Accept Terms And Conditions.");
                term.setFont(new Font("Arial", Font.PLAIN, 15));
                term.setSize(250, 20);
                term.setLocation(100, 400);
                c.add(term);

                sub = new JButton("Submit");
                sub.setFont(new Font("Arial", Font.PLAIN, 15));
                sub.setSize(100, 20);
                sub.setLocation(150, 450);
                sub.addActionListener(this);
                c.add(sub);

                reset = new JButton("Reset");
                reset.setFont(new Font("Arial", Font.PLAIN, 15));
                reset.setSize(100, 20);
                reset.setLocation(270, 450);
                reset.addActionListener(this);
                c.add(reset);

                tout = new JTextArea();
                tout.setFont(new Font("Arial", Font.PLAIN, 15));
                tout.setSize(300, 200);
                tout.setLocation(500, 100);
                tout.setLineWrap(true);
                tout.setEditable(false);
                c.add(tout);

                res = new JLabel("");
                res.setFont(new Font("Arial", Font.PLAIN, 20));
                res.setSize(500, 60);
                res.setLocation(100, 500);
                c.add(res);

                resadd = new JTextArea();
                resadd.setFont(new Font("Arial", Font.PLAIN, 15));
                resadd.setSize(200, 75);
                resadd.setLocation(580, 175);
                resadd.setLineWrap(true);
                c.add(resadd);

                setVisible(true);
        }

        public void Registered() {
                JOptionPane.showMessageDialog(this, "Registered Successful. Click 'OK' to view Instructions.");
        }

        public void instructions() {
                JOptionPane.showMessageDialog(this,
                                "<html><p>Registered successfully</p><p>Try and match all cards in least turns possible</p></html>");

        }

        // method actionPerformed()
        // to get the action performed
        // by the user and act accordingly
        public void actionPerformed(ActionEvent e) {
                if (e.getSource() == sub) {
                        if (term.isSelected()) {
                                String data1;
                                String data = "Name : "
                                                + tname.getText() + "\n";
                                if (male.isSelected())
                                        data1 = "Gender : Male"
                                                        + "\n";
                                else
                                        data1 = "Gender : Female"
                                                        + "\n";
                                String data2 = "DOB : "
                                                + (String) date.getSelectedItem()
                                                + "/" + (String) month.getSelectedItem()
                                                + "/" + (String) year.getSelectedItem()
                                                + "\n";

                                String data3 = "Location : " + country.getSelectedItem();
                                tout.setText(data + data1 + data2 + data3);
                                tout.setEditable(false);
                                res.setText("<html><p>Registered successfully</p></html>");

                                this.Registered();
                                this.instructions();
                                Board b = new Board();
                                b.setPreferredSize(new Dimension(1030, 1030)); // need to use this instead of setSize
                                b.setLocation(200, 250);
                                b.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                                b.pack(); // Automatically adjusts size of cards based on change in panel size
                                b.setVisible(true); // Displays Board panel
                                // this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
                                b = null;
                        } else {
                                tout.setText("");
                                resadd.setText("");
                                res.setText("Please accept the"
                                                + " terms & conditions..");
                        }

                }

                else if (e.getSource() == reset) {
                        String def = "";
                        tname.setText(def);
                        country.setSelectedIndex(0);
                        res.setText(def);
                        tout.setText(def);
                        term.setSelected(false);
                        date.setSelectedIndex(0);
                        month.setSelectedIndex(0);
                        year.setSelectedIndex(0);
                        resadd.setText(def);
                }
                // this.instructions();
        }

}

// Driver Code

/*
 * class Registration {
 * 
 * public static void main(String[] args) throws Exception {
 * MyFrame f = new MyFrame();
 * }
 * }
 */