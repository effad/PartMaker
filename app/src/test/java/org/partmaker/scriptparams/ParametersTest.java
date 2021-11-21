package org.partmaker.scriptparams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** ParametersTest tests Parameters.
 * @author Robert Lichtenberger
 */
public class ParametersTest {
	
	@Test
	public void testAddAndGet() {
		Parameters parameters = new Parameters();

		Assertions.assertEquals(parameters.getParameters().size(), 0);
		parameters.add(new StringParameter("Mellon"));		
		Assertions.assertEquals(parameters.getParameters().size(), 1);
		Assertions.assertEquals(parameters.getParameters().get(0).getClass(), StringParameter.class);
		Assertions.assertEquals(parameters.getParameters().get(0).getName(), "Mellon");		
	}
	
	@Test
	public void testFluentAPI() {
		Parameters parameters = new Parameters();
		parameters.add(new StringParameter("Mellon")).maxLength(6).required();		

		StringParameter s = (StringParameter) parameters.getParameters().get(0);
		Assertions.assertTrue(s.isRequired());
		Assertions.assertEquals(s.getMaxLength(), 6);				
	}
	
	@Test
	public void testMultipleParameter() {
		Parameters parameters = new Parameters();
		
		parameters.add(new IntegerParameter("Hobbits")).max(4).required();		
		parameters.add(new IntegerParameter("Men")).max(2);		
		parameters.add(new IntegerParameter("Elves")).max(1);		
		parameters.add(new IntegerParameter("Dwarves")).max(1);		
		parameters.add(new IntegerParameter("Wizards")).max(1);
		
		Assertions.assertEquals(parameters.getParameters().size(), 5);		

		Assertions.assertEquals(parameters.getParameters().get(0).getName(), "Hobbits");		
		Assertions.assertTrue(parameters.getParameters().get(0).isRequired());		
		
		Assertions.assertEquals(parameters.getParameters().get(1).getName(), "Men");		
		Assertions.assertFalse(parameters.getParameters().get(1).isRequired());		

		Assertions.assertEquals(parameters.getParameters().get(4).getName(), "Wizards");		
		Assertions.assertEquals(((IntegerParameter) parameters.getParameters().get(4)).getMax(), 1);		
	}
}
