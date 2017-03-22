import java.util.*;
import java.io.*;

public class MatrixMultiply {
	public static void main(String[] args) {
		System.out.println("Test");
		int flag = parseInt(args[1]);
		int n = parseInt(args[2]);
		String inputfile = args[3];

		if (flag == 0) {
			int[][] A = new int[n][n];
			int[][] B = new int[n][n];
			try {
				BufferedReader in = new BufferedReader(new FileReader(inputfile));
				int i = 0;
				while (i < n) {
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


	}



	// assume c is all zeroes
	public static void regMult(int[][] a, int[][] b, int[][] c, int ai, int aj, int bi, int bj, int ci, int cj, int len) {
		for (int i = 0; i < len; i++) {
			for (int j = 0; j < len; j++) {
				for (int k = 0; k < len; k++) {
					c[i+ci][j+cj] += a[i+ai][k+aj] * b[k+bi][j+bj];
				}
			}
		}
		return;
	}

	public static void strassen(int[][] a, int[][] b, int[][] c, ArrayList<int[][][]> Matrices, int ai, int aj, int bi, int bj, int ci, int cj, int len, int crossover, int depth) {
		if (len <= crossover) {
			regMult(a, b, c, ai, aj, bi, bj, ci, cj, len);
			return;
		}

		m = Matrices.get(depth);

		// p6
		// B - D goes i m0
		sub(a, a, m[0], ai + len/2, aj, ai+len/2, aj+len/2, 0, 0, len/2);
		// G + H goes in m1
		add(b, b, m[1], bi, bj+len/2, bi+len/2, bj+len/2, 0, 0, len/2);
		// multiply goes in m2
		strassen(m[0], m[1], m[2], Matrices, 0, 0, 0, 0, 0, 0, len/2, depth + 1);

		//p2
		// A+B goes in m0
		add(a, a, m[0], ai, aj, ai + len/2, aj, 0, 0, len/2);
		// multiply goes in m1
		strassen(m[0], b, m[1], Matrices, 0, 0, bi+len/2, bj+len/2, 0, 0, len/2, depth + 1);

		//p4
		// G-E goes in m0
		sub(b, b, m[0], bi + len/2, bj, bi, bj, 0, 0, len/2);
		// multiply goes in m3
		strassen(a, m[0], m[3], Matrices, ai+len/2, aj+len/2, 0, 0, 0, 0, len/2, depth+1);

		//now we don't need B or G
		//p6 moves to B's spot
		move(m[2], a, 0, 0, ai, aj+len/2, len/2);
		//p2 moves to G's spot
		move(m[1], b, 0, 0, bi+len/2, bj, len/2);

		//p7
		//A-C goes in m0
		sub(a, a, m[0], ai, aj, ai+len/2, aj, len/2);
		//E+F goes in m1
		add(b, b, m[1], bi, bj, bi, bj+len/2, len/2);
		//multiply goes in m2
		strassen(m[0], m[1], m[2], Matrices, 0, 0, 0, 0, 0, 0, len/2, depth+1);

		//p1
		//F-H goes in m0
		sub(b, b, m[0], bi, bj+len/2, bi+len/2, bj+len/2, 0, 0, len/2);
		// multiply goes in m1
		strassen(a, m[0], m[1], Matrices, ai, aj, 0, 0, 0, 0, len/2, depth+1);

		//p3
		//C+D goes in m0
		add(a, a, m[0], ai+len/2, aj, ai+len/2, aj+len/2, len/2);
		//multiply goes in m4
		strassen(m[0], b, m[4], Matrices, 0, 0, bi, bj, 0, 0, len/2, depth+1);

		//now we don't need C or F
		//p1 goes in C's spot
		move(m[1], a, 0, 0, ai+len/2, aj, len/2);
		//p3 goes in F's spot
		move(m[4], b, 0, 0, bi, bj+len/2, len/2);

		//p5
		//A+D goes in m0
		add(a, a, m[0], 0, 0, ai+len/2, aj+len/2, 0, 0, len/2);
		//E+H goes in m1
		add(b, b, m[1], 0, 0, bi+len/2, bj+len/2, 0, 0, len/2);
		//multiply goes in m4
		strassen(m[0], m[1], m[4], Matrices, 0, 0, 0, 0, 0, 0, len/2, depth+1);

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
