package org.partmaker.scriptparams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javafx.scene.control.TextField;

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
		RealParameter r = new RealParameter("ElvenRings").min(3.0);
		Assertions.assertEquals(r.getMin(), 3.0);		
	}
	
	@Test
	public void testMax() {
		RealParameter r = new RealParameter("DwarvenRings").max(7.0);
		Assertions.assertEquals(r.getMax(), 7.0);		
	}	

	@Test
	public void testInputWidgetAndValue() {
		RealParameter r = new RealParameter("Butter");
		Assertions.assertTrue(r.getInputControl() instanceof TextField);
		TextField input = (TextField) r.getInputControl();
		input.setText("12.345");
		Assertions.assertEquals(r.getValue(), 12.345);
	}
}
