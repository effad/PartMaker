package org.partmaker;

import java.util.function.Supplier;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/** Style is a helper factory to create nodes that contain css classes.
 * @author Robert Lichtenberger
 */
public class Style {
	private static final String PREFIX = "org.partmaker";
	
	public static Label createLabel(Object parent, String id) {
		return prepare(Label::new, parent, id);
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
