package de.rosaschulz.spiele.solitair;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import de.rosaschulz.spiele.solitair.model.Board;
import de.rosaschulz.spiele.solitair.model.Move;

public class SolitairApplet extends Applet implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6440697892097673927L;

	private static final String ANY_MOVE_COMMAND = "ANY_MOVE_COMMAND";

	private static final String BEST_MOVE_COMMAND = "BEST_MOVE_COMMAND";

	private static final String START_COMMAND = "START_COMMAND";

	private static final String UNDO_MOVE_COMMAND = "UNDO_MOVE_COMMAND";

	// private MouseListener myMouseListener;
	public Board myBoard;

	private Random random;

	private BoardComponent boardComponent;

	private Container topOfPage;

	private Container bodyOfPage;

	private Container bottomOfPage;

	private Container title;

	private Button anyMoveButton;

	private Label bestResult;

	private Label steineCount;

	private Button startButton;

	private Vector<Move> moveHistory;

	private Button bestMoveButton;
	private Button undoMoveButton;

	private Move bestMove;

	public SolitairApplet() throws HeadlessException {
		super();
		super.setBounds(0, 0, 300, 300);
		
		// System.out.println("SolitairApplet created");

		// GraphicsConfiguration config = new GraphicsConfiguration();

		// setLayout(new GridLayout(4,1));
		setLayout(new BorderLayout());

		title = new Panel();
		topOfPage = new Panel();
		bodyOfPage = new Panel();
		bottomOfPage = new Panel();

		add(title, BorderLayout.PAGE_START);
		add(topOfPage, BorderLayout.NORTH);
		add(bodyOfPage, BorderLayout.CENTER);
		add(bottomOfPage, BorderLayout.SOUTH);

		title.add(new Label("** Solitaire **"));

		add(new Label("left"), BorderLayout.WEST);
		add(new Label("right"), BorderLayout.EAST);

		title.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		title.add(new Label("Hi There!"));
		title.add(new Label("Another Label"));

		topOfPage.add(new Label("topOfPage"));

		bodyOfPage.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

		boardComponent = new BoardComponent();
//		boardComponent.setBoard(myBoard);
		bodyOfPage.add(boardComponent);

		bestResult = new Label();
		// bestResult.setText("best
		// possible:"+boardComponent.getBoard().getBestPossibleResult());
		steineCount = new Label();
		// steineCount.setText("best
		// possible:"+boardComponent.getBoard().getBestPossibleResult());

		// Canvas canvas = new Canvas();
		// canvas.setBounds(10, 50, 12 * 7, 12 * 7);
		// this.add(canvas);

		bottomOfPage.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));
		// bottomOfPage.add(new Label("bottomOfPage"));

		startButton = new Button("start");
		startButton.addActionListener(this);
		startButton.setActionCommand(START_COMMAND);
		bottomOfPage.add(startButton);

		undoMoveButton = new Button("undo");
		undoMoveButton.addActionListener(this);
		undoMoveButton.setActionCommand(UNDO_MOVE_COMMAND);
		bottomOfPage.add(undoMoveButton);

		bestMoveButton = new Button("do best Move");
		bestMoveButton.addActionListener(this);
		bestMoveButton.setActionCommand(BEST_MOVE_COMMAND);
		bottomOfPage.add(bestMoveButton);

		anyMoveButton = new Button("do any Move");
		anyMoveButton.addActionListener(this);
		anyMoveButton.setActionCommand(ANY_MOVE_COMMAND);
		bottomOfPage.add(anyMoveButton);

		bottomOfPage.add(bestResult);
		bottomOfPage.add(steineCount);

		// this.doLayout();
//		updateBoardDisplay();
		startGame();
//		updateBoardDisplay();
		random = new Random();
	}

	@Override
	public void start() {
		// MouseListener myMouseListener;
		// myMouseListener = new MyMouseListener(this);
		// this.addMouseListener(myMouseListener);

		super.start();
		System.out.println("SolitairApplet started");

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
		super.stop();
		System.out.println("SolitairApplet stopped");
	}

	// public void paint(Graphics g) {
	// java.util.Date now = new java.util.Date();
	// String out = now.toString();
	// int lineheight = 12;
	// // Ausgabe
	// g.drawString(out, 0, lineheight);
	// g.drawString("Hallo gro�e sch�ne Welt", 0, lineheight * 2);
	// // MyBoard.SetStart();
	//
	// int boardx = 12 * 0;
	// int boardy = 12 * 6;
	// int fieldwidth = 12;
	// int fieldheight = 12;
	// g.draw3DRect(boardx, boardy, fieldwidth * 7, fieldheight * 7, true);
	// myBoard.paint(g, boardx, boardy, fieldwidth, fieldheight);
	//
	// int charwidth = 8;
	// g.draw3DRect(0, lineheight * 3, charwidth * 11, lineheight - 1, true);
	// g.drawString("random Move", 0, lineheight * (3 + 1));
	//
	// g.drawString("", 0, 36);
	// }

	public void doAnyMove() {
		Collection<Move> moves = myBoard.getAllMoves();
		int moveIndex = random.nextInt(moves.size());
		int i=0;
		for (Move move : moves) {
			if(i==moveIndex) {
				doMove(move);
				break;
			}
			i++;
		}
	}

	private void doMove(Move move) {
		moveHistory.add(move);
		myBoard.doMove(move);
		boardComponent.setBoard(myBoard);
		updateBoardDisplay();
	}

	private void startGame() {
		myBoard = new Board();
		//myBoard.setSolution();
		moveHistory = new Vector<Move>();
		updateBoardDisplay();
	}

	private void updateBoardDisplay() {
		boardComponent.setBoard(myBoard);
		boardComponent.repaint();
		// System.err.println("repaint() called");
		bestMove = null;
		Board board = boardComponent.getBoard();
		bestResult.setText("best possible:"
				+ board.getBestResult());
		bestMove = board.getBestMove();

		steineCount.setText("Steine:"
				+ board.countSteine2());
	}

	public void actionPerformed(ActionEvent e) {
		System.out.println("received ActionEvent \"" + e.getActionCommand()
				+ "\"");
		if (e.getActionCommand().equals(ANY_MOVE_COMMAND)) {
			doAnyMove();
		} else if (e.getActionCommand().equals(BEST_MOVE_COMMAND)) {
			doBestMove();
		} else if (e.getActionCommand().equals(UNDO_MOVE_COMMAND)) {
			undoMove();
		} else if (e.getActionCommand().equals(START_COMMAND)) {
			startGame();
		}

	}

	private void undoMove() {
		myBoard.undoMove();
		//boardComponent.setBoard(myBoard);
		boardComponent.selected = null;
		updateBoardDisplay();
	}

	private void doBestMove() {
		doMove(bestMove);
	}


}

// interface ForEachFieldInterface{ abstract}

// public class AllMovesGetter implements ForEachFieldInterface{}

