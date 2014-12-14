package de.kwirz.yapne.app;

import de.kwirz.yapne.io.PnmlParser;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.FileSystems;
import java.nio.file.Files;


public class App extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {
    	final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_window.fxml"));
    	final Parent root = (Parent) loader.load();
    	final AppController controller = (AppController) loader.getController();
    	
    	controller.setPrimaryStage(primaryStage);
        

        final String source =
                new String(Files.readAllBytes(FileSystems.getDefault().getPath("/Users/konstantin/Development/pnml/Beispiel1.pnml")));
        PnmlParser parser = new PnmlParser();
        //final Net root = Net.createFromModel(parser.parse(source));
        
        //root.getChildren().clear();

        /*
        final Place place = PlaceBuilder.create()
                .label("place 1")
                .marking(1)
                .centerX(200)
                .centerY(100)
                .build();
        place.setCenterX(200);
        place.setCenterY(100);

        final Transition transition = TransitionBuilder.create()
                .label("trans 1")
                .centerX(50)
                .centerY(150)
                .build();

        Arc arrow = ArcBuilder.create()
                .source(transition)
                .target(place)
                .build();
        arrow.setStrokeWidth(2);
        arrow.setSmooth(true);

        root.getChildren().addAll(place, transition, arrow);
        */

        primaryStage.setTitle("PetriNetEditor");
        primaryStage.setScene(new Scene(root, 1100, 600));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
