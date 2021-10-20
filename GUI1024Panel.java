package project2;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

public class GUI1024Panel extends JPanel {

	private JLabel[][] gameBoardUI;
	private NumberGameArrayList gameLogic;
	private int nRows, nCols;
	private JButton upButton, downButton, leftButton, rightButton;
	private JPanel playPanel, gamePanel;

	private JMenu options;
	public JMenuBar menuBar;

	public JLabel currentScoreLabel = new JLabel();
	public JLabel highScoreLabel = new JLabel();
	public JLabel sessionsPlayedLabel = new JLabel();
	public JLabel numSlidesLabel = new JLabel();
	public JLabel numWinsLabel = new JLabel();

	// Options: JMenu Items
	private JMenuItem resetItem;
	private JMenuItem exitItem;
	private JMenuItem changeGoalScoreItem;
	private JMenuItem changeBoardSizeItem;

	public JPanel p;

	private String boardSize;
	private int winningVal = 2048;
	private int highScore = 0;
	private int playedSessions = 0;
	private int numSlides = 0;
	private int numWins = 0;
	

	public GUI1024Panel() { 
		// Use helper function to initialize game
		// This lets us reuse the function to allow the user to
		// change the board size or goal
		initializeGame();
		LabelPanel();
	}

	private void initializeGame() {
		boardSize = JOptionPane.showInputDialog(null, "Choose values for rows and columns (ROWxCOL): ");
		while(!isValidBoardSize(boardSize)) {
			if(boardSize == null) {
				System.exit(0);
			}
			JOptionPane.showMessageDialog(null, "Please enter a valid board size. Please try again.");
			boardSize = JOptionPane.showInputDialog(null, "Choose values for rows and columns (ROWxCOL): ");
		}
		
		// Initialize GridBagLayoutConstraints
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		// Indicate which column
		c.gridx = 0; 
		// Indicate which row
		c.gridy = 0;
		// Indicate number of columns to span
		//c.gridwidth = 1;
		// Specify custom height
		//c.ipady = 40;

		// Initialize the game board appearance
		setBorder(BorderFactory.createLineBorder(Color.ORANGE));
		setLayout(new GridBagLayout());
		playPanel = new JPanel();
		playPanel.setLayout(new GridBagLayout());
		add(playPanel);
		// Initialize the game panel
		gamePanel = new JPanel();
		playPanel.add(gamePanel, c); 	

		// Allow keys to be pressed to control the game
		setFocusable(true);
		addKeyListener(new SlideListener());

		// Initialize the game GUI and logic
		resizeBoard();
		//gameLogic.resizeBoard(Integer.parseInt(firstBoard[0]), Integer.parseInt(firstBoard[1]), 512);

		// Initialize Menu
		menuBar = new JMenuBar();
		options = new JMenu("Options");
		resetItem = new JMenuItem("Reset");
		exitItem = new JMenuItem ("Exit");
		changeGoalScoreItem = new JMenuItem("Change your goal score");
		changeBoardSizeItem = new JMenuItem("Change your board size");
		
		options.add(resetItem);
		options.add(changeGoalScoreItem);
		options.add(changeBoardSizeItem);
		options.add(exitItem);

		menuBar.add(options);
		resetItem.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				gameLogic.reset();
				playedSessions++;
				updateBoard();
			}
		});
		exitItem.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				System.exit(0);
			}
		});
		changeGoalScoreItem.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				String newValue = JOptionPane.showInputDialog(null, "Enter new goal: ");
				if(newValue == null) {
					return;
				}
				try {
					gameLogic.resizeBoard(nRows, nCols, Integer.parseInt(newValue));
					winningVal = Integer.parseInt(newValue);
					gameLogic.reset();
					playedSessions++;
					updateBoard();
				} 
				catch (NumberFormatException a) {
					JOptionPane.showMessageDialog(null, "Value must be a whole number! Please try again.");
				} 
				catch (IllegalArgumentException b) {
					JOptionPane.showMessageDialog(null, "Value must be a power of two greater than 4! Please try again.");
				}
			}
		});
		changeBoardSizeItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boardSize = JOptionPane.showInputDialog(null, "Choose values for rows and columns (ROWxCOL): ");
				while(!isValidBoardSize(boardSize)) {
					if(boardSize == null) {
						return;
					}
					JOptionPane.showMessageDialog(null, "Please enter a valid board size. Please try again.");
					boardSize = JOptionPane.showInputDialog(null, "Choose values for rows and columns (ROWxCOL): ");
				}
				for (int k = 0; k < nRows; k++) {
					for (int m = 0; m < nCols; m++) {

						//c.fill = GridBagConstraints.HORIZONTAL;
						// Indicate which column 
						c.gridx = m;
						// Indicate which row
						c.gridy = k;
						// Indicate number of columns to span
						c.gridwidth = 1;
						// Specify custom height
						//						c.ipady = 40;
						gamePanel.remove(gameBoardUI[k][m]);
					}
				}
				resizeBoard();
			}
		});
	}

	public void LabelPanel() {
		// Initialize GridBagLayoutConstraints
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		// Indicate which column
		c.gridx = 0; 
		// Indicate which row
		c.gridy = 0;

		// Initialize the game board appearance
		setLayout(new GridBagLayout());
		p = new JPanel();
		p.setLayout(new GridBagLayout());
		add(p);

		c.gridx = 0;
		c.gridy = 1;
		p.add(currentScoreLabel, c);

		c.gridx = 0;
		c.gridy = 2;
		p.add(highScoreLabel, c);

		c.gridx = 0;
		c.gridy = 3;
		p.add(sessionsPlayedLabel, c);
		
		c.gridx = 0;
		c.gridy = 4;
		p.add(numSlidesLabel, c);
		
		c.gridx = 0;
		c.gridy = 5;
		p.add(numWinsLabel, c);
	}


	private void updateBoard() {
		currentScoreLabel.setText("Current Score: " + getCurrentScore());
		if (highScore < getCurrentScore()) {
			highScore = getCurrentScore();
		} 
		highScoreLabel.setText("High Score: " + highScore);
		sessionsPlayedLabel.setText("Sessions Played: " + playedSessions);
		numSlidesLabel.setText("Number of Slides: " + numSlides);
		numWinsLabel.setText("Number of Wins: " + numWins);

		for (JLabel[] row : gameBoardUI)
			for (JLabel s : row) {
				s.setText("");
				s.setBackground(Color.GRAY.brighter());
				s.setOpaque(true);
			}

		ArrayList<Cell> out = gameLogic.getNonEmptyTiles();
		if (out == null) {
			JOptionPane.showMessageDialog(null, "Incomplete implementation getNonEmptyTiles()");
			return;
		}
		for (Cell c : out) { 
			JLabel z = gameBoardUI[c.getRow()][c.getColumn()];
			z.setText(String.valueOf(Math.abs(c.getValue())));
			
			// changing tile colors based on value (Dynamic implementation)
			int green = (int)(-256.0 / winningVal * c.getValue() + 256);
			z.setBackground(new Color(255, green, 0));
		}
	}

	private class SlideListener implements KeyListener, ActionListener {
		@Override
		public void keyTyped(KeyEvent e) { }

		@Override
		public void keyPressed(KeyEvent e) {
			boolean moved = false;
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				moved = gameLogic.slide(SlideDirection.UP);
				break;
			case KeyEvent.VK_LEFT:
				moved = gameLogic.slide(SlideDirection.LEFT);
				break;
			case KeyEvent.VK_DOWN:
				moved = gameLogic.slide(SlideDirection.DOWN);
				break;
			case KeyEvent.VK_RIGHT:
				moved = gameLogic.slide(SlideDirection.RIGHT);
				break;
			case KeyEvent.VK_U:
				try {
					System.out.println("Attempt to undo");
					gameLogic.undo();
					moved = true;
				} catch (IllegalStateException exp) {
					JOptionPane.showMessageDialog(null, "Can't undo beyond the first move");
					moved = false;
				}
			}
			if (moved) {
				numSlides++;
				updateBoard();
				if (gameLogic.getStatus().equals(GameStatus.USER_WON)) {
					numWins++;
					JOptionPane.showMessageDialog(null, "You won");
					int resp = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "YOU WON",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (resp == JOptionPane.YES_OPTION) {
						gameLogic.reset();
						playedSessions += 1;
						updateBoard();
					} else {
						System.exit(0);
					}
				} else if (gameLogic.getStatus().equals(GameStatus.USER_LOST)) {
					int resp = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "TentOnly Over!",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (resp == JOptionPane.YES_OPTION) {
						gameLogic.reset();
						playedSessions += 1;
						updateBoard();
					} else {
						System.exit(0);
					}
				} 
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			
		}

		@Override
		public void actionPerformed(ActionEvent e) {
		}
	}

	public void resizeBoard() {
		
		// Initialize the game logic
		nRows = Integer.parseInt(boardSize.split("x")[0]);
		nCols = Integer.parseInt(boardSize.split("x")[1]);

		gameLogic = new NumberGameArrayList();
		
		//checks if winningVal has been changed, used for changing board size while keeping old winningVal
//		if(winningVal != 0) {
//			gameLogic.setWinningVal(winningVal);
//		}
		gameLogic.resizeBoard(nRows, nCols, winningVal); 

		// Update the GUI
		// Start with changing the panel size and creating a new
		// gamePanel
		setSize(new Dimension(100*(nCols), 100*(nRows)));
		gamePanel.setLayout(new GridLayout(nRows, nCols));
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;

		// Initialize the game board GUI
		gameBoardUI = new JLabel[nRows][nCols];

		Font myTextFont = new Font(Font.SERIF, Font.BOLD, 40);
		for (int k = 0; k < nRows; k++) {
			for (int m = 0; m < nCols; m++) {
				gameBoardUI[k][m] = new JLabel(); 
				gameBoardUI[k][m].setFont(myTextFont);
				gameBoardUI[k][m].setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
				gameBoardUI[k][m].setPreferredSize(new Dimension(100, 100));
				gameBoardUI[k][m].setMinimumSize(new Dimension(100, 100));

				//						c.fill = GridBagConstraints.HORIZONTAL;
				// Indicate which column 
				c.gridx = m;
				// Indicate which row
				c.gridy = k;
				// Indicate number of columns to span
				c.gridwidth = 1;
				// Specify custom height
				//						c.ipady = 40;
				gamePanel.add(gameBoardUI[k][m]);
			}
		}

		gameLogic.reset();
		updateBoard();
	}
	
	private boolean isValidBoardSize(String boardSize) {
		if(boardSize == null) return false;
		if(boardSize.split("x").length == 2) {
			try {
				int tempNumRows = Integer.parseInt(boardSize.split("x", 2)[0]);
				int tempNumCols = Integer.parseInt(boardSize.split("x", 2)[1]);
				if(tempNumRows > 1 && tempNumCols > 1) {
					return true;
				}
			}
			catch (NumberFormatException a) {
				return false;
			}
		}
		return false;
	}
	
	private int getCurrentScore() {
		int highest = 0;

	    for (int s = 0; s < gameLogic.getNonEmptyTiles().size(); s++){
	        int curValue = gameLogic.getNonEmptyTiles().get(s).getValue();
	        if (curValue > highest) {
	            highest = curValue;
	        }
	    }
		return highest;
	}
}