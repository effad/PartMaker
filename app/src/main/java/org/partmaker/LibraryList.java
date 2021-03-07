package org.partmaker;

import java.io.File;

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

/** LibraryList displays the list of .zip - Files read from the directory set by its directory property.
 * @author Robert Lichtenberger
 */
public class LibraryList {
	
	private ObjectProperty<File> directory = new SimpleObjectProperty<>(null);
	private ListView<File> listView = new ListView<>();
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
	
	private class ListLoaderService extends Service<File[]> {
		private File parentDir;
		
		void load(File parentDir) {
			this.parentDir = parentDir;
			restart();
		}

		@Override
		protected Task<File[]> createTask() {
			return new Task<File[]>() {
				@Override
				protected File[] call() throws Exception {
					return parentDir.listFiles(File::isDirectory);
				}
			};
		}
	}
	
	private void libraryLoaded(WorkerStateEvent event) {
		File[] subDirectories = (File[]) event.getSource().getValue();
		if (subDirectories != null) {
			listView.getItems().setAll(subDirectories);
		} else {
			listView.getItems().clear();
		}		
	}
	
	private static class PartCell extends ListCell<File> {
		@Override
		protected void updateItem(File item, boolean empty) {
			super.updateItem(item, empty);
            if (item == null) {
            	setText(null);
            } else {
            	setText(item.getName());
            }
		}
	}
}
