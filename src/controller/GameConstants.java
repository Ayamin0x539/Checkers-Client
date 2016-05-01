package controller;

import model.Color;

public class GameConstants {
	public static final int USER_MODE = 0;
	public static final int SERVER_MODE = 1;
	public static final Color THUNK_COLOR = Color.WHITE;
	public static final Color USER_COLOR = Color.BLACK;
	public static final int MAX_PASSIVE_MOVES = 50;
	public static final int MAX_SEARCH_DEPTH = 7;
	public static final int BOARD_SIZE = 8;
	
	/* Parameters to the heuristic */
	public static final boolean APEX = true;
	public static final boolean BACK = true;
	public static final boolean CENT = true;
	public static final boolean MOB = true;
	public static final boolean PIECE_DIFFERENTIAL = true;
	
}
