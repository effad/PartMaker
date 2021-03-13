package org.partmaker;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

/** LibraryList displays the list of parts read from the directory set by its directory property.
 * @author Robert Lichtenberger
 */
public class LibraryList {
	
	private ObjectProperty<File> directory = new SimpleObjectProperty<>(null);
	private ListView<PartDescriptor> listView = new ListView<>();
	private ListLoaderService listLoaderService = new ListLoaderService();
	
	LibraryList() {
		directoryProperty().addListener(this::loadLibrary);
		listLoaderService.setOnSucceeded(this::libraryLoaded);
	}
	
	public Region createPresentation() {
		VBox.setVgrow(listView, Priority.ALWAYS);
		listView.setCellFactory(lv -> new PartCell());
		return listView;
	}
	
	public ObjectProperty<File> directoryProperty() {
		return directory;
	}
	
	private void loadLibrary(ObservableValue<? extends File> observable, File oldValue, File newValue) {
		listView.getItems().clear();
		if (newValue != null) {
			listLoaderService.load(newValue);
		}
	}
	
	static class ListLoaderService extends Service<List<PartDescriptor>> {
		private File parentDir;
		
		void load(File parentDir) {
			this.parentDir = parentDir;
			restart();
		}

		@Override
		protected Task<List<PartDescriptor>> createTask() {
			return new Task<List<PartDescriptor>>() {
				@Override
				protected List<PartDescriptor> call() throws Exception {
					return Arrays.stream(parentDir.listFiles(File::isDirectory))
						.map(dir -> PartDescriptor.readFrom(new File(dir, "partmaker.json")))
						.collect(Collectors.toList())
					;
				}
			};
		}
	}
	
	private void libraryLoaded(WorkerStateEvent event) {
		@SuppressWarnings("unchecked")
		List<PartDescriptor> parts = (List<PartDescriptor>) event.getSource().getValue();
		if (parts != null) {
			listView.getItems().setAll(parts);
		} else {
			listView.getItems().clear();
		}		
	}
	
	private static class PartCell extends ListCell<PartDescriptor> {
		@Override
		protected void updateItem(PartDescriptor item, boolean empty) {
			super.updateItem(item, empty);
            if (item == null) {
            	setText(null);
            } else {
            	setText(item.getName());
            }
		}
	}
}
