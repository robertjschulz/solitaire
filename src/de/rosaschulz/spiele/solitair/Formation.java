package de.rosaschulz.spiele.solitair;

import de.rosaschulz.spiele.solitair.model.FieldValue;
import de.rosaschulz.spiele.solitair.model.Coordinate;

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
public class Formation {

	public FieldValue[][] feld;

	/**
	 * 
	 * @param i
	 *            x-koordinate
	 * @param j
	 *            y-koordinate
	 * 
	 * @return 0=gehört nicht zum Spielfeld 1=gehört zum Spielfeld
	 */

	// public int FeldBei(int i,int j)
	// {
	// if ( (i>1 && i<5) || (j>1 && j<5) )
	// return 1;
	// else
	// return 0;
	// }
	public Formation() {
		feld = new FieldValue[7][7];
		setStart();
	}

	public void ForEachField(IFForEachField fef) {
		int x, y;
		for (x = 0; x < 7; x++) {
			for (y = 0; y < 7; y++) {
				if(Coordinate.checkRange(x, y))
				{
					Coordinate k = new Coordinate(x, y);
					fef.doeach(this, k);
				}
			}
		}
	}



	void set(Coordinate k, FieldValue s) {
		feld[k.x][k.y] = s;
	}

	class StartSetter implements IFForEachField {
		public void doeach(Formation f, Coordinate k) {
			f.set(k, (k.ok) ? ((k.x == 3 && k.y == 3) ? FieldValue.leer
					: FieldValue.stein) : FieldValue.none);
		}
	}

	// public void SetStart_old()
	// {
	// int i,j;
	// for(i=0;i<7;i++)
	// {
	// for(j=0;j<7;j++)
	// {
	// if ( FeldBei(i,j) == 1)
	// if(i==3 && j==3)
	// feld[i][j] = leer;
	// else
	// feld[i][j] = stein;
	// else
	// feld[i][j] = none;
	// }
	// }
	// }

	public void setStart() {
		int i, j;
		for (i = 0; i < 7; i++) {
			for (j = 0; j < 7; j++) {
				Coordinate k = new Coordinate(i, j);
				set(k, (k.ok ) ? ((i == 3 && j == 3) ? FieldValue.leer : FieldValue.stein)
						: FieldValue.none);
			}
		}
	}

//	@Override
//	protected Object clone() throws CloneNotSupportedException {
//		// TODO Auto-generated method stub
//		return super.clone();
//	}

	@Override
	protected Object clone() throws CloneNotSupportedException {
		Formation formation = new Formation();
		copyFeld(formation);
		return(Object) formation;
	}

	protected void copyFeld(Formation destinationFormation) {
//		destinationFormation.feld = new Field[7][7];
		int x, y;
		for (x = 0; x < 7; x++) {
			for (y = 0; y < 7; y++) {
				destinationFormation.feld[x][y]=feld[x][y];
			}
		}
	}
}
