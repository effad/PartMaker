package org.partmaker;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/** LogOutputBridge serves as bridge between Java Logging System and the LogOutput instance.
 * @author Robert Lichtenberger
 */
public class LogOutputBridge extends Handler {
	
	public LogOutputBridge() {
	}
	
	@Override
	public void publish(LogRecord record) {
		LogOutput.getInstance().publish(record);
	}

	@Override
	public void flush() {
	}

	@Override
	public void close() throws SecurityException {
	}
}
