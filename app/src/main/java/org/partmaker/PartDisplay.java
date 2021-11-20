package org.partmaker;

import java.lang.reflect.Constructor;
import java.util.stream.Collectors;

import groovy.lang.GroovyClassLoader;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** PartDisplay displays a single PartDescriptor and allows to execute it's script.
 * @author Robert Lichtenberger
 *
 */
public class PartDisplay {
	
	private ObjectProperty<PartDescriptor> part = new SimpleObjectProperty<>(null);
	
	private VBox outer = Style.createVBox(this, "outer");
	private Label nameLabel = Style.createLabel(this, "name");
	private Label descriptionLabel = Style.createLabel(this, "description");
	private Label authorsLabel = Style.createLabel(this, "authors");
	private Label exceptionLabel = Style.createLabel(this, "exception");
	
	PartDisplay() {
		partProperty().addListener(this::loadPart);		
	}
	
	public ObjectProperty<PartDescriptor> partProperty() {
		return part;
	}
	
	private void loadPart(ObservableValue<? extends PartDescriptor> observable, PartDescriptor oldValue, PartDescriptor part) {
		if (part == null) {
			nameLabel.setText(null);
			descriptionLabel.setText(null);
			authorsLabel.setText(null);
			exceptionLabel.setText(null);
		} else {
			nameLabel.setText(part.getName());
			descriptionLabel.setText(part.getDescription());
			authorsLabel.setText(part.getAuthors().stream().collect(Collectors.joining(", ")));
			if (part.getException() != null) {				
				exceptionLabel.setText(part.getException().toString());
			} else {
				exceptionLabel.setText(null);
			}
		}
		try (GroovyClassLoader loader = new GroovyClassLoader()){
			Class<?> scriptClass = loader.parseClass(part.getScriptFile());
			Constructor<?> constructor = scriptClass.getConstructor(new Class[] {});
		    Object scriptInstance = constructor.newInstance() ;
		    // TODO use annotations...
			scriptClass.getDeclaredMethod( "defineParameters", new Class[] {} ).invoke( scriptInstance, new Object[] {} ) ;			
		} catch (Exception e) {
			exceptionLabel.setText(e.toString());
		}
	}
	
	public Region createPresentation() {
		outer.getChildren().addAll(nameLabel, descriptionLabel, authorsLabel, exceptionLabel);
		return outer;
	}
	
	
	

}
