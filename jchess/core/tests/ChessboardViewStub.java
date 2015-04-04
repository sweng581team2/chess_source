package jchess.core.tests;

import java.awt.Point;

import jchess.core.*;
import jchess.display.views.chessboard.*;

public class ChessboardViewStub extends ChessboardView
{
	private Chessboard m_chessboard;
	
	public ChessboardViewStub(Chessboard board)
	{
		m_chessboard = board;
	}

	@Override
	public Square getSquare(int clickedX, int clickedY) 
	{
		//Assume 1x1 pixel squares and just return the square
		return m_chessboard.getSquare(clickedX, clickedY);
	}

	@Override
	public void unselect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getChessboardWidht() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getChessboardHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getChessboardWidht(boolean includeLables) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getChessboardHeight(boolean includeLabels) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSquareHeight() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void resizeChessboard(int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Point getTopLeftPoint() {
		// TODO Auto-generated method stub
		return null;
	}

}
