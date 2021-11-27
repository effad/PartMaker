package org.partmaker.scriptparams;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javafx.scene.Node;
import net.synedra.validatorfx.Check.Context;

/** ParameterBaseTest tests ParameterBase by deriving a fictitious parameter class.
 * @author Robert Lichtenberger
 *
 */
public class ParameterBaseTest {

	static class HobbitParameter extends ParameterBase<HobbitParameter, String> {
		public HobbitParameter(String name) {
			super(name);
		}

		@Override
		public Node getInputControl() {
			return null;
		}

		@Override
		public String getValue() {
			return null;
		}

		@Override
		protected void checkValidValue(Context c) {
		}
	}
	
	@Test
	public void testName() {
		HobbitParameter h = new HobbitParameter("Bilbo");
		Assertions.assertEquals(h.getName(), "Bilbo");
	}
	
	@Test
	public void testRequired() {
		HobbitParameter h = new HobbitParameter("Samwise");
		Assertions.assertFalse(h.isRequired());
		h = h.required();		
		Assertions.assertTrue(h.isRequired());
	}
}
