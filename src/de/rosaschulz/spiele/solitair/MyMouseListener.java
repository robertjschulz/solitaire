package de.rosaschulz.spiele.solitair;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class MyMouseListener implements MouseListener {

	private SolitairApplet mySolitairApplet;

	public MyMouseListener(SolitairApplet mySolitairApplet) {
		super();
		this.mySolitairApplet = mySolitairApplet;
	}

	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		if (e.getButton() == MouseEvent.BUTTON1) {
			Point point = e.getPoint();
			if(point.y <36) {
				mySolitairApplet.doAnyMove();
			}
//			System.out.println("mouseClicked");
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
