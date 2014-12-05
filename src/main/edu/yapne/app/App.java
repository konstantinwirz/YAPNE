package edu.yapne.app;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.nio.file.FileSystems;
import java.nio.file.Files;

import edu.yapne.io.PnmlParser;
import edu.yapne.scene.*;


public class App extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));

        final String source =
                new String(Files.readAllBytes(FileSystems.getDefault().getPath("/Users/konstantin/Development/pnml/Kaffee.pnml")));
        PnmlParser parser = new PnmlParser();
        final Net root = Net.createFromModel(parser.parse(source));
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
