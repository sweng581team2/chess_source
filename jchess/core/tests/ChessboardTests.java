package jchess.core.tests;

import jchess.core.*;
import jchess.core.pieces.Piece;
import jchess.core.pieces.implementation.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ChessboardTests 
{
	private static final int MinMinus = -1;
	private static final int Min = 0;
	private static final int MinPlus = 1;
	private static final int Nom = 3;
	private static final int MaxMinus = 6;
	private static final int Max = 7;
	private static final int MaxPlus = 8;
	
	private static final int[] NormalBoundaryRange =
		{ Min, MinPlus, Nom, MaxMinus, Max };
	
	private static final int[] InvalidValues =
		{ MinMinus, MaxPlus };
    
	@Test
	public void MovesWithinBoundaryDoNotThrowExceptions()
	{
		BoardSetup setup = new BoardSetup();
		Chessboard board = setup.getChessboard();
		clearBoard(board);
		
		Square[][] boardSquares = board.getSquares();
		Queen queen = new Queen(board, setup.getWhitePlayer());
		
		//Iterate each combination of boundary moves
		for (MoveIndices indices : getValidBoundaryMoves())
		{
			int fromX = indices.FromX;
			int fromY = indices.FromY;
			int toX = indices.ToX;
			int toY = indices.ToY;

			// Place the queen at the from location
			boardSquares[fromX][fromY].piece = queen;

			String errorMessageString = "Attempt to move queen from [" + fromX
					+ "," + fromY + "] to [" + toX + "," + toY + "] failed.";

			try 
			{
				// Try to make the move
				board.move(fromX, fromY, toX, toY);

				// Verify it was moved
				Piece actualPiece = board.getSquares()[toX][toY].piece;
				assertEquals(errorMessageString, queen, actualPiece);
			} 
			catch (Exception e) // Pokemon exception handling. Gotta catch 'em all!
			{
				fail(errorMessageString);
			}
		}
	}
	
	@Test
	public void MovesOutsideBoundaryThrowArrayIndexOutOfBoundsExceptions()
	{
		BoardSetup setup = new BoardSetup();
		Chessboard board = setup.getChessboard();
		clearBoard(board);
		
		Square[][] boardSquares = board.getSquares();
		Queen queen = new Queen(board, setup.getWhitePlayer());
		
		//Test all moves from a valid square to an invalid square
		for (MoveIndices indices : getInvalidBoundaryMoves()) 
		{
			int fromX = indices.FromX;
			int fromY = indices.FromY;
			int toX = indices.ToX;
			int toY = indices.ToY;

			// Place the queen at the from location
			boardSquares[fromX][fromY].piece = queen;

			try 
			{
				// Try to make the move. This should catch the exception and do nothing.
				board.move(fromX, fromY, toX, toY);

				// Verify it was not moved from the original square
				Piece actualPiece = boardSquares[fromX][fromY].piece;

				String errorMessageString = "Attempt to move queen from ["
						+ fromX + "," + fromY + "] to [" + toX + "," + toY
						+ "] was allowed, " + "which is not expected";
				assertEquals(errorMessageString, queen, actualPiece);
			} 
			catch (Exception e) // Pokemon exception handling. Gotta catch 'em all!
			{
				String errorMessageString = "Attempt to move queen from ["
						+ fromX + "," + fromY + "] to [" + toX + "," + toY
						+ "] threw an exception," + "which is not expected.";
				fail(errorMessageString);
			}
		}
	}
	
	private static void clearBoard(Chessboard board)
	{
		Square[][] boardSquares = board.getSquares();
        for (int x = 0; x < 8; x++)
        {
            for (int y = 0; y < 8; y++)
            {
                boardSquares[x][y].piece = null;
            }
        }
	}
	
	private static List<MoveIndices> getValidBoundaryMoves()
	{
		List<MoveIndices> moves = new ArrayList<MoveIndices>();
		for (int fromX : NormalBoundaryRange)
		{
			for (int fromY : NormalBoundaryRange)
			{
				for (int toX : NormalBoundaryRange)
				{
					for (int toY : NormalBoundaryRange)
					{
						if (fromX == toX && fromY == toY)
							continue; //Skip moving from/to the same square
						
						moves.add(new MoveIndices(fromX, fromY, toX, toY));
					}
				}
			}
		}
		
		return moves;
	}
	
	private static List<MoveIndices> getInvalidBoundaryMoves()
	{
		List<MoveIndices> moves = new ArrayList<MoveIndices>();
		for (int validIndex1 : NormalBoundaryRange)
		{
			for (int validIndex2 : NormalBoundaryRange) 
			{
				for (int invalidIndex : InvalidValues) 
				{
					// Test the invalid index for toX and toY.
					//Can't go from an invalid index because we cannot set the piece outside the board.
					for (int validIndex3 : NormalBoundaryRange) 
					{
						moves.add(new MoveIndices(validIndex1, validIndex2, invalidIndex, validIndex3));
						moves.add(new MoveIndices(validIndex1, validIndex2, validIndex3, invalidIndex));
					}
					
					//Worst case -- both toX and toY invalid
					moves.add(new MoveIndices(validIndex1, validIndex2, invalidIndex, invalidIndex));
				}
			}
		}
		
		return moves;
	}
	
	private static class MoveIndices
	{
		public MoveIndices(int fromX, int fromY, int toX, int toY)
		{
			FromX = fromX;
			FromY = fromY;
			ToX = toX;
			ToY = toY;
		}
		
		public int FromX;
		public int FromY;
		public int ToX;
		public int ToY;
	}
}
