
/**
 * Author:LIUYI CHAI
 * Team:Microsoft Fans
 * Date:12/10/2018
 * Purpose:This class is a UI for users to invite other players before the game starts
 */
import java.awt.Image;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JList;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.awt.SystemColor;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ClientUI extends JFrame {
	private JPanel contentPane;
	private JList<String> list = new JList();
	private List<JLabel> playerIconList = new ArrayList<JLabel>();
	private ClientCommunicate communicate;
	private int optionInt = 0;
	private GameUI gameUI;
	private JButton btnStart;
	private JButton btnleave;

	/**
	 * Launch the application.
	 */

	/**
	 * Create the frame.
	 */

	// disconnect the soket when closing the window by click the "x"
	public ClientUI(ClientCommunicate communicate) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {

				communicate.disconnect();
				System.exit(0);

			}
		});
		this.communicate = communicate;
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(590, 409);
		setLocationRelativeTo(null);
		contentPane = new JPanel();
		contentPane.setBackground(Color.WHITE);
		setContentPane(contentPane);
		contentPane.setLayout(null);

		btnleave = new JButton("");
		btnleave.setBorderPainted(false);
		btnleave.setEnabled(false);
		Image leaveimg = new ImageIcon(this.getClass().getResource("/exit.png")).getImage();
		btnleave.setIcon(new ImageIcon(leaveimg));
		btnleave.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				communicate.leaveGameRoom();
				for (int j = 0; j < 8; j++) {
					playerIconList.get(j).setVisible(false);
				}
				btnStart.setEnabled(false);
				btnleave.setEnabled(false);
			}
		});
		btnleave.setBounds(503, 52, 32, 29);
		contentPane.add(btnleave);

		JLabel lblOnlinePlayers = new JLabel("Online players");
		lblOnlinePlayers.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblOnlinePlayers.setForeground(Color.WHITE);
		lblOnlinePlayers.setBackground(Color.DARK_GRAY);
		lblOnlinePlayers.setOpaque(true);
		lblOnlinePlayers.setBounds(16, 34, 128, 19);
		contentPane.add(lblOnlinePlayers);

		JLabel lblplayer1 = new JLabel("");
		lblplayer1.setForeground(Color.WHITE);
		lblplayer1.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		lblplayer1.setBounds(186, 85, 61, 90);
		contentPane.add(lblplayer1);

		JLabel lblplayer2 = new JLabel("");
		lblplayer2.setForeground(Color.WHITE);
		lblplayer2.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		lblplayer2.setBounds(278, 85, 61, 90);
		contentPane.add(lblplayer2);

		JLabel lblplayer3 = new JLabel("");
		lblplayer3.setForeground(Color.WHITE);
		lblplayer3.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		lblplayer3.setBounds(365, 85, 61, 90);
		contentPane.add(lblplayer3);

		JLabel lblplayer4 = new JLabel("");
		lblplayer4.setForeground(Color.WHITE);
		lblplayer4.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		lblplayer4.setBounds(452, 85, 61, 90);
		contentPane.add(lblplayer4);

		JLabel lblplayer5 = new JLabel("");
		lblplayer5.setBackground(SystemColor.activeCaption);
		lblplayer5.setForeground(Color.WHITE);
		lblplayer5.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		lblplayer5.setBounds(186, 187, 61, 90);
		contentPane.add(lblplayer5);

		JLabel lblplayer6 = new JLabel("");
		lblplayer6.setForeground(Color.WHITE);
		lblplayer6.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		lblplayer6.setBounds(278, 187, 61, 90);
		contentPane.add(lblplayer6);

		JLabel lblplayer7 = new JLabel("");
		lblplayer7.setForeground(Color.WHITE);
		lblplayer7.setBackground(SystemColor.activeCaption);
		lblplayer7.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		lblplayer7.setBounds(365, 187, 61, 90);
		contentPane.add(lblplayer7);

		JLabel lblplayer8 = new JLabel("");
		lblplayer8.setForeground(Color.WHITE);
		lblplayer8.setFont(new Font("Lucida Grande", Font.BOLD, 12));
		lblplayer8.setBounds(452, 187, 61, 90);
		contentPane.add(lblplayer8);

		// add the users' icons
		playerIconList.add(lblplayer1);
		playerIconList.add(lblplayer2);
		playerIconList.add(lblplayer3);
		playerIconList.add(lblplayer4);
		playerIconList.add(lblplayer5);
		playerIconList.add(lblplayer6);
		playerIconList.add(lblplayer7);
		playerIconList.add(lblplayer8);

		setPlayerIcon(playerIconList);

		// invite other players when click "INVITE"
		JButton btnInvite = new JButton("INVITE");
		btnInvite.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		btnInvite.setOpaque(true);
		btnInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				communicate.inviteOthers();
			}
		});
		btnInvite.setBounds(59, 323, 85, 29);
		contentPane.add(btnInvite);

		// show user icon on the top of the UI
		JLabel lbluser = new JLabel("");
		Image userimg = new ImageIcon(this.getClass().getResource("/user.png")).getImage();
		lbluser.setIcon(new ImageIcon(userimg));
		lbluser.setBounds(365, 0, 24, 36);
		contentPane.add(lbluser);

		// show user name on the top of the UI
		JLabel lblusername = new JLabel(ClientLoginUI.name);
		lblusername.setBounds(393, 10, 61, 24);
		contentPane.add(lblusername);

		// create a "log out" button for users to log out
		JButton btnlogoff = new JButton("Log out");
		btnlogoff.setHorizontalAlignment(SwingConstants.LEFT);
		btnlogoff.setFont(new Font("Lucida Grande", Font.PLAIN, 11));
		btnlogoff.setForeground(Color.GRAY);
		btnlogoff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// show a pop up to confirm when users click the log out button
				Image questionimg = new ImageIcon(this.getClass().getResource("/question.png")).getImage();
				Object[] options = { "Yes", "No" };
				int m = JOptionPane.showOptionDialog(null, "Are you sure you want to exit?", null,
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(questionimg), options,
						options[0]);
				if (m == JOptionPane.YES_OPTION) {
					communicate.disconnect();
					System.exit(0);
				}
			}
		});
		btnlogoff.setBounds(458, 10, 86, 19);
		contentPane.add(btnlogoff);

		// create the start button for users to start a game
		btnStart = new JButton("START GAME !");
		btnStart.setForeground(Color.BLACK);
		btnStart.setEnabled(false);
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				communicate.startGame();
			}
		});

		btnStart.setBackground(new Color(50, 205, 50));
		btnStart.setOpaque(true);
		btnStart.setBorder(BorderFactory.createRaisedBevelBorder());
		btnStart.setFont(new Font("Krungthep", Font.BOLD, 15));
		btnStart.setBounds(411, 283, 124, 40);
		contentPane.add(btnStart);

		// game room label
		JLabel lblGameRoom = new JLabel("Game Room");
		lblGameRoom.setForeground(Color.WHITE);
		lblGameRoom.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblGameRoom.setBackground(Color.DARK_GRAY);
		lblGameRoom.setOpaque(true);
		lblGameRoom.setBounds(156, 34, 379, 19);
		contentPane.add(lblGameRoom);

		JLabel lblplayer = new JLabel("");
		lblplayer.setBorder(BorderFactory.createEtchedBorder());
		lblplayer.setBounds(156, 34, 379, 289);
		contentPane.add(lblplayer);

		list.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		list.setBorder(BorderFactory.createEtchedBorder());
		list.setForeground(Color.BLACK);
		list.setLayoutOrientation(JList.HORIZONTAL_WRAP);
		list.setBounds(16, 52, 128, 271);
		contentPane.add(list);

		JLabel label = new JLabel("");
		label.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		label.setBorder(BorderFactory.createEtchedBorder());
		label.setBounds(359, 187, 61, 84);
		contentPane.add(label);

		// all the labels for showing users' icon
		JLabel label_1 = new JLabel("");
		label_1.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		label_1.setBorder(BorderFactory.createEtchedBorder());
		label_1.setBounds(446, 187, 61, 84);
		contentPane.add(label_1);

		JLabel label_2 = new JLabel("");
		label_2.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		label_2.setBorder(BorderFactory.createEtchedBorder());
		label_2.setBounds(272, 187, 61, 84);
		contentPane.add(label_2);

		JLabel label_3 = new JLabel("");
		label_3.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		label_3.setBorder(BorderFactory.createEtchedBorder());
		label_3.setBounds(180, 187, 61, 84);
		contentPane.add(label_3);

		JLabel label_4 = new JLabel("");
		label_4.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		label_4.setBorder(BorderFactory.createEtchedBorder());
		label_4.setBounds(180, 85, 61, 84);
		contentPane.add(label_4);

		JLabel label_5 = new JLabel("");
		label_5.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		label_5.setBorder(BorderFactory.createEtchedBorder());
		label_5.setBounds(272, 85, 61, 84);
		contentPane.add(label_5);

		JLabel label_6 = new JLabel("");
		label_6.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		label_6.setBorder(BorderFactory.createEtchedBorder());
		label_6.setBounds(359, 85, 61, 84);
		contentPane.add(label_6);

		JLabel label_7 = new JLabel("");
		label_7.setFont(new Font("Lucida Grande", Font.BOLD | Font.ITALIC, 13));
		label_7.setBorder(BorderFactory.createEtchedBorder());
		label_7.setBounds(446, 85, 61, 84);
		contentPane.add(label_7);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBackground(Color.DARK_GRAY);
		lblNewLabel.setOpaque(true);
		lblNewLabel.setBounds(180, 147, 61, 22);
		contentPane.add(lblNewLabel);

		JLabel label_8 = new JLabel("");
		label_8.setOpaque(true);
		label_8.setBackground(Color.DARK_GRAY);
		label_8.setBounds(359, 147, 61, 22);
		contentPane.add(label_8);

		JLabel label_9 = new JLabel("");
		label_9.setOpaque(true);
		label_9.setBackground(Color.DARK_GRAY);
		label_9.setBounds(446, 147, 61, 22);
		contentPane.add(label_9);

		JLabel label_10 = new JLabel("");
		label_10.setOpaque(true);
		label_10.setBackground(Color.DARK_GRAY);
		label_10.setBounds(180, 249, 61, 22);
		contentPane.add(label_10);

		JLabel label_11 = new JLabel("");
		label_11.setOpaque(true);
		label_11.setBackground(Color.DARK_GRAY);
		label_11.setBounds(272, 249, 61, 22);
		contentPane.add(label_11);

		JLabel label_12 = new JLabel("");
		label_12.setOpaque(true);
		label_12.setBackground(Color.DARK_GRAY);
		label_12.setBounds(359, 249, 61, 22);
		contentPane.add(label_12);

		JLabel label_13 = new JLabel("");
		label_13.setOpaque(true);
		label_13.setBackground(Color.DARK_GRAY);
		label_13.setBounds(446, 249, 61, 22);
		contentPane.add(label_13);

		JLabel label_14 = new JLabel("");
		label_14.setOpaque(true);
		label_14.setBackground(Color.DARK_GRAY);
		label_14.setBounds(272, 147, 61, 22);
		contentPane.add(label_14);

	}

	// show all the online users in a list
	public void updateUserTable(ArrayList<String> allusers) {

		DefaultListModel<String> playerList = new DefaultListModel<String>();
		for (int i = 0; i < allusers.size(); i++) {
			playerList.addElement(allusers.get(i));
		}
		list.setModel(playerList);

	}

	// show all the players in the game room area
	public void updatePlayerTable(ArrayList<String> allplayers) {
		for (int j = 0; j < 8; j++) {
			playerIconList.get(j).setVisible(false);
		}
		for (int i = 0; i < allplayers.size(); i++) {
			// show user's name under the icon
			playerIconList.get(i).setHorizontalTextPosition(SwingConstants.CENTER);
			playerIconList.get(i).setVerticalTextPosition(SwingConstants.BOTTOM);
			playerIconList.get(i).setText(allplayers.get(i));
			playerIconList.get(i).setVisible(true);
		}
		btnStart.setEnabled(true);
		btnleave.setEnabled(true);
	}

	// a pop up to show the results of invitation
	public void inviteOthersResult(Boolean invite_others_result) {
		if (invite_others_result == true) {
			Image smileimg = new ImageIcon(this.getClass().getResource("/smile.png")).getImage();
			Object[] options = { "ok" };
			int m = JOptionPane.showOptionDialog(null, "Invitation successed", null, JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, new ImageIcon(smileimg), options, options[0]);
		} else {
			Image sadimg = new ImageIcon(this.getClass().getResource("/sad.png")).getImage();
			Object[] options = { "ok" };
			int m = JOptionPane.showOptionDialog(null, "Invitation failed.A game is in process", null,
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(sadimg), options, options[0]);
		}
	}

	// show a question when someone invites me
	public boolean inviteMe() {
		if (optionInt == 0) {
			Image questionimg = new ImageIcon(this.getClass().getResource("/question.png")).getImage();

			Object[] options = { "Yes", "No" };
			int m = JOptionPane.showOptionDialog(null, "Would you accept the invitation?", null,
					JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(questionimg), options,
					options[0]);
			optionInt = 1;
			if (m == JOptionPane.YES_OPTION) {
				optionInt = 0;
				return true;
			} else {
				optionInt = 0;
				return false;
			}
		}
		return false;

	}

	public ClientCommunicate getClientCommunicate() {
		return communicate;
	}

	public ClientUI getCurrentObject() {
		return this;
	}

	// close the Client UI and open the game UI
	public void game() {
		getCurrentObject().setVisible(false);
		gameUI = new GameUI(getCurrentObject());
	}

	// clear the game room when a game ends
	public void gameEnd() {
		for (int j = 0; j < 8; j++) {
			playerIconList.get(j).setVisible(false);
		}
		btnStart.setEnabled(false);
		btnleave.setEnabled(false);
	}

	// show message when failing to start a game
	public void startGameFailed() {
		Image sadimg = new ImageIcon(this.getClass().getResource("/sad.png")).getImage();
		Object[] options = { "ok" };
		int m = JOptionPane.showOptionDialog(null, "Initialization failed. There should be no less than 2 players.",
				null, JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(sadimg), options, options[0]);
	}

	// show message when accepting a invitation
	public void inviteMeResultAck(boolean result) {
		if (result == true) {
			Image smileimg = new ImageIcon(this.getClass().getResource("/smile.png")).getImage();
			Object[] options = { "ok" };
			int m = JOptionPane.showOptionDialog(null, "You have joined the game!Waiting for the game starts...", null,
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(smileimg), options, options[0]);
		} else {
			Image smileimg = new ImageIcon(this.getClass().getResource("/smile.png")).getImage();
			Object[] options = { "ok" };
			int m = JOptionPane.showOptionDialog(null, "Fail to join game room. The game has started!", null,
					JOptionPane.YES_OPTION, JOptionPane.QUESTION_MESSAGE, new ImageIcon(smileimg), options, options[0]);
		}
	}

	// show message when leaving the game room
	public void leaveGameACK() {
		for (int i = 0; i < 8; i++) {
			playerIconList.get(i).setVisible(false);
		}
		Image smileimg = new ImageIcon(this.getClass().getResource("/smile.png")).getImage();
		Object[] options = { "ok" };
		int m = JOptionPane.showOptionDialog(null, "Left room successfully.", null, JOptionPane.YES_OPTION,
				JOptionPane.QUESTION_MESSAGE, new ImageIcon(smileimg), options, options[0]);
	}

	// set player's icon
	private void setPlayerIcon(List<JLabel> playerIconList) {
		Image player1 = new ImageIcon(this.getClass().getResource("/dandyman.png")).getImage();
		playerIconList.get(0).setIcon(new ImageIcon(player1));
		Image player2 = new ImageIcon(this.getClass().getResource("/devilgirl.png")).getImage();
		playerIconList.get(1).setIcon(new ImageIcon(player2));
		Image player3 = new ImageIcon(this.getClass().getResource("/sportsman.png")).getImage();
		playerIconList.get(2).setIcon(new ImageIcon(player3));
		Image player4 = new ImageIcon(this.getClass().getResource("/maidgirl.png")).getImage();
		playerIconList.get(3).setIcon(new ImageIcon(player4));
		Image player5 = new ImageIcon(this.getClass().getResource("/catwoman.png")).getImage();
		playerIconList.get(4).setIcon(new ImageIcon(player5));
		Image player6 = new ImageIcon(this.getClass().getResource("/priestman.png")).getImage();
		playerIconList.get(5).setIcon(new ImageIcon(player6));
		Image player7 = new ImageIcon(this.getClass().getResource("/captainesswoman.png")).getImage();
		playerIconList.get(6).setIcon(new ImageIcon(player7));
		Image player8 = new ImageIcon(this.getClass().getResource("/gangsterman.png")).getImage();
		playerIconList.get(7).setIcon(new ImageIcon(player8));

		for (int i = 0; i < playerIconList.size(); i++) {

			playerIconList.get(i).setVisible(false);
		}

	}
}
