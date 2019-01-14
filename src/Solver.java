import java.util.Scanner;
import javax.swing.JFrame;

// --5----32
// --------5
// ---61----
// --2--3---
// 7-------6
// ---4--1--
// ----82---
// 8--------
// 41----7--

//  -----4-6-
//  --4-3-1--
//  --7-----2
//  --57--92-
//  -76------
//  -8--6----
//  7-----68-
//  6--9-5-3-
//  ----4-21-

// ----1---5
// -5-4-7-31                  
// -9-3----7                  
// ------4--
// ---9-45--                  
// --4-2----                  
// 63-2-----
// -----8-1-
// 7-5---2-8

// 2-9------
// 6-----348
// --4-5----
// --8----64
// 5--3-4--2
// ---5-8---
// -----9-5-
// --14-2-7-
// --27-----
public class Solver{
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Enter Sudoku grid.: ");
		Scanner input = new Scanner(System.in);
		int[][] input_cells  = new int[9][9];
		for (int i = 0; i < 9; i++) {
			String line = input.next();
			if (line.length() != 9) {
				System.out.println("Error input at line " + i);
				break;
			}else {
				for (int j = 0; j < 9; j++) {
					int value = Character.getNumericValue(line.charAt(j));
					if (value < 1 || value > 9) {
						value = 0;
					}
					input_cells[i][j] = value;  
				}
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
		grid.hiddenSingleAll();
		grid.nakedpairAll();
		// grid.hiddenSingleAll();

		input.close();
	}

}
