package jchess.core.tests;

import jchess.core.Game;
import jchess.core.Square;
import jchess.core.moves.*;
import jchess.core.pieces.Piece;

public class MovesStub extends Moves
{
	private Square m_lastMoveBegin;
	private Square m_lastMoveEnd;
	
	public MovesStub(Game game) 
	{
		super(game);
	}

	public void addMove(Square begin, Square end, boolean registerInHistory, Castling castlingMove, boolean wasEnPassant, Piece promotedPiece)
	{
		m_lastMoveBegin = begin;
		m_lastMoveEnd = end;
		super.addMove(begin, end, registerInHistory, castlingMove, wasEnPassant, promotedPiece);
	}
	
	public Square lastMoveBegin()
	{
		return m_lastMoveBegin;
	}
	
	public Square lastMoveEnd()
	{
		return m_lastMoveEnd;
	}
}
