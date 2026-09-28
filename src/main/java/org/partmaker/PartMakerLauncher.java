package org.partmaker;

/** Helper class to make launching from eclipse possible. 
 * @author Robert Lichtenberger
 */
public class PartMakerLauncher {
    public static void main(String[] args) {
    	javafx.application.Application.launch(PartMaker.class, args);
    }
}