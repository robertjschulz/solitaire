package de.rosaschulz.spiele.solitair.model;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {

	LineUp lineUpStart;
	LineUp lineUpEmpty;
	LineUp lineUpFinish;
	private Collection<LineUp> randomLineUps;
	
	LineUp LineUp1;
	
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
		
		String board = "  XOX  \n"
				+ "  OOO  \n"
				+ "XOXOOXX\n"
				+ "OOOXXXX\n"
				+ "OOXOOXX\n"
				+ "  XOX  \n"
				+ "  XXX\n";
		LineUp1 = new LineUp();
		LineUp1.parse(board);
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
				long current = Board.getLineUpID(lineUp2);
				assertEquals(expected, current);
			}
		}
	}

}
