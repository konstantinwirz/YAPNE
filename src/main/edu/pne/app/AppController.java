package edu.pne.app;

import edu.pne.scene.Place;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.canvas.Canvas;

import java.net.URL;
import java.util.ResourceBundle;


public class AppController implements Initializable {

    @FXML
    private Canvas canvas;

    @FXML
    void onQuitClicked() {
        Platform.exit();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
