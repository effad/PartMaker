package org.partmaker.scriptparams;

/** ParameterBase is the base class for all types of parameters that can be defined in PartMaker scripts.
 * @param <T> The type of the concrete class (required for fluent API) 
 * @author Robert Lichtenberger
 */
public abstract class ParameterBase<T extends ParameterBase<?>> {
	
	private String name;
	private boolean required;
	
	public ParameterBase(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@SuppressWarnings("unchecked")
	public T required() {
		required = true;
		return (T) this;
	}
	
	public boolean isRequired() {
		return required;
	}
}
