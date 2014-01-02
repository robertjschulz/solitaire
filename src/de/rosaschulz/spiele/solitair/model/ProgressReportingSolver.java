package de.rosaschulz.spiele.solitair.model;

import java.util.Date;

public abstract class ProgressReportingSolver {

	/**
	 * @return ranking: number of left pins, 0 for one in the middle (perfect solution)
	 */
	public abstract int doSolve();

	protected Board board;
	protected long stepCounter;
	protected double progress;
	protected Date startDate;

	protected void setBestMove(int bestRanking, Move bestMove) {
		board.bestResult = bestRanking;
		board.bestMove = bestMove;
	}
}
