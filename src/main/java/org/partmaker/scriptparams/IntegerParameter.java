package org.partmaker.scriptparams;

import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Check.Context;

/** IntegerParameter represents integer numbers (in the mathematical sense). A long value is used internally.
 * @author Robert Lichtenberger
 *
 */
public class IntegerParameter extends ParameterBase<IntegerParameter, Long> {

	private Long min = null;
	private Long max = null;
	private TextField inputControl = new TextField();
	
	public IntegerParameter(String name) {
		super(name);
		emptyProperty.bind(inputControl.textProperty().isEmpty());
		valueProperty.bind(Bindings.createObjectBinding(this::getValue, inputControl.textProperty()));
	}
	
	public IntegerParameter max(Integer max) {
		return max(max.longValue());
	}

	public IntegerParameter max(Long max) {
		this.max = max;
		return this;
	}
	
	public Long getMax() {
		return max;
	}
	
	public IntegerParameter min(Integer min) {
		return min(min.longValue());
	}	
	
	public IntegerParameter min(Long min) {
		this.min = min;
		return this;
	}
	
	public Long getMin() {
		return min;
	}

	public IntegerParameter defaultValue(Integer defaultValue) {
		return defaultValue(defaultValue.longValue());
	}
	
	@Override
	public Node getInputControl() {
		return inputControl;
	}

	@Override
	public Long getValue() {
		Long value = null;
		try {
			value = convertToLong();
		} catch (NumberFormatException e) { } // should not happen
		return value;
	}
	
	@Override
	public void loadValue(Long value) {
		inputControl.setText("" + value);
	}

	public void loadValue(Integer value) {
		inputControl.setText("" + value);
	}

	private Long convertToLong() {
		return Long.parseLong(inputControl.getText());
	}

	@Override
	protected ObservableValue<? extends Object> getAdditionalValidationProperty() {
		return inputControl.textProperty();
	}

	@Override
	protected void checkValidValue(Context c) {
		try {
			convertToLong();
			if (min != null && getValue() < min) {
				c.error(getName() + " too small. Must at least be " + getMin());
			}
			if (max != null && getValue() > max) {
				c.error(getName() + " too big. Must at most be " + getMin());
			}
		} catch (NumberFormatException e) {
			c.error(getName() + ": " + inputControl.getText() + " is not an integer number.");
		}
	}

}
