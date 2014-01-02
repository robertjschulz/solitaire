package de.rosaschulz.spiele.solitair.model;

import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

public class IterativeSolver extends ProgressReportingSolver {
	
	private Date endDate;
	MoveHistory solution;
	
	Vector<Set<Long> > checkedLineUpIds;
	int checkedLineUpIdsLastMove;
	int checkedLineUpIdsCleanDepth;
	
	public IterativeSolver(Board board2) {
		board=board2;
		solution = new MoveHistory();
		
		checkedLineUpIdsLastMove = GeneralField.FIELD_COUNT - 5;
		checkedLineUpIdsCleanDepth = 8;
		
		checkedLineUpIds = new Vector<>(checkedLineUpIdsLastMove);
		for (int i = 0; i <= checkedLineUpIdsLastMove; i++) {
			checkedLineUpIds.add(new TreeSet<Long>());
		}
	}

	public static void main(String[] args) {
		Board board = new Board();
		IterativeSolver solver = new IterativeSolver(board);
		solver.doSolve();
	}

	@Override
	public int doSolve() {
		stepCounter = 0;
		progress = 0.;
		Thread progressWatcherThread = new Thread(new ProgressWatcher(this));
		progressWatcherThread.start();
		
		startDate = new Date();
		long start = System.nanoTime();
		int ranking = solve(0, 1);
		long end=System.nanoTime();
		endDate = new Date();

		progressWatcherThread.interrupt();
		try {
			progressWatcherThread.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println("made "+ stepCounter+ " steps. In " + (endDate.getTime()-startDate.getTime())/1000 + " seconds");
		System.out.println("solving took " + (end-start)*1E-9 + " seconds...");
		System.out.println();
		setBestMove(ranking, solution.isEmpty()?null:solution.getFirst());

		return ranking;
	}
	
	public int solve(double progressStart, double progressEnd) {
		int bestRating = 49;
//		try {
//			Thread.sleep(1);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

		Collection<Move> currentPossibleMoves = board.possibleMoves;
		if (currentPossibleMoves.size() == 0) {
			bestRating = board.getRanking(); //countSteine();
		} else {
			progress=progressStart;
			double progressStep=(progressEnd-progressStart)/currentPossibleMoves.size();
			for (Iterator<Move> iter = currentPossibleMoves.iterator(); iter.hasNext();) {
				Move move = iter.next();
				board.doMove(move);
				board.useLineUpId =true;
				
				boolean solveThisBoard = true;

				if(board.turnNumber <= checkedLineUpIdsLastMove) {
					Set<Long> set = checkedLineUpIds.get(board.turnNumber);
					if(set.contains(board.currentLineUpId)) {
						solveThisBoard = false;
					} else {
						set.add(board.currentLineUpId);
					}
				}
				
				if( solveThisBoard ) {
					int lastRating = solve(progress, progress+progressStep);
					if (lastRating < bestRating) {
						bestRating = lastRating;
					}
					
					int cleanTurnNr = board.turnNumber+checkedLineUpIdsCleanDepth;
					if(cleanTurnNr <= checkedLineUpIdsLastMove) {
						Set<Long> set = checkedLineUpIds.get(cleanTurnNr);
						set.clear();
					}
				}
				board.undoMove();
				
				stepCounter++;
				progress+=progressStep;
				

//				for (int i = 0; i < checkedLineUpIdsLastMove; i++) {
//					checkedLineUpIds.setElementAt(new TreeSet<Long>(), i);
//				}
			

				if (bestRating == 0) {
					solution.addFirst(move);
					break;
				}
			}
		}
		if (bestRating == 0) {
			//System.out.println("Zug Nr. " + turnNumber + ":");
			board.printOut(System.out);
		}
		return bestRating;
	}



	
}
