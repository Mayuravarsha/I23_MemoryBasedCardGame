// Copyright 2022 mayur
// 
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
// 
//     http://www.apache.org/licenses/LICENSE-2.0
// 
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.JFrame;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.Timer;

public class Foo
        extends Frame
        implements WindowListener {

    private Timer timer;
    Label tempLabel = new Label("First Label.");

    Foo() {
        // adding WindowListener to the Frame
        // and using the windowClosing() method of WindowAdapter class
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                dispose();
            }
        });
    }

    public void initTimerComponent() {
        int delay = 1000; // milliseconds
        tempLabel.setBounds(50, 100, 100, 30);
        timer = new Timer(delay, null);
        timer.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                tempLabel.setVisible(true);
                String tmp = "test";
                tempLabel.setText("Temperature :  " + tmp);
            }
        });

        timer.start();
    }

    public Label getTempLabel() {
        return this.tempLabel;
    }

    @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
        timer.stop();
        this.dispose();
    }

    @Override
    public void windowClosed(WindowEvent e) {
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }

    public static void main(String args[]) {
        Foo n = new Foo();
        n.initTimerComponent();
        Frame f = new Frame("Label example");
        f.add(n.getTempLabel());
        f.setSize(400, 400);
        f.setLayout(null);
        f.setVisible(true);
    }
}
