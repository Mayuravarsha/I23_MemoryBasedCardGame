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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class SwingDemo {

    public void gg1() {
        JFrame frame1 = new JFrame("Demo1");
        frame1.setSize(550, 300);
        // frame.addWindowListener(listener);
        frame1.setVisible(true);
        Container container = frame1.getContentPane();
        JTextPane pane = new JTextPane();
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setItalic(attributeSet, true);
        StyleConstants.setForeground(attributeSet, Color.black);
        StyleConstants.setBackground(attributeSet, Color.orange);
        pane.setCharacterAttributes(attributeSet, true);
        pane.setText("This is a demo1 text!");
        JScrollPane scrollPane = new JScrollPane(pane);
        container.add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String args[]) throws BadLocationException {
        SwingDemo L1 = new SwingDemo();
        JFrame frame = new JFrame("Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WindowListener listener = new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                Frame frame = (Frame) evt.getSource();
                System.out.println("Closing = " + frame.getTitle());
                L1.gg1();
            }
        };
        Container container = frame.getContentPane();
        JTextPane pane = new JTextPane();
        SimpleAttributeSet attributeSet = new SimpleAttributeSet();
        StyleConstants.setItalic(attributeSet, true);
        StyleConstants.setForeground(attributeSet, Color.black);
        StyleConstants.setBackground(attributeSet, Color.orange);
        pane.setCharacterAttributes(attributeSet, true);
        pane.setText("This is a demo text!");
        JScrollPane scrollPane = new JScrollPane(pane);
        container.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(550, 300);
        frame.addWindowListener(listener);
        frame.setVisible(true);

    }
}
