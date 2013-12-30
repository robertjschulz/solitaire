package de.rosaschulz.spiele.solitair.model;

import java.util.Collection;
import java.util.Vector;


/**
 * @author robert
 * each of a field should be once in the game/jvm!
 * 
 */
public class GeneralField {
	/**
	 * index to list of all Fields
	 */
	int idx;
	/**
	 * coordinates starting up-left corner
	 */
	int x,y;
	
	// static stuff
	public static final int FIELD_COUNT = 33;
	public static final int SYMMETRIES_COUNT = 7;
	
	private static GeneralField[] fieldList;

	/**
	 * List of all generalMoves, that start here
	 */
	Collection<Move> fromMoves;

	/**
	 * List of all generalMoves, that end here
	 */
	Collection<Move> toMoves;

	/**
	 * List of all generalMoves, that jump over here
	 */
	Collection<Move> midMoves;

	/**
	 * map x,y --> idx
	 */
	static int[][] mapXYToIdx;

	// static int[] symmetryNop;
	static int[] symmetryRot90;
	static int[] symmetryRot180;
	static int[] symmetryRot270;
	static int[] symmetryFlip;
	static int[] symmetryFlipRot90;
	static int[] symmetryFlipRot180;
	static int[] symmetryFlipRot270;


	static int[][] symmetries;

	private GeneralField(int _idx, int _x,int _y) {
		x = _x;
		y = _y;
		idx = _idx;
		fromMoves = new Vector<Move>();
		toMoves = new Vector<Move>();
		midMoves = new Vector<Move>();
	}
	
	private static void createField(int _idx, int _x,int _y) {
		GeneralField field = new GeneralField( _idx,  _x, _y);
		fieldList[_idx]=field;
		mapXYToIdx[_x][_y]=_idx;
	}
	
	static {
		mapXYToIdx = new int[7][7];
		
		// 36 NON-Fields in this array:
		mapXYToIdx[0][0]=-1;
		mapXYToIdx[0][1]=-1;
		mapXYToIdx[0][5]=-1;
		mapXYToIdx[0][6]=-1;
		mapXYToIdx[1][0]=-1;
		mapXYToIdx[1][1]=-1;
		mapXYToIdx[1][5]=-1;
		mapXYToIdx[1][6]=-1;
		mapXYToIdx[5][0]=-1;
		mapXYToIdx[5][1]=-1;
		mapXYToIdx[5][5]=-1;
		mapXYToIdx[5][6]=-1;
		mapXYToIdx[6][0]=-1;
		mapXYToIdx[6][1]=-1;
		mapXYToIdx[6][5]=-1;
		mapXYToIdx[6][6]=-1;
		
		fieldList = new GeneralField[FIELD_COUNT];
		
		// add all those coordinates weighted: first those to clean early...
		// the order is essential
		
		// 1.) all coordinates at the outer border first
		createField( 0, 2, 0);
		createField( 1, 3, 0); 
		createField( 2, 4, 0); 
		createField( 3, 2, 6); 
		createField( 4, 3, 6); 
		createField( 5, 4, 6); 
		createField( 6, 0, 2); 
		createField( 7, 0, 3); 
		createField( 8, 0, 4); 
		createField( 9, 6, 2); 
		createField(10, 6, 3); 
		createField(11, 6, 4); 

		// 2.) all coordinates at the second row from the outer border
		createField(12, 2, 1); 
		createField(13, 3, 1); 
		createField(14, 4, 1); 
		createField(15, 2, 5); 
		createField(16, 3, 5); 
		createField(17, 4, 5); 
		createField(18, 1, 2); 
		createField(19, 1, 3); 
		createField(20, 1, 4); 
		createField(21, 5, 2); 
		createField(22, 5, 3); 
		createField(23, 5, 4); 

		// 3.) inner circle ( the 4 duplicate corners commented out)
		createField(24, 2, 2); 
		createField(25, 3, 2); 
		createField(26, 4, 2); 
		createField(27, 2, 4); 
		createField(28, 3, 4); 
		createField(29, 4, 4); 
//		createField(xx, 2, 2); 
		createField(30, 2, 3); 
//		createField(xx, 2, 4); 
//		createField(xx, 4, 2); 
		createField(31, 4, 3); 
//		createField(xx, 4, 4); 

		// 4.) inner point
		createField(32, 3, 3); 

		// createSymmetries();
		int[] symmetryId = new int[FIELD_COUNT];
		for (int i = 0; i < symmetryId.length; i++) {
			symmetryId[i] = i;
		}
		symmetryRot90      = CreateSymmetrieRot90(symmetryId);
		symmetryRot180     = CreateSymmetrieRot90(symmetryRot90);
		symmetryRot270     = CreateSymmetrieRot90(symmetryRot180);
		symmetryFlip       = CreateSymmetryFlip  (symmetryId);
		symmetryFlipRot90  = CreateSymmetrieRot90(symmetryFlip);
		symmetryFlipRot180 = CreateSymmetrieRot90(symmetryFlipRot90);
		symmetryFlipRot270 = CreateSymmetrieRot90(symmetryFlipRot180);
		
		symmetries = new int[7][];
		symmetries[0]=symmetryRot90     ;
		symmetries[1]=symmetryRot180    ;
		symmetries[2]=symmetryRot270    ;
		symmetries[3]=symmetryFlip      ;
		symmetries[4]=symmetryFlipRot90 ;
		symmetries[5]=symmetryFlipRot180;
		symmetries[6]=symmetryFlipRot270;
	}

	public static int getIdxFromXY(int x2, int y2) {
		return mapXYToIdx[x2][y2];
	}
	public static GeneralField getAt(int idx) {
		return fieldList[idx];
	}
	public static GeneralField getAtXY(int x2, int y2) {
		return fieldList[mapXYToIdx[x2][y2]];
	}
	
	public static GeneralField getAtSymmetry(int [] symmetry, int idx) {
		return 	fieldList[symmetry[idx] ];
	}

	public static int applySymmetry(int [] symmetry, int idx) {
		return 	symmetry[idx];
	}



	/**
	 * @param symmetryRot902
	 * @param symmetryId
	 * 
	 * take a transformation and rotate it by 90 degrees...
	 * 
	 * ----> x
	 * |
	 * |
	 * y
	 */
	private static int[] CreateSymmetrieRot90( int[] source) {
		int[] target = new int[FIELD_COUNT];
		for (int i = 0; i < source.length; i++) {
			GeneralField field1 = fieldList[source[i]];
			int idx1 = field1.idx;
			int x1   = field1.x;
			int y1   = field1.y;
			int x2   = 6-y1;
			int y2   = x1;
			int idx2 = mapXYToIdx[x2][y2];
			target[i] = idx2;
		}
		return target;
	}
	
	/**
	 * @param target
	 * @param source
	 * 
	 * flips horizontally
	 */
	private static int[]  CreateSymmetryFlip( int[] source) {
		int[] target = new int[FIELD_COUNT];
		for (int i = 0; i < source.length; i++) {
			GeneralField field1 = fieldList[source[i]];
			int idx1 = field1.idx;
			int x1   = field1.x;
			int y1   = field1.y;
			int x2   = 6-x1;
			int y2   = y1;
			int idx2 = mapXYToIdx[x2][y2];
			target[i] = idx2;
		}
		return target;
	}
	

}
