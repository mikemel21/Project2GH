package project2;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import project2.Cell;

public class NumberGameArrayList implements NumberSlider {

	// instance variables:
	private int numRows, numCols, winningVal;
	private ArrayList<Cell> cells;
	private ArrayList< ArrayList<Cell> > boardStates = new ArrayList< ArrayList<Cell> >();
	private GameStatus gameStatus = GameStatus.IN_PROGRESS;

	public NumberGameArrayList () {
		cells = new ArrayList<Cell>();
	}
	
//	private void setGameStatus(GameStatus gameStatus) {
//		this.gameStatus = gameStatus;
//	}
	
	@Override
	public void resizeBoard(int numRows, int numCols, int winningVal) {
		cells = new ArrayList<Cell>();
		
		//adds a new cell for each row and column.
		for (int r = 0; r < numRows; r++) {
			for (int c = 0; c < numCols; c++) {
				//getCellAt(r, c);
				cells.add(new Cell(r, c, 0));
			}
		}
		
		//sets new values to instance variables
		this.numRows = numRows;
		this.numCols = numCols;
		this.winningVal = winningVal;
	}

	@Override
	public void reset() {
		
		//sets all cells to 0, regardless of current value.
		for(int i = 0; i < cells.size(); i++) {
			cells.get(i).setValue(0);
		}
		
		//places 2 random values
		placeRandomValue();
		placeRandomValue();
		
//		int[][] ref = {
//				{4, 4, 8, 8, 0, 2, 2, 4},
//				{2, 2, 0, 4, 2, 2, 0, 4},
//				{2, 2, 2, 2, 2, 2, 2, 2},
//				{2, 2, 2, 4, 2, 2, 2, 4},
//				{2, 2, 2, 4, 2, 0, 0, 0},
//				{2, 2, 0, 4, 0, 2, 2, 0},
//				{2, 2, 8, 4, 2, 4, 2, 4},
//				};
//		setValues(ref);
	}
 
	@Override
	public void setValues(int[][] ref) {
		
		//loops through the 2d array and sets the value of the cell at that location to that value.
		for (int row = 0; row < ref.length; row++) {
			for (int col = 0; col < ref[row].length; col++) {
				getCellAt(row, col).setValue(ref[row][col]);
			}
		}
	}

	@Override
	public Cell placeRandomValue() {
		int randVal = 0;
		int randRow = 0;
		int randCol = 0;
		for(int i = 0; i < cells.size(); i++) {
			randVal = getRandomValue();
			randRow = getRandomRow();
			randCol = getRandomCol();
			if(getCellAt(randRow, randCol).getValue() == 0) {
				getCellAt(randRow, randCol).setValue(randVal);
				return getCellAt(randRow, randCol);
			}
		}
		
		//this should only happen if there were no empty squares, will implement later
		return null;
	}

	@Override
	public boolean slide(SlideDirection dir) {
		boolean moved = false;
		
		ArrayList<Cell> currentBoardState = new ArrayList<Cell>();
		for(Cell c : cells) {
			currentBoardState.add(new Cell(c.getRow(), c.getColumn(), c.getValue()));
		}
		boardStates.add(currentBoardState);
		
		if(dir == SlideDirection.RIGHT) {
			moved = slideRight();
		}
		else if(dir == SlideDirection.DOWN) {
			moved = slideDown();
		}
		else if(dir == SlideDirection.LEFT) {
			moved = slideLeft();
		}
		else {
			moved = slideUp();
		}
		if(moved) {
			placeRandomValue();
		}
		else {
			boardStates.remove(boardStates.size() - 1);
		}
		return moved;
	}
	
	@Override
	public ArrayList<Cell> getNonEmptyTiles() {
		ArrayList<Cell> nonEmptyTiles = new ArrayList<Cell>();
		for (Cell c: cells) {
			if (c.getValue() != 0) {
				nonEmptyTiles.add(c);
			}
		}
		return nonEmptyTiles;
	}

	@Override
	public GameStatus getStatus() {
		return null;
	}

	@Override
	public void undo() {
		if(boardStates.size() == 0) {
			throw new IllegalStateException();
		}
		else {
			cells = boardStates.remove(boardStates.size() - 1);
		}
	}
	
	/**
	 * Converts 2d array to linear arrayList
	 * 
	 * @param row the row of the Cell
	 * @param col the col of the Cell
	 * @return the cell at the appropriate index
	 */
	private Cell getCellAt (int row, int col) {

		//we will probably need exceptions thrown here to make sure row and col are good inputs
		int index = numCols * row + col;

		return cells.get(index);
	}

	/** 
	 * Makes random choice between 2 and 4 for placeRandomvalue() method 
	 * 
	 * @return int: random cell value
	 * */
	private int getRandomValue() {
		Random r = new Random();
		
		if(r.nextInt(10) < 9) {
			return 2;
		}
		else {
			return 4;
		}
		//returns random integer of either 2 or 4
//		return (r.nextInt(2) + 1) * 2;
	}

	/** 
	 * returns random integer from 0 to numRows - 1
	 * 
	 * @return int: value used for a random row
	 * */
	private int getRandomRow() {
		Random r = new Random();

		//return random integer from 0 to numRows - 1
		return r.nextInt(numRows);
	}

	/** 
	 * returns random integer from 0 to numCols - 1
	 * 
	 * @return int: value used for a random column
	 * */
	private int getRandomCol() {
		Random r = new Random();

		//return random integer from 0 to numCols - 1
		return r.nextInt(numCols);
	}
	
	
	private boolean slideRight() {
		boolean moved = false;
		for(int row = 0; row < numRows; row++) {
			
			//The farthest right column that can be used. Used to ensure a tile doesn't merge twice
			int accessibleCol = numCols;
			for(int col = numCols - 2; col >= 0; col--) {
				if(getNonEmptyTiles().contains(getCellAt(row, col))) {
					int i = 1;
					
					//checks if the next column is accessible, and checks if the next value is either 0 or the same value
					while(col + i < accessibleCol 
							&& (getCellAt(row, col + i).getValue() == 0 
							|| getCellAt(row, col + i).getValue() == getCellAt(row, col + i - 1).getValue())) {

						if(getCellAt(row, col + i).getValue() == getCellAt(row, col + i - 1).getValue()) {
							getCellAt(row, col + i).setValue(2 * getCellAt(row, col + i).getValue());
							getCellAt(row, col + i - 1).setValue(0);
							
							//sets the accessibleCol to the current column
							accessibleCol = col + i;
						}
						else {
							getCellAt(row, col + i).setValue(getCellAt(row, col + i - 1).getValue());
							getCellAt(row, col + i - 1).setValue(0);
							i++;
						}
						moved = true;
					}
				}
			}
		}
		return moved;
	}
	
	private boolean slideDown() {
		boolean moved = false;
		for(int col = 0; col < numCols; col++) {
			
			//The farthest right column that can be used. Used to ensure a tile doesn't merge twice
			int accessibleRow = numRows;
			for(int row = numRows - 2; row >= 0; row--) {
				if(getNonEmptyTiles().contains(getCellAt(row, col))) {
					int i = 1;
					
					//checks if the next column is accessible, and checks if the next value is either 0 or the same value
					while(row + i < accessibleRow 
							&& (getCellAt(row + i, col).getValue() == 0 
							|| getCellAt(row + i, col).getValue() == getCellAt(row + i - 1, col).getValue())) {

						if(getCellAt(row + i, col).getValue() == getCellAt(row + i - 1, col).getValue()) {
							getCellAt(row + i, col).setValue(2 * getCellAt(row + i, col).getValue());
							getCellAt(row + i - 1, col).setValue(0);
							
							//sets the accessibleCol to the current column
							accessibleRow = row + i;
						}
						else {
							getCellAt(row + i, col).setValue(getCellAt(row + i - 1, col).getValue());
							getCellAt(row + i - 1, col).setValue(0);
							i++;
						}
						moved = true;
					}
				}
			}
		}
		return moved;
	}
	
	private boolean slideLeft() {
		boolean moved = false;
		for(int row = 0; row < numRows; row++) {
			
			//The farthest right column that can be used. Used to ensure a tile doesn't merge twice
			int accessibleCol = -1;
			for(int col = 1; col < numCols; col++) {
				if(getNonEmptyTiles().contains(getCellAt(row, col))) {
					int i = 1;
					
					//checks if the next column is accessible, and checks if the next value is either 0 or the same value
					while(col - i > accessibleCol 
							&& (getCellAt(row, col - i).getValue() == 0 
							|| getCellAt(row, col - i).getValue() == getCellAt(row, col - i + 1).getValue())) {

						if(getCellAt(row, col - i).getValue() == getCellAt(row, col - i + 1).getValue()) {
							getCellAt(row, col - i).setValue(2 * getCellAt(row, col - i).getValue());
							getCellAt(row, col - i + 1).setValue(0);
							
							//sets the accessibleCol to the current column
							accessibleCol = col - i;
						}
						else {
							getCellAt(row, col - i).setValue(getCellAt(row, col - i + 1).getValue());
							getCellAt(row, col - i + 1).setValue(0);
							i++;
						}
						moved = true;
					}
				}
			}
		}
		return moved;
	}
	
	private boolean slideUp() {
		boolean moved = false;
		for(int col = 0; col < numCols; col++) {
			
			//The farthest right column that can be used. Used to ensure a tile doesn't merge twice
			int accessibleRow = -1;
			for(int row = 1; row < numRows; row++) {
				if(getNonEmptyTiles().contains(getCellAt(row, col))) {
					int i = 1;
					
					//checks if the next column is accessible, and checks if the next value is either 0 or the same value
					while(row - i > accessibleRow 
							&& (getCellAt(row - i, col).getValue() == 0 
							|| getCellAt(row - i, col).getValue() == getCellAt(row - i + 1, col).getValue())) {

						if(getCellAt(row - i, col).getValue() == getCellAt(row - i + 1, col).getValue()) {
							getCellAt(row - i, col).setValue(2 * getCellAt(row - i, col).getValue());
							getCellAt(row - i + 1, col).setValue(0);
							
							//sets the accessibleCol to the current column
							accessibleRow = row - i;
						}
						else {
							getCellAt(row - i, col).setValue(getCellAt(row - i + 1, col).getValue());
							getCellAt(row - i + 1, col).setValue(0);
							i++;
						}
						moved = true;
					}
				}
			}
		}
		return moved;
	}
}