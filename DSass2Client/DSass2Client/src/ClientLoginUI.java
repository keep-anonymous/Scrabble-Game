
/**
 * Author:LIUYI CHAI
 * Team:Microsoft Fans
 * Date:12/10/2018
 * Purpose:This class is a UI for users to login
 */

import java.awt.EventQueue;
import javax.swing.JFrame;
import java.awt.Image;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.*;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Color;

public class ClientLoginUI {
	public static String name;
	private JFrame loginframe;
	private ClientUI playerframe;
	private ClientCommunicate communicate;
	private Socket socket;
	// private JTextArea instruArea;
	private JTextField hostField;
	private JTextField portField;
	private JTextField nameField;
	private JLabel lblLogin;
	private JButton btnLogin;
	private JLabel lblloginIcon;

	public static void main(String[] args) {

		JTextArea instruArea = new JTextArea();

		try {

			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						ClientLoginUI loginWindow = new ClientLoginUI();
						loginWindow.loginframe.setVisible(true);

					} catch (Exception e) {
					}
				}
			});

		} catch (NullPointerException e) {
			instruArea.append("Connection reset." + "/n");
			System.out.println("Connection reset.");

		}

	}

	public ClientLoginUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		loginframe = new JFrame();
		loginframe.getContentPane().setBackground(Color.WHITE);
		loginframe.getContentPane().setFont(new Font("Cooper Black", Font.BOLD, 19));
		loginframe.setSize(700, 850);
		loginframe.setLocationRelativeTo(null);
		loginframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JLabel lblHost = new JLabel("Host");

		lblHost.setFont(new Font(".SF NS Text", Font.PLAIN, 28));
		lblHost.setForeground(Color.GRAY);
		lblHost.setBounds(107, 243, 111, 42);

		JLabel lblPort = new JLabel("Port");
		lblPort.setFont(new Font(".SF NS Text", Font.PLAIN, 28));
		lblPort.setForeground(Color.GRAY);
		lblPort.setBounds(107, 317, 111, 42);

		JLabel lblUserName = new JLabel("Name");
		lblUserName.setFont(new Font(".SF NS Text", Font.PLAIN, 28));
		lblUserName.setForeground(Color.GRAY);
		lblUserName.setBounds(107, 390, 111, 42);

		hostField = new JTextField();
		hostField.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		hostField.setBounds(216, 240, 325, 51);
		hostField.setColumns(10);

		portField = new JTextField();
		portField.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		portField.setBounds(216, 314, 325, 51);
		portField.setColumns(10);

		nameField = new JTextField();
		nameField.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		nameField.setBounds(216, 387, 325, 51);
		nameField.setColumns(10);

		lblLogin = new JLabel("Scrabble");
		lblLogin.setForeground(new Color(72, 61, 139));
		lblLogin.setBounds(229, 107, 386, 87);
		lblLogin.setFont(new Font("Footlight MT Light", Font.BOLD, 66));

		btnLogin = new JButton("LOGIN");
		Image userimg = new ImageIcon(this.getClass().getResource("/ok.png")).getImage();
		btnLogin.setIcon(new ImageIcon(userimg));

		// when clicking the login button, loginUI closed, ClientUI opened
		btnLogin.setBounds(121, 539, 417, 51);
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String host = hostField.getText().trim();
				int port = Integer.parseInt(portField.getText().trim());
				name = nameField.getText().trim();

				{

					try {
						socket = new Socket(host, port);// build a soket
						communicate = new ClientCommunicate(getObject(), socket);
						communicate.setName(name);
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				}
			}
		});

		btnLogin.setFont(new Font("Lucida Grande", Font.PLAIN, 25));
		loginframe.getContentPane().setLayout(null);
		loginframe.getContentPane().add(lblLogin);
		loginframe.getContentPane().add(lblPort);
		loginframe.getContentPane().add(lblHost);
		loginframe.getContentPane().add(portField);
		loginframe.getContentPane().add(hostField);
		loginframe.getContentPane().add(lblUserName);
		loginframe.getContentPane().add(nameField);
		loginframe.getContentPane().add(btnLogin);

		lblloginIcon = new JLabel("");
		Image loginimg = new ImageIcon(this.getClass().getResource("/login.png")).getImage();
		lblloginIcon.setIcon(new ImageIcon(loginimg));
		lblloginIcon.setBounds(305, 49, 64, 64);
		loginframe.getContentPane().add(lblloginIcon);

	}

	// check if the user name already exists
	public void setNameSuccess(Boolean blName) {
		if (blName == true) {
			playerframe = new ClientUI(communicate);
			communicate.setClientUI(playerframe);
			loginframe.dispose();
			playerframe.setVisible(true);
		} else {
			name = JOptionPane.showInputDialog("The name already exists. Please enter another name:");
			communicate.setName(name);
		}

	}

	private ClientLoginUI getObject() {
		return this;
	}
}
