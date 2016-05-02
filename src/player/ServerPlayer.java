package player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import controller.Game;
import model.Board;
import model.Color;
import model.Move;
import controller.Communication;

public class ServerPlayer extends Player {
	private Game game;
	private static String IP = "137.99.164.83";
	private static int PORT = 3499;
	private Socket _socket = null;
    private PrintWriter _out = null;
    private BufferedReader _in = null;
	
	public ServerPlayer(Color color, Board board, Game game) {
		super(color, board);
		this.game = game;
	}
	
	public ArrayList<Move> makeMove(ArrayList<Move> moves) {
		//Does full sequence of moves, then computer responds. This might cause bugs
		for (Move move : moves){
			game.requestMove(move);
		}
		
		ArrayList<Move> computerMoves = new ArrayList<Move>();
		
		/* Make the computer move */
		Move computerMove = game.makeComputerMove();
		
		if (moves != null) {
			computerMoves.add(computerMove);
		}
		
		/* While the computer can still move, ask it to move */
		while (game.getComputer().isInJumpSequence()) {
			computerMove = game.makeComputerMove();
			if (moves != null) {
				computerMoves.add(computerMove);
			}
		}
		
		/* Return the list of moves made by the computer */
		return computerMoves;
	}
	
	public void listen(String name, String pass, String opponent) {
		_socket = openSocket();
		ArrayList<Move> moves;
		ArrayList<Move> computerMoves;
		try{
			readAndEcho(); //Start message
			readAndEcho(); //User query
			writeMessageAndEcho(name); //Send Username
			readAndEcho(); //Password query
			writeMessageAndEcho(pass); //Send Password
			readAndEcho(); //Opponent query
			writeMessageAndEcho(opponent); //Send opponent
			System.out.println("Waiting on opponent: " + opponent);
			String gameID = (readAndEcho().substring(5,9)); //Game ID
			String selectedColor = readAndEcho().substring(6,11); //Color
			System.out.println("I am playing as "+color+" in game number "+ gameID);
			if (selectedColor.equalsIgnoreCase("white")){ //Change colors accordingly
				this.setColor(color.BLACK); //Almost caused a bug here. whoops!
				game.getComputer().setColor(color.WHITE);
				//Proceed to main loop
			}
			else{
				this.setColor(color.WHITE);
				game.getComputer().setColor(color.BLACK);
				computerMoves = makeMove(null); //Act first, probably won't work now
				String moveString = Communication.moveToString(computerMoves); //Put move into string
				readAndEcho(); //Read move query
				writeMessageAndEcho(moveString); //Send move string
				readAndEcho();
			}
								
			boolean over = false;
			//Main execution loop, stops when someone wins. Will probably need to write in a Tie recognizer
			//Because the AI likes to dance in the corner
			while(!over) { //While game isn't over
				String moveMsg = readAndEcho(); //Move query or result
				if (moveMsg.contains("Result")){
					over = true; //Cut it if it's result
				}
				else {
					moves = Communication.stringToMove(moveMsg); //Otherwise update board
					computerMoves = makeMove(moves);			 //Make moves and computer moves
					String moveString = Communication.moveToString(computerMoves);
					readAndEcho(); //Read move query
					writeMessageAndEcho(moveString); //Send moves
					readAndEcho(); //Read the move echo'd back
				}
					
			}
		}
		catch (Exception e){
			System.out.println("Exception occured:\n" + e);
			System.exit(1);
		}
		/* Listen to server and wait for incoming messages.
		 * and GameConstants.SERVER_COLOR according to who the server says
		 * will go first.
		 */
		
		/* Busy-wait on moves sent from the server. When receiving a move
		 * message, call the static Communication.stringToMove() method.
		 * This will return an ArrayList of moves the server wishes to make.
		 * For each move, call the makeMove() method. This method will return
		 * the sequence of moves made by the computer in response to the
		 * server's move. However, in the case where the server is 
		 * making a sequence of moves, only the last call to makeMove will 
		 * return something meaningful. That last call will return an 
		 * ArrayList of moves made by the computer. Call the 
		 * Communication.moveToString() static method on this list to get 
		 * the string to send to the server;
		 */
	}

    public String readAndEcho() throws IOException
    {
    	String readMessage = _in.readLine();
    	System.out.println("read: "+readMessage);
    	return readMessage;
    }

    public void writeMessage(String message) throws IOException
    {
    	_out.print(message+"\r\n");  
    	_out.flush();
    }
 
    public void writeMessageAndEcho(String message) throws IOException
    {
    	_out.print(message+"\r\n");  
    	_out.flush();
    	System.out.println("sent: "+ message);
    }
			       
    public  Socket openSocket(){
	//Create socket connection, adapted from Sun example
	try{
		_socket = new Socket(IP, PORT);
		_out = new PrintWriter(_socket.getOutputStream(), true);
		_in = new BufferedReader(new InputStreamReader(_socket.getInputStream()));
     } catch (UnknownHostException e) {
    	 System.out.println("Unknown host: " + IP);
    	 System.exit(1);
     } catch  (IOException e) {
    	 System.out.println("No I/O");
    	 System.exit(1);
     }
     return _socket;
  }
}
