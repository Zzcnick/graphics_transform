import java.io.*;
import java.util.*;

// ===================================================
// Canvas Class - Drawing and Saving
// ===================================================
public class Canvas {
    private Pixel[][] canvas; // Drawing Canvas
    private Matrix edges; // Lines
    private Matrix transform; // Transformation Matrix
    private int x, y; // Dimensions
    
    // Constructors
    public Canvas() {
	canvas = new Pixel[500][500];
	x = 500;
	y = 500;
	fill(255, 255, 255);
	edges = new Matrix();
	transform = Matrix.identity(4);
    }
    public Canvas(int x, int y) {
	canvas = new Pixel[y][x];
	this.x = x;
	this.y = y;
	fill(255, 255, 255);
	edges = new Matrix();
	transform = Matrix.identity(4);
    }
    public Canvas(int x, int y, Pixel p) {
	this(x, y);
	fill(p);
    }
    public Canvas(int x, int y, int R, int G, int B) {
	this(x, y);
	fill(R, G, B);
    }

    // Accessors + Mutators
    public int[] getXY() {
	return new int[]{x, y};
    }
    public int getX() {
	return x;
    }
    public int getY() {
	return y;
    }
    public Matrix getEdges() {
	return edges;
    }
    public Matrix getTransform() {
	return transform;
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
    public Matrix scale(double s) {
	return scale(s, s, s);
    }
    public Matrix translate(double dx, double dy, double dz) {
	Matrix left = Matrix.identity(4);
	left.set(0,3,dx);
	left.set(1,3,dy);
	left.set(2,3,dz);
	transform = left.multiply(transform);
	return transform;
    }
    public Matrix rotate(char axis, double theta) {
	Matrix left = Matrix.identity(4);
	double rad = Math.toRadians(theta);
	if (axis == 'z') {
	    left.set(0,0,Math.cos(rad));
	    left.set(1,1,Math.cos(rad));
	    left.set(0,1,-1 * Math.sin(rad));
	    left.set(1,0, Math.sin(rad));
	} 
	else if (axis == 'y') {
	    left.set(0,0,Math.cos(rad));
	    left.set(2,2,Math.cos(rad));
	    left.set(0,2,-1 * Math.sin(rad));
	    left.set(2,0,Math.sin(rad));
	}
	else if (axis == 'x') {
	    left.set(1,1,Math.cos(rad));
	    left.set(2,2,Math.cos(rad));
	    left.set(1,2,-1 * Math.sin(rad));
	    left.set(2,1,Math.sin(rad));
	}
	transform = left.multiply(transform);
	return transform;
    }
    public Matrix apply() {
	edges.copy(transform.multiply(edges));
	transform = Matrix.identity(4);
	return edges;
    }

    // Canvas Methods
    public boolean draw_pixel(int x, int y, Pixel p) {
	canvas[y][x] = p;
	return true;
    }
    public boolean draw_pixel(int x, int y) {
	return draw_pixel(x, y, new Pixel(0, 0, 0));
    }
    public boolean draw_pixel(int x, int y, int R, int G, int B) {
	return draw_pixel(x, y, new Pixel(R, G, B));
    }

    public boolean fill(Pixel p) {
	for (int i = 0; i < y; i++) {
	    for (int j = 0; j < x; j++) {
		canvas[i][j] = p;
	    }
	}
	return true;
    }
    public boolean fill(int R, int G, int B) {
	return fill(new Pixel(R, G, B));
    }

    // Bresenham's Line Algorithm - 8 Octants
    public boolean line(int x1, int y1, int x2, int y2) {
	return line(x1, y1, x2, y2, new Pixel(0, 0, 0));
    }
    public boolean line(int x1, int y1, int x2, int y2, Pixel p) {
	if (x2 < x1) return line(x2, y2, x1, y1, p);
	int dy = y2 > y1 ? y2 - y1 : y1 - y2; // positive difference
	int dx = x2 - x1; // always positive
	int m = y2 > y1 ? 1 : -1;
	if (dy > dx)
	    if (m > 0)
		return line2(x1, y1, x2, y2, p); // Vertical - Octant 2
	    else
		return line7(x1, y1, x2, y2, p); // Vertical - Octant 7
	else
	    if (m > 0)
		return line1(x1, y1, x2, y2, p); // Horizontal - Octant 1
	    else
		return line8(x1, y1, x2, y2, p); // Horizontal - Octant 8
    }
    public boolean line7(int x1, int y1, int x2, int y2, Pixel p) {
	int A = y2 - y1; // dy
	int B = x1 - x2; // -dx
	int d = -2 * B + A;
	A = 2 * A;
	B = -2 * B;
	while (y1 >= y2) {
	    draw_pixel(x1, y1, p);
	    if (d > 0) {
		x1++;
		d += A;
	    }
	    y1--;
	    d += B;
	}
	return true;
    }
    public boolean line2(int x1, int y1, int x2, int y2, Pixel p) {
	int A = y2 - y1; // dy
	int B = x1 - x2; // -dx
	int d = 2 * B + A;
	A = 2 * A;
	B = 2 * B;
	while (y1 <= y2) {
	    draw_pixel(x1, y1, p);
	    if (d < 0) {
		x1++;
		d += A;
	    }
	    y1++;
	    d += B;
	}
	return true;
    }
    public boolean line8(int x1, int y1, int x2, int y2, Pixel p) {
	int A = y2 - y1; // dy
	int B = x1 - x2; // -dx
	int d = 2 * A - B;
	A = 2 * A;
	B = -2 * B;
	while (x1 <= x2) {
	    draw_pixel(x1, y1, p);
	    if (d < 0) {
		y1--;
		d += B;
	    }
	    x1++;
	    d += A;
	}
	return true;
    }
    public boolean line1(int x1, int y1, int x2, int y2, Pixel p) {
	int A = y2 - y1; // dy
	int B = x1 - x2; // -dx
	int d = 2 * A + B;
	A = 2 * A;
	B = 2 * B;
	while (x1 <= x2) {
	    draw_pixel(x1, y1, p);
	    if (d > 0) {
		y1++;
		d += B;
	    }
	    x1++;
	    d += A;
	}
	return true;
    }

    // Other Designs
    public boolean triangle(int x, int y, Pixel p) {
	int layer = 0;
	while (y > -1) {
	    for (int i = Math.max(0, x - layer); i < Math.min(x + layer + 1, this.x); i++) {
		canvas[y][i] = p;
	    }
	    layer++; y--;
	}
	return true;
    }

    // EdgeMatrix Functions
    public boolean edge(double x1, double y1, double x2, double y2) {
	return edge(x1, y1, x2, y2, new Pixel(0,0,0));
    }
    public boolean edge(double x1, double y1, double x2, double y2, Pixel p) {
	return edges.add_edge(x1, y1, x2, y2, p);
    }
    public boolean edge(double x1, double y1, double z1,
			double x2, double y2, double z2) {
	return edges.add_edge(x1, y1, z1, x2, y2, z2, new Pixel(0,0,0));
    }
    public boolean edge(double x1, double y1, double z1,
			double x2, double y2, double z2, Pixel p) {
	return edges.add_edge(x1, y1, z1, x2, y2, z2, p);
    }

    public boolean draw() {
	Iterator<double[]> edgelist = edges.iterator();
	Iterator<Pixel> colors = edges.colorIterator();
	while (edgelist.hasNext()) {
	    double[] p1 = edgelist.next();
	    double[] p2 = edgelist.next();
	    int x1 = (int)(p1[0]);
	    int y1 = (int)(p1[1]);
	    int x2 = (int)(p2[0]);
	    int y2 = (int)(p2[1]);
	    line(x1, y1, x2, y2, colors.next());
	}
	return true;
    }
    
    public boolean clearEdges() {
	edges = new Matrix();
	return true;
    }
    public boolean clearTransform() {
	transform = Matrix.identity(4);
	return true;
    }

    // File Creation
    public boolean save(String name) throws FileNotFoundException {
	PrintWriter pw = new PrintWriter(new File(name));
	pw.print("P3 " + x + " " + y + " 255\n"); // Heading
	for (int i = y - 1; i > -1; i--) {
	    for (int j = 0; j < x; j++) {
		// System.out.printf("x: %d\ty: %d\n", j, i); // Debugging
		pw.print(canvas[i][j]);
	    }
	}
	pw.close();
	return true;
    }
}
