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