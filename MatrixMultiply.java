import java.util.*;
import java.io.*;

public class MatrixMultiply {
	public static void main(String[] args) {
		int flag=parseInt(args[0]);

		// Matrix dimension
		int n=parseInt(args[1]);

		// Initialize crossover
		int crossover;

		// Testing to make sure that
		// padding was working as desired
		if (flag == 3) {
			crossover = parseInt(args[2]);
			System.out.println(pad(n,crossover));
			return;
		}

		// We set the crossover here.
		crossover = 15;

		// Manually set crossover for testing
		// with random matrices.
		if (flag == 4) {
			crossover = parseInt(args[4]);
		}

		
		// Determine buffer
		int m = pad(n,crossover);

		// Initialize matrices
		long[][] a = new long[m][m];
		long[][] b = new long[m][m];

		// Get matrices from input file
		if (flag == 0) {
			// Input filename
			String inputfile = args[2];
			try {
				BufferedReader in = new BufferedReader(new FileReader(inputfile));
				for (int i=0; i<n; i++) {
					for (int j=0; j<n; j++) {
						a[i][j] = parseInt(in.readLine());
					}
				}
				for (int i=0; i<n; i++) {
					for (int j=0; j<n; j++) {
						b[i][j] = parseInt(in.readLine());
					}
				}
			} catch (IOException e) {
				System.out.println("Error: Incorrect input format.");
				System.exit(0);
			}
		}
		// Randomly generate matrices
		else if (flag == 1 || flag == 4) {
			int min = parseInt(args[2]);
			int max = parseInt(args[3]);
			randMatrix(n,min,max,a);
			randMatrix(n,min,max,b);
		}
		// Fill in the rest of a and b with
		// 1s on the diagonal. This makes
		// multiplication of padded matrices
		// work as desired.
		for (int i = n; i < m; i++) {
			a[i][i] = 1;
			b[i][i] = 1;
		}

		long[][] c = new long[m][m];
		long[][] d = new long[m][m];

		ArrayList<long[][][]> matrices = new ArrayList<long[][][]>();
		makeMatrices(matrices, m, crossover);
		regMult(a, b, d, 0, 0, 0, 0, 0, 0, n);
		strassen(a, b, c, matrices, 0, 0, 0, 0, 0, 0, m, crossover, 0);
		
		System.out.println("Comparison");
		System.out.println("------------------");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j<n; j++) System.out.println(i + "," + j + ":   " + c[i][j] + " " + d[i][j]);
		}
	}

	// Generate a random matrix and store it in c
	public static void randMatrix(int n, int min, int max, long[][] c) {
		Random rng = new Random();
		int range = max-min+1;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				c[i][j] = rng.nextInt(range) + min;
			}
		}
	}

	// Figure out how much to pad the matrix to
	// make Strassen's algorithm run effortlessly
	public static int pad(int n, int crossover) {
		int m = 1;
		while (m * crossover < n) {
			m = m*2;
		}
		while (m * crossover >= n) {
			crossover = crossover-1;
		}
		return (crossover + 1)*m;
	}

	// fills in matrices arraylist
	// should be first called with len=n
	public static void makeMatrices(ArrayList<long[][][]> matrices, int len, int crossover) {
		if (len <= crossover) {
			return;
		}
		matrices.add(new long[5][len/2][len/2]);
		makeMatrices(matrices, len/2, crossover);
	}

	// Add a len x len submatrix of a to
	// a submatrix of b and store in c
	public static void add(long[][] a, long[][] b, long[][] c,int ai, int aj, int bi, int bj, int ci, int cj, int len) {
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				c[i+ci][j+cj] = a[i+ai][j+aj] + b[i+bi][j+bj];
			}
		}
	}

	// Subtract a len x len submatrix of a
	// from a submatrix of b and store in a
	// submatrix of c
	public static void sub(long[][] a, long[][] b, long[][] c,int ai, int aj, int bi, int bj, int ci, int cj, int len) {
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				c[i+ci][j+cj] = a[i+ai][j+aj] - b[i+bi][j+bj];
			}
		}
	}

	// Multiply a len x len submatrix of a
	// by a submatrix of b and store in a 
	// submatrix of c with the classic method.
	public static void regMult(long[][] a, long[][] b, long[][] c, int ai, int aj, int bi, int bj, int ci, int cj, int len) {
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				c[i+ci][j+cj] = 0;
				for (int k = 0; k < len; k++) {
					c[i+ci][j+cj] += a[i+ai][k+aj] * b[k+bi][j+bj];
				}
			}
		}
	}

	// Move a len x len submatrix of a to
	// a submatrix of c
	public static void move(long[][] a, long[][] c, int ai, int aj, int ci, int cj, int len) {
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				c[i+ci][j+cj] = a[i+ai][j+aj];
			}
		}
	}

	public static void strassen(long[][] a, long[][] b, long[][] c, ArrayList<long[][][]> matrices, int ai, int aj, int bi, int bj, int ci, int cj, int len, int crossover, int depth) {
		if (len <= crossover) {
			regMult(a, b, c, ai, aj, bi, bj, ci, cj, len);
			return;
		}
		long[][] d = new long[len][len];
		regMult(a,b,d,ai,aj,bi,bj,0,0,len);

		//depth 0 corresponds to n/2, etc
		long[][][] m = matrices.get(depth);

		// p6
		// B - D goes in m0
		sub(a, a, m[0], ai, aj + len/2, ai+len/2, aj+len/2, 0, 0, len/2);
		// G + H goes in m1
		add(b, b, m[1], bi+len/2, bj, bi+len/2, bj+len/2, 0, 0, len/2);
		// multiply goes in m2
		strassen(m[0], m[1], m[2], matrices, 0, 0, 0, 0, 0, 0, len/2, crossover, depth+1);

		//p2
		// A+B goes in m0
		add(a, a, m[0], ai, aj, ai, aj + len/2, 0, 0, len/2);
		// multiply goes in m1
		strassen(m[0], b, m[1], matrices, 0, 0, bi+len/2, bj+len/2, 0, 0, len/2, crossover, depth+1);
		//p4
		// G-E goes in m0
		sub(b, b, m[0], bi + len/2, bj, bi, bj, 0, 0, len/2);
		// multiply goes in m3
		strassen(a, m[0], m[3], matrices, ai+len/2, aj+len/2, 0, 0, 0, 0, len/2, crossover, depth+1);
		//now we don't need B or G
		//p6 moves to B's spot
		move(m[2], a, 0, 0, ai, aj+len/2, len/2);
		//p2 moves to G's spot
		move(m[1], b, 0, 0, bi+len/2, bj, len/2);

		//p7
		//A-C goes in m0
		sub(a, a, m[0], ai, aj, ai+len/2, aj, 0, 0, len/2);
		//E+F goes in m1
		add(b, b, m[1], bi, bj, bi, bj+len/2, 0, 0, len/2);
		//multiply goes in m2
		strassen(m[0], m[1], m[2], matrices, 0, 0, 0, 0, 0, 0, len/2, crossover, depth+1);
		//p1
		//F-H goes in m0
		sub(b, b, m[0], bi, bj+len/2, bi+len/2, bj+len/2, 0, 0, len/2);
		// multiply goes in m1
		strassen(a, m[0], m[1], matrices, ai, aj, 0, 0, 0, 0, len/2, crossover, depth+1);
		//p3
		//C+D goes in m0
		add(a, a, m[0], ai+len/2, aj, ai+len/2, aj+len/2, 0, 0, len/2);
		//multiply goes in m4
		strassen(m[0], b, m[4], matrices, 0, 0, bi, bj, 0, 0, len/2, crossover, depth+1);
		//now we don't need C or F
		//p1 goes in C's spot
		move(m[1], a, 0, 0, ai+len/2, aj, len/2);
		//p3 goes in F's spot
		move(m[4], b, 0, 0, bi, bj+len/2, len/2);

		//p5
		//A+D goes in m0
		add(a, a, m[0], ai, aj, ai+len/2, aj+len/2, 0, 0, len/2);
		//E+H goes in m1
		add(b, b, m[1], bi, bj, bi+len/2, bj+len/2, 0, 0, len/2);
		//multiply goes in m4
		strassen(m[0], m[1], m[4], matrices, 0, 0, 0, 0, 0, 0, len/2, crossover, depth+1);
		//p1 is in C's spot
		//p2 is in G's spot
		//p3 is in F's spot
		//p4 is in m[3]
		//p5 is in m[4]
		//p6 is in B's spot
		//p7 is in m[2]

		//add things and move to c
		//AE+BG
		//p5+p6, save in p6
		add(m[4], a, a, 0, 0, ai, aj+len/2, ai, aj+len/2, len/2);
		//subtract p2
		sub(a, b, a, ai, aj+len/2, bi+len/2, bj, ai, aj+len/2, len/2);
		//add p4
		add(a, m[3], c, ai, aj+len/2, 0, 0, ci, cj, len/2);

		//AF+BH
		//p1+p2
		add(a, b, c, ai+len/2, aj, bi+len/2, bj, ci, cj+len/2, len/2);

		//CE+DG
		//p3+p4
		add(b, m[3], c, bi, bj+len/2, 0, 0, ci+len/2, cj, len/2);

		//CF+DH
		//p5+p1, save in p5
		add(m[4], a, m[4], 0, 0, ai+len/2, aj, 0, 0, len/2);
		//subtract p3
		sub(m[4], b, m[4], 0, 0, bi, bj+len/2, 0, 0, len/2);
		//subtract p7
		sub(m[4], m[2], c, 0, 0, 0, 0, ci+len/2, cj+len/2, len/2);
		System.out.println("Comparison");
		System.out.println("------------------");
		for (int i = 0; i < len; i++) {
			for (int j = 0; j<len; j++) System.out.println(i + "," + j + ":   " + c[i][j] + " " + d[i][j]);
		}
	}

	public static int parseInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			System.out.println("Error: Incorrect input format.");
			System.exit(0);
		}
		return 0;
	}	
}
