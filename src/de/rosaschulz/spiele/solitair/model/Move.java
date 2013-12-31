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
public class Move implements Comparable<Move>{
	int idx;
	public Coordinate from;
	public Coordinate to;
	public Coordinate mid;
	int fromIdx, toIdx, midIdx;
	Richtung richtung;

	public Coordinate getFrom() {
		return from;
	}

	public void setFrom(Coordinate from) {
		this.from = from;
	}

	/**
	 * Konstruktor
	 * 
	 */
	public Move(Coordinate _from, Coordinate _to) {
		from = _from;
		to   = _to;
		mid  = getMid();
		richtung = Coordinate.getRichtung(from, to);
		fromIdx  = GeneralField.getIdxFromXY(from.x, from.y);
		toIdx    = GeneralField.getIdxFromXY(to.x  , to.y);
		midIdx   = GeneralField.getIdxFromXY(mid.x , mid.y);
	}

	
	/**
	 * @param _from start-felde
	 * @param r Richtung
	 */
	@Deprecated public Move(Coordinate _from, Richtung r) {
		from = _from;
		to   = ziel(getFrom(), r);
		mid  = getMid();
		richtung = r;
		fromIdx  = GeneralField.getIdxFromXY(from.x, from.y);
		toIdx    = GeneralField.getIdxFromXY(to.x  , to.y);
		midIdx   = GeneralField.getIdxFromXY(mid.x , mid.y);
	}

	public static Coordinate ziel(Coordinate _from, Richtung r) {
		Coordinate _to = new Coordinate(_from.x, _from.y);
		_to.go(r, 2);
		_to.checkRange();
		return _to;
	}



	public static Coordinate start(Coordinate _to, Richtung r) {
		Coordinate _from = new Coordinate(_to.x, _to.y);
		_from.go(r, -2);
		_from.checkRange();
		return _from;
	}


	@Override
	public String toString() {
		return ("from:"+this.getFrom()+" to:"+this.to);
	}

	public Coordinate getMid() {
		if(mid == null) {
			mid = new Coordinate((getFrom().x+to.x)/2,(getFrom().y+to.y)/2);
		}
		return mid;
	}

	

	@Override
	public int compareTo(Move o) {
		return Integer.compare(idx, o.idx);
	}
	
//	@Override
//	public int compareTo(Move o) {
//		return Integer.compare(hash(), o.hash());
//	}
//
//	private int hash() {
//		return getFrom().x+7*(getFrom().y+7*(to.x+7*to.y));
//	}

}
