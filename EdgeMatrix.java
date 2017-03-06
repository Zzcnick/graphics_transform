import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

// ===================================================
// EdgeMatrix Class - Matrix of Edges
// ===================================================

public class EdgeMatrix extends Matrix {
    private int size;
    private ArrayList<Pixel> colors;
    private Matrix transform;

    // Constructors
    public EdgeMatrix() {
	matrix = new ArrayList<double[]>();
	colors = new ArrayList<Pixel>();
	transform = Matrix.identity(4); // 4x4
	rows = 4;
	columns = 0;
	size = 0;
    }

    // Transformations
    public Matrix scale(double sx, double sy, double sz) {
	Matrix left = Matrix.identity(4);
	left.set(0,0,sx);
	left.set(1,1,sy);
	left.set(2,2,sz);
	transform = left.multiply(transform);
	return transform;
    }

    // Methods
    public boolean add_point(double[] p) {
	columns++;
	return matrix.add(p);
    }
    public boolean add_point(double x, double y, double z, double a) {
	return add_point(new double[]{x, y, z, a});
    }
    public boolean add_point(double x, double y, double z) {
	return add_point(new double[]{x, y, z, 1.0});
    }
    public boolean add_point(double x, double y) {
	return add_point(new double[]{x, y, 0, 1.0});
    }

    public boolean add_edge(double[] p1, double[] p2) {
	return add_edge(p1, p2, new Pixel(0,0,0));
    }
    public boolean add_edge(double[] p1, double[] p2, Pixel p) {
	add_point(p1);
	add_point(p2);
	size++;
	colors.add(p);
	return true;
    }
    public boolean add_edge(double x1, double y1, double x2, double y2) {
	return add_edge(x1, y1, x2, y2, new Pixel(0,0,0));
    }
    public boolean add_edge(double x1, double y1, double x2, double y2, Pixel p) {
	add_point(x1, y1);
	add_point(x2, y2);
	size++;
	colors.add(p);
	return true;
    }

    // Iterators
    public Iterator<double[]> iterator() {
	return matrix.iterator();
    }
    public Iterator<Pixel> colorIterator() {
	return colors.iterator();
    }

    // toString Utilities
    public String displayPoints() {
	return toString();
    }
    public String displayTransform() {
	return transform.toString();
    }
}

