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
    public final float epsilon = 0.00001f;


    // ================== CONSTRUCTORS BEGIN ==================
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
     * Makes a matrix from another matrix.
     * (Copy constructor-ish)
     * @param mat matrix to be copied from
     */
    public Matrix(Matrix mat) {
        m = mat.m; n = mat.n;
        values = new float[n][m];
        for (int col = 0; col < n; col++) {
            if (m >= 0) System.arraycopy(mat.values[col], 0, values[col], 0, m);
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

    // ================== CONSTRUCTORS END ==================



    // ================== SPECIAL MATRICES BEGIN ==================

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

    // ================== SPECIAL MATRICES END ==================


    // ================== ARITHMETIC BEGIN ==================

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

    // ================== ARITHMETIC END ==================


    // ================== RREF BEGIN ==================

    public void to_echelon() {
        int h = 0; // row
        int k = 0; // col
        int i_max; // row
        float f;
        while (h < m && k < n) {
            i_max = partial_pivot(h,m-1,k);

            if (Math.abs(values[k][i_max]) < epsilon ) {
                ++k;
            }
            else {
                swap_rows(h,i_max);

                for (int i = h + 1; i < m; i++) {
                    f = values[k][i] / values[k][h];
                    values[k][i] = 0;
                    for (int j = k + 1; j < n; j++) {
                        values[j][i] = values[j][i] - values[j][h] * f;
                    }
                }
                ++h; ++k;
            }
        }
    }

    /**
     * Row reduces the matrix, replacing the old values.
     *
     * First, brings the matrix to an echelon form.
     * Then, beginning with the rightmost pivot and working upward and to the left,
     * creates zeroes above each pivot. If a pivot is not 1, makes it 1 by a scaling operation.
     */
    public void reduce() {
        to_echelon();

        int h = m - 1; // row
        int k;         // col
        float f;
        while (h >= 0) {
            k = rightmost_pivot_column(h);
            // maybe make the above return -1 if no non-zeroes found?

            if (Math.abs(values[k][h]) < epsilon) {
                --h;
            }
            else {
                if (h > 0) { // if this is not the first row
                    // create zeroes above this pivot
                    for (int i = h - 1; i >= 0; i--) {
                        f = values[k][i] / values[k][h];
                        values[k][i] = 0;
                        for (int j = k + 1; j < n; j++) {
                            values[j][i] = values[j][i] - values[j][h] * f;
                        }
                    }
                }

                // if a pivot is not 1, make it 1 by a scaling operation
                if (Math.abs(1 - values[k][h]) > epsilon) {
                    f = values[k][h];
                    values[k][h] = 1;
                    for (int j = k+1; j < n; j++) {
                        values[j][h]/=f;
                    }
                }

                --h;
            }
        }
    }

    /**
     * Create the reduced echelon form of the argument matrix
     * @param m original matrix
     * @return row reduced matrix
     */
    public static Matrix rref(Matrix m) {
        Matrix result = new Matrix(m);
        result.reduce();
        return result;
    }

    // ================== RREF END ==================


    // ================== VECTORIZER BEGIN ==================

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

    // ================== VECTORIZER END ==================


    // ================== GET/SET BEGIN ==================

    public Vector getColumn(int index) {
        return new Vector(values[index]);
    }

    public void setColumn(int index, Vector c) {
        values[index] = c.entries;
    }

    public void setColumn(int index, float[] column) {
        values[index] = column;
    }


    public float[] getRow(int index) {
        float[] result = new float[n];
        for (int i = 0; i < n; i++) {
            result[i] = values[i][index];
        }
        return result;
    }

    public void setRow(int index, float [] row) {
        for (int i = 0; i < n; i++) {
            values[i][index] = row[i];
        }
    }

    public float getEntry(int row, int column) {
        return values[column][row];
    }

    /**
     * Finds the largest pivot position for the given column.
     * Largest is used to minimize roundoff errors, in a process called partial pivoting.
     * @param start_row row to start looking for the pivot
     * @param end_row row to end looking for the pivot
     * @param column column in which the pivot is sought
     * @return the index of the pivot row
     */
    private int partial_pivot(int start_row, int end_row, int column) {
        int max = start_row;
        for (int i = start_row; i <= end_row; i++) {
            if (Math.abs(values[column][i]) > Math.abs(values[column][max])) { max = i; }
        }
        return max;
    }

    /**
     * Swaps two rows
     * @param a index 1
     * @param b index 2
     */
    private void swap_rows(int a, int b) {
        if (a < 0 || b < 0) { throw new IllegalArgumentException("One of the rows to be swapped has a negative index"); }
        if (a >= m || b >= m) { throw new IllegalArgumentException("One of the rows to be swapped has a too high index"); }

        float [] temp = getRow(b);

        setRow(b,getRow(a));
        setRow(a,temp);
    }

    /**
     * Finds the rightmost pivot column in a given row
     * @param row row to search for the pivot in
     * @return index of the pivot column
     */
    private int rightmost_pivot_column(int row) {
        int col = 0;
        while (col < n && Math.abs(values[col][row]) < epsilon) ++col;
        return col;
    }

    // ================== GET/SET END ==================


    // ================== GROW BEGIN ==================

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

    // ================== GROW END ==================


    // ================== TO STRING BEGIN ==================

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
                result.append(String.format("% 8.2f ",values[col][row]));
            }
            result.append("\n");
        }
        return result.toString();
    }

    // ================== TO STRING END ==================
}
