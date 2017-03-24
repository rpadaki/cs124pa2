import java.util.*;
import java.io.*;

public class MatrixMultiply {
	public static void main(String[] args) {
		int flag=parseInt(args[0]);

		// Matrix dimension
		int n=parseInt(args[1]);

		// Initialize crossover
		int crossover;

		// Initialize matrices
		long[][] a = new long[n][n];
		long[][] b = new long[n][n];
		long[] diag = new long[n];

		// Let flag be abc in binary.
		// a -- 0 for diagonal, 1 for time
		// b -- 0 for file, 1 for random
		// c -- 0 for Strassen, 1 for regular
		if (flag % 2 == 0) {
			crossover = parseInt(args[2]);
			if (flag % 4 == 0) {
				setMatricesFromFile(a,b,n,args[3]);
			}
			else {
				int min = parseInt(args[3]);
				int max = parseInt(args[4]);
				randMatrix(n,min,max,a);
				randMatrix(n,min,max,b);	
			}
			if (flag < 4) {
				for (long i : diagStrassen(n,crossover,a,b)) {
					System.out.println(i);
				}	
			}
			else {
				System.out.println(timeStrassen(n,crossover,a,b));
			}
		}
		else {
			if (flag % 4 == 1) {
				setMatricesFromFile(a,b,n,args[2]);
			}
			else {
				int min = parseInt(args[2]);
				int max = parseInt(args[3]);
				randMatrix(n,min,max,a);
				randMatrix(n,min,max,b);	
			}
			if (flag < 4) {
				for (long i : diagRegular(n,a,b)) {
					System.out.println(i);
				}	
			}
			else {
				System.out.println(timeRegular(n,a,b));
			}
		}
	}

	public static long timeStrassen(int n, int crossover, long[][] a1, long[][] b1) {
		//Determine buffer
		int m = pad(n,crossover);

		// Initialize padded matrices
		long[][] a = new long[m][m];
		long[][] b = new long[m][m];
		long[][] c = new long[m][m];

		// Fill in the padded matrices with
		// a1 and b1.
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				a[i][j] = a1[i][j];
				b[i][j] = b1[i][j];
			}
		}

		ArrayList<long[][][]> matrices = new ArrayList<long[][][]>();
		makeMatrices(matrices, m, crossover);
		long start = System.currentTimeMillis();
		strassen(a, b, c, matrices, 0, 0, 0, 0, 0, 0, m, crossover, 0);
		long end = System.currentTimeMillis();
		return end-start;
	}

	public static long timeRegular(int n, long[][] a, long[][] b) {
		long[][] c = new long[n][n];
		long start = System.currentTimeMillis();
		regMult(a, b, c, 0, 0, 0, 0, 0, 0, n);
		long end = System.currentTimeMillis();
		return end-start;
	}

	public static long[] diagStrassen(int n, int crossover, long[][] a1, long[][] b1) {
		//Determine buffer
		int m = pad(n,crossover);

		// Initialize padded matrices
		long[][] a = new long[m][m];
		long[][] b = new long[m][m];
		long[][] c = new long[m][m];
		long[] out = new long[n];

		// Fill in the padded matrices with
		// a1 and b1.
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				a[i][j] = a1[i][j];
				b[i][j] = b1[i][j];
			}
		}

		ArrayList<long[][][]> matrices = new ArrayList<long[][][]>();
		makeMatrices(matrices, m, crossover);
		strassen(a, b, c, matrices, 0, 0, 0, 0, 0, 0, m, crossover, 0);
		for (int i = 0; i < n; i++) {
			out[i] = c[i][i];
		}
		return out;
	}


	public static long[] diagRegular(int n, long[][] a, long[][] b) {
		long[][] c = new long[n][n];
		long[] out = new long[n];
		regMult(a, b, c, 0, 0, 0, 0, 0, 0, n);
		for (int i = 0; i < n; i++) {
			out[i] = c[i][i];
		}
		return out;
	}

	public static void setMatricesFromFile(long[][] a, long[][] b, int n, String inputfile) {
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
		matrices.add(new long[7][len/2][len/2]);
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

	// recursive function that calculates the product of a and b and stores it in c using strassen's algorithm
	public static void strassen(long[][] a, long[][] b, long[][] c, ArrayList<long[][][]> matrices, int ai, int aj, int bi, int bj, int ci, int cj, int len, int crossover, int depth) {
		// base case of our recursion
		if (len <= crossover) {
			regMult(a, b, c, ai, aj, bi, bj, ci, cj, len);
			return;
		}

		// depth 0 corresponds to n/2, depth 1 to n/4, etc
		long[][][] m = matrices.get(depth);

		// half-length
		int hl = len/2;

		// Calculate helper functions
		// P6
		// we put B - D in m[0]
		sub(a, a, m[0], ai, aj + hl, ai+hl, aj+hl, 0, 0, hl);
		// we put G + H in m[1]
		add(b, b, m[1], bi+hl, bj, bi+hl, bj+hl, 0, 0, hl);
		// we put P6 in m[2]
		strassen(m[0], m[1], m[2], matrices, 0, 0, 0, 0, 0, 0, hl, crossover, depth+1);

		// P2
		// we put A + B in m[0]
		add(a, a, m[0], ai, aj, ai, aj + hl, 0, 0, hl);
		// we put P2 in m[1]
		// we move H before and after since we will need it in the future
		move(b, m[5], bi+hl, bj+hl, 0, 0, hl);
		strassen(m[0], b, m[1], matrices, 0, 0, bi+hl, bj+hl, 0, 0, hl, crossover, depth+1);
		move(m[5], b, 0, 0, bi+hl, bj+hl, hl);

		// P4
		// we put G - E in m[0]
		sub(b, b, m[0], bi + hl, bj, bi, bj, 0, 0, hl);
		// we put P4 in m[3] and save D for later use
		move(a, m[5], ai+hl, aj+hl, 0, 0, hl);
		strassen(a, m[0], m[3], matrices, ai+hl, aj+hl, 0, 0, 0, 0, hl, crossover, depth+1);
		move(m[5], a, 0, 0, ai+hl, aj+hl, hl);

		// now we no longer need B or G
		// we move P6 to B's position
		move(m[2], a, 0, 0, ai, aj+hl, hl);
		// we move P2 to G's position
		move(m[1], b, 0, 0, bi+hl, bj, hl);

		// P7
		// we put A - C in m[0]
		sub(a, a, m[0], ai, aj, ai+hl, aj, 0, 0, hl);
		// we put E + F in m[1]
		add(b, b, m[1], bi, bj, bi, bj+hl, 0, 0, hl);
		// we put P7 in m[2]
		strassen(m[0], m[1], m[2], matrices, 0, 0, 0, 0, 0, 0, hl, crossover, depth+1);

		// P1
		// we put F - H in m[0]
		sub(b, b, m[0], bi, bj+hl, bi+hl, bj+hl, 0, 0, hl);
		// we put P1 in m[1] and save A for later use
		move(a,m[5],ai,aj,0,0,hl);
		strassen(a, m[0], m[1], matrices, ai, aj, 0, 0, 0, 0, hl, crossover, depth+1);
		move(m[5],a,0,0,ai,aj,hl);

		// P3
		// we put C + D in m[0]
		add(a, a, m[0], ai+hl, aj, ai+hl, aj+hl, 0, 0, hl);
		// we put P3 in m[4] and save E for later use
		move(b,m[5],bi,bj,0,0,hl);
		strassen(m[0], b, m[4], matrices, 0, 0, bi, bj, 0, 0, hl, crossover, depth+1);
		move(m[5],b,0,0,bi,bj,hl);

		// now we no longer need C or F
		// we move P1 to C's position
		move(m[1], a, 0, 0, ai+hl, aj, hl);
		// we move P3 to F's position
		move(m[4], b, 0, 0, bi, bj+hl, hl);

		// P5
		// we put A + D in m[0]
		add(a, a, m[0], ai, aj, ai+hl, aj+hl, 0, 0, hl);
		// we put E + H in m[1]
		add(b, b, m[1], bi, bj, bi+hl, bj+hl, 0, 0, hl);
		// we put P5 in m[4]
		strassen(m[0], m[1], m[4], matrices, 0, 0, 0, 0, 0, 0, hl, crossover, depth+1);

		// At this point:
		// P1 is in C's position
		// P2 is in G's position
		// P3 is in F's position
		// P4 is in m[3]
		// P5 is in m[4]
		// P6 is in B's position
		// P7 is in m[2]

		// Calculate the four quadrants of c using our helper functions
		// AE + BG
		// we add P5 + P6 and save the result in P6
		add(m[4], a, a, 0, 0, ai, aj+hl, ai, aj+hl, hl);
		// we subtract P2
		sub(a, b, a, ai, aj+hl, bi+hl, bj, ai, aj+hl, hl);
		// we add P4 and put the final result in c
		add(a, m[3], c, ai, aj+hl, 0, 0, ci, cj, hl);

		// AF + BH
		// we add P1 + P2 and put the result in c
		add(a, b, c, ai+hl, aj, bi+hl, bj, ci, cj+hl, hl);

		// CE + DG
		//we add P3 + P4 and put the result in c
		add(b, m[3], c, bi, bj+hl, 0, 0, ci+hl, cj, hl);

		// CF + DH
		// we add P5 + P1 and save the result in P5
		add(m[4], a, m[4], 0, 0, ai+hl, aj, 0, 0, hl);
		// we subtract P3
		sub(m[4], b, m[4], 0, 0, bi, bj+hl, 0, 0, hl);
		// we subtract P7 and put the final result in c
		sub(m[4], m[2], c, 0, 0, 0, 0, ci+hl, cj+hl, hl);
	}

	public static int parseInt(String s) {
		try {
			return Integer.parseInt(s);
		} catch (NumberFormatException e) {
			System.out.println("Error: Incorrect integer format." + " \'" + s + "\'");
			System.exit(0);
		}
		return 0;
	}	
}
