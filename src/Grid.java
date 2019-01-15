import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Line2D;

import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.xml.namespace.QName;

public class Grid extends JPanel  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	GridLayout grid_layout = new GridLayout(9,9);
	Cell[][] Rows = new Cell[9][9];
	Cell[][] Cols = new Cell[9][9];
	Cell[][] Blocks = new Cell[9][9];
	
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
		for (int i = 0; i < 3; i++)
			for (int j = 0; j < 3; j++)
			Blocks[i*3 + j] = getBlock(Rows[i*3][j*3]);
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
					// waitInput();
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
	
	public Boolean hiddenSingle(Cell[][] cells_group) {
//		search row, col, block for unique missing number in unknown group 
// 		return True if there is change
		Boolean isChange = false;
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
							isChange = true;
							System.out.println("missing");
							// waitInput();
							cell.value = number;
							eliminate(cell);
						}
		}
		return isChange;
	}
	public boolean hiddenSingleAll() {
		boolean halt = false;
		while (!halt) {
			halt = !(hiddenSingle(this.Rows) ||
			hiddenSingle(this.Cols) ||
			hiddenSingle(this.Blocks));
		}
		return halt;
		
	}
	public Boolean nakedpair(Cell[][] cells_group) {
		Boolean isChange = false;
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
						isChange = isChange || cell.possible.removeAll(pair) ;
					}
					if (cell.possible.size() == 1){
						cell.value = cell.possible.pop();
						eliminate(cell);
					}
				}
			}
		}
		return isChange;
	}
	public Boolean nakedpairAll(){
		boolean halt = false;
		while (!halt) {
			halt = !(nakedpair(this.Blocks) ||
			nakedpair(this.Rows) ||
			nakedpair(this.Cols));
		}
		return halt;
	}

	public boolean pointingPair(){
		boolean isChange = false;
		for (Cell[] group : Blocks)
			for (int i = 0; i < group.length; i++) {
				Cell cell1 = group[i];
				if (cell1.value == 0){
					for (Integer number : cell1.possible) {
						int link = 0;
						for (int j = i; j < group.length; j++) {
							Cell cell2  = group[j];
							if (cell2.value == 0 &&
							cell2.possible.contains(number)){
								link++;
								if (check_pointing(cell1, cell2, number))
									for (Cell curCell : group)
										if (curCell != cell1 && curCell != cell2){
											isChange = isChange  || curCell.possible.remove(number);
											if (curCell.possible.size() == 1 && curCell.value == 0){
												curCell.value = curCell.possible.pop();
												eliminate(curCell);
											}
										}

								}
						}
							if ( link == 1 ) {
								for (Cell curCell : group)
									if (curCell != cell1 && curCell.equals(cell1)){
									}
								}
								
					}
				}
		}
		repaint();
		return !isChange;
	}
	public Boolean check_pointing(Cell cell1, Cell cell2, Integer number){
		// return true if no candidate in row or col
		if (cell1.row == cell2.row) {

			for (Cell curCell : Rows[cell1.row]) 
				if (curCell.value == 0 &&
				curCell != cell1 && 
				curCell != cell2 &&
				curCell.possible.contains(number))
					return false;
		} else if (cell1.col == cell2.col){
			for (Cell curCell : Cols[cell1.col]) 
			if (curCell.value == 0 &&
			curCell != cell1 && 
			curCell != cell2 &&
			curCell.possible.contains(number))
					return false;
		} else return false;
		System.out.println("pointing " + cell1.row + cell1.col + number);
			
		return true;
	}
	public boolean runAll() {
		boolean halt = false;
		while (!halt) {
			halt = (hiddenSingleAll() && 
			nakedpairAll() &&
			pointingPair());
			repaint();
		}
		return halt;
	}

	public void waitInput() {
        try {
        	System.out.println(" ");
			System.in.read();
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
