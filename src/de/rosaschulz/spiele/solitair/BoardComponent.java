package de.rosaschulz.spiele.solitair;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Set;

import de.rosaschulz.spiele.solitair.model.Board;
import de.rosaschulz.spiele.solitair.model.FieldValue;
import de.rosaschulz.spiele.solitair.model.Coordinate;
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

	@Override
	public void paint(Graphics g) {
		
		System.out.println("paint called - actual width=" + this.getWidth()
				+ " height=" + this.getHeight());
		super.paint(g);
		int x, y;
		for (x = 0; x < 7; x++) {
			for (y = 0; y < 7; y++) {
				boolean field = board.getFeld(x, y);

				Rectangle framerec = getFieldFrame(x, y);
				Rectangle fieldrec = getFieldRectangle(x, y);
				g.setColor(Color.BLACK);
				g.drawRect(framerec.x, framerec.y, framerec.width,
						framerec.height);
				if (field) {
					boolean isSelected = false;
					boolean isBestMove = (board.bestMove != null 
							&& board.bestMove.getFrom().equals(x,y));
					if (selected != null && selected.equals(x,y) ) {
						isSelected = true;
					}
					g.setPaintMode();
					if (isSelected) {
						g.setColor(Color.RED);
					} else {
						boolean gefunden = isInAllMovesFrom(x, y);
						if (isBestMove) {
							g.setColor(Color.GREEN);
						} else if(gefunden) {
							g.setColor(Color.YELLOW);
						} else {
							g.setColor(Color.GRAY);
						}
					}
					g.fillOval(fieldrec.x, fieldrec.y, fieldrec.width,
							fieldrec.height);
				} else {
					g.clearRect(fieldrec.x, fieldrec.y, fieldrec.width,
							fieldrec.height);
				}

				// g.drawString(s, xoff + fieldwidth * x, yoff + fieldheight
				// * (y + 1));
			}
		}
	}

	private boolean isInAllMovesFrom(int x, int y) {
		Collection<Move> allMoves = board.getAllMoves();
		for (Move move : allMoves) {
			if (move.getFrom().equals(x,y)) return true;
		}
		return false;
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
						if (selected == null) {
							if (isInAllMovesFrom(x, y)) {
								selected = new Coordinate(x, y);
								this.repaint();
							}
						} else {
							if( ! board.getFeld(x, y)) {
								// todo: check move
								Move move = new Move(selected, new Coordinate(x,y));
								board.doMove(move);
								//board.setSolution();
								selected = null;
								this.repaint();
							} else if(isInAllMovesFrom(x, y)) {
								selected = new Coordinate(x, y);
								this.repaint();
							}
						}
					}
				}
			}
		}

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
