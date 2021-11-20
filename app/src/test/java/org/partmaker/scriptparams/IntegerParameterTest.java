package org.partmaker.scriptparams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** IntegerParameterTest tests IntegerParameter.
 * @author Robert Lichtenberger
 */
public class IntegerParameterTest {
	@Test
	public void testName() {
		IntegerParameter i = new IntegerParameter("Radagast");
		Assertions.assertEquals(i.getName(), "Radagast");
	}
	
	@Test
	public void testRequired() {
		IntegerParameter i = new IntegerParameter("Radagast").required();
		Assertions.assertTrue(i.isRequired());
	}

	@Test
	public void testMin() {
		IntegerParameter i = new IntegerParameter("HobbitMeals").min(7);
		Assertions.assertEquals(i.getMin(), 7);		
	}
	
	@Test
	public void testMax() {
		IntegerParameter i = new IntegerParameter("FellowshipMembers").max(9);
		Assertions.assertEquals(i.getMax(), 9);		
	}	
}
