import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * This class displays various matrix operations and handles user input.
 */
public class VisualPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private final int width;
    private final int height;

    private final float SCALING_FACTOR = 0.1f, ROTATION_AMOUNT = 5;
    private boolean scalePointsWithZoom;

    private float mouseX, mouseY, previousMouseX, previousMouseY, pointRadius;

    private Matrix data, transposeToOrigin, transposeFromOrigin;

    public VisualPanel(int width, int height) {
        super(null);
        this.width = width;
        this.height = height;
        pointRadius = 3;
        scalePointsWithZoom = false;

        data = createRandomDataMatrix(0);

        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);

        setupGUI();
    }

    private Matrix createRandomDataMatrix(int numberOfPoints) {
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
        // clears the screen
        g.clearRect(0, 0, width, height);

        // for every column (point) in the data matrix
        for (float[] column : data.values) {
            g.fillOval((int) ((column[0] + width / 2) - pointRadius), (int) ((column[1] + height / 2) - pointRadius),
                    (int) (2 * pointRadius), (int) (2 * pointRadius));
        }
    }

    public void mouseClicked(MouseEvent e) {
        // adds a new point where the mouse was clicked
        // does not interfere with dragging the points
        data = data.addColumn(new float[]{e.getX() - width / 2, e.getY() - height / 2, 1});
        repaint();
    }

    public void mousePressed(MouseEvent e) {

    }

    public void mouseReleased(MouseEvent e) {

    }

    public void mouseEntered(MouseEvent e) {

    }

    public void mouseExited(MouseEvent e) {

    }

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

    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX() - width / 2;
        mouseY = e.getY() - height / 2;
        previousMouseX = mouseX;
        previousMouseY = mouseY;
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        // matrices that transport the figure to the origin
        // only need to be updated if zoom or rotation is to be performed
        updateTransposeToOriginMatrices();

        if (e.isShiftDown()) {
            // if wheel is scrolled down
            if (e.getWheelRotation() > 0) {
                // rotate clockwise by performing
                // (From Origin)*(Rotate)*(To Origin)*(data)
                data = Matrix.product(Matrix.product(transposeFromOrigin, Matrix.product(Matrix.rotationMatrix(ROTATION_AMOUNT), transposeToOrigin)),
                        data);
            } else {
                // rotate counterclockwise by performing
                // (From Origin)*(Rotate)*(To Origin)*(data)
                data = Matrix.product(Matrix.product(transposeFromOrigin, Matrix.product(Matrix.rotationMatrix(-ROTATION_AMOUNT), transposeToOrigin)),
                        data);
            }
        } else {
            if (e.getWheelRotation() > 0) {
                // scale down by performing
                // (From Origin)*(Scale)*(To Origin)*(data)
                data = Matrix.product(Matrix.product(transposeFromOrigin, Matrix.product(Matrix.scalingMatrix(1 - SCALING_FACTOR), transposeToOrigin)),
                        data);
                // scale the radius of the points by the same factor, if necessary
                if (scalePointsWithZoom)
                    pointRadius *= 1 - SCALING_FACTOR;
                else
                    pointRadius = 3;
            } else {
                // scale up by performing
                // (From Origin)*(Scale)*(To Origin)*(data)
                data = Matrix.product(Matrix.product(transposeFromOrigin, Matrix.product(Matrix.scalingMatrix(1 + SCALING_FACTOR), transposeToOrigin)),
                        data);
                // scale the radius of the points by the same factor, if necessary
                if (scalePointsWithZoom)
                    pointRadius *= 1 + SCALING_FACTOR;
                else
                    pointRadius = 3;
            }
        }
        repaint();
    }

    /**
     * Creates and arranges text and buttons.
     */
    private void setupGUI() {
        JLabel addingPointsInfo = new JLabel();
        addingPointsInfo.setFont(addingPointsInfo.getFont().deriveFont(20.0f));
        addingPointsInfo.setText("Add new points by clicking LMB");
        addingPointsInfo.setSize(addingPointsInfo.getPreferredSize());
        addingPointsInfo.setLocation(0, 0);
        add(addingPointsInfo);
        
        JLabel translationInfo = new JLabel();
        translationInfo.setFont(translationInfo.getFont().deriveFont(20.0f));
        translationInfo.setText("Drag by holding LMB");
        translationInfo.setSize(translationInfo.getPreferredSize());
        translationInfo.setLocation(0, addingPointsInfo.getY() + addingPointsInfo.getHeight());
        add(translationInfo);

        JLabel scalingInfo = new JLabel();
        scalingInfo.setFont(scalingInfo.getFont().deriveFont(20.0f));
        scalingInfo.setText("Scale with Mouse Wheel");
        scalingInfo.setSize(scalingInfo.getPreferredSize());
        scalingInfo.setLocation(0, translationInfo.getY() + translationInfo.getHeight());
        add(scalingInfo);

        JLabel rotationInfo = new JLabel();
        rotationInfo.setFont(rotationInfo.getFont().deriveFont(20.0f));
        rotationInfo.setText("Rotate with SHIFT + Mouse Wheel");
        rotationInfo.setSize(rotationInfo.getPreferredSize());
        rotationInfo.setLocation(0, scalingInfo.getY() + scalingInfo.getHeight());
        add(rotationInfo);

        JButton clearPointsButton = new JButton("Clear Points");
        clearPointsButton.setSize(clearPointsButton.getPreferredSize());
        clearPointsButton.setLocation(0, rotationInfo.getY() + rotationInfo.getHeight());
        clearPointsButton.setFocusable(false);
        clearPointsButton.addActionListener(e -> {
            data = createRandomDataMatrix(0);
            repaint();
        });
        add(clearPointsButton);

        JButton randomPointsButton = new JButton("Fill With Random Points");
        randomPointsButton.setSize(randomPointsButton.getPreferredSize());
        randomPointsButton.setLocation(0, clearPointsButton.getY() + clearPointsButton.getHeight() + 5);
        randomPointsButton.setFocusable(false);
        randomPointsButton.addActionListener(e -> {
            data = createRandomDataMatrix(100);
            repaint();
        });
        add(randomPointsButton);

        JCheckBox scalePointsWithZoomBox = new JCheckBox("Scale points with zoom");
        scalePointsWithZoomBox.setSize(scalePointsWithZoomBox.getPreferredSize());
        scalePointsWithZoomBox.setLocation(0, randomPointsButton.getY() + randomPointsButton.getHeight());
        scalePointsWithZoomBox.setBackground(Color.WHITE);
        scalePointsWithZoomBox.setFocusable(false);
        scalePointsWithZoomBox.addActionListener(e -> {
            scalePointsWithZoom = !scalePointsWithZoom;
            pointRadius = 3;
            repaint();
        });
        add(scalePointsWithZoomBox);
    }
}