import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.ImageIcon;

/*this class designed for playing games. the chess board will be initialized in this part, players could put the word
 * in the tile by turn and if the player want to vote for his word, the word can be highlighted by the system. each round
 * have only 30 second. if time is up, a new round would start.
 */
public class GameUI {

	private JFrame frame;
	private String name;
	private int[][] CBV = new int[20][20];
	private JTextField character[][] = new JTextField[20][20];
	private JButton Pass = new JButton("Pass");
	private JButton charButton[][] = new JButton[20][20];
	private JPanel chessBoardCell[][] = new JPanel[20][20];
	private CardLayout cardCell[][] = new CardLayout[20][20];
	private JTextArea playerScore = new JTextArea();
	private JPanel chessBoard = new JPanel();
	private JTextField currentPlayer;
	private JTextField timeCount;
	public ClientCommunicate gameCommunicate;
	public ClientUI clientUI;
	public static Timer timer;

	/**
	 * Create the application.
	 */
	public GameUI(ClientUI clientUI) {
		this.name = ClientLoginUI.name;
		this.clientUI = clientUI;
		gameCommunicate = clientUI.getClientCommunicate();
		gameCommunicate.setGameUI(this);
		initialize();
		timer = new Timer();
		frame.setVisible(true);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				int result = JOptionPane.showConfirmDialog(null, "Are you sure ", "result", JOptionPane.YES_NO_OPTION);
				if (result == 0) {
					gameCommunicate.endGame();
					gameCommunicate.disconnect();
				}
			}
		});
	}

	public GameUI getGameUI() {
		return this;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setForeground(Color.WHITE);
		frame.getContentPane().setForeground(Color.WHITE);
		frame.setBounds(100, 100, 929, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				CBV[i][j] = 0;
			}
		}
		chessBoard.setForeground(Color.BLACK);

		chessBoard.setBackground(Color.WHITE);
		chessBoard.setBounds(12, 64, 609, 543);
		frame.getContentPane().add(chessBoard);
		chessBoard.setLayout(new GridLayout(20, 20));
		playerScore.setFont(new Font("Bahnschrift", Font.PLAIN, 18));
		playerScore.setForeground(Color.GREEN);
		playerScore.setBackground(new Color(255, 255, 255));

		playerScore.setBounds(693, 110, 195, 384);
		frame.getContentPane().add(playerScore);

		JButton Exit = new JButton("");
		Exit.setForeground(Color.GRAY);
		Image EixtGame = new ImageIcon(this.getClass().getResource("/EixtGame.png")).getImage();
		Exit.setIcon(new ImageIcon(EixtGame));
		Exit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameCommunicate.endGame();
				frame.dispose();
				clientUI.gameEnd();
				clientUI.setVisible(true);
			}
		});
		Exit.setBounds(773, 507, 62, 60);
		frame.getContentPane().add(Exit);
		Pass.setBackground(Color.BLACK);
		Pass.setFont(new Font("Bahnschrift", Font.BOLD | Font.ITALIC, 18));
		Pass.setForeground(Color.RED);

		Pass.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameCommunicate.pass();
			}
		});

		Pass.setBounds(246, 620, 126, 40);
		frame.getContentPane().add(Pass);

		currentPlayer = new JTextField();
		currentPlayer.setBounds(86, 30, 105, 24);
		frame.getContentPane().add(currentPlayer);
		currentPlayer.setColumns(10);

		JLabel CurrentPlayer = new JLabel("");
		Image player = new ImageIcon(this.getClass().getResource("/sportsman.png")).getImage();
		CurrentPlayer.setIcon(new ImageIcon(player));
		CurrentPlayer.setFont(new Font("Tahoma", Font.PLAIN, 15));
		CurrentPlayer.setBounds(24, 13, 48, 50);
		frame.getContentPane().add(CurrentPlayer);

		timeCount = new JTextField();
		timeCount.setBounds(766, 46, 96, 31);
		frame.getContentPane().add(timeCount);
		timeCount.setColumns(10);

		JLabel countdownLabel = new JLabel("");
		Image timecount = new ImageIcon(this.getClass().getResource("/timecount.png")).getImage();
		countdownLabel.setIcon(new ImageIcon(timecount));
		countdownLabel.setFont(new Font("Tahoma", Font.PLAIN, 15));
		countdownLabel.setBounds(691, 30, 144, 60);
		frame.getContentPane().add(countdownLabel);

		// chess board initialize
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				cardCell[i][j] = new CardLayout();
				charButton[i][j] = new JButton();
				character[i][j] = new JTextField();
				character[i][j].setFont(new Font("Bahnschrift", Font.BOLD, 25));
				chessBoardCell[i][j] = new JPanel();
				chessBoardCell[i][j].setLayout(cardCell[i][j]);
				chessBoardCell[i][j].add(charButton[i][j]);
				chessBoardCell[i][j].add(character[i][j]);
				chessBoard.add(chessBoardCell[i][j]);
				int x = i;
				int y = j;

				charButton[i][j].addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						boolean flag = true;
						while (flag) {
							String str = JOptionPane.showInputDialog("Please input a value");
							if (str.length() == 1 && str.charAt(0) <= 'z' && str.charAt(0) >= 'a') {
								character[x][y].setText(str);
								cardCell[x][y].next(chessBoardCell[x][y]);
								CBV[x][y] = 1;
								new VotingWindow(getGameUI(), gameCommunicate, x, y, str); // player start voting
								setButtonFalse();
								break;
							} else {
								JOptionPane.showMessageDialog(null, "Please input character a to z");
							}
						}
					}
				});
			}
		}
	}

	public void updateBoardWithoutVote(int x, int y, String input) {
		character[x][y].setText(input);
		cardCell[x][y].next(chessBoardCell[x][y]);
		CBV[x][y] = 1;
	}

	// update the chess board
	public boolean updateBoardHighlightVote(String command, int x, int y, String input) {
		CBV[x][y] = 1;
		character[x][y].setText(input);
		cardCell[x][y].next(chessBoardCell[x][y]);
		timer.cancel();
		timeCount.setText("");
		if (command.equals("row")) {
			character[x][y].setText(input);
			highlightRow(x, y);
		}
		if (command.equals("column")) {
			character[x][y].setText(input);
			highlightColum(x, y);
		}
		if (command.equals("both")) {
			character[x][y].setText(input);
			highlightRow(x, y);
			highlightColum(x, y);
		}
		int flag = JOptionPane.showConfirmDialog(null, "Do you agree?", "Voting", JOptionPane.YES_NO_OPTION);
		if (flag == 0) {
			return true;
		} else {
			return false;
		}
	}

	public void updateScoreTable(ArrayList playerInfo) {
		playerScore.setText("");
		for (int i = 0; i < playerInfo.size(); i++) {
			playerScore.append(playerInfo.get(i) + "\n");
		}
	}

	// when new round come, the timer start time count down
	public void newRound(String name) {
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				character[i][j].setBackground(Color.white);
			}
		}
		timer.cancel();
		timer = new Timer();
		boolean myTurn;
		if (this.name.equals(name))
			myTurn = true;
		else
			myTurn = false;
		TimerTask task = new TimerTask() {
			private int count = 31;

			@Override
			public void run() {
				this.count--;
				timeCount.setText(Integer.toString(count));
				;
				if (count == 0) {
					timeCount.setText("");
					timer.cancel();
					if (myTurn == true)
						gameCommunicate.pass();
				}
				Pass.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						timeCount.setText("");
						timer.cancel();
					}
				});
			}
		};
		timer.schedule(task, 0, 1000);
		currentPlayer.setText(name);
		if (this.name.equals(name) != true) {
			setButtonFalse();
			frame.getContentPane().setBackground(Color.black);
		} else {
			setButtonTrue();
			frame.getContentPane().setBackground(Color.red);
		}

	}

	public void gameover(String winner) {
		timer.cancel();
		int result = JOptionPane.showConfirmDialog(null, "the winner is: " + winner, "result",
				JOptionPane.YES_NO_OPTION);
		if (result == 0) {
			frame.dispose();
			clientUI.gameEnd();
			clientUI.setVisible(true);
		}
	}

	public void highlightRow(int x, int y) {
		System.out.println(CBV[x][y]);
		CBV[x][y] = 1;
		for (int i = 0; i < y + 1; i++) {
			if (CBV[x][y - i] == 1) {
				System.out.println(x);
				character[x][y - i].setBackground(Color.yellow);
			} else {
				break;
			}
		}
		for (int i = 0; i < 20 - y; i++) {
			if (CBV[x][y + i] == 1) {
				character[x][y + i].setBackground(Color.yellow);
			} else {
				break;
			}
		}
	}

	public void highlightColum(int x, int y) {
		for (int i = 0; i < x + 1; i++) {
			if (CBV[x - i][y] == 1) {
				character[x - i][y].setBackground(Color.yellow);
			} else {
				break;
			}
		}
		for (int i = 0; i < 20 - y; i++) {
			if (CBV[x + i][y] == 1) {
				character[x + i][y].setBackground(Color.yellow);
			} else {
				break;
			}
		}
	}

	public void setButtonFalse() {
		Pass.setEnabled(false);
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				charButton[i][j].setEnabled(false);
			}
		}
	}

	public void setButtonTrue() {
		Pass.setEnabled(true);
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 20; j++) {
				charButton[i][j].setEnabled(true);
			}
		}
	}
}
