package org.partmaker.scriptparams;

/** IntegerParamater represents integer numbers (in the mathematical sense). A long value is used internally.
 * @author Robert Lichtenberger
 *
 */
public class IntegerParameter extends ParameterBase<IntegerParameter> {

	long min = Long.MIN_VALUE;
	long max = Long.MAX_VALUE;
	
	public IntegerParameter(String name) {
		super(name);
	}
	
	public IntegerParameter max(long max) {
		this.max = max;
		return this;
	}
	
	public long getMax() {
		return max;
	}
	
	public IntegerParameter min(long min) {
		this.min = min;
		return this;
	}
	
	public long getMin() {
		return min;
	}	
}
