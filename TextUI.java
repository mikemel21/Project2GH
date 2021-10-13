package Project2;


import java.util.Scanner;

import javax.swing.JOptionPane;

public class TextUI {
    private NumberSlider game;
    private int[][] grid; 
    private static int CELL_WIDTH = 3;
    private static String NUM_FORMAT, BLANK_FORMAT; 
    private Scanner inp; 

    public TextUI() {
        game = new NumberGameArrayList();

        if (game == null) {
            System.err.println ("*---------------------------------------------*");
            System.err.println ("| You must first modify the UI program.       |");
            System.err.println ("| Look for the first TODO item in TextUI.java |");
            System.err.println ("*---------------------------------------------*");
            System.exit(0xE0);
        }
        game.resizeBoard(4, 4, 64);
        grid = new int[4][4];

        /* Set the string to %4d */
        NUM_FORMAT = String.format("%%%dd", CELL_WIDTH + 1);

        /* Set the string to %4s, but without using String.format() */
        BLANK_FORMAT = "%" + (CELL_WIDTH + 1) + "s";
        inp = new Scanner(System.in);
    }

    private void renderBoard() {
        /* reset all the 2D array elements to ZERO */
        for (int k = 0; k < grid.length; k++)
            for (int m = 0; m < grid[k].length; m++)
                grid[k][m] = 0;

        // =========================================================================
        // TODO
        /* fill in the 2D array using information for non-empty tiles */
        // =========================================================================
        for (int i = 0; i < game.getNonEmptyTiles().size(); i++) {
        	for (int r = 0; r < grid.length; r++) {
        		for (int c = 0; c < grid[r].length; c++) {
        			if (game.getNonEmptyTiles().get(i).getRow() == r && game.getNonEmptyTiles().get(i).getColumn() == c) {
        				grid[r][c] = game.getNonEmptyTiles().get(i).getValue();
        			}
        		}
        	}
        }
        
        /* Print the 2D array using dots and numbers */
        for (int k = 0; k < grid.length; k++) {
            for (int m = 0; m < grid[k].length; m++)
                if (grid[k][m] == 0)
                    System.out.printf (BLANK_FORMAT, ".");
                else
                    System.out.printf (NUM_FORMAT, grid[k][m]);
            System.out.println();
        }
    }

    /**
     * The main loop for playing a SINGLE game session. Notice that
     * the following method contains NO GAME LOGIC! Its main task is
     * to accept user input and invoke the appropriate methods in the
     * game engine.
     */
    public void playLoop() {
        /* Place the first two random tiles */
        game.placeRandomValue();
        game.placeRandomValue();
    	renderBoard();

        /* To keep the right margin within 75 columns, we split the
           following long string literal into two lines
         */
        System.out.print ("Slide direction (W, S, Z, A), " +
                "[U]ndo or [Q]uit? ");


        // =========================================================================
        // TODO
        // loop on:
        //        Get user input and slide up, down, left, right
        //        renderBoard
        // =========================================================================
        
        //MAKE IT A FOR LOOP
        
    }

    public static void main(String[] arg) {
        TextUI t = new TextUI();
        t.playLoop();
    }
}



