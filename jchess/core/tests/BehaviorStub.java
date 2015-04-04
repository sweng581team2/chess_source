package jchess.core.tests;

import java.util.HashSet;
import java.util.Set;

import jchess.core.Square;
import jchess.core.pieces.Piece;
import jchess.core.pieces.traits.behaviors.Behavior;

public class BehaviorStub extends Behavior
{
	private boolean m_getSquaresInRangeWasCalled;
	private Set<Square> m_squaresInRange;
	
	public BehaviorStub(Piece piece) 
	{
		super(piece);
		m_getSquaresInRangeWasCalled = false;
		m_squaresInRange = new HashSet<Square>();
	}
	
	public boolean getSquaresInRangeWasCalled()
	{
		return m_getSquaresInRangeWasCalled;
	}
	
	public void addSquareInRange(Square square)
	{
		m_squaresInRange.add(square);
	}
	
	public Set<Square> getSquaresInRange()
	{
		m_getSquaresInRangeWasCalled = true;
		return m_squaresInRange;
	}
}
