package jchess.core.tests;

import jchess.core.*;
import jchess.core.pieces.implementation.King;
import jchess.utils.Settings;

public class ChessboardStub extends Chessboard
{
	private Square m_lastMoveBegin;
	private Square m_lastMoveEnd;
	
	public ChessboardStub(Settings settings, jchess.core.moves.Moves Moves) 
	{
		super(settings, Moves);
	}
	
	public void move(Square begin, Square end, boolean refresh, boolean clearForwardHistory)
	{
		//Save the begin/end squares
		m_lastMoveBegin = begin;
		m_lastMoveEnd = end;
		super.move(begin, end, refresh, clearForwardHistory);
	}
	
	public Square lastMoveBegin()
	{
		return m_lastMoveBegin;
	}
	
	public Square lastMoveEnd()
	{
		return m_lastMoveEnd;
	}
	
	public void setKingWhite(King king)
	{
		kingWhite = king;
	}
	
	public void setKingBlack(King king)
	{
		kingBlack = king;
	}
}
