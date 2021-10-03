package Project2;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class NumberGameArrayList implements NumberSlider {

	// instance variables:
	private int height, width, winningVal; 

	private ArrayList<Cell> cells;
	private ArrayList<Cell> nonEmptyCells;

	public NumberGameArrayList () {
		cells = new ArrayList<Cell>();
		nonEmptyCells = new ArrayList<Cell>();
	}
	
	public int getWinningVal() {
		return winningVal;
	}
	public void setWinningVal(int winningVal) {
		this.winningVal = winningVal;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	
	@Override
	public void resizeBoard(int height, int width, int winningValue) {
		setHeight(height);
		setWidth(width);
		
		//setValues(new int[getHeight()][getWidth()]);
		for (int r = 0; r < getHeight(); r++) {
			for (int c = 0; c < getWidth(); c++) {
				//getCellAt(r, c);
				cells.add(new Cell(r, c, 0));
			}
		}		
		
		// reset game logic
		reset();
		
				
		// set new winning value
//		if (winningValue % 2 == 0) {
//			setWinningVal(winningValue);
//		} else {
			// throws IAE if winning value is not even
//			throw new IllegalArgumentException(); 
//		}
	}

	@Override
	public void reset() {
		placeRandomValue();
	}

	@Override
	public void setValues(int[][] ref) {
	}

	@Override
	public Cell placeRandomValue() {
		Random r = new Random(); 
		Cell c = new Cell();
		
		// random row
		int ranRow = getRandomRow();
		// random col
		int ranCol = getRandomColumn();
		// random value
		int ranVal = getRandomValue(); 
		
		for (int i = 0; i < getWidth(); i++) {
			for (int j = 0; j < getHeight(); j++) {
				if (getCellAt(i, j).getValue() == 0) {
					getCellAt(ranRow, ranCol).setValue(ranVal);
				} else {
					ranRow = getRandomRow();
					ranCol = getRandomColumn();
				}
			}
		}
		
		return c;
	}
	
	/** 
	 * Makes random choice between 2 and 4 for placeRandomvalue() method 
	 * 
	 * @return int: random cell value
	 * */
	public int getRandomValue() {
		Random r = new Random();
		// chooses an int of either 2, 3, or 4
		int randomValue = ThreadLocalRandom.current().nextInt(2,4);
		// runs if the randomValue is 3 
		while (randomValue == 3) {
			randomValue = ThreadLocalRandom.current().nextInt(2,4);
		}
		
		return randomValue;
	}
	
	/** 
	 * gets a random number between 0 and the amount of rows (getWidth())
	 * 
	 * @return int: value used for a random row
	 * */
	public int getRandomRow () {
		int randomRow = ThreadLocalRandom.current().nextInt(0, getWidth());
		return randomRow;
	}
	
	/** 
	 * gets a random number between 0 and the amount of columns (getHeight())
	 * 
	 * @return int: value used for a random column
	 * */
	public int getRandomColumn() {
		int randomColumn = ThreadLocalRandom.current().nextInt(0, getHeight());
		return randomColumn;
	}

	@Override
	public boolean slide(SlideDirection dir) { 
		return false;
	}

	// 2d array to linear arraylist
	private Cell getCellAt (int row, int col) {
		int index = 4 * row + col;
		
		return cells.get(index);
	}
	
	@Override
	public ArrayList<Cell> getNonEmptyTiles() {
//		for (int r = 0; r < getHeight(); r++) { 
//			for (int c = 0; c < getWidth(); c++) {
//				if (getCellAt(r, c).getValue() != 0) {   
//					nonEmptyCells.add(new Cell(getCellAt(r,c).getRow(), getCellAt(r,c).getColumn(), getCellAt(r,c).getValue())); 
//				}
//			} 
//		}
		// TESTING PURPOSES cells.add(new Cell(1,1,2));
		for (Cell c: cells) {
			if (c.getValue() != 0) {
				nonEmptyCells.add(c);
			}
		}
		
		// return ArrayList of cells that have a value
		return nonEmptyCells;
	}

	@Override
	public GameStatus getStatus() {
		return null;
	}

	@Override
	public void undo() {
	} 
}
