package org.partmaker.scriptparams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.partmaker.TestBase;
import org.testfx.framework.junit5.ApplicationExtension;

import javafx.scene.control.TextField;

/** IntegerParameterTest tests IntegerParameter.
 * @author Robert Lichtenberger
 */
@ExtendWith(ApplicationExtension.class)
public class IntegerParameterTest extends TestBase {
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
	
	@Test
	public void testInputWidgetAndValue() {
		IntegerParameter i = new IntegerParameter("Orcs");
		Assertions.assertTrue(i.getInputControl() instanceof TextField);
		TextField input = (TextField) i.getInputControl();
		input.setText("12345");
		Assertions.assertEquals(i.getValue(), 12345L);
	}
	
	@Test
	public void testLoadAndGet() {
		IntegerParameter i = new IntegerParameter("Orcs");
		i.loadValue(10000L);
		Assertions.assertEquals(i.getValue(), 10000);		
	}
	
}
