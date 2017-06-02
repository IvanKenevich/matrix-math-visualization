package src;

/**
 * This class represents a matrix whose
 * entries are stored in column-major order
 *
 * Created by Ivan on 5/4/2017.
 */
public class Matrix {
    public final int m;
    public final int n;
    public final float[][] values;

    /**
     * Constructor for an empty generic matrix.
     *
     * @param m number of rows
     * @param n number of columns
     */
    public Matrix(int m, int n) {
        values = new float[n][m];
        this.m = m;
        this.n = n;
    }

    /**
     * Constructor for a matrix, based on an array of values
     *
     * @param values array of values that will become entries of the matrix
     */
    public Matrix(float[][] values) {
        this.values = values;

        m = values[0].length; //number of rows
        n = values.length;    // number of columns
    }

    /**
     * Creates a 2D homogeneous translation matrix
     *
     * @param dx translation in x
     * @param dy translation in y
     * @return 3x3 translation matrix
     */
    public static Matrix translationMatrix(float dx, float dy) {
        return new Matrix(new float[][]{{1, 0, 0},
                {0, 1, 0},
                {dx, dy, 1}});
    }

    /**
     * Creates a 2D homogeneous scaling matrix
     *
     * @param k scalar to scale by
     * @return 3x3 scaling matrix
     */
    public static Matrix scalingMatrix(float k) {
        return new Matrix(new float[][]{{k, 0, 0},
                {0, k, 0},
                {0, 0, 1}});
    }

    /**
     * Creates a 2D homogeneous rotation matrix.
     *
     * @param angle the angle to rotate by
     * @return 3x3 rotation matrix
     */
    public static Matrix rotationMatrix(double angle) {
        angle = Math.toRadians(angle);
        return new Matrix(new float[][]{{(float) Math.cos(angle), (float) Math.sin(angle), 0},
                                        {-(float) Math.sin(angle), (float) Math.cos(angle), 0},
                                        {0, 0, 1}});
    }

    /**
     * Product of two matrices implemented as
     * AB = [ Ab1  Ab2  ...  Abn ]
     *
     * @param a multiplier
     * @param b multiplicand
     * @return product ab
     */
    public static Matrix product(Matrix a, Matrix b) {
        Matrix result = new Matrix(a.m, b.n);
        if (a.n == b.m) {
            //going through columns of b
            for (int i = 0; i < b.n; i++) {
                //and multiplying each one by A
                result.setColumn(i, a.times(b.getColumn(i)));
            }
            return result;
        } else {
            throw new ArithmeticException("Dimension mismatch. Attempted to multiply matrices of improper size.");
        }
    }

    /**
     * Computes the product of this matrix and a vector
     * Ax = b
     *
     * @param x vector to be multiplied by
     * @return this matrix times x
     */
    public Vector times(Vector x) {
        Vector result = new Vector(x.length);
        if (x.length == n) {
            //going through entries of x
            for (int i = 0; i < x.length; i++) {
                //multiplying each entry by corresponding column of the matrix
                //and adding the results into a single vector
                result = result.add(this.getColumn(i).scale(x.getEntry(i)));
            }
            return result;
        } else {
            throw new ArithmeticException("Dimension mismatch. Attempted to multiply matrix by a vector of improper length.");
        }
    }

    public Vector getColumn(int index) {
        return new Vector(values[index]);
    }

    public void setColumn(int index, Vector c) {
        values[index] = c.entries;
    }

    public int[] getRow(int index) {
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = (int) values[i][index];
        }
        return result;
    }

    public float getEntry(int row, int column) {
        return values[column][row];
    }

    /**
     * Prints the matrix in a terminal as a block of numbers.
     * Used primarily for testing.
     *
     * @return text version of the matrix
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int row = 0; row < values[0].length; row++) {
            for (int col = 0; col < values.length; col++) {
                result.append(values[col][row]).append(" ");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
