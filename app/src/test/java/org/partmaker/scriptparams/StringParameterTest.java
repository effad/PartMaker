package org.partmaker.scriptparams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javafx.scene.control.TextField;

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
		Assertions.assertEquals(s.getMaxLength(), null);
		s = s.maxLength(5);
		Assertions.assertEquals(s.getMaxLength(), 5);
		s = s.maxLength(null);
		Assertions.assertEquals(s.getMaxLength(), null);		
	}
	
	@Test
	public void testInputWidgetAndValue() {
		StringParameter s = new StringParameter("Dunedain");
		Assertions.assertTrue(s.getInputControl() instanceof TextField);
		TextField input = (TextField) s.getInputControl();
		input.setText("Aragorn");
		Assertions.assertEquals(s.getValue(), "Aragorn");
	}
}
