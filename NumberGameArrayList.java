/*****************************************************************
Class with logic required to make a 1024/2048 game

@author Jayson Willey, Michael Melei
@version Fall 2021
 *****************************************************************/
package project2;

import java.util.ArrayList;
import java.util.Random;

import project2.Cell;

public class NumberGameArrayList implements NumberSlider {

	/** current number of rows of the board */
	private int numRows;
	
	/** current number of column of the board */
	private int numCols;
	
	/** current value required to win */
	private int winningVal;

	/** ArrayList for all cells on the board */ 
	private ArrayList<Cell> cells;

	/** ArrayList that holds the different states of the board */
	private ArrayList< ArrayList<Cell> > boardStates = new ArrayList< ArrayList<Cell> >();
	
	/** the status of the game described as a GameStatus enum */
	private GameStatus gameStatus;

	/**
	 * Default constructor creates a NumberGameArrayList with cells set
	 * to an empty arrayList and winningVal set to 1024
	 */
	public NumberGameArrayList () {
		cells = new ArrayList<Cell>();
		winningVal = 1024;
	}

	/**
	 * Reset the game logic to handle a board of a given dimension
	 *
	 * @param height the number of rows in the board
	 * @param width the number of columns in the board
	 * @param winningValue the value that must appear on the board to
	 *                     win the game
	 * @throws IllegalArgumentException when the winning value is not power of two
	 *  or is negative
	 */
	@Override
	public void resizeBoard(int numRows, int numCols, int winningVal) {
		if((numRows < 2 || numCols < 2) ||
				!isPowerTwo(winningVal) ||
				winningVal <= 4) {
			throw new IllegalArgumentException();
		}
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

	/**
	 * Remove all numbered tiles from the board and place
	 * TWO non-zero values at random location
	 */
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
		boardStates.clear();
	}

	/**
	 * Set the game board to the desired values given in the 2D array.
	 * This method should use nested loops to copy each element from the
	 * provided array to your own internal array. Do not just assign the
	 * entire array object to your internal array object. Otherwise, your
	 * internal array may get corrupted by the array used in the JUnit
	 * test file. This method is mainly used by the JUnit tester.
	 * @param ref
	 */
	@Override
	public void setValues(int[][] ref) {

		//loops through the 2d array and sets the value of the cell at that location to that value.
		for (int row = 0; row < ref.length; row++) {
			for (int col = 0; col < ref[row].length; col++) {
				getCellAt(row, col).setValue(ref[row][col]);
			}
		}
	}

	/**
	 * Insert one random tile into an empty spot on the board.
	 *
	 * @return a Cell object with its row, column, and value attributes
	 *  initialized properly
	 *
	 * @throws IllegalStateException when the board has no empty cell
	 */
	@Override
	public Cell placeRandomValue() {
		Cell randCell = getRandomCell();
		if(randCell == null) {
			throw new IllegalStateException();
		}
		else {
			randCell.setValue(getRandomValue());
			return randCell;
		}
	}

	/**
	 * Slide all the tiles in the board in the requested direction
	 * The value should be the number 2 or 4 (random)
	 * @param dir move direction of the tiles
	 *
	 * @return true when the board changes
	 */
	@Override
	public boolean slide(SlideDirection dir) {
		boolean moved = false;

		ArrayList<Cell> currentBoardState = new ArrayList<Cell>();
		for(Cell c : getNonEmptyTiles()) {
			currentBoardState.add(new Cell(c.getRow(), c.getColumn(), c.getValue()));
		}

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
			boardStates.add(currentBoardState);
		}

		return moved;
	}

	/**
	 *
	 * @return an arraylist of Cells. Each cell holds the (row,column) and
	 * value of a tile
	 */
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

	/**
	 * Return the current state of the game
	 * @return one of the possible values of GameStatus enum
	 */
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
	 * Undo the most recent action, i.e. restore the board to its previous
	 * state. Calling this method multiple times will ultimately restore
	 * the game to the very first initial state of the board holding two
	 * random values. Further attempt to undo beyond this state will throw
	 * an IllegalStateException.
	 *
	 * @throws IllegalStateException when undo is not possible
	 */
	@Override
	public void undo() {
		if(boardStates.size() == 0) {
			throw new IllegalStateException();
		}
		else {
			for(Cell c : cells) {
				c.setValue(0);
			}
			ArrayList<Cell> boardState = boardStates.remove(boardStates.size() - 1);
			for(Cell c : boardState) {
				getCellAt(c.getRow(), c.getColumn()).setValue(c.getValue());
			}
		}
	}

	/**
	 * checks if an integer is a power of two
	 * 
	 * @param z the integer to check
	 * @return true if z is a power of two, false otherwise
	 */
	private boolean isPowerTwo(int z) {
		int v = 1;
		while(v < z)
			v *= 2;
		return v == z;
	}

	/**
	 * checks the cell ArrayList to see if any cell as the winning value
	 * 
	 * @return true if a cell has the winning value and false if no cells 
	 * have the winning value
	 */
	private boolean hasWinningValue () {
		for (int i = 0; i < getNonEmptyTiles().size(); i++) {
			if (getNonEmptyTiles().get(i).getValue() >= winningVal) {
				return true;
			} 
		}
		return false;
	}

	/**
	 * Checks if a move is possible in the current board state.
	 * This is a helper method for the getStatus method
	 * 
	 * @return true if a move is possible, false otherwise
	 */
	private boolean movePossible() {
		if(horizontalMergePossible() || verticalMergePossible()) {
			return true;
		}
		else{
			return !(getNonEmptyTiles().size() == cells.size());
		}
	}

	/**
	 *Checks if any tiles can merge horizontally directly next to each 
	 *other. In other words, checks if the value of one cell is the
	 *same as the cell directly to it's right. This is a helper method
	 *for the getStatus method
	 * 
	 * @return true if horizontal merge is possible
	 */
	private boolean horizontalMergePossible() {
		for(int row = 0; row < numRows; row++) {
			for(int col = 0; col < numCols - 1; col++) {
				if(getCellAt(row, col).getValue() == 
						getCellAt(row, col + 1).getValue()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 *Checks if any tiles can merge vertically directly next to each 
	 *other. In other words, checks if the value of one cell is the
	 *same as the cell directly above. This is a helper method
	 *for the getStatus method
	 * 
	 * @return true if vertical merge is possible
	 */
	private boolean verticalMergePossible() {
		for(int col = 0; col < numCols; col++) {
			for(int row = 0; row < numRows - 1; row++) {
				if(getCellAt(row, col).getValue() == 
						getCellAt(row + 1, col).getValue()) {
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
		if(row >= numRows || col >= numCols) {
			throw new IllegalArgumentException();
		}

		int index = numCols * row + col;
		return cells.get(index);
	}

	/** 
	 * Makes random choice between 1, 2, and 4 for placeRandomvalue() method 
	 * 
	 * @return either a 1, 2, or 4. There is a 45% chance to return both 
	 * a 1 or 2, and a 10% chance it returns a 4.
	 * */
	private int getRandomValue() {
		Random r = new Random();

		if(r.nextInt(10) < 9) {
			if(r.nextInt(2) < 1) {
				return 1;
			}
			else {
				return 2;
			}
		}
		else {
			return 4;
		}
	}

	/**
	 * returns an arrayList of all the empty cells on the board. Helper method
	 * for placeRandomValue
	 *
	 * @return an arrayList of Cells. Each cell holds the (row,column) and
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
	 * returns a random index of an empty tile. returns -1 if board is full.
	 * Helper method for placeRandomValue
	 * 
	 * @return random index of getEmptyTiles(). returns -1 if 
	 * getEmptyTiles() is empty.
	 */
	private int getRandomIndex() {
		Random r = new Random();
		if(getEmptyTiles().size() > 0) {
			return r.nextInt(getEmptyTiles().size());
		}
		else {

			//only happens when there are no empty tiles
			return -1;
		}
	}

	/**
	 * returns a random empty cell. returns null if there are no empty cells.
	 * Helper method for placeRandomValue
	 * 
	 * @return random empty cell in cells. returns null if there are no empty cells.
	 */
	private Cell getRandomCell() {
		int randIndex = getRandomIndex();
		if(randIndex == -1) {

			//should only happen when board is full
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
				if(getCellAt(row, col).getValue() != 0) {
					for(int i = 1; col + i < accessibleCol; i++) {
						if(getCellAt(row, col + i).getValue() == 
								getCellAt(row, col + i - 1).getValue()) {
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
				if(getCellAt(row, col).getValue() != 0) {
					for(int i = 1; row + i < accessibleRow; i++) {
						if(getCellAt(row + i, col).getValue() == 
								getCellAt(row + i - 1, col).getValue()) {
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
				if(getCellAt(row, col).getValue() != 0) {
					for(int i = 1; col - i > accessibleCol; i++) {
						if(getCellAt(row, col - i).getValue() == 
								getCellAt(row, col - i + 1).getValue()) {
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
				if(getCellAt(row, col).getValue() != 0) {
					for(int i = 1; row - i > accessibleRow; i++) {
						if(getCellAt(row - i, col).getValue() == 
								getCellAt(row - i + 1, col).getValue()) {
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