package org.partmaker;

import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import javafx.scene.Node;
import javafx.scene.control.TextArea;

/** Logger provides a simple text output facility for log messages.
 * @author Robert Lichtenberger
 *
 */
public class LogOutput {
	
	private TextArea output = Style.createTextArea(this, "logger");
	private SimpleFormatter formatter = new SimpleFormatter();
	
	
	private static final LogOutput instance = new LogOutput();

	public static LogOutput getInstance() {
		return instance;	
	}
	
	private LogOutput() {
		output.setEditable(false);
	}
	
	public Node getPresentation() {
		return output;
	}

	public void publish(LogRecord record) {
		output.appendText(formatter.format(record));
	}
}
