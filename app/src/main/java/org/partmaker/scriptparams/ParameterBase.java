package org.partmaker.scriptparams;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import net.synedra.validatorfx.Check;
import net.synedra.validatorfx.Check.Context;
import net.synedra.validatorfx.Validator;

/** ParameterBase is the base class for all types of parameters that can be defined in PartMaker scripts.
 * @param <T> The type of the concrete class (required for fluent API) 
 * @param <V> The value type of the parameter
 * @author Robert Lichtenberger
 */
public abstract class ParameterBase<T extends ParameterBase<?, ?>, V> {
	
	private String name;
	private boolean required;
	
	// Must be bound by subclasses
	protected SimpleBooleanProperty emptyProperty = new SimpleBooleanProperty(true);
	protected SimpleObjectProperty<V> valueProperty = new SimpleObjectProperty<>(null);
	
	public ParameterBase(String name) {
		this.name = name;
	}
	
	public abstract Node getInputControl();
	public abstract V getValue();
	protected abstract void checkValidValue(Context c);
	
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
	
	public boolean isEmpty() {
		return emptyProperty.get();
	}
	
	public void setValidation(Validator validator) {
		Check check = validator.createCheck()
			.dependsOn("empty", emptyProperty)
			.dependsOn("value", valueProperty)
			.withMethod(this::checkValue)
			.decorates(getInputControl())
		;
		if (getAdditionalValidationProperty() != null) {
			check.dependsOn("additional", getAdditionalValidationProperty());
		}
		check.immediate();
	}	
	
	protected ObservableValue<? extends Object> getAdditionalValidationProperty() {
		return null;
	}
	
	protected void checkValue(Context c) {
		if (isRequired() && isEmpty()) {
			c.error(getName() + " is required.");			
		} else {
			checkValidValue(c);
		}
	}
}
