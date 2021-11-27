package org.partmaker;

import java.util.function.Supplier;

import org.kordamp.ikonli.javafx.FontIcon;

import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

/** Style is a helper factory to create nodes that contain css classes.
 * @author Robert Lichtenberger
 */
public class Style {
	private static final String PREFIX = "org.partmaker";
	
	public static Button createButton(Object parent, String id) {
		return prepare(Button::new, parent, id);
	}

	public static Button createButton(Object parent, String id, String text) {
		return prepare(() -> new Button(text), parent, id);
	}
	
	public static Button createButton(Object parent, String id, String text, String iconCode) {
		return prepare(() -> new Button(text, new FontIcon(iconCode)), parent, id);
	}
	
	public static Canvas createCanvas(Object parent, String id) {
		return prepare(Canvas::new, parent, id);
	}
	
	public static GridPane createGridPane(Object parent, String id) {
		return prepare(GridPane::new, parent, id);
	}

	public static Label createLabel(Object parent, String id) {
		return prepare(Label::new, parent, id);
	}
	
	public static Label createLabel(Object parent, String id, String text) {
		return prepare(() -> new Label(text), parent, id);
	}
	
	public static TabPane createTabPane(Object parent, String id) {
		return prepare(TabPane::new, parent, id);
	}
	
	public static ToolBar createToolbar(Object parent, String id) {
		return prepare(ToolBar::new, parent, id);
	}

	public static VBox createVBox(Object parent, String id) {
		return prepare(VBox::new, parent, id);
	}
	
	public static <T extends Node> T prepare(Supplier<T> supplier, Object parent, String id) {
		T node = supplier.get();		
		node.setId(id);
		Class<?> clz = parent.getClass();
		while (clz != null && clz.getPackageName().startsWith(PREFIX)) {
			node.getStyleClass().add(clz.getSimpleName());
			clz = clz.getSuperclass();			
		}		
		return node;
	}

}
