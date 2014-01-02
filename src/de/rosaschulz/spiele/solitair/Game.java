package de.rosaschulz.spiele.solitair;

/*
 * Created on 21.06.2003
 *
 * To change the template for this generated file go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */

import java.util.Vector;

import de.rosaschulz.spiele.solitair.model.Board;
import de.rosaschulz.spiele.solitair.model.IterativeSolver;
import de.rosaschulz.spiele.solitair.model.Move;

/**
 * @author robert
 * 
 * To change the template for this generated type comment go to
 * Window>Preferences>Java>Code Generation>Code and Comments
 */
/**
 * found and reoprganised this version from june 2003
 * 
 * @author gast
 * 
 */
public class Game {

	public Board myBoard;

	public java.util.Vector<Move> moves;

//	public java.util.Vector<Move> possibleMoves;

	public static void main(String[] args) throws CloneNotSupportedException {

		Game game = new Game();
		game.solveGame();
	}

	public void solveGame()  {
		System.out.println("trying to solve solitair");
		System.out.println();
		newGame();
		System.out.println("start board:");
		myBoard.printOut(System.out);
		System.out.println();

		System.out.println("solving...");
		System.out.println();
		
		long start = System.nanoTime();
		IterativeSolver solver = new IterativeSolver(myBoard);
		solver.doSolve();
		long end=System.nanoTime();
		System.out.println("solving took " + (end-start)*1E-9 + " seconds...");
		System.out.println();

	}



	public Game() {
		super();
		moves = new Vector<Move>();
//		possibleMoves = new Vector<Move>();
		myBoard = new Board();
		newGame();
	}

	/**
	 * 
	 */
	public void newGame() {
		moves.clear();
		myBoard.setStart();
	}

}
