import java.util.InputMismatchException;
import java.util.Random;


/**
 * This class represents a matrix whose
 * entries are stored in column-major order
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
        if (m > 0 && n > 0) {
            values = new float[n][m];
            this.m = m;
            this.n = n;
        } else {
            throw new InputMismatchException("Matrix has to have a positive number of rows and columns. " +
                    "Expected number of rows: >0  Provided number of rows: " + m + ".  Expected number of columns: >0  " +
                    "Provided number of columns: " + n);
        }
    }

    /**
     * Constructor for a matrix from a list of values
     *
     * @param m                           number of rows
     * @param n                           number of columns
     * @param valuesPassedInRowMajorOrder whether values are listed in row-major order
     * @param entries                     comma separated values for the matrix
     */
    public Matrix(int m, int n, boolean valuesPassedInRowMajorOrder, float... entries) {
        if (m > 0 && n > 0) {
            if (m * n == entries.length) {
                values = new float[n][m];
                this.m = m;
                this.n = n;
                if (valuesPassedInRowMajorOrder) {
                    int i = 0;
                    for (int row = 0; row < m; row++) {
                        for (int col = 0; col < n; col++) {
                            values[col][row] = entries[i];
                            i++;
                        }
                    }
                } else {
                    int i = 0;
                    for (int col = 0; col < n; col++) {
                        for (int row = 0; row < m; row++) {
                            values[col][row] = entries[i];
                            i++;
                        }
                    }
                }
            } else {
                throw new InputMismatchException("Number of entries provided doesn't match the expected number based on size. " +
                        "Expected: " + m * n + "  Provided: " + entries.length);
            }
        } else {
            throw new InputMismatchException("Matrix has to have a positive number of rows and columns. " +
                    "Expected number of rows: >0  Provided number of rows: " + m + ".  Expected number of columns: >0  " +
                    "Provided number of columns: " + n);
        }
    }

    /**
     * Constructor for a matrix from a list of column arrays
     *
     * @param columns columns in array form, separated by commas, that will become columns of the matrix
     */
    public Matrix(float[]... columns) {
        m = columns[0].length;
        n = columns.length;

        values = new float[n][m];
        float[] column;
        for (int col = 0; col < n; col++) {
            if (((column = columns[col]).length) == m) {
                values[col] = column;
            } else {
                throw new InputMismatchException("Columns provided to the constructor are not of uniform length.");
            }
        }
    }

    /**
     * Constructor for a matrix from a list of row arrays or column arrays
     *
     * @param arraysPassedAreRows whether the arrays are rows instead of columns.
     *                            If <code>false</code>, will construct a matrix
     *                            from column arrays
     * @param rows                rows in array form, separated by commas, that will become rows of the matrix
     */
    public Matrix(boolean arraysPassedAreRows, float[]... rows) {
        if (arraysPassedAreRows) {
            m = rows.length;
            n = rows[0].length;

            values = new float[n][m];
            float[] r;
            for (int row = 0; row < m; row++) {
                if (((r = rows[row]).length) == n) {
                    for (int col = 0; col < n; col++) {
                        values[col][row] = r[col];
                    }
                } else {
                    throw new InputMismatchException("Rows provided to the constructor are not of uniform length.");
                }
            }
        } else {
            m = rows[0].length; //number of rows
            n = rows.length;    // number of rows

            values = new float[n][m];
            float[] column;
            for (int col = 0; col < n; col++) {
                if (((column = rows[col]).length) == m) {
                    values[col] = column;
                } else {
                    throw new InputMismatchException("Columns provided to the constructor are not of uniform length.");
                }
            }
        }
    }

    /**
     * Constructor for a matrix, based on an array of values
     *
     * @param columns columns in vector form, separated by commas, that will become columns of the matrix
     */
    public Matrix(Vector... columns) {
        m = columns[0].length; //number of rows
        n = columns.length;    // number of columns

        values = new float[n][m];
        for (int i = 0; i < n; i++) {
            if (columns[i].length == m) {
                values[i] = columns[i].entries;
            } else {
                throw new InputMismatchException("Vectors provided to the constructor are not of uniform length.");
            }
        }
    }

    /**
     * Creates an m by n matrix, filled with random values between 0 and 1
     *
     * @param m number of rows
     * @param n number of columns
     * @return a random m by n matrix
     */
    public static Matrix randomMatrix(int m, int n) {
        Random random = new Random();
        Matrix result = new Matrix(m, n);
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                result.values[col][row] = random.nextFloat();
            }
        }
        return result;
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
     * Matrix addition
     *
     * @param a first summand
     * @param b second summand
     * @return componentwise sum of the two parameters
     */
    public static Matrix add(Matrix a, Matrix b) {
        if ((a.m == b.m) && (a.n == b.n)) {
            Matrix result = new Matrix(a.m, a.n);
            result.add(a);
            result.add(b);
            return result;
        } else {
            throw new InputMismatchException("Dimension mismatch. Attempted to add matrices of different sizes.");
        }
    }

    /**
     * Adds a matrix to this matrix
     *
     * @param a matrix to be added
     */
    public void add(Matrix a) {
        if ((a.m == m) && (a.n == n)) {
            for (int col = 0; col < n; col++) {
                for (int row = 0; row < m; row++) {
                    values[col][row] += a.values[col][row];
                }
            }
        } else {
            throw new InputMismatchException("Dimension mismatch. Attempted to add matrices of different sizes.");
        }

    }

    /**
     * Matrix subtraction
     *
     * @param a minuend
     * @param b subtrahend
     * @return componentwise difference of the two parameters
     */
    public static Matrix subtract(Matrix a, Matrix b) {
        if ((a.m == b.m) && (a.n == b.n)) {
            Matrix result = new Matrix(a.m, a.n);
            result.add(a);
            result.subtract(b);
            return result;
        } else {
            throw new InputMismatchException("Dimension mismatch. Attempted to subtract matrices of different sizes.");
        }
    }

    /**
     * Subtracts a matrix from this matrix
     *
     * @param a matrix to be subtracted
     */
    public void subtract(Matrix a) {
        if ((a.m == m) && (a.n == n)) {
            for (int col = 0; col < n; col++) {
                for (int row = 0; row < m; row++) {
                    values[col][row] -= a.values[col][row];
                }
            }
        } else {
            throw new InputMismatchException("Dimension mismatch. Attempted to subtract matrices of different sizes.");
        }

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
        if (a.n == b.m) {
            Matrix result = new Matrix(a.m, b.n);
            //going through columns of b
            for (int i = 0; i < b.n; i++) {
                //and multiplying each one by A
                result.setColumn(i, a.times(b.getColumn(i)));
            }
            return result;
        } else {
            throw new InputMismatchException("Dimension mismatch. Attempted to multiply matrices of improper size.");
        }
    }

    /**
     * Product of any number of matrices implemented as
     * ABCD...WXYZ = ABCD...WX(YZ) = ABCD...W(X(YZ))
     *
     * @param matrices matrices to be multiplied, separated by commas
     * @return product ABC...Z
     */
    public static Matrix product(Matrix... matrices) {
        // multiply the last two matrices first
        Matrix result = Matrix.product(matrices[matrices.length - 2], matrices[matrices.length - 1]);
        // multiply the result of that product by the next(preceding) matrix
        for (int i = matrices.length - 3; i >= 0; i--) {
            result = product(matrices[i], result);
        }
        return result;
    }

    /**
     * Computes the product of this matrix and a vector
     * Ax = b
     *
     * @param x vector to be multiplied by
     * @return this matrix times x
     */
    public Vector times(Vector x) {
        if (x.length == n) {
            Vector result = new Vector(m);
            //going through entries of x
            for (int i = 0; i < x.length; i++) {
                //multiplying each entry by corresponding column of the matrix
                //and adding the results into a single vector
                result.add(this.getColumn(i).scale(x.getEntry(i)));
            }
            return result;
        } else {
            throw new InputMismatchException("Dimension mismatch. Attempted to multiply matrix by a vector of improper length.");
        }
    }

    /**
     * Interface for applying a certain function to
     * all of the entries in the matrix.
     * <p>
     * Implement the <code>function</code> method to perform the
     * desired operation.
     */
    public interface Vectorizer {
        float function(float value);
    }

    /**
     * Applies a certain operation to all the elements in the matrix.
     *
     * @param vectorizer implemetation of Vectorizer interface that
     *                   can be easily made with a lambda expression:
     *                   <code>vectorize(value -> operation(value))</code>
     */
    public void vectorize(Vectorizer vectorizer) {
        for (int col = 0; col < n; col++) {
            for (int row = 0; row < m; row++) {
                values[col][row] = vectorizer.function(values[col][row]);
            }
        }
    }

    public Vector getColumn(int index) {
        return new Vector(values[index]);
    }

    public void setColumn(int index, Vector c) {
        values[index] = c.entries;
    }

    public void setColumn(int index, float[] column) {
        values[index] = column;
    }

    /**
     * Creates a new matrix that is the copy of this matrix plus
     * the column specified in the parameter.
     *
     * @param newColumn column to be added, in array format
     * @return matrix with an extra column
     */
    public Matrix addColumn(float[] newColumn) {
        Matrix result = new Matrix(m, n + 1);

        // copies all existing entries
        for (int col = 0; col < n; col++) {
            result.setColumn(col, this.getColumn(col));
        }

        // adds the last (new) column
        result.setColumn(n, newColumn);

        return result;
    }

    public Matrix addColumn(Vector newColumn) {
        return addColumn(newColumn.toFloatArray());
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
