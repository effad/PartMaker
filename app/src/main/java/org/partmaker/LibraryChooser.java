package org.partmaker;

import java.io.File;
import java.util.prefs.Preferences;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.stage.DirectoryChooser;

/** LibraryChooser allows the user to choose a directory which will be scanned for script files.
 * @author Robert Lichtenberger
 */
public class LibraryChooser {
	
	private ReadOnlyObjectWrapper<File> directory = new ReadOnlyObjectWrapper<>();
	private StringBinding labelBinding = Bindings.createStringBinding(this::createLabel, directory); 
	

	LibraryChooser() {
		Preferences prefs = Preferences.userNodeForPackage(getClass());
		directory.set(new File(prefs.get("directory", "")));
	}

	
	public Region createPresentation() {
		HBox chooser = new HBox();
		Label label = new Label();		
		label.textProperty().bind(labelBinding);
		Button button = Style.createButton(this, "directory-buttton", "Open", "fth-book-open");
		button.setOnAction(this::buttonClicked);
		chooser.getChildren().addAll(label, button);
		chooser.setAlignment(Pos.BASELINE_RIGHT);
		chooser.setSpacing(10);
		HBox.setHgrow(label, Priority.ALWAYS);
		return chooser;
	}
	
	private String createLabel() {
		return getDirectory() == null ? "" : getDirectory().getAbsolutePath();
	}

	private void buttonClicked(ActionEvent event) {
		DirectoryChooser dlg = new DirectoryChooser();
		dlg.setTitle("Open Part Library");
		if (getDirectory().isDirectory()) {
			dlg.setInitialDirectory(getDirectory());
		}
		File dir = dlg.showDialog(PartMaker.mainStage);
		if (dir != null) {
			directory.set(dir);
			Preferences prefs = Preferences.userNodeForPackage(getClass());
			prefs.put("directory", dir.getAbsolutePath());
		}
	}
	
	public File getDirectory() {
		return directory.get();
	}
	
	public ReadOnlyObjectProperty<File> directoryProperty() {
		return directory.getReadOnlyProperty();
	}
}
