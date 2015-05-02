package de.kwirz.yapne.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * Einstiegspunkt der Anwendung.
 * <p>
 * Hier wird unsere UI aus einer FXML Datei gelesen, Controller initialisiert, Fenstergröße und
 * Fenstertitel gesetzt.
 */
public class App extends Application {

    /**
     * Haupteinstiegspunkt für eine JavaFX Anwendung.
     * <p>
     * Wird ausgeführt nach dem das System bereit ist unsere Anwendung zu starten.
     * @param primaryStage Haupt-Stage dieser Anwendung
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
    	final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_window.fxml"));
    	final Parent root = loader.load();
    	final AppController controller = loader.getController();
    	
    	controller.setPrimaryStage(primaryStage);

        primaryStage.setTitle("YAPNE - Konstantin Wirz/8124396");
        primaryStage.setScene(new Scene(root, 1100, 600));
        primaryStage.sizeToScene();
        primaryStage.show();

        primaryStage.getIcons().add(new Image("images/yapne.png"));

        // Damit die Anwendung auch wirklich beendet wird, nachdem das Hauptfenster geschlossen ist
        // brauchen wir noch zusätzlich System.exit Aufruf (zumindest auf Mac OS X 10.10)
        primaryStage.setOnCloseRequest(windowEvent -> {
            Platform.exit();
            System.exit(0);
        });
    }

    /** Startet die Anwendung */
    public static void main(String[] args) {
        launch(args);
    }
}
