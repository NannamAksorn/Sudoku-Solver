import java.util.Scanner;
import javax.swing.JFrame;
//p1
// --5----32
// --------5
// ---61----
// --2--3---
// 7-------6
// ---4--1--
// ----82---
// 8--------
// 41----7--
//p2
// ---------
// -23---46-
// 8--6-7--9
// 5---3---7
// 9-------8
// -8-----1-
// --2---3--
// ---5-8---
// ----7----
//p3
// ---------
// --381----
// --2--756-
// --1----8-
// -4-----3-
// -5----6--
// -689--4--
// ----459--
// ---------
//p4
// -6-9-----
// --4---3-5
// -1-3---2-
// ------1-4
// ----2----
// 8-3------
// -5---1-8-
// 2-8---9--
// -----7-3-

//  -----4-6-
//  --4-3-1--
//  --7-----2
//  --57--92-
//  -76------
//  -8--6----
//  7-----68-
//  6--9-5-3-
//  ----4-21-
public class Solver{	
	public static void main(String[] args) {
		Scanner input = new Scanner(System.in);
		String[] input_string = new String[9];
		int[][] input_cells  = new int[9][9];

		if (args.length > 0) {
			input_string = args;
		}else{
			System.out.println("Enter Sudoku grid.: ");
			for (int i = 0; i < 9; i++) {
				String line = input.next();
				if (line.length() != 9) {
					System.out.println("Error input at line " + i);
					return ;
				}else {
					input_string[i] = line;
				}
			}
		}
		for (int i = 0; i < 9; i++) {
			String line = input_string[i];
			for (int j = 0; j < 9; j++) {
				int value = Character.getNumericValue(line.charAt(j));
				if (value < 1 || value > 9) {
					value = 0;
				}
				input_cells[i][j] = value;  
			}
		}

		
		Grid grid = new Grid(input_cells);
		
	    JFrame frame = new JFrame ("Sudoku Solver");
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setSize(630,630);
	    frame.setLocationRelativeTo(null);
	    frame.add(grid);
	    frame.setVisible(true);
	    frame.repaint();
	    
		grid.eliminateAll();
		grid.runAll();
		
		input.close();
		System.out.println("Done");
	}

}
