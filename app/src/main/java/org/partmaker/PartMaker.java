package org.partmaker;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PartMaker extends Application {
	
	public static Stage mainStage;
	
	private LibraryChooser libraryChooser = new LibraryChooser();
	private LibraryList libraryList = new LibraryList();

	@Override
	public void start(Stage stage) {
		mainStage = stage;
		Scene scene = new Scene(createPresentation(), 1280, 1024);
		libraryList.directoryProperty().bind(libraryChooser.directoryProperty());
		stage.setScene(scene);
		stage.show();
	}

	private Region createPresentation() {
		SplitPane outer = new SplitPane();
		outer.setOrientation(Orientation.HORIZONTAL);
		outer.getItems().addAll(createLibrary(), createPreview());
		return outer;
	}

	private Node createLibrary() {
		VBox library = new VBox();
		library.getChildren().addAll(libraryChooser.createPresentation(), libraryList.createPresentation());
		return library;
	}
	
	private Node createPreview() {
		return new Label("TODODODOOOOOOOOOOOOOOOOOOOOOOOOOOOOO");
	}

	public static void main(String[] args) {
		launch();
	}

}
