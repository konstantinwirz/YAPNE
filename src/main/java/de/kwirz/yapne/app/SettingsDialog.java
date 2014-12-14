package de.kwirz.yapne.app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import de.kwirz.yapne.utils.Settings;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;


public class SettingsDialog extends GridPane implements Initializable {
	
	private Settings settings = new Settings();
	
	private static int DEFAULT_STROKE_WIDTH = 1;
	private static int DEFAULT_NODE_SIZE = 14;
	
	@FXML
	private IntegerField nodeSizeInputField;
	
	@FXML
	private IntegerField strokeWidthInputField;
	
	
	public SettingsDialog() {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings_dialog.fxml"));
		loader.setRoot(this);
    	loader.setController(this);
    	
    	try {
			loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
		/*
		
		nodeSizeInputField.setValue(Integer.valueOf(settings.getValue("node_size",
				String.valueOf(DEFAULT_NODE_SIZE))));
		strokeWidthInputField.setValue(Integer.valueOf(settings.getValue("stroke_width",
				String.valueOf(DEFAULT_STROKE_WIDTH))));
		*/
		
    }
	
	@FXML
	public void handleCloseButtonAction() {
		try {
			settings.setValue("stroke_width", String.valueOf(strokeWidthInputField.getValue()));
			settings.setValue("node_size", String.valueOf(nodeSizeInputField.getValue()));
		} catch (Exception e) {
			
			;
		} finally {
			Stage stage = (Stage) nodeSizeInputField.getScene().getWindow();
			stage.close();
		}
	}

}
