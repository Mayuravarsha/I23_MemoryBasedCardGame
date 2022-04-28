package Try;
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

import java.awt.event.*;
import javax.swing.*;

public class MyWindow extends JFrame {
    MyWindow() {
        setSize(300, 200);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton b = new JButton("Close");
        b.setBounds((300 - 80) / 2, (200 - 30) / 2, 80, 30);
        //
        final MyWindow frame = this;
        b.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev) {
                        synchronized (frame) {
                            frame.notify();
                        }

                        frame.setVisible(false);
                        frame.dispose();
                    }
                });
        //
        getContentPane().add(b);

        setVisible(true);

        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
            }
        }
    }

    MyWindow(int a) {
        setSize(300, 200);
        setLayout(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JButton b = new JButton("Close");
        b.setBounds((400 - 80) / 2, (400 - 30) / 2, 80, 30);
        //
        final MyWindow frame = this;
        b.addActionListener(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent ev) {
                        synchronized (frame) {
                            frame.notify();
                        }

                        frame.setVisible(false);
                        frame.dispose();
                    }
                });
        //
        getContentPane().add(b);

        setVisible(true);

        synchronized (this) {
            try {
                this.wait();
            } catch (InterruptedException ex) {
            }
        }
    }

    public static void main(String args[]) {
        new MyWindow();
        System.out.println("You are here");
        new MyWindow();
    }
}
