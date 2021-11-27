package org.partmaker.scriptparams;

import javafx.scene.Node;
import javafx.scene.control.TextField;
import net.synedra.validatorfx.Check.Context;

/** StringParameter represents strings of characters.
 * @author Robert Lichtenberger
 */
public class StringParameter extends ParameterBase<StringParameter, String> {
	
	private Integer maxLength = null;
	private TextField inputControl = new TextField();

	public StringParameter(String name) {
		super(name);
		emptyProperty.bind(inputControl.textProperty().isEmpty());
		valueProperty.bind(inputControl.textProperty());
	}
	
	/** Set maximum string length.
	 * @param maxLength The maximum number of characters the string parameter should have. Specify null to remove the limit.
	 * @return
	 */
	public StringParameter maxLength(Integer maxLength) {
		this.maxLength = maxLength;
		return this;		
	}
	
	public Integer getMaxLength() {
		return maxLength;
	}
	
	@Override
	public Node getInputControl() {
		return inputControl;
	}

	@Override
	public String getValue() {
		return inputControl.getText();
	}

	@Override
	protected void checkValidValue(Context c) {
		if (getMaxLength() != null && getValue().length() > getMaxLength()) {
			c.error(getName() + " too long. Must have at most " + getMaxLength() + " characters.");			
		}
	}
}
