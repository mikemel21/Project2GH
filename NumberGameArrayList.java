package Project2;


import java.util.ArrayList;
import java.util.Arrays;

public class NumberGameArrayList implements NumberSlider {
	// IDK if these will work or are helpful 
	// instance variables:
	private int height, width, winningVal;
	
	// ArrayList that holds cells on board
	ArrayList<Cell> boardCells = new ArrayList<Cell>();
	int[][] gridArray = new int[height][width];
	
	// ArrayList that holds the non empty cells
	ArrayList<Cell> nonEmptyCells = new ArrayList<Cell>();
	
	// 2D array for gridSize
	int[][] boardArray = new int[getWidth()][getHeight()];
	
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
		// sets values
		setWidth(width);
		setHeight(height);
		
		// reset game logic
		
		// set new winning value
		if (winningValue % 2 == 0 || winningValue > 0) {
			// set new winning value
			setWinningVal(winningValue);
		} else {
			// throw IAE if new winning value is not even or is negative
			throw new IllegalArgumentException();
		}
		
		// resize the board
		setValues(boardArray); // TEST LATER (PROBABLY WRONG)
	}

	@Override
	public void reset() {

	}

	@Override
	public void setValues(int[][] ref) {
		for (int r = 0; r < ref.length; r++) {
			for (int c = 0; c < ref.length; c++) {
				gridArray[r][c] = ref[r][c]; 
			}
		}
		//TESTING PURPOSES: System.out.println(Arrays.deepToString(gridArray));
	}

	@Override
	public Cell placeRandomValue() {
		Cell c = new Cell();
		// set value randomly between 1 or 2
		c.setValue((int) Math.random() * 2);
		
		// find an empty tile on grid
		
		
		// return
		return c;
	}

	@Override
	public boolean slide(SlideDirection dir) {
		return false;
	}

	@Override
	public ArrayList<Cell> getNonEmptyTiles() {
		/*
		 loop that goes on for size of boardCells.
		 go through each cell and check if its value is 0.
		 if value is not 0, add it to nonEmptyCells array list.
		 */
		for (int i = 0; i < boardCells.size(); i++) {
			if (boardCells.get(i).getValue() != 0) {  
				nonEmptyCells.add(boardCells.get(i));
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
