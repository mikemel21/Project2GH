package project2;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import project2.Cell;

public class NumberGameArrayList implements NumberSlider {

	// instance variables:
	private int numRows, numCols, winningVal; 

	private ArrayList<Cell> cells;

	public NumberGameArrayList () {
		cells = new ArrayList<Cell>();
	}
	
//	public int getWinningVal() {
//		return winningVal;
//	}
//	
//	public void setWinningVal(int winningVal) {
//		this.winningVal = winningVal;
//	}
//	
//	public int getNumRows() {
//		return numRows;
//	}
//	
//	public void setNumRows(int numRows) {
//		this.numRows = numRows;
//	}
//	
//	public int getNumCols() {
//		return numCols;
//	}
//	
//	public void setNumCols(int numCols) {
//		this.numCols = numCols;
//	}
	
	@Override
	public void resizeBoard(int numRows, int numCols, int winningVal) {
		
		//adds a new cell for each row and column. I think this might mess up if we resize during the game.
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
		return false;
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

		//returns random integer of either 2 or 4
		return (r.nextInt(2) + 1) * 2;
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
}
