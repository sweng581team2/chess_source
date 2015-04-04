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

/** Some top-down style integration tests. 
 * Testing the flow from:
 * Game.mousePressed --> Chessboard.move --> Moves.addMove --> King.isChecked --> Behavior.getSquaresInRange
 * @author Jeremy
 *
 */
public class IntegrationTests 
{
	@Test 
	public void MouseDownInvokesChessboardMoveWithCorrectArgs()
	{
		/* Test Game.mousePressed --> Chessboard.move
		* Just verify the correct arguments are passed
		*/
		
		//set up dependencies
		GameWithInjectableChessboard game = new GameWithInjectableChessboard();
		game.getSettings().setGameType(Settings.gameTypes.local);
		//Inject a chessboard stub
		ChessboardStub board = new ChessboardStub(game.getSettings(), game.getMoves());
		board.setChessboardView(new ChessboardViewStub(board));
		game.setChessboard(board);
		new BoardSetup(game);
		
		//Test moving pawns
		for (int i = 0; i < 8; i++)
		{
			//White move
			//Set pawn as the active piece
			board.setActiveSquare(board.getSquare(i, 6));
			//Create a mouse event to move the pawn
			MouseEvent event = CreateMousePressedEvent(game, i, 4);
			game.mousePressed(event);

			//Verify the call to move was made with the correct squares
			assertEquals(board.getSquare(i, 6), board.lastMoveBegin());
			assertEquals(board.getSquare(i, 4), board.lastMoveEnd());

			//Black move
			//Set pawn as the active piece
			board.setActiveSquare(board.getSquare(i, 1));
			//Create a mouse event to move the pawn
			event = CreateMousePressedEvent(game, i, 3);
			game.mousePressed(event);

			//Verify the call to move was made with the correct squares
			assertEquals(board.getSquare(i, 1), board.lastMoveBegin());
			assertEquals(board.getSquare(i, 3), board.lastMoveEnd());
		}
	}
	
	@Test 
	public void ChessboardMoveCallsAddMoveWithCorrectArgs()
	{
		/* Test Game.mousePressed --> Chessboard.move --> Moves.addMove
		* Just verify the correct arguments are passed
		*/
		
		//set up dependencies
		GameWithInjectableChessboard game = new GameWithInjectableChessboard();
		game.getSettings().setGameType(Settings.gameTypes.local);
		//Inject a Moves stub
		MovesStub moves = new MovesStub(game);
		Chessboard board = new Chessboard(game.getSettings(), moves);
		board.setChessboardView(new ChessboardViewStub(board));
		game.setChessboard(board);
		new BoardSetup(game);
		
		//Test moving pawns
		for (int i = 0; i < 8; i++)
		{
			//White move
			//Set pawn as the active piece
			Piece pawn = board.getSquare(i, 6).piece;
			board.setActiveSquare(board.getSquare(i, 6));
			//Create a mouse event to move the pawn
			MouseEvent event = CreateMousePressedEvent(game, i, 4);
			game.mousePressed(event);

			//Verify the chessboard called addMove with the correct args. 
			//Apparently it makes a copy of the Squares, so we need to do value-checking rather than a simple assertEquals
			assertEquals(pawn, moves.lastMoveBegin().piece);
			assertEquals(i, moves.lastMoveBegin().getPozX());
			assertEquals(6, moves.lastMoveBegin().getPozY());
			assertEquals(i, moves.lastMoveEnd().getPozX());
			assertEquals(4, moves.lastMoveEnd().getPozY());
			
			//Black move
			//Set pawn as the active piece
			pawn = board.getSquare(i, 1).piece;
			board.setActiveSquare(board.getSquare(i, 1));
			//Create a mouse event to move the pawn
			event = CreateMousePressedEvent(game, i, 3);
			game.mousePressed(event);

			//Verify the chessboard called addMove with the correct args. 
			//Apparently it makes a copy of the Squares, so we need to do value-checking rather than a simple assertEquals
			assertEquals(pawn, moves.lastMoveBegin().piece);
			assertEquals(i, moves.lastMoveBegin().getPozX());
			assertEquals(1, moves.lastMoveBegin().getPozY());
			assertEquals(i, moves.lastMoveEnd().getPozX());
			assertEquals(3, moves.lastMoveEnd().getPozY());
		}
	}
	
	@Test 
	public void AddMoveDoesNotAddPlusIfKingIsNotInCheck()
	{
		/* Test Game.mousePressed --> Chessboard.move --> Moves.addMove --> King.isChecked
		* Test that Moves makes a decision whether or not to add a + based on what King.isChecked returns
		*/
		
		//set up dependencies
		GameWithInjectableChessboard game = new GameWithInjectableChessboard();
		game.getSettings().setGameType(Settings.gameTypes.local);
		ChessboardStub board = new ChessboardStub(game.getSettings(), game.getMoves());
		board.setChessboardView(new ChessboardViewStub(board));
		game.setChessboard(board);
		BoardSetup setup = new BoardSetup(game);

		//Inject a king for the black player
		KingStub blackKing = new KingStub(board, setup.getBlackPlayer());
		blackKing.setSquare(board.getSquare(4, 0));
		board.getSquare(4, 0).piece = blackKing;
		blackKing.setIsChecked(false);
		board.setKingBlack(blackKing);

		//Set a pawn as the active piece
		board.setActiveSquare(board.getSquare(3, 6));
		//Create a mouse event to move the pawn
		MouseEvent event = CreateMousePressedEvent(game, 3, 4);
		game.mousePressed(event);

		//Verify the Moves queried if the black king was checked
		assertTrue(blackKing.isCheckedWasCalled());
		
		//Verify moves did not add a +
		ArrayList<String> moves = game.getMoves().getMoves();
		assertTrue(moves.size() > 0);
		String lastMove = moves.get(moves.size() - 1);
		
		assertNotEquals('+', lastMove.charAt(lastMove.length() - 1));
	}
	
	@Test 
	public void AddMoveAddsPlusIfKingIsInCheck()
	{
		/*Test Game.mousePressed --> Chessboard.move --> Moves.addMove --> King.isChecked
		* Test that Moves makes a decision whether or not to add a + based on what King.isChecked returns
		*/
		
		//set up dependencies
		GameWithInjectableChessboard game = new GameWithInjectableChessboard();
		game.getSettings().setGameType(Settings.gameTypes.local);
		ChessboardStub board = new ChessboardStub(game.getSettings(), game.getMoves());
		board.setChessboardView(new ChessboardViewStub(board));
		game.setChessboard(board);
		BoardSetup setup = new BoardSetup(game);

		//Inject a king for the black player
		KingStub blackKing = new KingStub(board, setup.getBlackPlayer());
		blackKing.setSquare(board.getSquare(4,0));
		board.getSquare(4, 0).piece = blackKing;
		blackKing.setIsChecked(true); //Put it in check
		board.setKingBlack(blackKing);

		//Set a pawn as the active piece
		board.setActiveSquare(board.getSquare(3, 6));
		//Create a mouse event to move the pawn
		MouseEvent event = CreateMousePressedEvent(game, 3, 4);
		game.mousePressed(event);

		//Verify the Moves queried if the black king was checked
		assertTrue(blackKing.isCheckedWasCalled());
		
		//Verify Moves added a +
		ArrayList<String> moves = game.getMoves().getMoves();
		assertTrue(moves.size() > 0);
		String lastMove = moves.get(moves.size() - 1);
		
		assertEquals('+', lastMove.charAt(lastMove.length() - 1));
	}
	
	@Test 
	public void MovesAddsPoundIfKingIsInCheckmate()
	{
		/*Test Game.mousePressed --> Chessboard.move --> Moves.addMove --> King.isChecked --> Behavior.getSquaresInRange
		* Test that the king decides if it is in check if any other pieces can attack it,
		* and it is in checkmate if it has no escape. The outcome can be observed by whether or not
		* Moves adds a # to the string.
		*/
		
		//set up dependencies
		GameWithInjectableChessboard game = new GameWithInjectableChessboard();
		game.getSettings().setGameType(Settings.gameTypes.local);
		ChessboardStub board = new ChessboardStub(game.getSettings(), game.getMoves());
		board.setChessboardView(new ChessboardViewStub(board));
		game.setChessboard(board);
		BoardSetup setup = new BoardSetup(game);

		//Set a pawn as the active piece
		Piece pawn = board.getSquare(3, 6).piece;
		board.setActiveSquare(board.getSquare(3, 6));
		
		BehaviorStub behavior = new BehaviorStub(pawn);
		/* Set the black king's square in range. 
		 * This actually puts the king in checkmate because it cannot remove the (fake) attack from the pawn
		 */
		behavior.addSquareInRange(board.getSquare(4, 0)); 
		pawn.addBehavior(behavior);
		//Create a mouse event to move the pawn
		MouseEvent event = CreateMousePressedEvent(game, 3, 4);
		game.mousePressed(event);

		//Verify Moves added a # because the king is in checkmate
		ArrayList<String> moves = game.getMoves().getMoves();
		assertTrue(moves.size() > 0);
		String lastMove = moves.get(moves.size() - 1);

		assertEquals('#', lastMove.charAt(lastMove.length() - 1));
	}
	
	private static MouseEvent CreateMousePressedEvent(Game game, int x, int y)
	{
		return new MouseEvent(game, MouseEvent.MOUSE_PRESSED, new Date().getTime(), MouseEvent.BUTTON1_MASK, x, y, 1, false);
	}
}
