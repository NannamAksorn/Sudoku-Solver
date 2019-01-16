import java.io.File;
import java.io.FileNotFoundException;
import java.util.Random;
import java.util.Scanner;
import javax.swing.JFrame;

public class Solver{	

	public static final boolean SHOW_GUI = true;

	public static final boolean SHOW_STATUS = false;

	public static final boolean TYPE_INPUT = false;
	public static final boolean RANDOM_FILE = false;

	public static final String FILE_NAME = "testdata" + ".txt";

	public static void main(String[] args) {
		Scanner input;
		try {
			//pick random dataset file
			File file = null;
			if (RANDOM_FILE){
				File dir = new File("dataset");
				File[] files = dir.listFiles();
				Random rand = new Random();
				file = files[rand.nextInt(files.length)];
			}else{
				file = new File("dataset/" + FILE_NAME);
			}
			input = new Scanner(file);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			input = new Scanner(System.in);
		}
		if (TYPE_INPUT){
			input = new Scanner(System.in);
		}

		String[] input_string = new String[9];
		int[][] input_cells  = new int[9][9];

		// if (args.length > 0) {
		// 	input_string = args;
		// }else{
		int dataset_count = input.nextInt();
		int total_blank = 0;
		int solve_count = 0;
		int error_count = 0;

		if(1 > dataset_count && dataset_count > 1000) return;
		for (int loop_id = 0; loop_id < dataset_count; loop_id++) {
			int dataset_number = input.nextInt();
			for (int i = 0; i < 9; i++) {
				String line = input.next();
				if (line.length() != 9) {
					System.out.println("Error input at line " + i);
					return ;
				}else {
					input_string[i] = line;
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
			if(SHOW_GUI){
				JFrame frame = new JFrame ("Sudoku Solver Question: " + dataset_number);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setSize(630,630);
				frame.setLocationRelativeTo(null);
				frame.add(grid);
				frame.addMouseMotionListener(grid);
				frame.setVisible(true);
				frame.repaint();
			}

			System.out.println(dataset_number);	
			grid.eliminateAll();
			grid.runAll();
			grid.x_wing();
			try {
				// grid.eliminateAll();
				// grid.runAll();
				// grid.x_wing();
			} catch (Exception e) {
				error_count++;
				System.out.println("Error");
				continue;
			}
			if (!grid.checkAll()){
				error_count++;
				System.out.println("Error");
				continue;
			}
			int blank_count = grid.printBlackCount();
			if (blank_count == 0 ){
				solve_count++;
			}else{
				total_blank += blank_count;
			}
			for (int i = 0; i < 9; i++) {
				for (int j = 0; j < 9; j++) {
					if(grid.Rows[i][j].value == 0){
						System.out.print("-");
					}else{
						System.out.print(grid.Rows[i][j].value);
					}
				}
				System.out.println("");
			}
			if (SHOW_STATUS){
				System.out.println("total_error = "  + error_count);
				System.out.println("total_blank = "  + total_blank);
				System.out.println("total_solve = "  + solve_count + "/" + dataset_count);
			}
			
		}
		
	input.close();
	}

}
