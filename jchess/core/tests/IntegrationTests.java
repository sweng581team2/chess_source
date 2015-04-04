package jchess.core.tests;

import jchess.core.*;
import jchess.core.pieces.Piece;
import jchess.utils.*;
import static org.junit.Assert.*;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class IntegrationTests 
{
	@Test 
	public void MouseDownInvokesChessboardMoveWithCorrectArgs()
	{
		//set up dependencies
		GameWithInjectableChessboard game = new GameWithInjectableChessboard();
		game.getSettings().setGameType(Settings.gameTypes.local);
		//Inject a chessboard stub
		ChessboardStub board = new ChessboardStub(game.getSettings(), game.getMoves());
		board.setChessboardView(new ChessboardViewStub(board));
		game.setChessboard(board);
		new BoardSetup(game);
		
		//Set a pawn as the active piece
		board.setActiveSquare(board.getSquare(3, 1));
		//Create a mouse event to move the pawn
		MouseEvent event = CreateMousePressedEvent(game, 3, 3);
		game.mousePressed(event);
		
		//Verify the call to move was made with the correct squares
		assertEquals(board.getSquare(3, 1), board.lastMoveBegin());
		assertEquals(board.getSquare(3, 3), board.lastMoveEnd());
	}
	
	@Test 
	public void ChessboardMoveCallsAddMoveWithCorrectArgs()
	{
		//set up dependencies
		GameWithInjectableChessboard game = new GameWithInjectableChessboard();
		game.getSettings().setGameType(Settings.gameTypes.local);
		//Inject a Moves stub
		MovesStub moves = new MovesStub(game);
		Chessboard board = new Chessboard(game.getSettings(), moves);
		board.setChessboardView(new ChessboardViewStub(board));
		game.setChessboard(board);
		new BoardSetup(game);
		
		//Set a pawn as the active piece
		Piece pawn = board.getSquare(3,  1).piece;
		board.setActiveSquare(board.getSquare(3, 1));
		//Create a mouse event to move the pawn
		MouseEvent event = CreateMousePressedEvent(game, 3, 3);
		game.mousePressed(event);
		
		//Verify the chessboard called addMove with the correct args. 
		//Apparently it makes a copy of the Squares, so we need to do value-checking rather than a simple assertEquals
		assertEquals(pawn, moves.lastMoveBegin().piece);
		assertEquals(3, moves.lastMoveBegin().getPozX());
		assertEquals(1, moves.lastMoveBegin().getPozY());
		assertEquals(3, moves.lastMoveEnd().getPozX());
		assertEquals(3, moves.lastMoveEnd().getPozY());
	}
	
	@Test 
	public void AddMoveChecksIfOpposingKingIsInCheck()
	{
		//set up dependencies
		GameWithInjectableChessboard game = new GameWithInjectableChessboard();
		game.getSettings().setGameType(Settings.gameTypes.local);
		ChessboardStub board = new ChessboardStub(game.getSettings(), game.getMoves());
		board.setChessboardView(new ChessboardViewStub(board));
		game.setChessboard(board);
		BoardSetup setup = new BoardSetup(game);

		//Inject a king for the black player
		KingStub blackKing = new KingStub(board, setup.getBlackPlayer());
		board.setKingBlack(blackKing);

		//Set a pawn as the active piece
		board.setActiveSquare(board.getSquare(3, 1));
		//Create a mouse event to move the pawn
		MouseEvent event = CreateMousePressedEvent(game, 3, 3);
		game.mousePressed(event);

		//Verify the Moves queried if the black king was checked
		assertTrue(blackKing.isCheckedWasCalled());
	}
	
	@Test 
	public void KingIsCheckedQueriesIfOpposingPiecesCanAttackIt()
	{
		//set up dependencies
		GameWithInjectableChessboard game = new GameWithInjectableChessboard();
		game.getSettings().setGameType(Settings.gameTypes.local);
		ChessboardStub board = new ChessboardStub(game.getSettings(), game.getMoves());
		board.setChessboardView(new ChessboardViewStub(board));
		game.setChessboard(board);
		BoardSetup setup = new BoardSetup(game);

		//Inject behaviors for all white players on the board
		List<Piece> pieces = GetAllPiecesForPlayer(board, setup.getWhitePlayer());
		List<BehaviorStub> behaviors =  new ArrayList<BehaviorStub>();
		for (Piece p : pieces)
		{
			BehaviorStub behavior = new BehaviorStub(p);
			p.addBehavior(behavior);
			behaviors.add(behavior);
		}
		
		//Set a pawn as the active piece
		board.setActiveSquare(board.getSquare(3, 1));
		//Create a mouse event to move the pawn
		MouseEvent event = CreateMousePressedEvent(game, 3, 3);
		game.mousePressed(event);

		//Verify getPiecesInRange was invoked for each behavior
		for (BehaviorStub behavior : behaviors)
		{
			assertTrue(behavior.getSquaresInRangeWasCalled());
		}
	}
	
	private static MouseEvent CreateMousePressedEvent(Game game, int x, int y)
	{
		return new MouseEvent(game, MouseEvent.MOUSE_PRESSED, new Date().getTime(), MouseEvent.BUTTON1_MASK, x, y, 1, false);
	}
	
	private static List<Piece> GetAllPiecesForPlayer(Chessboard board, Player player)
	{
		List<Piece> pieces = new ArrayList<Piece>();
		Square[][] squares = board.getSquares();
		for (int row = 0; row < squares.length; row++)
		{
			for (int column = 0; column < squares[row].length; column++)
			{
				Square square = squares[row][column];
				Piece piece = square.piece;
				if (piece != null && piece.getPlayer() == player)
					pieces.add(piece);
			}
		}
		
		return pieces;
	}
}
