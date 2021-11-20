package org.partmaker.scriptparams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** StringParameterTest tests StringParameter
 * @author Robert Lichtenberger
 */
public class StringParameterTest {

	@Test
	public void testName() {
		StringParameter s = new StringParameter("Gandalf");
		Assertions.assertEquals(s.getName(), "Gandalf");
	}
	
	@Test
	public void testRequired() {
		StringParameter s = new StringParameter("Gandalf").required();
		Assertions.assertTrue(s.isRequired());
	}
	
	@Test
	public void testMaxLength() {
		StringParameter s = new StringParameter("");
		Assertions.assertEquals(s.getMaxLength(), StringParameter.UNLIMITED);
		s = s.maxLength(5);
		Assertions.assertEquals(s.getMaxLength(), 5);
		s = s.maxLength(StringParameter.UNLIMITED);
		Assertions.assertEquals(s.getMaxLength(), StringParameter.UNLIMITED);		
	}
}
