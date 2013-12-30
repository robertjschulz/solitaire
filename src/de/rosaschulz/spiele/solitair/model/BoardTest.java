package de.rosaschulz.spiele.solitair.model;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.TreeSet;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {

	LineUp lineUpStart;
	LineUp lineUpEmpty;
	LineUp lineUpFinish;
	private Collection<LineUp> randomLineUps;
	
	Board board1;
	
	@Before
	public void setUp() throws Exception {
		lineUpEmpty = new LineUp();
		lineUpEmpty.clear();
		
		lineUpStart = new LineUp();
		lineUpStart.start();

		lineUpFinish = new LineUp();
		lineUpFinish.clear();
		lineUpFinish.set(3, 3, true);

		randomLineUps = new Vector<LineUp>();
		for (int i = 0; i < 10; i++) {
			LineUp lineUp = new LineUp();
			lineUp.random();
			randomLineUps.add(lineUp);
		}
		
		board1 = new Board();
		String boardString = "  XOX  \n"
				+ "  OOO  \n"
				+ "XOXOOXX\n"
				+ "OOOXXXX\n"
				+ "OOXOOXX\n"
				+ "  XOX  \n"
				+ "  XXX\n";
		board1.parse(boardString);
	}

	@Test
	public void testGetLineUpID() {
		assertEquals((1L << 32) - 1, Board.getLineUpID(lineUpStart));
		assertEquals((1L << 32), Board.getLineUpID(lineUpFinish));
		
		for (LineUp lineUp : randomLineUps) {
			long expected = Board.getLineUpID(lineUp);
			
			for (int i = 0; i < GeneralField.SYMMETRIES_COUNT; i++) {
				int[] symmetry = GeneralField.symmetries[i];
				LineUp lineUp2 = lineUp.getSymmetric(symmetry);
				long actual = Board.getLineUpID(lineUp2);
				assertEquals(expected, actual);
			}
		}
	}

	@Test
	public void testDoMove() {
		Collection<Move> possibleMoves = board1.possibleMoves;
		for (Move move : possibleMoves) {
			System.out.println("testing move:" +move);
			
			long lineUpIdBefore=Board.getLineUpID(board1.lineUp);
			assertEquals(board1.currentLineUpId, lineUpIdBefore);
			
			board1.doMove(move);
			board1.printOut(System.out);
			long lineUpIdAfter=Board.getLineUpID(board1.lineUp);
			assertEquals(board1.currentLineUpId, lineUpIdAfter);
			assertEquals(board1.possibleMoves, new TreeSet<Move>(board1.getAllMoves()));
			
			board1.undoMove();
			assertEquals(board1.currentLineUpId, lineUpIdBefore);
		}
	}

}
