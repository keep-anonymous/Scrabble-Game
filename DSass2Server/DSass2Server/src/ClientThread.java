
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ClientThread {
	private String name;
	private int score;
	@SuppressWarnings("unused")
	private Socket socket;
	private DataOutputStream output = null;
	private ServerTest server;

	public ClientThread(Socket socket, ServerTest server) {
		this.socket = socket;
		this.server = server;
		Thread t = new Thread(() -> Receive(socket));
		t.start();
		try {
			output = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public ClientThread() {
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getScore() {
		return score;
	}

	public void addScore(int getScore) {
		score = score + getScore;
	}

	// interface 4
	@SuppressWarnings("unchecked")
	public void sendAllUsers(ArrayList<String> userNames) {
		JSONArray jsonArray = new JSONArray();
		jsonArray = getJSONArrayFromStringList(userNames);
		JSONObject json = new JSONObject();
		json.put("type", "allusers");
		json.put("list", jsonArray);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.diconnectByUser(name);
		}
	}

	// interface 5
	@SuppressWarnings("unchecked")
	public void sendAllPlayers(ArrayList<String> playerNames) {
		JSONArray jsonArray = new JSONArray();
		jsonArray = getJSONArrayFromStringList(playerNames);
		JSONObject json = new JSONObject();
		json.put("type", "allplayers");
		json.put("list", jsonArray);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.diconnectByUser(name);
		}
	}

	// interface 6
	@SuppressWarnings("unchecked")
	public void inviteOthersResult(boolean bool) {
		JSONObject json = new JSONObject();
		json.put("type", "invite_others_result");
		json.put("result", bool);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.diconnectByUser(name);
		}
	}

	// interface 7
	@SuppressWarnings("unchecked")
	public void invite() {
		JSONObject json = new JSONObject();
		json.put("type", "invite_me");
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.diconnectByUser(name);
		}

	}

	// interface 8
	@SuppressWarnings("unchecked")
	public void startGame() {
		score = 0;
		JSONObject json = new JSONObject();
		json.put("type", "start_game_to_user");
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.diconnectByUser(name);
		}
	}

	// interface 9
	@SuppressWarnings("unchecked")
	public void inviteMeAck(boolean result) {
		JSONObject json = new JSONObject();
		json.put("type", "inviteMeResultAck");
		json.put("result", result);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.diconnectByUser(name);
		}
	}

	// interface 14.
	@SuppressWarnings("unchecked")
	public void updateBoardWithoutVote(int x, int y, String letter2) {
		JSONObject json = new JSONObject();
		json.put("type", "update_board_without_vote");
		json.put("row", x);
		json.put("column", y);
		json.put("letter", letter2);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.endGameByPlayer(name);
			server.diconnectByUser(name);
		}
	}

	// interface 15.
	@SuppressWarnings("unchecked")
	public void updateBoardWithHighlightVote(String direction, int x, int y, String letter) {
		JSONObject json = new JSONObject();
		json.put("type", "update_board_with_highlight&vote");
		json.put("highlight", direction);
		json.put("row", x);
		json.put("column", y);
		json.put("letter", letter);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.endGameByPlayer(name);
			server.diconnectByUser(name);
		}
	}

	// interface 16.
	@SuppressWarnings("unchecked")
	public void playerAndScore(ArrayList<String> playerScore) {
		JSONArray jsonArray = new JSONArray();
		jsonArray = getJSONArrayFromStringList(playerScore);
		JSONObject json = new JSONObject();
		json.put("type", "playername&score");
		json.put("list", jsonArray);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.endGameByPlayer(name);
			server.diconnectByUser(name);
		}
	}

	// interface 17.
	@SuppressWarnings("unchecked")
	public void newRound(String name2) {
		JSONObject json = new JSONObject();
		json.put("type", "new_round_player");
		json.put("player", name2);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.endGameByPlayer(name);
			server.diconnectByUser(name);
		}
	}

	@SuppressWarnings("unchecked")
	public void gameOverPlayer(String winnerAndScore) {
		JSONObject json = new JSONObject();
		json.put("type", "gameover_player");
		json.put("winner", winnerAndScore);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.diconnectByUser(name);
		}
	}

	@SuppressWarnings("unchecked")
	public void startGameFailed() {
		JSONObject json = new JSONObject();
		json.put("type", "start_game_failed");
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.diconnectByUser(name);
		}
	}

	@SuppressWarnings("unchecked")
	private void sendSetNameSuccess(boolean b) {
		JSONObject json = new JSONObject();
		json.put("type", "set_name_success");
		json.put("result", b);
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.diconnectByUser(name);
		}

	}

	@SuppressWarnings("unchecked")
	public void leaveGameRoomACK() {
		JSONObject json = new JSONObject();
		json.put("type", "leave_game_room_ack");
		try {
			output.writeUTF(json.toJSONString());
		} catch (IOException e) {
			server.diconnectByUser(name);
		}
	}

	//
	private void processReceiveInServer(JSONObject receive) {
		String command = receive.get("type").toString();
		switch (command) {
		case "invite_others": // interface 1
			boolean bool = server.inviteOthers(name);
			inviteOthersResult(bool);
			System.out.println("invite_others has been called!");
			break;
		case "start_game_to_server": // interface 2
			boolean success = server.startGame();
			if (success == false)
				startGameFailed();
			System.out.println("start_game_to_server has been called!");
			break;
		case "disconnect": // interface 3
			server.diconnectByUser(name);
			System.out.println("disconnect_by_user has been called!");
			break;
		case "update_with_highlight&vote": // interface 10
			String highlight = receive.get("highlight").toString();
			int row = Integer.parseInt(receive.get("row").toString());
			int column = Integer.parseInt(receive.get("column").toString());
			String letter = receive.get("letter").toString();
			server.updateWithHighlightVote(highlight, row, column, letter);
			System.out.println("update_with_highlight&vote has been called!");
			break;
		case "update_without_vote": // interface 11
			int row2 = Integer.parseInt(receive.get("row").toString());
			int column2 = Integer.parseInt(receive.get("column").toString());
			String letter2 = receive.get("letter").toString();
			server.updateWithoutVote(row2, column2, letter2);
			System.out.println("update_without_vote has been called!");
			break;
		case "pass": // interface 12
			server.pass();
			System.out.println("pass has been called!");
			break;
		case "endgame_by_player": // interface 13
			server.endGameByPlayer(name);
			System.out.println("endgame_by_player has been called!");
			break;
		case "set_name": // interface 1
			String receivedName = receive.get("name").toString();
			boolean nameValidation = server.validName(receivedName);
			if (nameValidation == true) {
				name = receivedName;
				server.addObjectToUserList(getObject());
				sendSetNameSuccess(true);
				server.broadcastUserListToAll();
			} else {
				sendSetNameSuccess(false);
			}
			System.out.println("set_name has been called!");
			break;
		case "invite_me_result":
			String bool2 = receive.get("result").toString();
			if (bool2.equals("true"))
				server.inviteMeResult(name, true);
			else
				server.inviteMeResult(name, false);
			System.out.println("invite_me_result has been called!");
			break;
		case "vote_from_player":
			String vote = receive.get("vote").toString();
			if (vote.equals("true"))
				server.voteFromPlayer(true);
			else
				server.voteFromPlayer(false);
			System.out.println("vote_from_player has been called!");
			break;
		case "leave_game_room":
			server.leaveGameRoom(name);
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

			// Receive more data
			while (true) {
				if (input.available() > 0) {
					// Attempt to convert read data to JSON
					try {
						JSONObject command = (JSONObject) parser.parse(input.readUTF());
						System.out.println("COMMAND RECEIVED: " + command.toString());
						processReceiveInServer(command);

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public JSONArray getJSONArrayFromStringList(ArrayList<String> list) {
		JSONArray jsonArray = new JSONArray();
		if (list == null || list.isEmpty()) {
			return jsonArray;// nerver return null
		}
		for (String object : list) {
			jsonArray.add(object);
		}
		return jsonArray;
	}

	private ClientThread getObject() {
		return this;
	}

}
