package de.kwirz.yapne.app;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * Hilfsklasse zum Erstellen von Dialogen.
 * <p>
 * Da JavaFX in 2.2 keine Unterstützung für Dialoge anbietet, müssen wir improvisieren.
 */
public class Dialogs {

    /**
     * Zeigt den Dialog und kehrt sofort zurück
     * @see #createDialog
     */
	public static void show(Parent root, Stage stage) {
        createDialog(root, stage).show();
	}

    /**
     * Zeigt den Dialog und kehrt solange nicht zurück bis Dialog offen ist
     * @see #createDialog
     */
    public static void showAndWait(Parent root, Stage stage) {
        createDialog(root, stage).showAndWait();
    }

    /**
     * Erstellt ein Dialog.
     * @param root Dialog
     * @param stage zu diesem Stage ist ersteller Dialog modal.
     * @return Dialog
     */
    private static Stage createDialog(Parent root, Stage stage) {
        final Scene sourceScene = stage.getScene();
        final Window sourceWindow = sourceScene.getWindow();

        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(sourceWindow);
        dialog.centerOnScreen();
        dialog.setResizable(false);

        dialog.setScene(new Scene(root));
        return dialog;
    }
	
}
