import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ServerTest {

	private ArrayList<ClientThread> userList = new ArrayList<ClientThread>();
	private ArrayList<ClientThread> playerList = new ArrayList<ClientThread>();
	private String[][] gameBoard = new String[20][20];
	private static ServerTest s;
	private ServerSocket server;
	private boolean gameFlag;
	private int currentTurnFlag;
	private int passCountFlag;
	private boolean voteResult;
	private int voteCount;
	private String highlightForVote;
	private int rowForVote;
	private int columnForVote;
	private String letterForVote;

	public static void main(String[] args) {
		s = new ServerTest();
		s.start();
	}

	public void start() {
		try {
			server = new ServerSocket(3000);
			System.out.println("Server is running!");
		} catch (IOException e) {
			System.out.println("Server startup failed!");
		}

		while (true) {
			try {
				Socket socket = server.accept();
				new ClientThread(socket, s);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public synchronized boolean inviteOthers(String name) {
		if (gameFlag == false) {
			int index = findIndexWithName(name, userList);
			if (index != 9999) {
				playerList.add(userList.get(index));
				userList.remove(index);
				broadcastUserListToAll();
				broadcastPlayerListToPlayers();
			}
			for (int i = 0; i < userList.size(); i++) {
				userList.get(i).invite();
			}
			return true;
		} else
			return false;
	}

	public boolean startGame() {
		if (playerList.size() < 2)
			return false;
		else {
			for (int i = 0; i < playerList.size(); i++) {
				playerList.get(i).startGame();
				// also initial each player'score in method ClientThread.startGame().
			}
			gameFlag = true;
			passCountFlag = 0;
			currentTurnFlag = 0;
			initialBoard();
			broadcastPlayerAndScore();
			broadcastNewRound();
			return true;
		}
	}

	public synchronized void diconnectByUser(String name) {
		int index = findIndexWithName(name, userList);
		// index != 9999 means player is in userList. Else, player is in playerList
		if (index != 9999) {
			userList.remove(index);
			broadcastUserListToAll();
		} else {
			index = findIndexWithName(name, playerList);
			playerList.remove(index);
			broadcastPlayerListToPlayers();
		}
	}

	public void updateWithHighlightVote(String direction, int x, int y, String letter) {
		passCountFlag = 0;
		voteResult = true;
		voteCount = 0;
		gameBoard[x][y] = letter;
		highlightForVote = direction;
		rowForVote = x;
		columnForVote = y;
		letterForVote = letter;

		for (ClientThread player : playerList) {
			// don't broadcast to current player
			if (!player.getName().equals(playerList.get(currentTurnFlag).getName())) {
				player.updateBoardWithHighlightVote(direction, x, y, letter);
			}
		}
	}

	private void updateWithHighlightVoteSecondPart(String direction, int x, int y, String letter) {
		int rowFirstScore = 0;
		int rowSecondScore = 0;
		int columnFirstScore = 0;
		int columnSecondScore = 0;

		int row = x;
		while ((gameBoard[row][y] != null) && (row > 0)) {
			row--;
			columnFirstScore += 1;
		}
		row = x;
		while ((gameBoard[row][y] != null) && (row < 20)) {
			row++;
			columnSecondScore += 1;
		}
		int column = y;
		while ((gameBoard[x][column] != null) && (column > 0)) {
			column--;
			rowFirstScore += 1;
		}
		column = y;
		while ((gameBoard[x][column] != null) && (column < 20)) {
			column++;
			rowSecondScore += 1;
		}
		switch (direction) {
		case "row":
			playerList.get(currentTurnFlag).addScore(rowFirstScore + rowSecondScore - 1);
			break;
		case "column":
			playerList.get(currentTurnFlag).addScore(columnFirstScore + columnSecondScore - 1);
			break;
		case "both":
			playerList.get(currentTurnFlag)
					.addScore(rowFirstScore + rowSecondScore + columnFirstScore + columnSecondScore - 3);
			break;
		default:
			;
		}
		broadcastPlayerAndScore();
		if (boardIsFull())
			gameOver();
		else {
			currentTurnFlag = (currentTurnFlag + 1) % playerList.size();
			System.out.println("加完分后的回合+1");
			broadcastNewRound();
		}
	}

	public void updateWithoutVote(int row2, int column2, String letter2) {
		gameBoard[row2][column2] = letter2;
		// check if the board is full after each updating.
		if (boardIsFull())
			gameOver();
		else {
			for (int i = 0; i < playerList.size(); i++) {
				// don't broadcast to current player
				if (playerList.get(i).getName().equals(playerList.get(currentTurnFlag).getName()))
					;
				else {
					playerList.get(i).updateBoardWithoutVote(row2, column2, letter2);
				}
			}
		}
		currentTurnFlag = (currentTurnFlag + 1) % playerList.size();
		System.out.println("填字但不投票后的回合+1");
		passCountFlag = 0;
		broadcastNewRound();

	}

	public void pass() {
		passCountFlag++;
		System.out.println("Now passCount is: " + passCountFlag);
		if (passCountFlag == playerList.size())
			gameOver();
		else {
			currentTurnFlag = (currentTurnFlag + 1) % playerList.size();
			System.out.println("pass后的回合+1");
			broadcastNewRound();
		}

	}

	class sortByScore implements Comparator<ClientThread> {
		public int compare(ClientThread a, ClientThread b) {
			return b.getScore() - a.getScore();
		}
	}

	public synchronized void gameOver() {
		Collections.sort(playerList, new sortByScore());
		String playerGameData = playerList.get(0).getName() + ": " + playerList.get(0).getScore();
		for (ClientThread player : playerList) {
			player.gameOverPlayer(playerGameData);
		}
		gameFlag = false;
		userList.addAll(playerList);
		playerList.clear();
		broadcastUserListToAll();
		broadcastPlayerListToPlayers();
	}

	public synchronized void endGameByPlayer(String name) {
		for (ClientThread player : playerList) {
			if (player.getName().equals(name)) {
				userList.add(player);
				playerList.remove(player);
				break;
			}
		}
		gameOver();
	}

	public synchronized void inviteMeResult(String name, boolean b) {
		if (b == true) {
			if (gameFlag == false) {
				int index = findIndexWithName(name, userList);
				if (index != 9999) {
					playerList.add(userList.get(index));
					broadcastPlayerListToPlayers();
					userList.get(index).inviteMeAck(true);
					userList.remove(index);
					broadcastUserListToAll();
				}
			} else {
				int index = findIndexWithName(name, userList);
				userList.get(index).inviteMeAck(false);
			}
		}
	}

	// broadcast all users' name to all users&players to update their UI.
	public void broadcastUserListToAll() {
		ArrayList<String> userNames = new ArrayList<String>();
		for (int i = 0; i < userList.size(); i++) {
			userNames.add(userList.get(i).getName());
		}
		for (int i = 0; i < userList.size(); i++) {
			userList.get(i).sendAllUsers(userNames);
		}
		for (int i = 0; i < playerList.size(); i++) {
			playerList.get(i).sendAllUsers(userNames);
		}
	}

	// broadcast all players' names to all players to update their UI.
	public void broadcastPlayerListToPlayers() {
		ArrayList<String> playerNames = new ArrayList<String>();
		for (int i = 0; i < playerList.size(); i++) {
			playerNames.add(playerList.get(i).getName());
		}
		for (int i = 0; i < playerList.size(); i++) {
			playerList.get(i).sendAllPlayers(playerNames);
		}
	}

	// broadcast new round with new round player'name to all players.
	public void broadcastNewRound() {
		for (int i = 0; i < playerList.size(); i++) {
			playerList.get(i).newRound(playerList.get(currentTurnFlag).getName());
		}
	}

	/*
	 * //sort player list by score. public void sortPlayerListByScore() { for(int
	 * i=0; i<playerList.size(); i++) for(int j=0; j<playerList.size()-i; j++) {
	 * if(playerList.get(j).getScore() < playerList.get(j+1).getScore())
	 * Collections.swap(playerList, j, j+1); } }
	 */

	// if object with given name exits in given list, return index. if not exits,
	// return 9999.
	public int findIndexWithName(String name, ArrayList<ClientThread> list) {
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getName().equals(name)) {
				return i;
			}
		}
		return 9999;
	}

	// when new game start, initial the board.
	public void initialBoard() {
		for (int i = 0; i < 20; i++)
			for (int j = 0; j < 20; j++)
				gameBoard[i][j] = null;
	}

	// after updating board, see if it's full.
	public boolean boardIsFull() {
		for (int i = 0; i < 20; i++)
			for (int j = 0; j < 20; j++)
				if (gameBoard[i][j] == null)
					return false;
		return true;
	}

	// after adding score, broadcast playername&score to all players
	public void broadcastPlayerAndScore() {
		ArrayList<String> playerScore = new ArrayList<String>();
		for (int i = 0; i < playerList.size(); i++)
			playerScore.add(playerList.get(i).getName() + ": " + playerList.get(i).getScore());
		for (int i = 0; i < playerList.size(); i++)
			playerList.get(i).playerAndScore(playerScore);
	}

	// get n-1 vote results
	public void voteFromPlayer(boolean vote2) {
		voteCount++;
		voteResult = voteResult && vote2;
		if (voteCount == playerList.size() - 1) {
			if (voteResult == true)
				updateWithHighlightVoteSecondPart(highlightForVote, rowForVote, columnForVote, letterForVote);
			else {
				if (boardIsFull())
					gameOver();
				else {
					currentTurnFlag = (currentTurnFlag + 1) % playerList.size();
					System.out.println("投票结果为否，不加分后的回合+1");
					broadcastNewRound();
				}
			}
		}
	}

	public boolean validName(String name) {
		for (ClientThread user : userList) {
			if (user.getName().equals(name)) {
				return false;
			}
		}
		for (ClientThread player : playerList) {
			if (player.getName().equals(name)) {
				return false;
			}
		}
		return true;
	}

	public synchronized void leaveGameRoom(String name) {
		int index = findIndexWithName(name, playerList);
		userList.add(playerList.get(index));
		playerList.get(index).leaveGameRoomACK();
		playerList.remove(index);
		broadcastUserListToAll();
		broadcastPlayerListToPlayers();
	}

	public synchronized void addObjectToUserList(ClientThread ct) {
		userList.add(ct);
	}

}
