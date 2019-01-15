import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.Arrays;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Cell extends JPanel{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int row;
	public int col;
	public int value;
	public LinkedList<Integer> possible = new LinkedList<Integer>(
			Arrays.asList(1,2,3,4,5,6,7,8,9));
	public Color color = Color.red;
	
	public Cell(int row, int col, int value)  {
		this.row = row;
		this.col = col;
		this.value = value;
		if(this.value == 0) {
		}
		setBorder(BorderFactory.createLineBorder(Color.black));
	}
	public Cell(int row, int col, int value, Color c)  {
		this(row,col,value);
		this.color = c;
	}
	
    @Override 
    public void paintComponent(Graphics g) {
    	if (this.value != 0) {
			g.setColor(this.color);
    		g.setFont(new Font("TimesRoman", Font.PLAIN, 48)); 
    		g.drawString(String.valueOf(this.value) , getWidth() / 3, (int) (getHeight() / 1.5));
    	}
    	else {
    		for (int i : possible) {
    			g.setColor(Color.BLUE);
        		g.setFont(new Font("TimesRoman", Font.PLAIN, 12)); 
				int row = 1  + ((i - 1)/ 3 );
				int col = (i + 2) % 3;				
        		g.drawString(String.valueOf(i) ,7 + col * ( getWidth() / 3 ), (int) (row * (getHeight() / 3.5)));
			}
    	}
    }
}
