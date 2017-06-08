package src;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * This class handles the display of various matrix operations and user input.
 */
public class VisualPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private final int width;
    private final int height;

    private final float SCALING_FACTOR = 0.1f, ROTATION_AMOUNT = 5;

    private float mouseX, mouseY, previousMouseX, previousMouseY;

    private Matrix data, transposeToOrigin, transposeFromOrigin;

    public VisualPanel(int width, int height) {
        super(null);
        this.width = width;
        this.height = height;

        data = createDataMatrix(0);

        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        setupGUI();
    }

    private Matrix createDataMatrix(int numberOfPoints) {
        Matrix result = new Matrix(3, numberOfPoints);
        Random r = new Random();
        for (int col = 0; col < numberOfPoints; col++) {
            result.setColumn(col, new float[]{r.nextInt(width) - width / 2, r.nextInt(height) - height / 2, 1});
        }
        return result;
    }

    private void updateTransposeToOriginMatrices() {
        transposeToOrigin = Matrix.translationMatrix(-mouseX, -mouseY);
        transposeFromOrigin = Matrix.translationMatrix(mouseX, mouseY);
    }

    public void paintComponent(Graphics g) {
        g.clearRect(0, 0, width, height);

        for (float[] column : data.values) {
            g.fillOval(((int) column[0] + width / 2) - 6, ((int) column[1] + height / 2) - 6, 12, 12);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        data = data.addColumn(new float[]{e.getX() - width/2, e.getY() - height/2, 1});
        repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

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
                data = Matrix.product(Matrix.product(transposeFromOrigin, Matrix.product(Matrix.rotationMatrix(ROTATION_AMOUNT), transposeToOrigin)),
                        data);
            } else {
                data = Matrix.product(Matrix.product(transposeFromOrigin, Matrix.product(Matrix.rotationMatrix(-ROTATION_AMOUNT), transposeToOrigin)),
                        data);
            }
        } else {
            if (e.getWheelRotation() > 0) {
                data = Matrix.product(Matrix.product(transposeFromOrigin, Matrix.product(Matrix.scalingMatrix(1 - SCALING_FACTOR), transposeToOrigin)),
                        data);
            } else {
                data = Matrix.product(Matrix.product(transposeFromOrigin, Matrix.product(Matrix.scalingMatrix(1 + SCALING_FACTOR), transposeToOrigin)),
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
        translationInfo.setLocation(0, 0);
        add(translationInfo);

        JLabel scalingInfo = new JLabel();
        scalingInfo.setFont(scalingInfo.getFont().deriveFont(20.0f));
        scalingInfo.setText("Scale with Mouse Wheel");
        scalingInfo.setSize(scalingInfo.getPreferredSize());
        scalingInfo.setLocation(0, 25);
        add(scalingInfo);

        JLabel rotationInfo = new JLabel();
        rotationInfo.setFont(rotationInfo.getFont().deriveFont(20.0f));
        rotationInfo.setText("Rotate with SHIFT + Mouse Wheel");
        rotationInfo.setSize(rotationInfo.getPreferredSize());
        rotationInfo.setLocation(0, 50);
        add(rotationInfo);
    }
}
