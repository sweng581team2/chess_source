package jchess.core.tests;

import jchess.core.Chessboard;
import jchess.core.Player;
import jchess.core.pieces.implementation.King;

public class KingStub extends King
{
	private boolean m_isCheckedWasCalled;
	
	public KingStub(Chessboard chessboard, Player player) 
	{
		super(chessboard, player);
		m_isCheckedWasCalled = false;
	}
	
	public boolean isChecked()
	{
		m_isCheckedWasCalled = true;
		return super.isChecked();
	}
	
	public boolean isCheckedWasCalled()
	{
		return m_isCheckedWasCalled;
	}
}
