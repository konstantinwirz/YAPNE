package de.kwirz.yapne.app;

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
import java.net.URL;
import java.util.ResourceBundle;

import de.kwirz.yapne.app.MessageBox.MessageType;


public class AppController implements Initializable {

    @FXML
    private Pane canvas;
    
    private Stage primaryStage;
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    	
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
        System.out.println("CLICKED");
    }
    
    @FXML
    public void openDocument() {
    	final FileChooser fileChooser = FileChooserBuilder.create()
    			.title("Open PNML File")
    			.extensionFilters(new FileChooser.ExtensionFilter("PNML files (*.pnml)", "*.pnml"))
    			.build();
    	
    	final File file = fileChooser.showOpenDialog(primaryStage);
    	System.out.println(file);
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
