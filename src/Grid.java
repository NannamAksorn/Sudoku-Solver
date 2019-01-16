import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Grid extends JPanel  {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static int counter = 0;
	GridLayout grid_layout = new GridLayout(9,9);
	Cell[][] Rows = new Cell[9][9];
	Cell[][] Cols = new Cell[9][9];
	Cell[][] Blocks = new Cell[9][9];
	
	public Grid(int[][] cells) {
		setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(grid_layout);
		
		for (int i = 0; i < 9; i++) {
			  for (int j = 0; j < 9; j++) {
				Cell temp = null;
				  if (cells[i][j] != 0){
					temp = new Cell(i,j,cells[i][j],Color.BLACK);
				  }else{  
				  	temp = new Cell(i,j,cells[i][j]);
				  }
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
					// System.out.println("eliminate row" +c.row+c.col+ val);
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
							// System.out.println("hiddenSingle" + cell.row + cell.col + number);
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
			halt = !(
			hiddenSingle(this.Rows) ||
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
						// System.out.println("nakedpair" + cell.row + cell.col + pair.toString());
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
		boolean isChange = false;
		isChange = (nakedpair(this.Blocks) ||
			nakedpair(this.Rows) ||
			nakedpair(this.Cols));
		return isChange;
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
											boolean removed = curCell.possible.remove(number);
											if (removed){
												// System.out.println("pointing " + curCell.row + curCell.col + number);
											}
											isChange = isChange  || removed;
											// linked list change error
											// if (curCell.possible.size() == 1 && curCell.value == 0){
											// 	curCell.value = curCell.possible.pop();
											// 	eliminate(curCell);
											// }
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
		// repaint();
		return isChange;
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
		return true;
	}
	public boolean hiddenPair(Cell[][] groups) {
		boolean isChange = false;
		for (Cell[] group : groups){
			int[] count = new int[9];
			ArrayList<Integer> doubleAppearance = new ArrayList<>();
			Arrays.fill(count, 0);
			for (Cell cell : group)
				if (cell.value == 0)
					for (Integer number : cell.possible)
						count[number-1]++;
			for (int i = 0; i < 9; i++)
				if (count[i] == 2) 
					doubleAppearance.add(i+1);
			ArrayList<ArrayList<Integer>> indexList = combine(doubleAppearance.size(), 2);
			for (ArrayList<Integer> indexPair : indexList) {
				int first = doubleAppearance.get(indexPair.get(0) - 1);
				int second = doubleAppearance.get(indexPair.get(1) - 1);
				LinkedList<Integer> pair = new LinkedList<Integer>(Arrays.asList(first, second));
				LinkedList<Integer> pair2 = new LinkedList<Integer>(Arrays.asList(first, second));
				LinkedList<Cell> cellPair = new LinkedList<>();
				for (Cell cell : group)
					if (cell.value == 0 &&
						cell.possible.containsAll(pair)){
							cellPair.add(cell);
					}
				if (cellPair.size() == 2) {
					isChange = isChange || 
								(cellPair.get(0).possible.size() != 2 || 
								cellPair.get(1).possible.size() != 2);
					cellPair.get(0).possible = pair;
					cellPair.get(1).possible = pair2;
					// System.out.println("hiddenPair" + cellPair.get(0).row + cellPair.get(1).col + pair);
					// waitInput();
				}
			}				
		}
		return isChange;
	}
	public Boolean hiddenPairAll(){
		boolean isChange = false;
		isChange = (
			hiddenPair(this.Blocks) ||
			hiddenPair(this.Rows) ||
			hiddenPair(this.Cols));
		return isChange;
	}

	public ArrayList<ArrayList<Integer>> combine(int n, int k) {
		ArrayList<ArrayList<Integer>> result = new ArrayList<ArrayList<Integer>>();
		if (n <= 0 || n < k)
			return result;
	 
		ArrayList<Integer> item = new ArrayList<Integer>();
		dfs(n, k, 1, item, result);
	 
		return result;
	}
	 
	private void dfs(int n, int k, int start, ArrayList<Integer> item,
			ArrayList<ArrayList<Integer>> res) {
		if (item.size() == k) {
			res.add(new ArrayList<Integer>(item));
			return;
		}	 
		for (int i = start; i <= n; i++) {
			item.add(i);
			dfs(n, k, i + 1, item, res);
			item.remove(item.size() - 1);
		}
	}

	public boolean x_wing() {
		boolean isChange = false;
		for (Cell[] group : Rows) {
			int[] count = new int[9];
			ArrayList<Integer> doubleAppearance = new ArrayList<>();
			Arrays.fill(count, 0);
			for (Cell cell : group)
				if (cell.value == 0)
					for (Integer number : cell.possible)
						count[number-1]++;
			for (int i = 0; i < 9; i++)
				if (count[i] == 2) 
					doubleAppearance.add(i+1);
			for (int number : doubleAppearance){
				ArrayList<Cell> cell1 = new ArrayList<>();
				for (Cell cell : group)
					if (cell.value == 0 && cell.possible.contains(number))
						cell1.add(cell);
				if (cell1.size() != 2) continue;
				for (Cell cell21 : Cols[cell1.get(0).col]) {
					if (cell21.value == 0 && cell21 != cell1.get(0)){
						Cell cell22 = Rows[cell21.row][cell1.get(1).col];
						if (
							cell22.value == 0
							&& cell21.possible.contains(number)
							&& cell22.possible.contains(number)
							&& checkX_Wing(Rows[cell21.row], number, new Cell[]{cell21,cell22})
						){
							// System.out.println("del" + number + ", col," + (cell21.col + 1));
							for(Cell delCell: Cols[cell21.col])
								if (delCell.value == 0 && delCell != cell21 && delCell != cell1.get(0)){
									isChange =  delCell.possible.remove((Object) number);
									if(delCell.possible.size() == 1) {
										delCell.value = delCell.possible.peek();
										eliminate(delCell);
										return isChange;
									}
								}
							// System.out.println("del" + number + ", col," + (cell22.col + 1));
							for(Cell delCell: Cols[cell22.col]){
								if (delCell.value == 0 && delCell != cell22 && delCell != cell1.get(1)){
									isChange =  delCell.possible.remove((Object) number);
									if(delCell.possible.size() == 1) {
										delCell.value = delCell.possible.peek();
										eliminate(delCell);
										return isChange;
									}
								}
								
							}
							if (isChange){
								return isChange;
								// System.out.println(
								// 	"X-wing"
								// 	+ "number " + number
								// 	+ " x1 " + cell1.get(0).row + "," +  cell1.get(0).col
								// 	+ " x2 "  + cell1.get(1).row + "," +  cell1.get(1).col
								// 	+ " y1 " + cell21.row + "," + cell21.col
								// 	+ " y2 " + cell22.row + "," + cell22.col
								// );
							}
						}
					}
				}
			}
					
		}	

		return isChange;
	}
	public boolean checkX_Wing(Cell[] group, int number, Cell[] except){
		search: for (Cell cell : group){
			for (Cell xcell : except)
				if (xcell == cell) break search;
			if (cell.value == 0 && cell.possible.contains(number))
				return false;
		}
		return true;
	}
	
	public boolean runAll() {
		boolean halt = false;
		while (!halt) {
			halt = (
			hiddenSingleAll() 
			&& !nakedpairAll()
			&& !pointingPair()
			&& !hiddenPairAll()
			);
			 repaint();
		}
		if (x_wing()){
			repaint();
			runAll();
		}
		return halt;
	}

	public void waitInput() {
        try {
			// System.in.read();
			counter++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(counter);
		// repaint();
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

	public int printBlackCount(){
		int blankCount = 0;
		for (int i = 0; i < 9; i++)
			for (int j = 0; j < 9; j++) 
				if(Rows[i][j].value == 0)
					blankCount++;
		System.out.println(blankCount);
		return blankCount;
	}

	public boolean checkGroup(Cell[][] groups){
		for (Cell[] group : groups){
			int count = 0;
			int sum = 0;
			for(Cell cell : group){
				count++;
				sum += cell.value;
			}
			if (count == 9)
				return true;
			if (sum != 45)
				return false;
		}
		return true;
	}
	public boolean checkAll(){
		return checkGroup(Rows) && checkGroup(Cols) && checkGroup(Blocks);
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
