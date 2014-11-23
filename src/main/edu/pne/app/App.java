package edu.pne.app;

import edu.pne.pnml.io.Parser;
import edu.pne.scene.*;

import edu.pne.scene.Arc;
import edu.pne.scene.ArcBuilder;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;

import java.nio.file.FileSystems;
import java.nio.file.Files;

public class App extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("main_window.fxml"));

        final String source = new String(Files.readAllBytes(FileSystems.getDefault().getPath("/Users/konstantin/Development/pnml/Beispiel1.pnml")));
        Parser parser = new Parser();
        final Net root = Net.createFromModel(parser.parse(source));
        root.getChildren().clear();



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


        primaryStage.setTitle("PetriNetEditor");
        primaryStage.setScene(new Scene(root, 500, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
