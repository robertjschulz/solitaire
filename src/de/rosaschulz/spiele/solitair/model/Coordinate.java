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
 * 
 *  0 1 2 3 ...
 * 0
 * 1
 * 2
 * 3
 * ...
 */
public class Coordinate {
	public int x,y;
	@Deprecated public boolean ok;
	

	
	public Coordinate(int _x,int _y){
		x=_x;
		y=_y;
		checkRange();
	}
	
	public Coordinate(Coordinate _koordinate){
		x=_koordinate.x;
		y=_koordinate.y;
		ok=_koordinate.ok;
	}
	
	public void go(Richtung r, int step) {
		switch (r) {
		case up:
			y -= step;
			break;
		case right:
			x += step;
			break;
		case down:
			y += step;
			break;
		case left:
			x -= step;
			break;
		}
		checkRange();
	}
	
	public static Richtung getRichtung(Coordinate from, Coordinate to) {
		if(to.x-from.x ==  2 && to.y-from.y ==  0) {
			return Richtung.right;
		}
		if(to.x-from.x == -2 && to.y-from.y ==  0) {
			return Richtung.left;
		}
		if(to.x-from.x ==  0 && to.y-from.y ==  2) {
			return Richtung.down;
		}
		if(to.x-from.x ==  0 && to.y-from.y == -2) {
			return Richtung.up;
		}
		throw new RuntimeException("cannot evaluate Richtung in ("+from.x+","+from.y+") --> ("+to.x+","+to.y+")");
	}
	
	public static boolean checkRange(int x, int y) {
//		return (x>=2 || y >=2 ) && (x<=4||y<=4);
		return ((2<=x && x<5 && 0<=y && y<7) || (2<=y && y<5 && 0<=x && x<7));
	}
	
	public boolean checkRange(){
		//ok = ( (2<=x && x<5 && 0<=y && y<7) || (2<=y && y<5 && 0<=x && x<7) );
		boolean _ok = checkRange(x,y);
		ok = _ok;
		return _ok;
	}
	
	public int add(Richtung r){
		return 0;		
	}
	@Override
	public String toString() {
		return ("("+x+","+y+")");
	}
	public boolean equals(int x2, int y2) {
		return (x==x2 && y==y2);
	}
	
	public boolean equals(Coordinate k) {
		return (x==k.x && y==k.y);
	}

	
}
