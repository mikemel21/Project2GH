package Project2;

import java.util.ArrayList;
import java.util.Random;

public class NumberGameArrayList implements NumberSlider {

	// IDK if these will work or are helpful 
	// instance variables:
	private int height, width, winningVal;
	
	// 2D array for gridSize
	int[][] boardArray;

	private ArrayList<Cell> cells = new ArrayList<Cell>();
	private ArrayList<Cell> nonEmptyCells;

//	NumberGameArrayList () {
//		// ArrayList that holds the non empty cells 
//		ArrayList<Cell> nonEmptyCells = new ArrayList<Cell>();
//	}
	
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
		// reset game logic
		reset();
		
		// sets values 
		setValues(new int[height][width]);
		for (int r = 0; r < height; r++) {
			for (int c = 0; c < width; c++) {
				cells.add(new Cell (r, c, 0));
			}
		} 
		
		
		// set new winning value
		if (winningValue % 2 == 0) {
			setWinningVal(winningValue);
		} else {
			// throws IAE if winning value is not even
			throw new IllegalArgumentException();
		}
	}

	@Override
	public void reset() {
		placeRandomValue();
		placeRandomValue();
	}

	@Override
	public void setValues(int[][] ref) {
//		for (int r = 0; ) {
//			for () {
//				
//			}
//		}
	}

	@Override
	public Cell placeRandomValue() {
		Random r = new Random(); 
		Cell c = new Cell();
		
		
		return c;
	}

	@Override
	public boolean slide(SlideDirection dir) { 
		return false;
	}

	@Override
	public ArrayList<Cell> getNonEmptyTiles() {
		nonEmptyCells = new ArrayList<Cell>();
		
		for (int i = 0; i < cells.size(); i++) {
			if (cells.get(i).getValue() != 0) {
				nonEmptyCells.add(cells.get(i));
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
