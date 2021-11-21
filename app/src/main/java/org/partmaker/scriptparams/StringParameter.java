package org.partmaker.scriptparams;

/** StringParameter represents strings of characters.
 * @author Robert Lichtenberger
 */
public class StringParameter extends ParameterBase<StringParameter> {
	
	public final static int UNLIMITED = -1;
	
	private int maxLength = UNLIMITED;

	public StringParameter(String name) {
		super(name);
	}
	
	/** Set maximum string length.
	 * @param maxLength The maximum number of characters the string parameter should have. Specify UNLIMITED to remove the limit.
	 * @return
	 */
	public StringParameter maxLength(int maxLength) {
		this.maxLength = maxLength;
		return this;		
	}
	
	public int getMaxLength() {
		return maxLength;
	}
}
