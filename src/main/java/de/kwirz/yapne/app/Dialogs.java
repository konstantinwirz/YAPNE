package de.kwirz.yapne.app;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Hilfsklasse zum Erstellen von modalen Dialogen
 *
 */
public class Dialogs {

	public static void show(Parent root, Stage stage) {
		final Scene sourceScene = stage.getScene();
        final Window sourceWindow = sourceScene.getWindow();
		
		final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(sourceWindow);
        dialog.centerOnScreen();
                
        dialog.setScene(new Scene(root));
        dialog.show();
	}
	
}
