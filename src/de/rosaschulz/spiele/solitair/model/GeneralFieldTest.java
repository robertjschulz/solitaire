/**
 * 
 */
package de.rosaschulz.spiele.solitair.model;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * @author robert
 *
 */
public class GeneralFieldTest {

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
	}

	/**
	 * Test method for {@link de.rosaschulz.spiele.solitair.model.GeneralField#getIdxFromXY(int, int)}.
	 */
	@Test
	public void testGetIdx() {
		assertEquals(0, GeneralField.getIdxFromXY(2, 0));
		assertEquals(1, GeneralField.getIdxFromXY(3, 0));
		assertEquals(2, GeneralField.getIdxFromXY(4, 0));
		// fail("Not yet implemented");
	}

	@Test
	public void testSymmetries() {
		GeneralField field;
		assertEquals(2, GeneralField.symmetryFlip[GeneralField.getIdxFromXY(2, 0)]);
		
		field = GeneralField.getAt(GeneralField.symmetryRot90[GeneralField.getIdxFromXY(2, 0)]);
		assertEquals(6, field.x );
		assertEquals(2, field.y );

		field = GeneralField.getAt(GeneralField.symmetryRot180[GeneralField.getIdxFromXY(2, 0)]);
		assertEquals(4, field.x );
		assertEquals(6, field.y );

		field = GeneralField.getAt(GeneralField.symmetryRot270[GeneralField.getIdxFromXY(2, 0)]);
		assertEquals(0, field.x );
		assertEquals(4, field.y );

		field = GeneralField.getAt(GeneralField.symmetryFlip[GeneralField.getIdxFromXY(2, 0)]);
		assertEquals(4, field.x );
		assertEquals(0, field.y );

		assertEquals(32, GeneralField.applySymmetry(GeneralField.symmetryRot90     ,32));
		assertEquals(32, GeneralField.applySymmetry(GeneralField.symmetryRot180    ,32));
		assertEquals(32, GeneralField.applySymmetry(GeneralField.symmetryRot270    ,32));
		assertEquals(32, GeneralField.applySymmetry(GeneralField.symmetryFlip      ,32));
		assertEquals(32, GeneralField.applySymmetry(GeneralField.symmetryFlipRot90 ,32));
		assertEquals(32, GeneralField.applySymmetry(GeneralField.symmetryFlipRot180,32));
		assertEquals(32, GeneralField.applySymmetry(GeneralField.symmetryFlipRot270,32));


		// fail("Not yet implemented");
	}

}
