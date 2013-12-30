package de.rosaschulz.spiele.solitair.model;

import java.util.Random;

public class LineUp implements Cloneable{
	boolean lineUp[];
	LineUp () {
		lineUp=new boolean[GeneralField.FIELD_COUNT];
	}
	
	boolean get(int idx) {
		return lineUp[idx];
	}
	
	void set(int idx, boolean value) {
		lineUp[idx]=value;
	}
	
	boolean get(int x, int y) {
		return lineUp[GeneralField.getIdxFromXY(x,y)];
	}

	public void clear() {
		for (int i = 0; i < lineUp.length; i++) {
			lineUp[i]=false;
		}		
	}
	
	public void start() {
		for (int i = 0; i < lineUp.length; i++) {
			lineUp[i]=true;
		}		
		lineUp[GeneralField.getIdxFromXY(3, 3)]=false;
	}
	
	public void invert() {
		for (int i = 0; i < lineUp.length; i++) {
			lineUp[i]=!lineUp[i];
		}
	}

	public void set(int x, int y, boolean value) {
		lineUp[GeneralField.getIdxFromXY(x,y)] = value;
	}
	
	public LineUp getSymmetric(int [] symmetry) {
		LineUp lineUp1 = new LineUp();
		for (int idx = 0; idx < GeneralField.FIELD_COUNT; idx++) {
			lineUp1.lineUp[GeneralField.applySymmetry(symmetry, idx)]=lineUp[idx];
		}	
		return lineUp1;
	}

	public void random() {
		Random random = new Random();
		for (int i = 0; i < lineUp.length; i++) {
			lineUp[i]=random.nextBoolean();
		}
	}

	public int count() {
		int result = 0;
		for (int i = 0; i < lineUp.length; i++) {
			if(lineUp[i]) {
				result++;
			}
		}
		return result;
	}
	
	@Override
	protected Object clone() throws CloneNotSupportedException {
		Object result = super.clone();
		((LineUp)result).lineUp = lineUp.clone();
		return result;

	}

	/**
	 * @param board
	 * example:
	 *   XOX  
  OOO  
XOXOOXX
OOOXXXX
OOXOOXX
  XOX  
  XXX  
	 */
	public void parse(String board) {
		String[] lines = board.split("\\n");
		int lineNr = 0;
		for (String line : lines) {
			line = line.replaceAll("[^XO]", "");
			if((lineNr == 0 || lineNr == 1 || lineNr == 5 || lineNr == 6 )
					&& line.length() == 3) {
				set(2, lineNr, line.charAt(0)=='X');
				set(3, lineNr, line.charAt(1)=='X');
				set(4, lineNr, line.charAt(2)=='X');
				lineNr++;
			} else if((lineNr == 2 || lineNr == 3 || lineNr == 4 )
					&& line.length() == 7) {
				for(int col=0; col<7;col++) {
					set(col, lineNr, line.charAt(col)=='X');
				}
				lineNr++;
			} else {
				throw new RuntimeException("cannot parse line nr. "+lineNr+" '"+line+"' of board:" + board);
			}

		}
	}
}
