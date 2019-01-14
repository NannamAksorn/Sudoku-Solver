import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Line2D;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;




public class Grid extends JPanel  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GridLayout grid_layout = new GridLayout(9,9);
	Cell[][] Rows = new Cell[9][9];
	Cell[][] Cols = new Cell[9][9];
	
	public Grid(int[][] cells) {
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(grid_layout);
		
		for (int i = 0; i < 9; i++) {
			  for (int j = 0; j < 9; j++) {
				  Cell temp = new Cell(i,j,cells[i][j]);
				  this.Rows[i][j] = temp;
				  this.Cols[j][i] = temp;	
				  add(temp);
			}
		}
	}
	public void eliminate(Cell cell) {
		int row = cell.row;
		int col = cell.col;
		int val = cell.value;
		Cell[][] sets = {Rows[row], Cols[col], getBlock(cell)};
		for (Cell[] set: sets)
			for (Cell c : set) 
				if (c.value == 0 ) {
					System.out.println("eliminate row" + val);
					waitInput();
					c.possible.remove((Object) val);
					if(c.possible.size() == 1) {
						c.value = c.possible.peek();
						eliminate(c);
					}
				}
	}
	
	public void eliminateAll() {
		for (Cell[] cells : Rows) 
			for (Cell cell : cells) 
				if (cell.value != 0 )
					eliminate(cell);			
	}
	
	public void hiddenSingle(Cell[][] cells_group) {
//		search row, col, block for unique missing number in unknown group
		for (Cell[] cells : cells_group) {
			LinkedList<Integer> unique = new LinkedList<Integer>();
			LinkedList<Integer> non_unique = new LinkedList<Integer>();
			for (Cell cell : cells) {
				if (cell.value == 0 )
					for (Integer val : cell.possible)
						if (unique.remove(val) || non_unique.contains(val)) {
							non_unique.add(val);
						}else {
							unique.add(val);
						}
			}
			for (Cell cell : cells)
				if (cell.value == 0 )
					for (Integer number : unique)
						if (cell.possible.contains(number)){
							System.out.println("missing");
							waitInput();
							cell.value = number;
							eliminate(cell);
						}
		}
	}
	public void hiddenSingleAll() {
		Cell[][] temp = this.Rows.clone();
		boolean halt = false;
		while (!halt) {
			hiddenSingle(this.Rows);
			hiddenSingle(this.Cols);
			halt = temp == this.Rows;
			temp = this.Rows;
		}
		
	}
	public void nakedpair(Cell[][] cells_group) {
		for (Cell[] group : cells_group) {
			LinkedList<LinkedList<Integer>> pairs = new LinkedList<>();
			for (Cell cell : group) {
				if (cell.value == 0 && cell.possible.size() == 2 ) {
					pairs.add(cell.possible);
				}
			}
			LinkedList<LinkedList<Integer>>  nakedpair = new LinkedList<>();
			while (!pairs.isEmpty()){
				LinkedList<Integer>  pair = pairs.pop();
				if (pairs.remove(pair))
					nakedpair.add(pair);
				
			}
			for (Cell cell: group) {
				if (!nakedpair.contains(cell.possible)){
					for (LinkedList<Integer> pair : nakedpair) {
						System.out.println("nakedpair" + pair.toString());
						cell.possible.removeAll(pair) ;
					}
					if (cell.possible.size() == 1){
						cell.value = cell.possible.pop();
						eliminate(cell);
					}
				}
			}
		}
	}
	public void nakedpairAll(){
		Cell[][] temp = this.Rows.clone();
		boolean halt = false;
		while (!halt) {
			nakedpair(this.Rows);
			nakedpair(this.Cols);
			halt = temp == this.Rows;
			temp = this.Rows;
		}
	}

	public void pointingPair(){
		
	}

	public void waitInput() {
        try {
        	System.out.println(" ");
//			System.in.read();
		} catch (Exception e) {
			e.printStackTrace();
		}
		repaint();
	}
	
	public Cell[] getBlock(Cell cell) {
		Cell[] cells = new Cell[9];
		int x1 = 3 * (cell.row / 3);
		int y1 = 3 * (cell.col / 3);
		int x2 = x1 + 2;
		int y2 = y1 + 2;
		int i = 0;
		for (int x = x1; x <= x2; x++)
			for (int y = y1; y <= y2; y++){
				cells[i] = this.Rows[x][y];
				i++;
			}
		return cells;
	}
    @Override 
    public void paintComponent(Graphics g) {
    	g.setColor(Color.WHITE);
    	g.fillRect(0, 0, getWidth(), getHeight());
        Graphics2D g2 = (Graphics2D) g;
    	g.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(10));
        int width = this.getWidth();
        int height = this.getHeight();
        for (int i = 0; i < 5; i++) {
        	int x = i * ( width / 3 );
            g2.draw(new Line2D.Float(x, 0, x, height));
            int y = i * ( height / 3 );
            g2.draw(new Line2D.Float(0, y, width, y));
            
		}

    }
	
	
}
