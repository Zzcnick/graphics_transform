import java.util.ArrayList;
import java.util.Arrays;

// ===================================================
// Matrix Class - Matrix Functions
// ===================================================

/* Structure:
 * [x  [x   ... x - x position
 *  y   y   ... y - y position
 *  z   z   ... z - z position
 *  a], a], ... a - alpha channel
 */

public class Matrix {
    protected ArrayList<double[]> matrix;
    protected int rows;
    protected int columns;
    
    // Constructors
    public Matrix() { // 4x4 Matrix
	matrix = new ArrayList<double[]>();
	for (int i = 0; i < 4; i++) {
	    matrix.add(new double[4]);
	}
	rows = 4;
	columns = 4;
    }
    public Matrix(int n, int m) {
	matrix = new ArrayList<double[]>();
	for (int i = 0; i < m; i++) {
	    matrix.add(new double[n]);
	}
	rows = n;
	columns = m;
    }
    public Matrix(double[][] darr) {
	matrix = new ArrayList<double[]>();
	for (double[] d : darr)
	    matrix.add(Arrays.copyOf(d, d.length));
	rows = darr[0].length;
	columns = darr.length;
    }
    
    // Accessors + Mutators
    public double get(int r, int c) {
	return matrix.get(c)[r];
    }
    public double set(int r, int c, double d) {
	double ret = matrix.get(c)[r];
	matrix.get(c)[r] = d;
	return ret;
    }
    public int getRows() {
	return rows;
    }
    public int getColumns() {
	return columns;
    }
    public double[] getRow(int r) {
	double[] ret = new double[columns];
	for (int i = 0; i < columns; i++) 
	    ret[i] = matrix.get(i)[r];
	return ret;
    }
    public double[] getColumn(int c) {
	double[] ret = new double[rows];
	for (int i = 0; i < rows; i++)
	    ret[i] = matrix.get(c)[i];
	return ret;
    }

    // Matrix Functions
    public boolean copy(Matrix m) {
	rows = m.getRows();
	columns = m.getColumns();
	matrix.clear();
	for (int i = 0; i < columns; i++)
	    matrix.add(m.getColumn(i));
	return true;
    }
    public boolean scale(double d) {
	for (double[] a : matrix) {
	    int l = a.length;
	    for (int i = 0; i < l; i++) 
		a[i] *= d;
	}
	return true;
    }
    public boolean add(Matrix m) {
	if (this.check_dimensions(m)) {
	    for (int i = 0; i < rows; i++)
		for (int j = 0; j < columns; j++)
		    matrix.get(j)[i] += m.get(i, j);
	    return true;
	}
	return false;
    }
    public boolean multiply(Matrix m) {
	if (this.check_multiply(m)) {
	    int r = rows;
	    int c = m.getColumns();

	    // Populating New Matrix
	    double[][] tmp = new double[c][r];
	    for (int row = 0; row < r; row++) 
		for (int column = 0; column < c; column++) 
		    tmp[column][row] = dot(this.getRow(row),
					   m.getColumn(column));
	    
	    // Trimming Stored Matrix
	    int dc = c - columns;
	    while (dc != 0) {
		if (dc < 0) {
		    matrix.remove(columns - 1);
		    dc++;
		    columns--;
		} else {
		    matrix.add(new double[r]);
		    dc--;
		    columns++;
		}
	    }

	    // Transferring Values
	    for (int row = 0; row < r; row++)
		for (int column = 0; column < c; column++) 
		    matrix.get(column)[row] = tmp[column][row];

	    return true;
	}
	return false;
    }

    // Helper Functions
    public boolean check_dimensions(Matrix m) {
	return (columns == m.getColumns() &&
		rows == m.getRows());
    }
    public boolean check_multiply(Matrix m) {
	return (this.columns == m.getRows());
    }
    public double dot(double[] u, double[] v) {
	double sum = 0;
	for (int i = 0; i < u.length; i++) 
	    sum += u[i] * v[i];
	return sum;
    }
    public static Matrix identity(int n) {
	Matrix m = new Matrix(n, n);
	for (int i = 0; i < n; i++) {
	    m.set(i, i, 1);
	}
	return m;
    }

    // ToString Utility
    public String toString() {
	String retStr = "|";
	for (int i = 0; i < rows; i++) {
	    double[] tmp = this.getRow(i);
	    for (int j = 0; j < tmp.length; j++) {
		retStr += " " + tmp[j] + "\t";
	    }
	    retStr += "|\n|";
	}
	return retStr.substring(0, retStr.length() - 3) + "|";
    }

    // Testing
    public static void main(String[] args) {
	Matrix pm = new Matrix();
	System.out.println("Creating new Matrix...\n" + pm + "\n");
	
	Matrix pm2 = new Matrix(new double[][]
	    { {1, 2, 3, 4},
	      {5, 6, 7, 8}, 
	      {9, 10, 11, 12},
	      {13, 14, 15, 16} } );
	System.out.println("Creating second Matrix...\n" + pm2 + "\n");

	pm.add(pm2);
	System.out.println("Adding Matrix 2 to Matrix 1...\n" + pm + "\n");

	pm.multiply(pm2);
	System.out.println("Taking the product of Matrix 1 and Matrix 2...\n" + pm + "\n");

	Matrix i = Matrix.identity(4);
	System.out.println("Creating 4 by 4 identity Matrix...\n" + i + "\n");

	pm.multiply(i);
	System.out.println("Multiplying Matrix 1 by identity Matrix...\n" + pm + "\n");

	Matrix pm3 = new Matrix(new double[][]
	    { {1, 2, 3, 4}, {5, 6, 7, 8} } );
	System.out.println("Creating third Matrix, 4 by 2 this time...\n" + pm3 + "\n");

	pm.copy(pm2);
	System.out.println("Copying contents of Matrix 2 to Matrix 1...\n" + pm + "\n");

	pm.multiply(pm3);
	System.out.println("Multipling Matrix 1 by Matrix 3...\n" + pm + "\n");

	pm.scale(2.5);
	System.out.println("Scaling contents of Matrix 1 by 2.5...\n" + pm + "\n");

	System.out.println("Ensuring Matrix 2 is preserved...\n" + pm2 + "\n");
    }
}
