package org.partmaker.scriptparams;

import java.util.ArrayList;
import java.util.List;

/** Parameters is an object passed to the groovy scripts to define parameters of a script.
 * @author Robert Lichtenberger
 */
public class Parameters {
	private List<ParameterBase<?, ?>> parameters = new ArrayList<>();
	
	public <T extends ParameterBase<?, ?>> T add(T parameter) {
		parameters.add(parameter);
		return parameter;
	}
	
	public List<ParameterBase<?, ?>> getParameters() {
		return parameters;
	}
}
