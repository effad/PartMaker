package org.partmaker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.InjectableValues;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/** PartDescriptor contains the properties (name, script-file, etc.) of a part.
 * @author Robert Lichtenberger
 */
public class PartDescriptor {

	private String name;
	private String description;
	private List<String> authors;
	@JsonDeserialize(using = ScriptFileDeserializer.class)
	private File scriptFile;
	private Exception exception;
	
	private PartDescriptor() { 		
	}
	
	/** Create invalid part descriptor
	 * @param exception The exception that occurred when reading. 
	 */
	private PartDescriptor(String name, IOException exception) {
		this.name = name;
		this.description = exception.getMessage();
		this.exception = exception;
		this.authors = new ArrayList<>();
		this.scriptFile = new File("");
	}

	/** Read part descriptor from .json file.
	 * @param file The .json file to read from.
	 * @return The part descriptor read from the .json file.
	 */
	public static PartDescriptor readFrom(File file) {
		ObjectMapper mapper = new ObjectMapper();
		mapper.setInjectableValues(new InjectableValues.Std().addValue("basePath", file.getParentFile()));
		PartDescriptor pd;
		try {
			pd = mapper.readValue(file, PartDescriptor.class);
		} catch (IOException e) {
			pd = new PartDescriptor(file.getParentFile().getName(), e);
		}
		return pd;
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public List<String> getAuthors() {
		return authors;
	}
	
	public File getScriptFile() {
		return scriptFile;
	}	
	
	public Exception getException() {
		return exception;
	}
	
	public boolean isValid() {
		return getException() == null && getScriptFile().isFile();
	}
}
