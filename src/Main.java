package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Main class.
 */
public class Main {
    public static void main(String[] args) {
        JFrame f = new JFrame("Ivan's 261 Project");
        f.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        f.add(new VisualPanel(f.getWidth(), f.getHeight()));
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
