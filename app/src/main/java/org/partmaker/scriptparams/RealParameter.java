package org.partmaker.scriptparams;

/** RealParameter represents real numbers (in the mathematical sense). A double value is used internally.
 * @author Robert Lichtenberger
 *
 */
public class RealParameter extends ParameterBase<RealParameter> {

	private double min = Long.MIN_VALUE;
	private double max = Long.MAX_VALUE;
	
	public RealParameter(String name) {
		super(name);
	}
	
	public RealParameter max(double max) {
		this.max = max;
		return this;
	}
	
	public double getMax() {
		return max;
	}
	
	public RealParameter min(double min) {
		this.min = min;
		return this;
	}
	
	public double getMin() {
		return min;
	}	
}
