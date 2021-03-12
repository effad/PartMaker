package org.partmaker;

import java.io.File;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/** PartDescriptorTest tests reading PartDescriptor objects from JSON files.
 * @author Robert Lichtenberger
 */
public class PartDescriptorTest {

	@Test
	public void testRead() {
		File test1 = new File(getClass().getResource("test1.json").getFile());
		PartDescriptor pd = PartDescriptor.readFrom(test1); 
		Assertions.assertNull(pd.getException());
		Assertions.assertEquals("TestPart#1", pd.getName());
		Assertions.assertEquals("This part is used for regression tests.", pd.getDescription());
		Assertions.assertEquals(List.of("Alice", "Bob"), pd.getAuthors());
		Assertions.assertEquals(new File(test1.getParentFile(), "test1.groovy"), pd.getScriptFile());  
	}
}
