import java.util.*;
import java.io.*;

public class MatrixMultiply {
	public static void main(String[] args) {
		int flag=parseInt(args[1]);

		// Matrix dimension
		int n=parseInt(args[2]);

		// Initialize matrices
		int[][] A = new int[n][n];
		int[][] B = new int[n][n];

		// Get matrices from input file
		if (flag == 0) {
			// Input filename
			String inputfile = args[3];
			try {
				BufferedReader in = new BufferedReader(new FileReader(inputfile));
				for (int i=0; i<n; i++) {
					A[i][j] = parseInt(in.readLine());
				}
				i = 0;
				while (i < n) {
					B[i][j] = parseInt(in.readLine());
				}
			} catch (IOException e) {
				System.out.println("Error: Incorrect input format.");
				System.exit(0);
			} finally {
				if (in != null) {
					in.close();
				}
			}
		}
		// Randomly generate matrices
		else if (flag == 1) {
			int min = parseInt(args[3]);
			int max = parseInt(args[4]);
			randMatrix(n,min,max,A);
			randMatrix(n,min,max,B);
		}
	}

	// Generate a random matrix and store it in c
	public static void randMatrix(int n, int min, int max, int[][] c) {
		Random rng = new Random();
		int range = max-min+1;
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				c[i][j] = rng.nextInt(range) + min;
			}
		}
	}

	// Add a len x len submatrix of a to
	// a submatrix of b and store in c
	public static void add(int[][] a, int[][] b, int[][] c,int ai, int aj, int bi, int bj, int ci, int cj, int len) {
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				c[i+ci][j+cj] = a[i+ai][j+aj] + b[i+bi][j+bj];
			}
		}
	}

	// Subtract a len x len submatrix of a
	// from a submatrix of b and store in a
	// submatrix of c
	public static void sub(int[][] a, int[][] b, int[][] c,int ai, int aj, int bi, int bj, int ci, int cj, int len) {
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				c[i+ci][j+cj] = a[i+ai][j+aj] - b[i+bi][j+bj];
			}
		}
	}

	// Multiply a len x len submatrix of a
	// by a submatrix of b and store in a 
	// submatrix of c with the classic method.
	public static void regMult(int[][] a, int[][] b, int[][] c, int ai, int aj, int bi, int bj, int ci, int cj, int len) {
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
	public static void move(int[][] a, int[][] c, int ai, int aj, int ci, int cj, int len) {
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				c[i+ci][j+cj] = a[i+ai][j+cj];
			}
		}
	}

	public static void strassen(int[][] a, int[][] b, int[][] c, int[][] p1, int[][] p2, int[][] p3, int[][] p4, int[][] p5, int[][] p6, int[][] p7, int ai, int aj, int bi, int bj, int ci, int cj, int len, int crossover) {
		// p6
		subtract(a, b, p1, ai + len/2, aj)
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
