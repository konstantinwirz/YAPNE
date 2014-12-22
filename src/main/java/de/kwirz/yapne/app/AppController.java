package de.kwirz.yapne.app;

import de.kwirz.yapne.model.PetriNet;
import de.kwirz.yapne.presentation.PetriNetArcPresentation;
import de.kwirz.yapne.presentation.PetriNetElementPresentation;
import de.kwirz.yapne.presentation.PetriNetNodePresentation;
import de.kwirz.yapne.presentation.PetriNetPresentation;
import de.kwirz.yapne.utils.Settings;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooserBuilder;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.logging.Logger;

import de.kwirz.yapne.io.PnmlParser;


public class AppController implements Initializable {
	
	private static final Logger logger = Logger.getLogger(AppController.class.getName());
	
	private boolean isDirty = false;
	private String currentFileName = "";
    private AppMode mode = AppMode.EDITING;

    @FXML
    private PetriNetPresentation canvas;
    
    private Stage primaryStage;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        canvas.setOnMouseClickedForEachElement(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleMouseEvent(mouseEvent);
            }
        });

        canvas.setOnMouseDraggedForEachElement(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleMouseEvent(mouseEvent);
            }
        });

    	logger.info("initialized controller");
    }
    
    public void setPrimaryStage(Stage stage) {
    	primaryStage = stage;
    }

    @FXML
    void quit() {
        Platform.exit();
    }

    @FXML
    public void newDocument() {
    	isDirty = false;
    	currentFileName = "";
    	canvas.getChildren().clear();
    }
    
    @FXML
    public void openDocument() {
        Settings settings = Settings.getInstance();
        final String initialDirectory = settings.getValue("last_directory", System.getProperty("user.home"));

    	final FileChooser fileChooser = FileChooserBuilder.create()
    			.title("Open PNML File")
    			.extensionFilters(new FileChooser.ExtensionFilter("PNML files (*.pnml)", "*.pnml"))
                .initialDirectory(new File(initialDirectory))
    			.build();
    	
    	final File file = fileChooser.showOpenDialog(primaryStage);

        if (file == null) // no selection
            return;

        // Store directory
        try {
            settings.setValue("last_directory", file.getParent());
        } catch (IOException e) {
            MessageBox.error("couldn't store settings", primaryStage);
        }

        String source = null;
		try {
			source = new String(Files.readAllBytes(Paths.get(file.getPath())));
		} catch (IOException e) {
			MessageBox.error("couldn't read file " + file.getPath(), primaryStage);
			return;
		}
		
		assert source != null;
		
        PnmlParser parser = new PnmlParser();
        canvas.setModel(parser.parse(source));



    	currentFileName = file.getPath();
    	isDirty = false;
    }

    @FXML
    public void saveDocument() {

    }
    
    @FXML
    public void activateEditingMode() {
    	onModeChanged(AppMode.EDITING);
    }
    
    @FXML
    public void activatePlaceCreationMode() {
    	onModeChanged(AppMode.PLACE_CREATION);
    }
    
    @FXML
    public void activateTransitionCreationMode() {
        onModeChanged(AppMode.TRANSITION_CREATION);
    }
    
    @FXML
    public void activateArcCreationMode() {
    	onModeChanged(AppMode.ARC_CREATION);
    }

    private void onModeChanged(AppMode mode) {
        this.mode = mode;
    }

    @FXML
    private void handleMouseEvent(MouseEvent event) {
        switch (mode) {
            case EDITING:
                if (event.getEventType().equals(MouseEvent.MOUSE_DRAGGED)) {
                    Object node = event.getSource();
                    if (node instanceof PetriNetNodePresentation) {
                        canvas.moveNode((PetriNetNodePresentation) node,
                                new Point2D(event.getSceneX(), event.getSceneY()));
                    }
                } else if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED)) {
                    Object node = event.getSource();
                    if (node instanceof PetriNetElementPresentation)
                        canvas.selectElement((PetriNetElementPresentation) node);
                }
                break;
            case PLACE_CREATION:
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED))
                    canvas.createPlace(event.getSceneX(), event.getSceneY());
                break;
            case TRANSITION_CREATION:
                if (event.getEventType().equals(MouseEvent.MOUSE_CLICKED))
                    canvas.createTransition(event.getSceneX(), event.getSceneY());
            default:
        }
    }

    @FXML
    public void handleKeyEvent(KeyEvent event) {
        if (mode.equals(AppMode.EDITING) &&
                event.getEventType().equals(KeyEvent.KEY_PRESSED) &&
                (event.getCode().equals(KeyCode.BACK_SPACE)
                        || event.getCode().equals(KeyCode.DELETE))) {
            canvas.removeSelectedElement();
        }
    }

    @FXML
    public void about() {
    	MessageBox.about("YAPNE v1.0", primaryStage);
    }
    
    @FXML
    public void settings() {
    	Dialogs.show(new SettingsDialog(), primaryStage);
    }
}
