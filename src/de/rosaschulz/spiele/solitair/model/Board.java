package de.rosaschulz.spiele.solitair.model;

/*
 * Created on 21.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

/**
 * @author robert
 *
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

import java.io.PrintStream;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;

import de.rosaschulz.spiele.solitair.Formation;
import de.rosaschulz.spiele.solitair.IFForEachField;

//public class Board extends Formation {
public class Board {

	// State
	LineUp lineUp;
	long currentLineUpId;
	int turnNumber;
	Collection<Move> possibleMoves;
	
	// History
	MoveHistory moveHistory;
	private Stack<Collection<Move> > possibleMovesStack;
	
	// solution model
	public int bestResult;
	public Move bestMove;
	long stepCounter;
	private double lastProgressInfo;

	// all static stuff in front

	static Vector<Move> generalMoves = createGeneralMoves();
	
	static long getLineUpID(LineUp lineUp) {
		long mask = 1;
		long result = 0;
		long [] symmetricResults = new long[GeneralField.SYMMETRIES_COUNT];
		for (int i = 0; i < GeneralField.SYMMETRIES_COUNT; i++) {
			symmetricResults[i] = 0;
		}
		for (int fieldIdx = 0; fieldIdx < GeneralField.FIELD_COUNT; fieldIdx++) {
			if(lineUp.get(fieldIdx)) { result = result | mask; }
			for (int i = 0; i < GeneralField.SYMMETRIES_COUNT; i++) {
				int[] symmetry = GeneralField.symmetries[i];
				if(lineUp.get(GeneralField.applySymmetry(symmetry, fieldIdx) ) ) {
					symmetricResults[i] = symmetricResults[i] | mask;
				}
			}
			mask <<= 1;
		}
		for (int i = 0; i < GeneralField.SYMMETRIES_COUNT; i++) {
			if (symmetricResults[i] < result) {
				result = symmetricResults[i];
			}
		}
		return result;
	}
	
	public Board() {
		super();
		lineUp = new LineUp();
		setStart();
	}



	private static LinkedList<Move> getGeneralMovesFrom(Coordinate from) {
		final Richtung[] richtungen = Richtung.values();
		LinkedList<Move> result = new LinkedList<Move>();
		if(from.checkRange()) {
			for (int i = 0; i < richtungen.length; i++) {
				Richtung richtung = richtungen[i];
				Coordinate to = Move.ziel(from, richtung);
				if (to.checkRange()) {
					Move move = new Move(from, to);
					result.add(move);
				}
			}
		}
		return result;
	}
	
	private static Vector<Move> createGeneralMoves() {
		Vector<Move> moves = new Vector<Move>();
		int x, y;
		for (x = 0; x < 7; x++) {
			for (y = 0; y < 7; y++) {
				Coordinate k=new Coordinate(x,y);
				moves.addAll(getGeneralMovesFrom(k));
			}
		}
		System.out.println("number of general moves: " + moves.size());
		
		// create "index" on Fields pointing to these moves
		for (Move move : moves) {
			GeneralField.getAt(move.fromIdx).fromMoves.add(move);
			GeneralField.getAt(move.toIdx).toMoves.add(move);
			GeneralField.getAt(move.midIdx).midMoves.add(move);
		}
//		Collections.sort(moves, new Comparator<Move>(){
//
//			@Override
//			public int compare(Move arg0, Move arg1) {
//				// TODO Auto-generated method stub
//				return 0;
//			}}
//		);
		
		return moves;
	}

	private Set<Move> getMovesByMoveChanges(Move move, Collection<Move> oldPossibleMoves) {
		//System.out.println("getMovesByMoveChanges: doMove: "+move);
		// Logger.log(Level.FINEST, "doMove: "+move);

		TreeSet<Move> result = new TreeSet<Move>();
		for (Move oldPossibleMove : oldPossibleMoves) {			
			if ( oldPossibleMove.fromIdx == move.fromIdx // || listedMove.equals(move)
					|| oldPossibleMove.fromIdx == move.midIdx
					|| oldPossibleMove.midIdx == move.fromIdx
					|| oldPossibleMove.midIdx == move.midIdx
					|| oldPossibleMove.toIdx == move.toIdx ) {
				// do not add
			} else {
				result.add(oldPossibleMove);
			}
		}
		//System.out.println("getMovesByMoveChanges board:");
		//printOut(System.out);
		//System.out.println("getMovesByMoveChanges: result="+result);

		addMovesTo(result, move.fromIdx);

		addMovesTo(result, move.midIdx);

		addMovesFrom(result, move.toIdx);

		addMovesOver(result, move.toIdx);
		//System.out.println("getMovesByMoveChanges: result="+result);

		return result;
	}
	
	public void doMove(Move move) {
		// System.out.println("doMove: "+move);
		
		// board
		lineUp.set(move.fromIdx, false);
		lineUp.set(move.midIdx, false);
		lineUp.set(move.toIdx, true);
		
		// History
		moveHistory.add(move);
		turnNumber++;
		// assert(turnNumber == moveHistory.size());
		
		// Moves 
		possibleMovesStack.push(possibleMoves);

		possibleMoves = getMovesByMoveChanges(move, possibleMoves);
		//possibleMoves = getAllMoves();
		//possibleMoves = getAllMoves2();
		
		// System.out.println("doMove: possibleMoves="+possibleMoves);

		assert(turnNumber == possibleMovesStack.size());

	}

	public void undoMove() {
		Move move = moveHistory.removeLast();
		turnNumber--;
		
		lineUp.set(move.fromIdx, true);
		lineUp.set(move.midIdx, true);
		lineUp.set(move.toIdx, false);
		
		possibleMoves = possibleMovesStack.pop();
	}

	private void setFeld(Coordinate koordinate, boolean value) {
		lineUp.set(koordinate.x, koordinate.y, value);
	}

	public boolean getFeld(Coordinate koordinate) {
		return lineUp.get(koordinate.x, koordinate.y);

	}

	public boolean getFeld(int x, int y) {
		return lineUp.get(x, y);
	}
	
	public void printOut(PrintStream out) {
		out.println("turn: " + turnNumber + "; last Move: " + (moveHistory.isEmpty()?"--":moveHistory.getLast())  + " possible Moves: " + possibleMoves + "");
		int row, col;
		String s;
		for (row = 0; row < 7; row++) {
			for (col = 0; col < 7; col++) {
				if (! Coordinate.checkRange(col, row))
					s = " ";
				else {
					boolean field = lineUp.get(col, row);
					if (field)
						s = "X";
					else
						s = "O";
				}
				out.print(s);
			}
			out.println();
		}
	}


	@Override
	protected Object clone() throws CloneNotSupportedException {
		Object board = super.clone();
		((Board)board).lineUp = (LineUp) lineUp.clone();
		return board;
	}

	/**
	 * @return ranking: number of left pins, 0 for one in the middle (perfect solution)
	 */
	public int solve()  {
		MoveHistory solution = new MoveHistory();
		stepCounter = 0;
		lastProgressInfo=0.0;
		
		System.out.println("start: " + new Date());
		int ranking = solve(solution, 0.0, 800.0);
		System.out.println("solved with best ranking "+ranking+" after trying " + stepCounter + " moves");
		bestResult=ranking;
		bestMove=null;
		if(solution.size() > 0) {
			bestMove=solution.getFirst();
		}
		return ranking;
	}

	/**
	 * @param solution
	 * @param stepCounter 
	 * @return number of sticks left in best result or 0 if last pin is in center
	 */
	public int solve(MoveHistory solution, double progressStart, double progressEnd) {

		// if (moveHistory.stepCounter % 1000 == 0) {
		// System.out.println("Board after " + moveHistory + " moves:");
		//System.out.println("solve: turn " + zugNummer + "");
		//System.out.print("stepCounter="+stepCounter+": ");
		//printOut(System.out);
		// }
		int bestResult = 49;
		//LinkedList<Move> possibleMoves = getAllMoves();
		Collection<Move> currentPossibleMoves = possibleMoves;
		if (currentPossibleMoves.size() == 0) {
			bestResult = getWertung(); //countSteine();
		} else {
			// int i = 0;
			double progress=progressStart;
			double progressStep=(progressEnd-progressStart)/currentPossibleMoves.size();
			for (Iterator<Move> iter = currentPossibleMoves.iterator(); iter.hasNext();) {
				Move move = iter.next();
				// System.out.println("Move Nr." + i + ": " + move.toString());
				if(progressStep >  1E-9 ) {
					System.out.println("solution progress: " + progress + " progressStep: " + progressStep + " stepCounter="+stepCounter+": ");
					printOut(System.out);
					lastProgressInfo=progress;
				}
				doMove(move);
				
				int lastResult = solve(solution, progress, progress+progressStep);
				undoMove();
				
				stepCounter++;
				progress+=progressStep;

				if (lastResult < bestResult) {
					bestResult = lastResult;
				}
				if (bestResult == 0) {
					solution.addFirst(move);
					break;
				}
				// i++;
			}
		}
		if (bestResult == 0) {
			//System.out.println("Zug Nr. " + turnNumber + ":");
			printOut(System.out);
		}
		return bestResult;
	}

	private int getSteineZahlFromZugNummer() {
		return 32-turnNumber;
	}

	/**
	 * @return number of sticks left in best result or 0 if last pin is in center
	 */
	public int getWertung() {
		int count = getSteineZahlFromZugNummer();
		if (count == 1) {
			if (lineUp.get(3,3)) {
				count = 0;
			}
		}
		return count;
	}
	
	/**
	 * @return number of sticks left in best result or 0 if last pin is in center
	 */
	@Deprecated
	public int countSteine2() {
		int count = countSteine();
		if (count == 1) {
			if (lineUp.get(3,3)) {
				count = 0;
			}
		}
		return count;
	}

	private int countSteine() {
		return lineUp.count();
	}

	private void addMovesFrom(Collection<Move> result, int fromIdx) {
		GeneralField fromField = GeneralField.getAt(fromIdx);
		if(lineUp.get(fromIdx)) {
			for (Move move : fromField.toMoves) {
				if(lineUp.get(move.midIdx) && !lineUp.get(move.toIdx)) {
					result.add(move);
				}
			}
		}
	}

	private void addMovesTo(Collection<Move> result, int toIdx) {
		GeneralField toField = GeneralField.getAt(toIdx);
		if(! lineUp.get(toIdx)) {
			for (Move move : toField.toMoves) {
				if(lineUp.get(move.fromIdx) && lineUp.get(move.midIdx)) {
					result.add(move);
				}
			}
		}
	}

	private void addMovesOver(Collection<Move> result, int midIdx) {
		GeneralField midField = GeneralField.getAt(midIdx);
		if(lineUp.get(midIdx)) {
			for (Move move : midField.midMoves) {
				if(lineUp.get(move.fromIdx) && ! lineUp.get(move.toIdx)) {
					result.add(move);
				}
			}
		}
	}


	public LinkedList<Move> getAllMoves() {
		LinkedList<Move> possibleMoves = new LinkedList<Move>();
		for (Move move : generalMoves) {
			if(isMovePossible(move)) {
				possibleMoves.add(move);
			}
		}
		return possibleMoves;
	}

	private boolean isMovePossible(Move move) {
		return (lineUp.get(move.fromIdx) 
				&& lineUp.get(move.midIdx) 
				&& !lineUp.get(move.toIdx) );
	}

//	public int getBestPossibleResult(MoveHistory moveHistory) {
//
//		// if (moveHistory.stepCounter % 1000 == 0) {
//		// System.out.println("Board after " + moveHistory + " moves:");
//		// printOut(System.out);
//		// }
//		int bestResult = 49;
//		LinkedList<Move> possibleMoves = getAllMoves();
//		if (possibleMoves.size() == 0) {
//			bestResult = countSteine2();
//			moveHistory.addGameEnd(this, bestResult);
//		} else {
//			for (Iterator<Move> iter = possibleMoves.iterator(); iter.hasNext();) {
//				Move move = iter.next();
//				// System.out.println("Move Nr." + i + ": " + move.toString());
//				Board boardAfterMove = (Board) this.clone();
//				boardAfterMove.doMove(move);
//				moveHistory.add(move);
//				int lastResult = boardAfterMove
//						.getBestPossibleResult(moveHistory);
//				moveHistory.removeElementAt(moveHistory.size() - 1);
//				if (lastResult < bestResult) {
//					bestResult = lastResult;
//				}
//				if (bestResult == 0) {
//					break;
//				}
//			}
//		}
//		return bestResult;
//	}

	public void setSolution() {
		solve();
		System.out.println("solved after trying " + stepCounter + " moves");
	}

	public int getBestResult() {
		return bestResult;
	}

	public Move getBestMove() {
		return bestMove;
	}

	public void setStart() {
		lineUp.start();
		moveHistory = new MoveHistory();
		possibleMovesStack = new Stack<Collection<Move> >();
		possibleMoves = getAllMoves();
		turnNumber = 0;
		
		assert(possibleMoves.size()==4);
		assert(getSteineZahlFromZugNummer()==32);
		assert(countSteine()==32);
	}

}
