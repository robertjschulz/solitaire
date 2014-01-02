package de.rosaschulz.spiele.solitair.model;

import java.util.Collection;
import java.util.Iterator;

public class InversionSolver {
	
	Board board;
	long foundBoards;
	long stepCounter;
	
	public static void main(String[] args) {
		InversionSolver solver = new InversionSolver();
		solver.doSolve();
	}

	private void doSolve() {
		board = new Board();
		foundBoards = 0;
		stepCounter = 0;
		count(15, 0, 1);
		System.out.println("found "+ foundBoards+ " boards.");
		System.out.println("made "+ stepCounter+ " steps.");
	}
	
	public void count(int untilTurn, double progressStart, double progressEnd) {
		if (board.turnNumber == untilTurn) {
			foundBoards++;
			return;
		} else {
			Collection<Move> currentPossibleMoves = board.possibleMoves;
			double progress=progressStart;
			double progressStep=(progressEnd-progressStart)/currentPossibleMoves.size();
			for (Iterator<Move> iter = currentPossibleMoves.iterator(); iter.hasNext();) {
				Move move = iter.next();
				// System.out.println("Move Nr." + i + ": " + move.toString());
				if(progressStep >  1E-6 ) {
					System.out.println("solution progress: " + progress 
							+ " progressStep: " + progressStep 
							+ " foundBoards: " + foundBoards 
							+ " stepCounter="+stepCounter+": ");
					board.printOut(System.out);
				}
				board.doMove(move);
				
				count(untilTurn, progress, progress+progressStep);
				board.undoMove();
				
				stepCounter++;
				progress+=progressStep;
			}
		}
	}
	
	
}
