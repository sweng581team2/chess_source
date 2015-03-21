package jchess.core.tests;

import jchess.core.*;

public class BoardSetup 
{
	private Game m_game;
	
	public BoardSetup()
	{
		m_game = new Game();		
		getChessboard().setPieces("", getWhitePlayer(), getBlackPlayer());
	}
	
	public Chessboard getChessboard()
	{
		return m_game.getChessboard();
	}
	
	public Player getWhitePlayer()
	{
		return m_game.getSettings().getPlayerWhite();
	}
	
	public Player getBlackPlayer()
	{
		return m_game.getSettings().getPlayerBlack();
	}
}
