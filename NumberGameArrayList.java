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
	private GameStatus gameStatus;
	
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
		gameStatus = GameStatus.IN_PROGRESS;
		
//		int[][] ref = {
//				{8, 4, 2, 4,},
//				{2, 2, 4, 2,},
//				{4, 4, 2, 4,},
//				{0, 2, 4, 2,}
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
		Cell randCell = getRandomCell();
		if(randCell == null) {
			//this should only happen if there were no empty squares, will implement later
			return null;
		}
		else {
			randCell.setValue(getRandomValue());
			return randCell;
		}
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
		// checks if hasWinningValue() is true (if any cells have the winning value)
		if (hasWinningValue()) {
			gameStatus = GameStatus.USER_WON;
		} 
		// then checks if all tiles have a nonzero value and if the winning value ISNT on the board
		else if (!movePossible()) {
			gameStatus = GameStatus.USER_LOST;
		}
		else {
			gameStatus = GameStatus.IN_PROGRESS;
		}
		return gameStatus;
	}
	
	/**
	 * checks the cell ArrayList to see if any cell as the winning value
	 * @return true if a cell has the winning value and false if no cells have the winning value
	 */
	public boolean hasWinningValue () {
		for (int i = 0; i < getNonEmptyTiles().size(); i++) {
			if (getNonEmptyTiles().get(i).getValue() >= winningVal) {
				return true;
			} 
		}
		return false;
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
	
	private boolean movePossible() {
		if(horizontalMergePossible() || verticleMergePossible()) {
			return true;
		}
		else{
			return !(getNonEmptyTiles().size() == cells.size());
		}
	}
	
	private boolean horizontalMergePossible() {
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols - 1; col++) {
				if(getCellAt(row, col).getValue() == getCellAt(row, col + 1).getValue()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean verticleMergePossible() {
		for(int col = 0; col < numCols; col++) {
			for(int row = 0; row < numRows - 1; row++) {
				if(getCellAt(row, col).getValue() == getCellAt(row + 1, col).getValue()) {
					return true;
				}
			}
		}
		return false;
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
	 * @return either a 2 or 4, with a 90% chance of it returning a 2
	 * */
	private int getRandomValue() {
		Random r = new Random();
		
		if(r.nextInt(10) < 9) {
			return 2;
		}
		else {
			return 4;
		}
	}
	
	/**
	 *
	 * @return an arraylist of Cells. Each cell holds the (row,column) and
	 * value of an empty tile
	 */
	private ArrayList<Cell> getEmptyTiles(){
		ArrayList<Cell> emptyTiles = new ArrayList<Cell>();
		for (Cell c: cells) {
			if (c.getValue() == 0) {
				emptyTiles.add(c);
			}
		}
		return emptyTiles;
	}
	
	/**
	 * 
	 * @return random index of getEmptyTiles(). returns -1 if getEmptyTiles() is empty.
	 */
	private int getRandomIndex() {
		Random r = new Random();
		if(getEmptyTiles().size() > 0) {
			return r.nextInt(getEmptyTiles().size());
		}
		else {
			
			//should only happen when there are no empty tiles
			return -1;
		}
	}
	
	/**
	 * 
	 * @return random empty cell in cells. returns null if there are no empty cells.
	 */
	private Cell getRandomCell() {
		int randIndex = getRandomIndex();
		if(randIndex == -1) {
			
			//should only happen when board is full, will implement later
			return null;
		}
		else {
			int randRow = getEmptyTiles().get(randIndex).getRow();
			int randCol = getEmptyTiles().get(randIndex).getColumn();
			return getCellAt(randRow, randCol);
		}
	}
	
	/**
	 * slides all tiles right, helper method for slide() method
	 * 
	 * @return true when the board changes
	 */
	private boolean slideRight() {
		boolean moved = false;
		for(int row = 0; row < numRows; row++) {
			
			//The farthest right column that can be used. Used to ensure a tile doesn't merge twice
			int accessibleCol = numCols;
			for(int col = numCols - 2; col >= 0; col--) {
				if(getNonEmptyTiles().contains(getCellAt(row, col))) {
					for(int i = 1; col + i < accessibleCol; i++) {
						if(getCellAt(row, col + i).getValue() == getCellAt(row, col + i - 1).getValue()) {
							getCellAt(row, col + i).setValue(2 * getCellAt(row, col + i).getValue());
							
							//sets the accessibleCol to the current column
							accessibleCol = col + i;
						}
						else if(getCellAt(row, col + i).getValue() == 0){
							getCellAt(row, col + i).setValue(getCellAt(row, col + i - 1).getValue());
						}
						else {
							break;
						}
						getCellAt(row, col + i - 1).setValue(0);
						moved = true;
					}
				}
			}
		}
		return moved;
	}
	
	/**
	 * slides all tiles down, helper method for slide() method
	 * 
	 * @return true when the board changes
	 */
	private boolean slideDown() {
		boolean moved = false;
		for(int col = 0; col < numCols; col++) {
			
			//The farthest right column that can be used. Used to ensure a tile doesn't merge twice
			int accessibleRow = numRows;
			for(int row = numRows - 2; row >= 0; row--) {
				if(getNonEmptyTiles().contains(getCellAt(row, col))) {
					for(int i = 1; row + i < accessibleRow; i++) {
						if(getCellAt(row + i, col).getValue() == getCellAt(row + i - 1, col).getValue()) {
							getCellAt(row + i, col).setValue(2 * getCellAt(row + i, col).getValue());
							
							//sets the accessibleCol to the current column
							accessibleRow = row + i;
						}
						else if(getCellAt(row + i, col).getValue() == 0){
							getCellAt(row + i, col).setValue(getCellAt(row + i - 1, col).getValue());
						}
						else {
							break;
						}
						getCellAt(row + i - 1, col).setValue(0);
						moved = true;
					}
				}
			}
		}
		return moved;
	}
	
	/**
	 * slides all tiles left, helper method for slide() method
	 * 
	 * @return true when the board changes
	 */
	private boolean slideLeft() {
		boolean moved = false;
		for(int row = 0; row < numRows; row++) {
			
			//The farthest right column that can be used. Used to ensure a tile doesn't merge twice
			int accessibleCol = -1;
			for(int col = 1; col < numCols; col++) {
				if(getNonEmptyTiles().contains(getCellAt(row, col))) {
					for(int i = 1; col - i > accessibleCol; i++) {
						if(getCellAt(row, col - i).getValue() == getCellAt(row, col - i + 1).getValue()) {
							getCellAt(row, col - i).setValue(2 * getCellAt(row, col - i).getValue());
							
							//sets the accessibleCol to the current column
							accessibleCol = col - i;
						}
						else if(getCellAt(row, col - i).getValue() == 0) {
							getCellAt(row, col - i).setValue(getCellAt(row, col - i + 1).getValue());
						}
						else {
							break;
						}
						getCellAt(row, col - i + 1).setValue(0);
						moved = true;
					}
				}
			}
		}
		return moved;
	}
	
	/**
	 * slides all tiles up, helper method for slide() method
	 * 
	 * @return true when the board changes
	 */
	private boolean slideUp() {
		boolean moved = false;
		for(int col = 0; col < numCols; col++) {
			
			//The farthest right column that can be used. Used to ensure a tile doesn't merge twice
			int accessibleRow = -1;
			for(int row = 1; row < numRows; row++) {
				if(getNonEmptyTiles().contains(getCellAt(row, col))) {
					for(int i = 1; row - i > accessibleRow; i++) {
						if(getCellAt(row - i, col).getValue() == getCellAt(row - i + 1, col).getValue()) {
							getCellAt(row - i, col).setValue(2 * getCellAt(row - i, col).getValue());
							
							//sets the accessibleCol to the current column
							accessibleRow = row - i;
						}
						else if(getCellAt(row - i, col).getValue() == 0){
							getCellAt(row - i, col).setValue(getCellAt(row - i + 1, col).getValue());
						}
						else {
							break;
						}
						getCellAt(row - i + 1, col).setValue(0);
						moved = true;
					}
				}
			}
		}
		return moved;
	}
}