package Project2;

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
	public JMenuBar mb;
	
	public JLabel curScore = new JLabel();
	public JLabel highScore = new JLabel();
	public JLabel sessionsPlayed = new JLabel ("Games Played: ");
	
	// Options: JMenu Items
	private JMenuItem reset;
	private JMenuItem exit;
	private JMenuItem changeGoalScore;
	// private JMenuItem resizeTheBoard;
	
	public JPanel p;
	
	// set Board Size (FIX CANCEL OPTION)
	private String boardsi;
	private String[] firstBoard;
	
	private int highscore;
	private int playedSessions;

	public GUI1024Panel() { 
		// Use helper function to initialize game
		// This lets us reuse the function to allow the user to
		// change the board size or goal
		initializeGame();
		LabelPanel();
	}

	private void initializeGame() {
		boardsi = JOptionPane.showInputDialog(null, "Choose values for rows and columns (ROWxCOL): ");
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
		mb = new JMenuBar();
		options = new JMenu("Options");
		reset = new JMenuItem("Reset");
		exit = new JMenuItem ("Exit");
		changeGoalScore = new JMenuItem("Change your goal score");
		
		options.add(reset);
		
		options.add(changeGoalScore);
		options.add(exit);
		
		mb.add(options);
		reset.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				gameLogic.reset();
				updateBoard();
			}});
		exit.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				System.exit(0);
			}});
		changeGoalScore.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				String newValue = JOptionPane.showInputDialog(null, "Enter new goal: ");
				try {
					gameLogic.setWinningVal(Integer.parseInt(newValue));
				} catch (NumberFormatException a) {
					JOptionPane.showMessageDialog(null, "Value must be a whole number! Please try again.");
				} catch (IllegalArgumentException b) {
					JOptionPane.showMessageDialog(null, "Value must be positive! Please try again.");
				}
				updateBoard();
			}});
		
		
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
		curScore.setText("current score: ");
		p.add(curScore, c);

		c.gridx = 0;
		c.gridy = 2;
		highScore.setText("high score: ");
		p.add(highScore, c);
		
		
		c.gridx = 0;
		c.gridy = 2;
		p.add(sessionsPlayed);
	}
	

	private void updateBoard() {
		curScore.setText("current score: " + gameLogic.getCurrentScore());
		if (highscore < gameLogic.getCurrentScore()) {
			highscore = gameLogic.getCurrentScore();
		} 
		highScore.setText("high score: " + highscore);
		sessionsPlayed.setText("Sessions played: " + playedSessions);
		
		for (JLabel[] row : gameBoardUI)
			for (JLabel s : row) {
				s.setText("");
				s.setBackground(Color.GRAY);
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
			int green = -255/gameLogic.getWinningVal()*c.getValue() + 255;
			z.setBackground(new Color(255, (int) green, 0));
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
				gameLogic.currentScore = gameLogic.getCurrentScore();
				updateBoard();
				
				if (gameLogic.getStatus().equals(GameStatus.USER_WON)) {
					JOptionPane.showMessageDialog(null, "You won");
					int resp = JOptionPane.showConfirmDialog(null, "Do you want to play again?", "YOU WON",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (resp == JOptionPane.YES_OPTION) {
						gameLogic.reset();
						playedSessions += 1;
						gameLogic.currentScore = 0;
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
						gameLogic.currentScore = 0;
						updateBoard();
					} else {
						System.exit(0);
					}
				} 
			}
		}

		@Override
		public void keyReleased(KeyEvent e) { }

		@Override
		public void actionPerformed(ActionEvent e) {
		}
	}

	public void resizeBoard() {
		
		// Initialize the game logic
		nRows = Integer.parseInt(boardsi.split("x")[0]);
		nCols = Integer.parseInt(boardsi.split("x")[1]);

		gameLogic = new NumberGameArrayList(); 
		gameLogic.currentScore = 0;
		gameLogic.resizeBoard(nRows, nCols, 16); 

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
}
