package jchess.core.tests;

import jchess.core.*;

public class GameWithInjectableChessboard extends Game 
{
	public GameWithInjectableChessboard()
	{
		super();
	}
	
	public void setChessboard(Chessboard board)
	{
		chessboard = board;
	}
}
