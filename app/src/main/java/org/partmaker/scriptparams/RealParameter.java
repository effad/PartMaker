package org.partmaker.scriptparams;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Check.Context;

/** RealParameter represents real numbers (in the mathematical sense). A double value is used internally.
 * @author Robert Lichtenberger
 *
 */
public class RealParameter extends ParameterBase<RealParameter, Double> {

	private Double min = null;
	private Double max = null;
	private TextField inputControl = new TextField();
	
	public RealParameter(String name) {
		super(name);
		emptyProperty.bind(inputControl.textProperty().isEmpty());
		valueProperty.bind(Bindings.createObjectBinding(this::getValue, inputControl.textProperty()));
	}
	
	public RealParameter max(Double max) {
		this.max = max;
		return this;
	}
	
	public Double getMax() {
		return max;
	}
	
	public RealParameter min(Double min) {
		this.min = min;
		return this;
	}
	
	public Double getMin() {
		return min;
	}	
	
	@Override
	public Node getInputControl() {
		return inputControl;
	}

	@Override
	public Double getValue() {
		Double value = 0.0;
		try {
			value = convertToDouble();
		} catch (NumberFormatException e) { }
		return value;
	}

	private Double convertToDouble() {
		Double value;
		value = Double.parseDouble(inputControl.getText());
		return value;
	}		
	
	@Override
	protected ObservableValue<? extends Object> getAdditionalValidationProperty() {
		return inputControl.textProperty();
	}
	
	@Override
	protected void checkValidValue(Context c) {
		try {
			convertToDouble();
			if (min != null && getValue() < min) {
				c.error(getName() + " too small. Must at least be " + getMin());
			}
			if (max != null && getValue() > max) {
				c.error(getName() + " too big. Must at most be " + getMin());
			}
		} catch (NumberFormatException e) {
			c.error(getName() + ": " + inputControl.getText() + " is not a real number.");
		}
	}
	
}
