package jchess.core.tests;

import jchess.core.*;
import jchess.core.Player.playerTypes;

public class GameWithInjectableChessboard extends Game 
{
	public GameWithInjectableChessboard()
	{
		super();
		Player whitePlayer = settings.getPlayerWhite();
		Player blackPlayer = settings.getPlayerBlack();
		whitePlayer.setType(playerTypes.localUser);
		whitePlayer.setName("Player1");
		blackPlayer.setType(playerTypes.localUser);
		blackPlayer.setName("Player2");
	}
	
	public void setChessboard(Chessboard board)
	{
		chessboard = board;
	}
}
