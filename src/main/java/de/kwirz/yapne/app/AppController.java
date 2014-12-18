package de.kwirz.yapne.app;

import de.kwirz.yapne.utils.Settings;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooserBuilder;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import de.kwirz.yapne.app.MessageBox.MessageType;
import de.kwirz.yapne.io.PnmlParser;
import de.kwirz.yapne.scene.Net;


public class AppController implements Initializable {
	
	private static final Logger logger = Logger.getLogger(AppController.class.getName());
	
	private boolean isDirty = false;
	private String currentFileName = "";
    private Settings settings = new Settings();


    @FXML
    private Pane canvas;
    
    private Stage primaryStage;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	logger.info("initilaized controller");
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
        final Net root = Net.createFromModel(parser.parse(source));
        canvas.getChildren().add(root);
    	currentFileName = file.getPath();
    	isDirty = false;
    }
    
    @FXML
    public void saveDocument() {
    }
    
    @FXML
    public void activateManipulationMode() {
    	
    }
    
    @FXML
    public void activatePlaceCreationMode() {
    	
    }
    
    @FXML
    public void activateTransitionCreationMode() {
    	
    }
    
    @FXML
    public void activateArcCreationMode() {
    	
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
