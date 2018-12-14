import java.util.InputMismatchException;

/**
 * This class is a representation of a vector in any number of dimensions.
 */
public class Vector {
    protected final float[] entries;
    protected final int length;

    /**
     * Constructs an empty vector of specific length
     *
     * @param length number of entries in the vector
     */
    public Vector(int length) {
        if (length >= 0) {
            entries = new float[length];
            this.length = length;
        }
        else
            throw new InputMismatchException("Length of a vector has to be a non-negative number.");
    }

    /**
     * Constructs a vector from a float array
     *
     * @param values array of values
     */
    public Vector(float... values) {
        length = values.length;
        entries = new float[length];

        for (int i = 0; i < length; i++) {
            entries[i] = values[i];
        }
    }

    /**
     * Copy constructor
     * @param v vector to copy from
     */
    public Vector(Vector v) {
        length = v.length;
        entries = new float[length];

        for (int i = 0; i < length; i++) {
            entries[i] = v.entries[i];
        }
    }

    /**
     * Componentwise vector addition
     *
     * @param that vector to be added
     */
    public void add(Vector that) {
        if (this.length == that.length) {
            for (int i = 0; i < length; i++) {
                this.entries[i] = this.entries[i] + that.entries[i];
            }
        } else
            throw new IllegalArgumentException("Dimension mismatch. Attempted to add vectors of different lengths:\n"
                    +this.length+" and "+that.length);
    }

    /**
     * Multiplies the vector by a nonzero scalar.
     *
     * @param k the scalar to be multiplied by
     * @return this vector scaled by k
     */
    public Vector scale(float k) {
        Vector result = new Vector(length);
        for (int i = 0; i < length; i++) {
            result.entries[i] = this.entries[i] * k;
        }
        return result;
    }

    /**
     * Vector dot product
     * @param that second vector in the product
     * @return dot product of this vector and the argument
     */
    public float dot(Vector that) {
        if (this.length == that.length) {
            float result = 0;
            for (int i = 0; i < length; i++) {
                result += this.entries[i] * that.entries[i];
            }
            return result;
        } else
            throw new IllegalArgumentException("Dimension mismatch. Attempted perform dot product on vectors of different lengths:\n"
                    +this.length+" and "+that.length);
    }

    /**
     * Vector dot product
     * @param a first vector
     * @param b second vector
     * @return dot product
     */
    public static float dot(Vector a, Vector b) {
        if (a.length == b.length) {
            float result = 0;
            for (int i = 0; i < a.length; i++) {
                result += a.entries[i] * b.entries[i];
            }
            return result;
        } else
            throw new IllegalArgumentException("Dimension mismatch. Attempted perform dot product on vectors of different lengths:\n"
                    +a.length+" and "+b.length);
    }

    public float getEntry(int index) {
        return entries[index];
    }

    /**
     * @return entries of this vector in an array form
     */
    public float[] toFloatArray() {
        return entries;
    }

    /**
     * Prints the vector in the terminal as a column of numbers.
     * Used primarily for testing.
     *
     * @return text version of a vector
     */
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < entries.length; i++) {
            result.append("").append(entries[i]).append("\n");
        }
        return result.toString();
    }
}
