package org.partmaker;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

/** ScriptEditor allows to edit script files.
 * @author Robert Lichtenberger
 *
 */
public class ScriptEditor {

    private static final String[] KEYWORDS = new String[] {
	    "abstract", "assert", "boolean", "break", "byte",
	    "case", "catch", "char", "class", "const",
	    "continue", "default", "do", "double", "else",
	    "enum", "extends", "final", "finally", "float",
	    "for", "goto", "if", "implements", "import",
	    "instanceof", "int", "interface", "long", "native",
	    "new", "package", "private", "protected", "public",
	    "return", "short", "static", "strictfp", "super",
	    "switch", "synchronized", "this", "throw", "throws",
	    "transient", "try", "void", "volatile", "while"
	};
    
    private static final String[] PARTMAKER_TYPES = new String[] {
    	"Parameters", "RealParameter", "IntegerParameter", "StringParameter"
    };
    
    private static final String[] PARTMAKER_VARIABLES = new String[] {
    	"parameters", "graphics", "document"
    };
    
	
	private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
	private static final String PARTMAKER_TYPE_PATTERN = "\\b(" + String.join("|", PARTMAKER_TYPES) + ")\\b";
	private static final String PARTMAKER_VARIABLE_PATTERN = "\\b(" + String.join("|", PARTMAKER_VARIABLES) + ")\\b";
	private static final String PAREN_PATTERN = "\\(|\\)";
	private static final String BRACE_PATTERN = "\\{|\\}";
	private static final String BRACKET_PATTERN = "\\[|\\]";
	private static final String SEMICOLON_PATTERN = "\\;";
	private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";
	
	private static final Pattern PATTERN = Pattern.compile(
		"(?<KEYWORD>" + KEYWORD_PATTERN + ")"
        + "|(?<PARTMAKERTYPE>" + PARTMAKER_TYPE_PATTERN + ")"
        + "|(?<PARTMAKERVARIABLE>" + PARTMAKER_VARIABLE_PATTERN + ")"
        + "|(?<PAREN>" + PAREN_PATTERN + ")"
        + "|(?<BRACE>" + BRACE_PATTERN + ")"
        + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
        + "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")"
        + "|(?<STRING>" + STRING_PATTERN + ")"
        + "|(?<COMMENT>" + COMMENT_PATTERN + ")"
	);
	
	private VBox outer;
	private ToolBar toolbar;
	private CodeArea codeArea;
	
	private SimpleObjectProperty<File> fileProperty = new SimpleObjectProperty<>(null);
	private SimpleBooleanProperty dirty = new SimpleBooleanProperty(false);
		
	public ScriptEditor() {
		outer = Style.createVBox(this, "outer");
		
		toolbar = Style.createToolbar(this, "toolbar");
		Button saveButton = Style.createButton(this, "save", "Save", "fth-save");
		saveButton.textProperty().bind(Bindings.when(dirty).then("Save *").otherwise("Save"));
		saveButton.disableProperty().bind(fileProperty.isNull());
		saveButton.setOnAction(this::save);
		saveButton.sceneProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				newValue.getAccelerators().put(
					new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN),
					saveButton::fire
				);
			}
		});
		toolbar.getItems().add(saveButton);
		
		codeArea = new CodeArea();
        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));    
        codeArea.multiPlainChanges()
            .successionEnds(Duration.ofMillis(500))
            .retainLatestUntilLater(PartMaker.higlightingExecutor)
            .supplyTask(this::computeHighlightingAsync)
            .awaitLatest(codeArea.multiPlainChanges())
            .filterMap(t -> {
                if(t.isSuccess()) {
                    return Optional.of(t.get());
                } else {
                    t.getFailure().printStackTrace();
                    return Optional.empty();
                }
            })
            .subscribe(this::applyHighlighting);
        codeArea.plainTextChanges().subscribe(c -> dirty.set(true));
        VBox.setVgrow(codeArea, Priority.ALWAYS);
        
        outer.getChildren().addAll(toolbar, codeArea);
	}
	
	public void load(File file) {
		fileProperty.set(file);
		if (file == null) {
			codeArea.replaceText("");
		} else {
			try {
				codeArea.replaceText(FileUtils.readFileToString(file, StandardCharsets.UTF_8));
			} catch (IOException e) {
				codeArea.replaceText(e.getMessage());
			}
		}
		dirty.set(false);
	}
	
	public void save(ActionEvent actionEvent) {
		try {
			FileUtils.writeStringToFile(fileProperty.get(), codeArea.getText(), StandardCharsets.UTF_8);
			dirty.set(false);
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error saving file");
			alert.setHeaderText("'" + fileProperty.get().getAbsolutePath() + "' could not be saved");
			alert.setContentText(ExceptionUtils.getStackTrace(e));
		}
	}
	
	public Node getEditor() {
		return outer;
	}

    private Task<StyleSpans<Collection<String>>> computeHighlightingAsync() {
        String text = codeArea.getText();
        Task<StyleSpans<Collection<String>>> task = new Task<StyleSpans<Collection<String>>>() {
            @Override
            protected StyleSpans<Collection<String>> call() throws Exception {
                return computeHighlighting(text);
            }
        };
        PartMaker.higlightingExecutor.execute(task);
        return task;
    }

    private void applyHighlighting(StyleSpans<Collection<String>> highlighting) {
        codeArea.setStyleSpans(0, highlighting);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder
                = new StyleSpansBuilder<>();
        while(matcher.find()) {
            String styleClass =
                	matcher.group("KEYWORD") != null ? "keyword" :
                    matcher.group("PARTMAKERTYPE") != null ? "pmtype" :
                    matcher.group("PARTMAKERVARIABLE") != null ? "pmvariable" :
                    matcher.group("PAREN") != null ? "paren" :
                    matcher.group("BRACE") != null ? "brace" :
                    matcher.group("BRACKET") != null ? "bracket" :
                    matcher.group("SEMICOLON") != null ? "semicolon" :
                    matcher.group("STRING") != null ? "string" :
                    matcher.group("COMMENT") != null ? "comment" :
                    null; /* never happens */ assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastKwEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
        return spansBuilder.create();
    }

}
