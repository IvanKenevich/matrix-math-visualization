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
        Matrix m0 = new Matrix(4,4,true,
                1,1,1,1,1,1,1,1,0,1,2,3,0,1,2,3);
        Matrix m1 = new Matrix(3,6,true,0,3,-6,6,4,-5,3,-7,8,-5,8,9,3,-9,12,-9,6,15);
        Matrix m2 = new Matrix(3,4,true,0,1,-4,8,2,-3,2,1,4,-8,12,1);

        Matrix m3 = new Matrix(true, new float[]{1,2,3},new float[]{4,5,6},new float[]{7,8,9});
        System.out.println(m3);
//        System.out.println(Matrix.rref(m3));
    }
}
