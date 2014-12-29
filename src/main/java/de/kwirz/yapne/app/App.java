package de.kwirz.yapne.app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


/**
 *
 *
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
    	final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_window.fxml"));
    	final Parent root = (Parent) loader.load();
    	final AppController controller = (AppController) loader.getController();
    	
    	controller.setPrimaryStage(primaryStage);

        primaryStage.setTitle("PetriNetEditor");
        primaryStage.setScene(new Scene(root, 1100, 600));
        primaryStage.show();

        primaryStage.getIcons().add(new Image("images/yapne.png"));

        // Damit die Anwendung auch wirklich beendet wird, nachdem das Hauptfenster geschlossen ist
        // brauchen wir noch zusätzlich System.exit Aufruf (zumindest auf Mac OS X 10.10)
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Platform.exit();
                System.exit(0);
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
