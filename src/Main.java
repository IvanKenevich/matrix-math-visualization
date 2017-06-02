package src;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Ivan on 5/2/2017.
 */
public class Main {
    public static void main(String[] args) {
        JFrame f = new JFrame("Graphics");
        f.setSize(Toolkit.getDefaultToolkit().getScreenSize());
        VisualPanel visualPanel = new VisualPanel(f.getWidth(), f.getHeight());
        f.add(visualPanel);
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.setVisible(true);
    }
}
