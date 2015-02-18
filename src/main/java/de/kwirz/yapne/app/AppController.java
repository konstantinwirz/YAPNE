package de.kwirz.yapne.app;

import javafx.application.*;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.*;
import javafx.fxml.*;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.stage.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.logging.*;

import de.kwirz.yapne.presentation.*;
import de.kwirz.yapne.utils.Settings;
import de.kwirz.yapne.io.PnmlParser;


/**
 * Controller der <b>YAPNE</b> Anwendung.
 * <p>
 * von hier aus wird die Anwendung gesteuert.
 */
public class AppController {

    /** Wird zum Loggen verwendet */
	private static final Logger logger = Logger.getLogger(AppController.class.getName());

    /** Zeigt an, ob irgendwelche Änderungen im Petri Netz gibt */
    private SimpleBooleanProperty isDirty = new SimpleBooleanProperty(false);

    /**
     * Gesetzt falls Petri Netz aus einer Datei gelesen oder in eine Datei gespeichert wurde.
     */
    private SimpleStringProperty currentFileName = new SimpleStringProperty("");

    /**
     * Anwendungsmodi
     */
    public enum AppMode {
        /**
         * Hauptmodus. In diesem Modus ist folgendes möglich:
         * <ul>
         *     <li>Verschieben von Stellen und Transitionen</li>
         *     <li>Löschen von Stellen, Transitionen und Kanten</li>
         *     <li>Ändern von Namen von Stellen und Transitionen</li>
         *     <li>Ändern von Markierungen von Stellen</li>
         *     <li>Schalten von aktivierten Transitionen</li>
         * </ul>
         */
        SELECT,
        /**
         * In diesem Modus ist nur Erstellung von Stellen möglich
         */
        PLACE_CREATION,
        /**
         * In diesem Modus ist nur Erstellung von Transitionen möglich
         */
        TRANSITION_CREATION,
        /**
         * In diesem Modus ist nur Erstellung von Kanten möglich
         */
        ARC_CREATION;
    }

    /**
     * Aktueller Anwendungsmodus.
     * <p> <b>Standardwert:</b> <br>
     * {@link AppMode#SELECT}
     */
    private AppMode mode = AppMode.SELECT;

    /** Hier werden Statusnachrichten angezeigt */
    @FXML
    private Text statusBar;

    /** In diesem Element wird Petri Netz angezeigt */
    @FXML
    private PetriNetPresentation canvas;

    /** In diese ScrollPane wird {@link de.kwirz.yapne.presentation.PetriNetPresentation} eingebettet */
     @FXML
     private ScrollPane scrollPane;

    /** Menu Eintrag <b>Save</b> */
    @FXML
    private MenuItem saveMenuItem;

    /** Menu Eintrag <b>Save As</b> */
    @FXML
    private MenuItem saveAsMenuItem;

    /** Menu Eintrag <b>New</b> */
    @FXML
    private MenuItem newMenuItem;

    /** Hauptstage der Anwendung */
    private Stage primaryStage;

    /**
     * Wird ausgeführt nachdem root Element komplett abgearbeitet ist.
     * <p>
     *
     */
    @FXML
    private void initialize() {
        registerListeners();
        setupMenu();
        showStatusMessage("Ready");

    	logger.info("initialized controller");
    }

    /**
     * Konfiguriert Menü.
     * <ul>
     *   <li>Menüeintrag 'Save' ist aktiviert falls <code>isDirty=true</code></li>
     *   <li>Menüeintrag 'Save As' ist aktiviert falls <code>isDirty=true</code> und
     *   <code>currentFileName=""</code></li>
     *   <li>Menüeintrag 'New' ist aktiviert falls <code>isDirty=true</code> und
     *   <code>currentFileName=""</code></li>
     * </ul>
     *
     */
    private void setupMenu() {
        saveMenuItem.disableProperty().bind(isDirty.not());
        saveAsMenuItem.disableProperty().bind(isDirty.not().and(currentFileName.isEqualTo("")));
        newMenuItem.disableProperty().bind(isDirty.not().and(currentFileName.isEqualTo("")));

        isDirty.setValue(false);
    }

    /**
     * Registriert Listener.
     * <p>Folgende Listener werden registriert: <br>
     * <ul>
     *     <li>Führe {@link #handleMouseEvent} für jede Stelle, Transition und Kante aus für Events:</li>
     *     <ul>
     *         <li>MouseClicked</li>
     *         <li>MouseDragged</li>
     *     </ul>
     *     <li>Setze {@link javafx.scene.control.ScrollPane#fitToHeight} und
     *         {@link javafx.scene.control.ScrollPane#fitToWidth} Eigenschaften auf <code>true</code>
     *         wenn Inhalt der ScrollPane größer als sichtbares Bereich wird, sonst setze sie auf
     *         <code>false</code>. <br> Dies ist ein Workaround, damit Mouse Events korrekt ausgelöst
     *         werden.
     *         </li>
     * </ul>
     */
    private void registerListeners() {
        canvas.setOnMouseClickedForEachElement(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleMouseEvent(mouseEvent);
            }
        });

        canvas.setOnMouseDraggedForEachElement(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                handleMouseEvent(mouseEvent);
            }
        });

        scrollPane.viewportBoundsProperty().addListener(new ChangeListener<Bounds>() {
            @Override
            public void changed(ObservableValue<? extends Bounds> observableValue,
                                Bounds oldBounds,
                                Bounds newBounds) {
                Bounds layoutBounds = scrollPane.getContent().getLayoutBounds();
                scrollPane.setFitToHeight(layoutBounds.getHeight() < newBounds.getHeight());
                scrollPane.setFitToWidth(layoutBounds.getWidth() < newBounds.getWidth());
            }
        });
    }

    /**
     * Setzt die Hauptstage
     */
    public void setPrimaryStage(Stage stage) {
    	primaryStage = stage;
    }

    /**
     * Schließt die Anwendung.
     */
    @FXML
    private void quit() {
        Platform.exit();
        logger.log(Level.INFO, "finishing application...");
        System.exit(0);
    }

    /**
     * Säubert das Arbeitsbereich.
     */
    @FXML
    private void newDocument() {
    	currentFileName.setValue("");
        isDirty.setValue(false);
        canvas.clear();
    }

    /**
     * Öffnet <b>PNML</b> Dokument.
     */
    @FXML
    private void openDocument() {
        Settings settings = Settings.getInstance();
        final String initialDirectory = settings.getValue("last_directory",
                System.getProperty("user.home"));

    	final FileChooser fileChooser = FileChooserBuilder.create()
    			.title("Open PNML File")
    			.extensionFilters(new FileChooser.ExtensionFilter("PNML files (*.pnml)", "*.pnml"))
                .initialDirectory(new File(initialDirectory))
    			.build();
    	
    	final File file = fileChooser.showOpenDialog(primaryStage);

        if (file == null) // no selection
            return;

        // Store directory
        try {
            settings.setValue("last_directory", file.getParent());
        } catch (IOException e) {
            MessageBox.error("couldn't store settings", primaryStage);
        }

        String source = null;
		try {
			source = new String(Files.readAllBytes(Paths.get(file.getPath())));
		} catch (IOException e) {
			MessageBox.error("couldn't read file " + file.getPath(), primaryStage);
			return;
		}
		
		assert source != null;
		
        PnmlParser parser = new PnmlParser();
        try {
            canvas.setModel(parser.parse(source));
        } catch (Exception e) {
            MessageBox.error(e.getMessage(), primaryStage);
            return;
        }

    	currentFileName.setValue(file.getPath());
        isDirty.setValue(false);
    }

    /**
     * Speichert <b>PNML</b> Dokument
     */
    @FXML
    private void saveDocument() {
        File file = null;

        if (!currentFileName.getValue().isEmpty()) {
            file = new File(currentFileName.getValue());
        } else {
            saveAsDocument();
            return;
        }

        assert file != null;

        try {
            if (!file.toPath().getFileName().toString().endsWith(".pnml")) {
                file = new File(file.toPath().toString() + ".pnml");
            }

            if (!file.exists())
                file.createNewFile();

            FileWriter writer = new FileWriter(file);
            writer.write(canvas.getModel().toPNML());
            writer.close();

            isDirty.setValue(false);
            currentFileName.setValue(file.getPath());

        } catch (IOException e) {
            MessageBox.error(e.getMessage(), primaryStage);
        }
    }

    /**
     * Speichert <b>PNML</b> Dokument.
     * <p>Es öffnet sich ein Dateiauswahl-Dialog.
     */
    @FXML
    private void saveAsDocument() {
        final String initialDirectory =
                Settings.getInstance().getValue("initial_directory", System.getProperty("user.home"));

        final FileChooser fileChooser = FileChooserBuilder.create()
                .title("Save PNML file")
                .initialDirectory(new File(initialDirectory))
                .build();
        File file = fileChooser.showSaveDialog(primaryStage);

        if (file == null) {
            logger.log(Level.WARNING, "no file to save chosen");
        } else {
            currentFileName.set(file.getPath());
            saveDocument();
        }
    }

    /**
     * Wird aufgerufen wenn der Benutzer Anwendungsmodus ändert.
     */
    @FXML
    private void handleModeChange(ActionEvent event) {
        assert event.getEventType() == ActionEvent.ACTION;

        RadioMenuItem sourceButton = ((RadioMenuItem) event.getSource());

        // Workaround (JavaFX bug)
        // wird Mode mit einem Shortcut aktiviert, erneuert sich die Anzeige nicht (Menu)
        sourceButton.setSelected(true);

        if (!sourceButton.isSelected()) {
            mode = AppMode.SELECT;
        } else {
            switch(sourceButton.getId()) {
                case "select" : mode = AppMode.SELECT; break;
                case "place" : mode = AppMode.PLACE_CREATION; break;
                case "transition" : mode = AppMode.TRANSITION_CREATION; break;
                case "arc" : mode = AppMode.ARC_CREATION; break;
                default: throw new IllegalArgumentException("this place will be never reached");
            }
        }

        logger.log(Level.INFO, "new mode: " + mode);
        showStatusMessage("Mode: " + mode.toString().split("_")[0]);
    }

    /**
     * MouseEvent Handler.
     * <p>
     * Hier werden abhängig von {@link AppMode} Stellen, Transitionen und Kanten erstellt,
     * entfernt, verschoben und ausgewählt.
     */
    @FXML
    private void handleMouseEvent(MouseEvent event) {
        final Object source = event.getSource();
        final Object target = event.getTarget();
        final EventType<? extends Event> eventType = event.getEventType();

        switch (mode) {

            case SELECT:
                if ( eventType == MouseEvent.MOUSE_DRAGGED && source instanceof PetriNetNodePresentation ) {
                    canvas.selectElementById(((Node) source).getId());
                    canvas.moveNode((PetriNetNodePresentation) source,
                            canvas.sceneToLocal(new Point2D(event.getSceneX(), event.getSceneY())));
                    isDirty.setValue(true);
                } else if ( eventType == MouseEvent.MOUSE_CLICKED && source instanceof PetriNetElementPresentation) {
                    canvas.selectElementById(((Node) source).getId());
                } else if ( eventType == MouseEvent.MOUSE_CLICKED && target instanceof  PetriNetPresentation ) {
                    canvas.unselectElement();
                }
                break;

            case PLACE_CREATION:
                if (    eventType == MouseEvent.MOUSE_CLICKED &&
                        source instanceof PetriNetPresentation &&
                        target instanceof PetriNetPresentation ) {
                    canvas.createPlace(event.getSceneX(), event.getSceneY());
                    isDirty.setValue(true);
                }
                break;

            case TRANSITION_CREATION:
                if (    eventType == MouseEvent.MOUSE_CLICKED &&
                        source instanceof PetriNetPresentation &&
                        target instanceof PetriNetPresentation ) {
                    canvas.createTransition(event.getSceneX(), event.getSceneY());
                    isDirty.setValue(true);
                }
                break;

            case ARC_CREATION:
                try {
                    if (eventType == MouseEvent.MOUSE_CLICKED) {
                        if (canvas.getSelectedElement() == null) {
                            if (source instanceof PetriNetNodePresentation)
                                canvas.selectElementById(((Node) source).getId());

                        } else { // element is selected
                            if (source instanceof PetriNetPresentation &&
                                    target instanceof PetriNetPresentation) {
                                canvas.unselectElement();
                            } else if (source instanceof PetriNetNodePresentation &&
                                    !canvas.getSelectedElement().getClass().equals(source.getClass())) {
                                canvas.createArc((PetriNetNodePresentation) canvas.getSelectedElement(),
                                        (PetriNetNodePresentation) source);
                                canvas.selectElementById(((Node) source).getId());
                                isDirty.setValue(true);
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                    showStatusMessage(e.getMessage());
                }

                break;

            default:
                throw new RuntimeException("this place will be never reached");
        }
    }

    /**
     * KeyEvent Handler.
     * <p>
     * Falls aktueller Modus {@link AppMode#SELECT} ist und <b>Backspace</b> oder <b>Entfernen</b>
     * Taste betätigt wurde - aktuell ausgewähltes Element wird entfernt.
     */
    @FXML
    private void handleKeyEvent(KeyEvent event) {
        if (mode.equals(AppMode.SELECT) &&
                event.getEventType().equals(KeyEvent.KEY_PRESSED) &&
                (event.getCode().equals(KeyCode.BACK_SPACE)
                        || event.getCode().equals(KeyCode.DELETE))) {
            canvas.removeSelectedElement();
        }
    }

    /**
     * Zeigt Informationen über diese Anwendung.
     */
    @FXML
    private void about() {
    	MessageBox.about("YAPNE v1.0", primaryStage);
    }

    /**
     * Zeigt den Einstellungsdialog.
     * <p>
     * Nachdem Dialog geschlossen wird, erneuert sich die Anzeige.
     */
    @FXML
    private void settings() {
    	Dialogs.showAndWait(new SettingsDialog(), primaryStage);
        canvas.reload();
    }

    /**
     * Zeigt eine Nachricht im Statusbar.
     * @param message Nachricht
     */
    private void showStatusMessage(String message) {
        statusBar.setText(message);
    }

}
