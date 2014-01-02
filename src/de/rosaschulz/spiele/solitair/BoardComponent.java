package de.rosaschulz.spiele.solitair;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;
import java.util.Vector;

import de.rosaschulz.spiele.solitair.model.Board;
import de.rosaschulz.spiele.solitair.model.FieldValue;
import de.rosaschulz.spiele.solitair.model.Coordinate;
import de.rosaschulz.spiele.solitair.model.GeneralField;
import de.rosaschulz.spiele.solitair.model.IterativeSolver;
import de.rosaschulz.spiele.solitair.model.Move;

public class BoardComponent extends Component implements MouseListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4683953394128355858L;

	private Board board;

	private static int fieldwidth = 24;

	private static int fieldheight = 24;

	private int xoff;

	private int yoff;

	Coordinate selected;

	private Thread solverThread;

	@Override
	public void paint(Graphics g) {
		
		System.out.println("paint called - actual width=" + this.getWidth()
				+ " height=" + this.getHeight());
		super.paint(g);
		int x, y;
		for (x = 0; x < 7; x++) {
			for (y = 0; y < 7; y++) {
				if(Coordinate.checkRange(x, y)) {
					Rectangle framerec = getFieldFrame(x, y);
					Rectangle fieldrec = getFieldRectangle(x, y);
					g.setColor(Color.BLACK);
					g.drawRect(framerec.x, framerec.y, framerec.width,
							framerec.height);
					boolean field = board.getFeld(x, y);
					if(field) {
						boolean isSelected = false;
						boolean isBestMoveSrc = (board.bestMove != null 
								&& board.bestMove.getFrom().equals(x,y));
						if (selected != null && selected.equals(x,y) ) {
							isSelected = true;
						}
						g.setPaintMode();
						Collection<Move> movesFromHere = getMovesFrom(x, y);
						if (isSelected) {
							g.setColor(Color.RED);
						} else {
							if (isBestMoveSrc) {
								g.setColor(Color.GREEN);
							} else if(movesFromHere.size()>0) {
								g.setColor(Color.YELLOW);
							} else {
								g.setColor(Color.GRAY);
							}
						}
						g.fillOval(fieldrec.x, fieldrec.y, fieldrec.width,
								fieldrec.height);
						// Paint Move-Directions
						for (Move move : movesFromHere) {
							boolean isBestMove = (board.bestMove != null 
									&& board.bestMove.equals(move));
							g.setColor(isBestMove?Color.BLACK:Color.GRAY);
							int midx = fieldrec.x+fieldrec.width/2;
							int midy = fieldrec.y+fieldrec.height/2;
							int dx = (move.to.x-move.from.x)/2;
							int dy = (move.to.y-move.from.y)/2;
							int tx = midx+fieldrec.width/2*dx;
							int ty = midy+fieldrec.height/2*dy;
							g.drawLine(midx, midy, tx, ty);
							int w=2,l=5;
							Polygon p = new Polygon();
							p.addPoint(tx, ty);
							p.addPoint(tx += -l*dx -   w*dy , ty +=    w*dx - l*dy);
							p.addPoint(tx +=       + 2*w*dy , ty += -2*w*dx       );
							p.addPoint(tx +=  l*dx -   w*dy , ty +=    w*dx + l*dy);
							g.drawPolygon(p );
						}
					} else {
						g.clearRect(fieldrec.x, fieldrec.y, fieldrec.width,
								fieldrec.height);
					}
				}

				// g.drawString(s, xoff + fieldwidth * x, yoff + fieldheight
				// * (y + 1));

			}
		}
	}

	private Collection<Move> getMovesFrom(int x, int y) {
		Collection<Move> moves = new Vector<Move>();
		board.addMovesFrom(moves, GeneralField.getIdxFromXY(x, y));
		return moves;
	}

	private Rectangle getFieldRectangle(int x, int y) {
		Rectangle fieldrec = new Rectangle(1 + xoff + x * (fieldwidth + 1), 1
				+ yoff + y * (fieldheight + 1), fieldwidth, fieldheight);
		return fieldrec;
	}

	private Rectangle getFieldFrame(int x, int y) {
		Rectangle fieldrec = new Rectangle(xoff + x * (fieldwidth + 1), yoff
				+ y * (fieldheight + 1), fieldwidth + 1, fieldheight + 1);
		return fieldrec;
	}

	public BoardComponent() {
		super();
		xoff = 0;
		yoff = 0;
		selected = null;
		this.setMinimumSize(getPreferredSize());
		this.addMouseListener(this);
		solverThread = null;
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(7 * (fieldwidth + 1) + 1,
				7 * (fieldheight + 1) + 1);
		// return super.getSize();
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
		startSolverThread();
	}

	public void mouseClicked(MouseEvent e) {
		if (e.getButton() == MouseEvent.BUTTON1) {
			int mx = e.getX();
			int my = e.getY();
			int x, y;
			for (x = 0; x < 7; x++) {
				for (y = 0; y < 7; y++) {
					// Field field = board.feld[x][y];
					Rectangle fieldrec = getFieldRectangle(x, y);
					if (fieldrec.contains(mx, my)) {
						Collection<Move> movesFrom = getMovesFrom(x, y);
						if (!movesFrom.isEmpty()) {
							if (movesFrom.size()==1) {
								doMove(movesFrom.iterator().next());
							} else {
								selected = new Coordinate(x, y);
							}
							this.repaint();
						} else {
							if (selected != null && ! board.getFeld(x, y)) {
								Move checkedMove = null;
								Collection<Move> movesFromSelected = getMovesFrom(selected.x,selected.y);
								for (Move moveFromSelected : movesFromSelected) {
									if(moveFromSelected.to.equals(x,y)) {
										checkedMove=moveFromSelected;
										break; // foreach
									}
								}
								if(checkedMove == null) {
									selected = null;
								} else {
									doMove(checkedMove);
								}
								this.repaint();
							}
						}
					}
				}
			}
		}

	}


	private void doMove(Move checkedMove) {
		board.doMove(checkedMove);
		startSolverThread();
		//workerThread.st
		selected = null;
	}

	private void startSolverThread() {
		if(solverThread!=null) {
			System.out.println("SolverThread in state: "+solverThread.getState());
		}
		if(solverThread!=null && solverThread.isAlive()) {
			System.out.println("SolverThread is still alive... interrupting!");
			solverThread.interrupt();
			try {
				solverThread.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			solverThread=null;
		}
		solverThread = new Thread() { 
			public void run() {
				Board solverBoard;
				try {
					solverBoard = (Board) board.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					solverThread = null;
					return;
				}
				// TODO: progressMeter/-Bar
				System.out.println("running SolverThread...");
				IterativeSolver solver = new IterativeSolver(solverBoard);
				solver.doSolve();
				
				setBestMove(solverBoard.bestResult, solverBoard.bestMove);
			}
		};

		// TODOcheck if we have to stop?
		//solverThread.stop();
		solverThread.start();
	}

	// TODO: check synchronization!
	synchronized protected void setBestMove(int bestResult, Move bestMove) {
		board.bestResult = bestResult;
		board.bestMove = bestMove;
		// invalidate();
		repaint();
	}

	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}

}
