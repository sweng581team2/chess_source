package jchess.core.tests;

import jchess.core.Chessboard;
import jchess.core.Player;
import jchess.core.pieces.implementation.King;

public class KingStub extends King
{
	private boolean m_isCheckedWasCalled;
	private boolean m_isChecked;
	
	public KingStub(Chessboard chessboard, Player player) 
	{
		super(chessboard, player);
		m_isCheckedWasCalled = false;
		m_isChecked = false;
	}
	
	public void setIsChecked(boolean isChecked)
	{
		m_isChecked = isChecked;
	}
	
	public boolean isChecked()
	{
		m_isCheckedWasCalled = true;
		return m_isChecked;
	}
	
	public boolean isCheckedWasCalled()
	{
		return m_isCheckedWasCalled;
	}
}
