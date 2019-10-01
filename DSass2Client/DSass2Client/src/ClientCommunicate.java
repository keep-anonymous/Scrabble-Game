import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ClientCommunicate {
	@SuppressWarnings("unused")
	private Socket socket;
	private DataOutputStream output;
	private ClientUI clientUI;
	private GameUI gameUI;
	private ClientLoginUI loginUI;

	public ClientCommunicate(ClientLoginUI loginUI, Socket socket) {
		this.loginUI = loginUI;
		this.socket = socket;
		Thread t = new Thread(() -> Receive(socket));
		t.start();
		try {
			output = new DataOutputStream(socket.getOutputStream());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public ClientCommunicate() {
	}

	public void setClientUI(ClientUI playerUI) {
		this.clientUI = playerUI;
	}

	public void setGameUI(GameUI gameUI) {
		this.gameUI = gameUI;
	}

	// interface 7
	@SuppressWarnings("unchecked")
	public void inviteOthers() {
		JSONObject json = new JSONObject();
		json.put("type", "invite_others");
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There is a problem communicating with the server! Please restart!");
			System.exit(0);
		}

	}

	// interface 8
	@SuppressWarnings("unchecked")
	public void startGame() {
		JSONObject json = new JSONObject();
		json.put("type", "start_game_to_server");
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There is a problem communicating with the server! Please restart!");
			System.exit(0);
		}

	}

	// interface 9
	@SuppressWarnings("unchecked")
	public void disconnect() {
		JSONObject json = new JSONObject();
		json.put("type", "disconnect");
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There is a problem communicating with the server! Please restart!");
			System.exit(0);
		}
	}

	// interface 15
	@SuppressWarnings("unchecked")
	public void updateWithVote(String highlight, int row, int column, String letter) {
		JSONObject json = new JSONObject();
		json.put("type", "update_with_highlight&vote");
		json.put("highlight", highlight);
		json.put("row", row);
		json.put("column", column);
		json.put("letter", letter);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There is a problem communicating with the server! Please restart!");
			System.exit(0);
		}
	}

	// interface 16
	@SuppressWarnings("unchecked")
	public void updateWithoutVote(int row, int column, String letter) {
		JSONObject json = new JSONObject();
		json.put("type", "update_without_vote");
		json.put("row", row);
		json.put("column", column);
		json.put("letter", letter);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There is a problem communicating with the server! Please restart!");
			System.exit(0);
		}
	}

	// interface 17
	@SuppressWarnings("unchecked")
	public void pass() {
		JSONObject json = new JSONObject();
		json.put("type", "pass");
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There is a problem communicating with the server! Please restart!");
			System.exit(0);
		}
	}

	// interface 18
	@SuppressWarnings("unchecked")
	public void endGame() {
		JSONObject json = new JSONObject();
		json.put("type", "endgame_by_player");
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There is a problem communicating with the server! Please restart!");
			System.exit(0);
		}
	}

	// interface 19
	@SuppressWarnings("unchecked")
	public void setName(String name) {
		JSONObject json = new JSONObject();
		json.put("type", "set_name");
		json.put("name", name);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There is a problem communicating with the server! Please restart!");
			System.exit(0);
		}
	}

	// interface 20
	@SuppressWarnings("unchecked")
	public void inviteMeResult(boolean result) {
		JSONObject json = new JSONObject();
		json.put("type", "invite_me_result");
		json.put("result", result);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There is a problem communicating with the server! Please restart!");
			System.exit(0);
		}
	}

	@SuppressWarnings("unchecked")
	public void voteResult(boolean result) {
		JSONObject json = new JSONObject();
		json.put("type", "vote_from_player");
		json.put("vote", result);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There is a problem communicating with the server! Please restart!");
			System.exit(0);
		}
	}

	@SuppressWarnings("unchecked")
	public void leaveGameRoom() {
		JSONObject json = new JSONObject();
		json.put("type", "leave_game_room");
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			JOptionPane.showMessageDialog(null, "There is a problem communicating with the server! Please restart!");
			System.exit(0);
		}
	}

	private void processReceiveInClient(JSONObject receive) {
		JSONParser parser = new JSONParser();
		String command = receive.get("type").toString();
		JSONArray jsonArray = null;
		switch (command) {
		case "allusers": // interface 1
			try {
				jsonArray = (JSONArray) parser.parse(receive.get("list").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			ArrayList<String> userList = getListFromStringJSONArray(jsonArray);
			clientUI.updateUserTable(userList);
			break;
		case "allplayers": // interface 2
			try {
				jsonArray = (JSONArray) parser.parse(receive.get("list").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			ArrayList<String> playerList = getListFromStringJSONArray(jsonArray);
			clientUI.updatePlayerTable(playerList);
			break;
		case "invite_others_result": // interface 3
			String result = receive.get("result").toString();
			if (result.equals("true")) {
				clientUI.inviteOthersResult(true);
			} else {
				clientUI.inviteOthersResult(false);
			}
			break;
		case "invite_me": // interface 4
			boolean bool = clientUI.inviteMe();
			inviteMeResult(bool);
			break;
		case "start_game_to_user": // interface 5
			clientUI.game();
			break;
		case "inviteMeResultAck": // interface 6
			String result2 = receive.get("result").toString();
			if (result2.equals("true"))
				clientUI.inviteMeResultAck(true);
			else
				clientUI.inviteMeResultAck(false);
			break;
		case "update_board_without_vote": // interface 10
			int row = Integer.parseInt(receive.get("row").toString());
			int column = Integer.parseInt(receive.get("column").toString());
			String letter = receive.get("letter").toString();
			gameUI.updateBoardWithoutVote(row, column, letter);
			break;
		case "update_board_with_highlight&vote": // interface 11
			String highlight = receive.get("highlight").toString();
			int row2 = Integer.parseInt(receive.get("row").toString());
			int column2 = Integer.parseInt(receive.get("column").toString());
			String letter2 = receive.get("letter").toString();
			boolean vote = gameUI.updateBoardHighlightVote(highlight, row2, column2, letter2);
			voteResult(vote);
			break;
		case "playername&score": // interface 12
			try {
				jsonArray = (JSONArray) parser.parse(receive.get("list").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			ArrayList<String> playerScoreList = getListFromStringJSONArray(jsonArray);
			gameUI.updateScoreTable(playerScoreList);
			break;
		case "new_round_player": // interface 13
			String playerName = receive.get("player").toString();
			gameUI.newRound(playerName);
			break;
		case "gameover_player": // interface 14
			String winnerAndScore = receive.get("winner").toString();
			gameUI.gameover(winnerAndScore);
			break;
		case "start_game_failed":
			clientUI.startGameFailed();
			break;
		case "set_name_success":
			String success = receive.get("result").toString();
			if (success.equals("true"))
				loginUI.setNameSuccess(true);
			else
				loginUI.setNameSuccess(false);
			break;
		case "leave_game_room_ack":
			clientUI.leaveGameACK();
			break;
		default:
			;
		}
	}

	private void Receive(Socket client) {
		try (Socket clientSocket = client) {
			JSONParser parser = new JSONParser();
			// Input stream
			DataInputStream input = new DataInputStream(clientSocket.getInputStream());
			// Receive more data..
			while (true) {
				if (input.available() > 0) {
					// Attempt to convert read data to JSON
					try {
						JSONObject command = (JSONObject) parser.parse(input.readUTF());
						System.out.println("Received Object: " + command.toString());
						processReceiveInClient(command);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<String> getListFromStringJSONArray(JSONArray jsonArray) {
		ArrayList<String> list = new ArrayList<String>();
		if (jsonArray == null || jsonArray.isEmpty()) {
			return list;// nerver return null
		}
		for (Object object : jsonArray) {
			String s = object.toString();
			list.add(s);
		}
		return list;
	}

}
