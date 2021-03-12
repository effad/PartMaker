package org.partmaker;

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class ScriptFileDeserializer extends StdDeserializer<File> {
	
	private static final long serialVersionUID = 1L;

	public ScriptFileDeserializer() {
		super(File.class);
	}

	@Override
	public File deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String filename = p.getText();
		File base = (File) ctxt.findInjectableValue("basePath", null, null);
		return new File(base, filename);
	}

}
