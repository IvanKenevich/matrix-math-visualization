import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Main class.
 */
public class Main {
    public static void main(String[] args) {
//        JFrame f = new JFrame("Ivan's 261 Project");
//        f.setSize(Toolkit.getDefaultToolkit().getScreenSize());
//        f.add(new VisualPanel(f.getWidth(), f.getHeight()));
//        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        f.setVisible(true);
        Matrix m0 = new Matrix(3,4,true,2,1,-1,8,-3,-1,2,-11,-2,1,2,-3);
        Matrix m1 = new Matrix(3,6,true,0,3,-6,6,4,-5,3,-7,8,-5,8,9,3,-9,12,-9,6,15);
        Matrix m2 = new Matrix(3,4,true,0,1,-4,8,2,-3,2,1,4,-8,12,1);
        System.out.println(m2);
        m2.reduce();
        System.out.println(m2);
    }
}
