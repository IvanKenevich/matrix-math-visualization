/**
 * Created by Ivan on 5/10/2017.
 */
public class Vector {
    public final float[] entries;
    public final int length;

    /**
     * Constructs an empty vector of specific length
     *
     * @param length number of entries in the vector
     */
    public Vector(int length) {
        entries = new float[length];
        this.length = length;
    }

    /**
     * Constructs a vector from a float array
     *
     * @param values array of values
     */
    public Vector(float[] values) {
        entries = values;
        length = values.length;
    }

    /**
     * Componentwise vector addition
     *
     * @param x vector to be added
     * @return this vector + x
     */
    public Vector add(Vector x) {
        Vector result = new Vector(length);
        if (this.length == x.length) {
            for (int i = 0; i < length; i++) {
                result.entries[i] = this.entries[i] + x.entries[i];
            }
        } else
            throw new ArithmeticException("Dimension mismatch. Attempted to add vectors of different lengths.");
        return result;
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
