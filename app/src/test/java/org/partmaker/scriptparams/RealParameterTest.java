package org.partmaker.scriptparams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** RealParameterTest tests RealParameter.
 * @author Robert Lichtenberger
 */
public class RealParameterTest {
	@Test
	public void testName() {
		RealParameter r = new RealParameter("Saruman");
		Assertions.assertEquals(r.getName(), "Saruman");
	}
	
	@Test
	public void testRequired() {
		RealParameter r = new RealParameter("Galadriel").required();
		Assertions.assertTrue(r.isRequired());
	}

	@Test
	public void testMin() {
		RealParameter i = new RealParameter("ElvenRings").min(3.0);
		Assertions.assertEquals(i.getMin(), 3.0);		
	}
	
	@Test
	public void testMax() {
		RealParameter i = new RealParameter("DwarvenRings").max(7.0);
		Assertions.assertEquals(i.getMax(), 7.0);		
	}	
}
