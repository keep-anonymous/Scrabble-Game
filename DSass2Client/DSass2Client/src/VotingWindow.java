import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

public class VotingWindow {

	private JFrame frame;
	private GameUI gameUI;
	private int x;
	private int y;
	private ClientCommunicate gameCommunicate;
	private String character;

	/**
	 * Create the application.
	 */
	public VotingWindow(GameUI gameUI, ClientCommunicate gameCommunicate, int x, int y, String character) {
		this.gameUI = gameUI;
		this.gameCommunicate = gameCommunicate;
		this.x = x;
		this.y = y;
		this.character = character;
		gameUI.timer.cancel();
		initialize();
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

	public void setGameUI(GameUI gameUI) {
		this.gameUI = gameUI;
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 532, 392);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton row = new JButton("row");
		row.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameCommunicate.updateWithVote("row", x, y, character);
				frame.dispose();
			}
		});
		row.setBounds(31, 111, 113, 27);
		frame.getContentPane().add(row);

		JButton column = new JButton("column");
		column.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameCommunicate.updateWithVote("column", x, y, character);
				frame.dispose();
			}
		});
		column.setBounds(203, 111, 113, 27);
		frame.getContentPane().add(column);

		JButton both = new JButton("both");
		both.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameCommunicate.updateWithVote("both", x, y, character);
				frame.dispose();
			}
		});
		both.setBounds(372, 111, 113, 27);
		frame.getContentPane().add(both);

		JButton No = new JButton("No");
		No.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameCommunicate.updateWithoutVote(x, y, character);
				frame.dispose();
			}
		});
		No.setBounds(203, 211, 113, 27);
		frame.getContentPane().add(No);
	}
}
