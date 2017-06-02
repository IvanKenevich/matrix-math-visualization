package src;

import sun.font.FontFamily;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * Created by Ivan on 5/4/2017.
 */
public class VisualPanel extends JPanel implements MouseMotionListener, MouseWheelListener {
    private final int width;
    private final int height;

    private float scalingFactor, rotationAmount;

    private float mouseX, mouseY, previousMouseX, previousMouseY;

    private Matrix data, transposeToOrigin, transposeFromOrigin;

    public VisualPanel(int width, int height) {
        super(null);
        this.width = width;
        this.height = height;

        data = createDataMatrix(100);

        setFocusable(true);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        setupGUI();
    }

    private Matrix createDataMatrix(int numberOfPoints) {
        float[][] entries = new float[numberOfPoints][3];
        Random r = new Random();
        for (float[] column : entries) {
            column[0] = r.nextInt(width) - width / 2;
            column[1] = r.nextInt(height) - height / 2;
            column[2] = 1;
        }
        return new Matrix(entries);
    }

    private void updateTransposeToOriginMatrices() {
        transposeToOrigin = Matrix.translationMatrix(-mouseX, -mouseY);
        transposeFromOrigin = Matrix.translationMatrix(mouseX, mouseY);
    }

    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, width, height);

        for (float[] point : data.values) {
            g.fillOval(((int) point[0] + width / 2) - 3, ((int) point[1] + height / 2) - 3, 6, 6);
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        mouseX = e.getX() - width / 2;
        mouseY = e.getY() - height / 2;
        float dx = mouseX - previousMouseX;
        float dy = mouseY - previousMouseY;
        previousMouseX = mouseX;
        previousMouseY = mouseY;
        data = Matrix.product(Matrix.translationMatrix(dx, dy), data);
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX() - width / 2;
        mouseY = e.getY() - height / 2;
        previousMouseX = mouseX;
        previousMouseY = mouseY;
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        updateTransposeToOriginMatrices();

        if (e.isShiftDown()) {
            if (e.getWheelRotation() > 0) {
                data = Matrix.product(Matrix.product(transposeFromOrigin, Matrix.product(Matrix.rotationMatrix(rotationAmount), transposeToOrigin)), 
                        data);
            } else {
                data = Matrix.product(Matrix.product(transposeFromOrigin, Matrix.product(Matrix.rotationMatrix(-rotationAmount), transposeToOrigin)), 
                        data);
            }
        } else {
            if (e.getWheelRotation() > 0) {
                data = Matrix.product(Matrix.product(transposeFromOrigin, Matrix.product(Matrix.scalingMatrix(1-scalingFactor), transposeToOrigin)), 
                        data);
            } else {
                data = Matrix.product(Matrix.product(transposeFromOrigin, Matrix.product(Matrix.scalingMatrix(1+scalingFactor), transposeToOrigin)), 
                        data);
            }
        }
        repaint();
    }

    private void setupGUI() {
        JLabel translationInfo = new JLabel();
        translationInfo.setFont(translationInfo.getFont().deriveFont(20.0f));
        translationInfo.setText("Drag with Left Mouse Button");
        translationInfo.setSize(translationInfo.getPreferredSize());
        translationInfo.setLocation(0,0);
        add(translationInfo);

        JLabel scalingInfo = new JLabel();
        scalingInfo.setFont(scalingInfo.getFont().deriveFont(20.0f));
        scalingInfo.setText("Scale with Mouse Wheel");
        scalingInfo.setSize(scalingInfo.getPreferredSize());
        scalingInfo.setLocation(0,20);
        add(scalingInfo);

        JLabel rotationInfo = new JLabel();
        rotationInfo.setFont(rotationInfo.getFont().deriveFont(20.0f));
        rotationInfo.setText("Rotate with SHIFT + Mouse Wheel");
        rotationInfo.setSize(rotationInfo.getPreferredSize());
        rotationInfo.setLocation(0,40);
        add(rotationInfo);

        JSlider rotationAmountSlider = new JSlider(1,10,5);
        rotationAmountSlider.setSize(rotationAmountSlider.getPreferredSize());
        rotationAmountSlider.setLocation(200,200);
        rotationAmount = rotationAmountSlider.getValue();
        rotationAmountSlider.addChangeListener(e -> rotationAmount = rotationAmountSlider.getValue());
        rotationAmountSlider.setBackground(Color.WHITE);
        add(rotationAmountSlider);

        JSlider scalingFactorSlider = new JSlider(1,20,10);
        scalingFactorSlider.setSize(scalingFactorSlider.getPreferredSize());
        scalingFactorSlider.setLocation(200,300);
        scalingFactor = (float)scalingFactorSlider.getValue()/100;
        scalingFactorSlider.addChangeListener(e -> scalingFactor = (float)scalingFactorSlider.getValue()/100);
        scalingFactorSlider.setBackground(Color.WHITE);
        add(scalingFactorSlider);
    }
}
