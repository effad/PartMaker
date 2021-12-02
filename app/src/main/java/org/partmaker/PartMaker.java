package org.partmaker;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.LogManager;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PartMaker extends Application {
	
	public static Stage mainStage;
	
	private LibraryChooser libraryChooser = new LibraryChooser();
	private LibraryList libraryList = new LibraryList();
	private PartDisplay partDisplay = new PartDisplay();
	
	public static final ExecutorService higlightingExecutor = Executors.newSingleThreadExecutor();
	
	static {
	      InputStream stream = PartMaker.class.getResourceAsStream("/org/partmaker/logging.properties");
	      try {
	          LogManager.getLogManager().readConfiguration(stream);
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	}

	@Override
	public void start(Stage stage) {
		mainStage = stage;
		Scene scene = new Scene(createPresentation(), 1280, 1024);
		libraryList.directoryProperty().bind(libraryChooser.directoryProperty());
		partDisplay.partProperty().bind(libraryList.partProperty());
		stage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("partmaker.css").toExternalForm());		
		stage.show();
	}
	
	@Override
	public void stop() throws Exception {
		super.stop();
		higlightingExecutor.shutdown();
	}


	private Region createPresentation() {
		SplitPane upper = new SplitPane();
		upper.setOrientation(Orientation.HORIZONTAL);
		upper.getItems().addAll(createLibrary(), createPreview());
		upper.setDividerPositions(0.3, 0.7);
		
		SplitPane outer = new SplitPane();
		outer.setOrientation(Orientation.VERTICAL);
		outer.getItems().addAll(upper, LogOutput.getInstance().getPresentation());
		outer.setDividerPositions(0.7, 0.3);
		return outer;
	}

	private Node createLibrary() {
		VBox library = new VBox();
		library.getChildren().addAll(libraryChooser.createPresentation(), libraryList.createPresentation());
		return library;
	}
	
	private Node createPreview() {
		return partDisplay.createPresentation();
	}

	public static void main(String[] args) {
		launch();
	}

}
