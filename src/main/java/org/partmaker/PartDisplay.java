package org.partmaker;

import java.io.File;
import java.io.FileWriter;
import java.lang.reflect.Constructor;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.partmaker.scriptparams.ParameterBase;
import org.partmaker.scriptparams.Parameters;

import com.jsevy.jdxf.DXFDocument;

import groovy.lang.Binding;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyShell;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import net.synedra.validatorfx.TooltipWrapper;
import net.synedra.validatorfx.Validator;

/** PartDisplay displays a single PartDescriptor and allows to execute it's script.
 * @author Robert Lichtenberger
 *
 */
public class PartDisplay {
	
	private final static Logger LOGGER = Logger.getLogger(PartDisplay.class.getName());
	
	private ObjectProperty<PartDescriptor> partProperty = new SimpleObjectProperty<>(null);
	
	private VBox outer = Style.createVBox(this, "outer");
	private Label nameLabel = Style.createLabel(this, "name");
	private Label descriptionLabel = Style.createLabel(this, "description");
	private Label authorsLabel = Style.createLabel(this, "authors");
	private TabPane tabPane = Style.createTabPane(this, "tab");
	private GridPane parameterGrid = Style.createGridPane(this, "parameters");

	private ScriptEditor scriptEditor = new ScriptEditor();	
	private FileChooser fileChooser = new FileChooser();
	
	PartDisplay() {
		partProperty().addListener((observable, oldValue, newValue) -> loadPart());
		scriptEditor.savedProperty().addListener((observable, oldValue, newValue) -> loadPart()); 
		fileChooser.getExtensionFilters().setAll(new ExtensionFilter("DXF Files", "*.dxf"));
	}
	
	public ObjectProperty<PartDescriptor> partProperty() {
		return partProperty;
	}
	
	public Region createPresentation() {
		Tab sourceTab = new Tab("Source", scriptEditor.getEditor());
		sourceTab.textProperty().bind(Bindings.when(scriptEditor.dirtyProperty()).then("Source *").otherwise("Source"));
		tabPane.getTabs().addAll(new Tab("Execute", parameterGrid), sourceTab);
		VBox.setVgrow(tabPane, Priority.ALWAYS);
		VBox.setVgrow(tabPane, Priority.SOMETIMES);
		outer.getChildren().addAll(nameLabel, descriptionLabel, authorsLabel, tabPane);
		return outer;
	}
	
	private void loadPart() {
		PartDescriptor part = partProperty.get();
		if (part == null) {
			nameLabel.setText(null);
			descriptionLabel.setText(null);
			authorsLabel.setText(null);
			scriptEditor.load(null);
		} else {
			nameLabel.setText(part.getName());
			descriptionLabel.setText(part.getDescription());
			authorsLabel.setText(part.getAuthors().stream().collect(Collectors.joining(", ")));
			if (part.getException() != null) {	
				LOGGER.log(Level.WARNING, "Invalid part definition", part.getException());
				scriptEditor.load(null);
				parameterGrid.getChildren().clear();
			} else {
				scriptEditor.load(part.getScriptFile());
				doLoadPart(part);
			}
		}
	}

	private void doLoadPart(PartDescriptor part) {
		parameterGrid.getChildren().clear();
		try (GroovyClassLoader loader = new GroovyClassLoader()){
			Class<?> scriptClass = loader.parseClass(part.getScriptFile());
			Constructor<?> constructor = scriptClass.getConstructor(new Class[] {});
		    Object scriptInstance = constructor.newInstance() ;
		    Parameters parameters = new Parameters();
			scriptClass.getDeclaredMethod("defineParameters", Parameters.class).invoke(scriptInstance, parameters);
			int row = 0;
			Validator validator = new Validator();
			for (ParameterBase<?, ?> parameter : parameters.getParameters()) {
				parameter.setValidation(validator);
				parameterGrid.add(Style.createLabel(this, "parameterLabel_" + row, parameter.getName()), 0, row);
				parameterGrid.add(parameter.getInputControl(), 1, row);
				parameter.loadDefaultValue();
				row++;
			}
			
			Button execute = Style.createButton(this, "execute", "Run", "fth-play");
			execute.setOnAction(e -> {
				executeScript(part, parameters);
			});
			TooltipWrapper<Button> executeWrapper = new TooltipWrapper<>(
				execute, 
				validator.containsErrorsProperty(), 
				Bindings.concat("Cannot run script:\n", validator.createStringBinding())
			);
			parameterGrid.add(executeWrapper, 0, row);
			
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Could not load part", part.getException());
		}
	}
	
	private void executeScript(PartDescriptor part, Parameters parameters) {
		try {
			File outFile = fileChooser.showSaveDialog(outer.getScene().getWindow());
			if (outFile != null) {
				fileChooser.setInitialDirectory(outFile.getParentFile());
				Binding binding = createBinding(part, parameters);
				DXFDocument dxfDocument = new DXFDocument(part.getName());
				binding.setProperty("document", dxfDocument);
				binding.setProperty("graphics", dxfDocument.getGraphics());
				GroovyShell groovyShell = new GroovyShell(binding); 
				groovyShell.evaluate(part.getScriptFile());
				try (FileWriter writer = new  FileWriter(outFile)) {
					writer.write(dxfDocument.toDXFString());
				}
			}
		} catch (Exception e) {
			LOGGER.log(Level.WARNING, "Error in script", e);
		}
	}

	private Binding createBinding(PartDescriptor part, Parameters parameters) {
		Binding binding = new Binding();
		binding.setProperty("log", Logger.getLogger(part.getName()));
		for (ParameterBase<?, ?> parameter : parameters.getParameters()) {
			binding.setProperty(parameter.getName(), parameter.getValue());
		}
		return binding;
	}
}
