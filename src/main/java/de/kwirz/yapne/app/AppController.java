package de.kwirz.yapne.app;

import de.kwirz.yapne.io.PnmlParser;
import de.kwirz.yapne.presentation.PetriNetElementPresentation;
import de.kwirz.yapne.presentation.PetriNetNodePresentation;
import de.kwirz.yapne.presentation.PetriNetPresentation;
import de.kwirz.yapne.utils.Settings;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


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
        ARC_CREATION
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

    /** Ist <code>true</code> falls gerade eine Verschiebung der Elemente stattfindet */
    boolean dragActive = false;

    /**
     * Wird ausgeführt nachdem root Element komplett abgearbeitet ist.
     * <p>
     *
     */
    @FXML
    private void  initialize() {
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
        canvas.setOnMouseClickedForEachElement(this::handleMouseEvent);
        canvas.setOnMouseDraggedForEachElement(this::handleMouseEvent);

        /*
        scrollPane.viewportBoundsProperty().addListener((observableValue, oldBounds, newBounds) -> {
            Bounds layoutBounds = scrollPane.getContent().getLayoutBounds();
            scrollPane.setFitToHeight(layoutBounds.getHeight() < newBounds.getHeight());
            scrollPane.setFitToWidth(layoutBounds.getWidth() < newBounds.getWidth());
        });
        */
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

    	final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open PNML File");
        fileChooser.setInitialDirectory(new File(initialDirectory));
        fileChooser.getExtensionFilters()
            .add(new FileChooser.ExtensionFilter("PNML files (*.pnml)", "*.pnml"));

    	final File file = fileChooser.showOpenDialog(primaryStage);

        if (file == null) // keine Datei ausgewählt
            return;

        // Pfad wird gespeichert
        try {
            settings.setValue("last_directory", file.getParent());
        } catch (IOException e) {
            MessageBox.error("couldn't store settings", primaryStage);
        }

        String source;
		try {
			source = new String(Files.readAllBytes(Paths.get(file.getPath())));
		} catch (IOException e) {
			MessageBox.error("couldn't read file " + file.getPath(), primaryStage);
			return;
		}

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
        File file;

        if (!currentFileName.getValue().isEmpty()) {
            file = new File(currentFileName.getValue());
        } else {
            saveAsDocument();
            return;
        }

        try {
            if (!file.toPath().getFileName().toString().endsWith(".pnml")) {
                file = new File(file.toPath().toString() + ".pnml");
            }

            if (!file.exists()) {
                boolean success = file.createNewFile();
                if (!success) {
                    logger.warning("Datei " + file.getName() + " existiert bereits");
                }
            }

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

        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save PNML file");
        fileChooser.setInitialDirectory(new File(initialDirectory));
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
                    dragActive = true;
                    if (canvas.getSelectedElements().size() == 1 && !event.isControlDown())
                        canvas.unselectAllElements();
                    canvas.selectElementById(((Node) source).getId());
                    canvas.moveSelectedNodes((PetriNetNodePresentation) source,
                            canvas.sceneToLocal(event.getSceneX(), event.getSceneY()));
                    isDirty.setValue(true);
                } else if ( eventType == MouseEvent.MOUSE_CLICKED && source instanceof PetriNetElementPresentation) {
                    if (!dragActive) {
                        String id = ((Node) source).getId();
                        if (event.isControlDown())
                            canvas.switchSelectedStateById(id);
                        else
                            canvas.selectExclusiveElementById(id);
                    }
                    dragActive = false;
                } else if ( eventType == MouseEvent.MOUSE_CLICKED && target instanceof  PetriNetPresentation ) {
                    if (!dragActive && !event.isControlDown())
                        canvas.unselectAllElements();
                    dragActive = false;
                }
                break;

            case PLACE_CREATION:
                if (    eventType == MouseEvent.MOUSE_CLICKED &&
                        source instanceof PetriNetPresentation &&
                        target instanceof PetriNetPresentation ) {
                    canvas.createPlace(canvas.sceneToLocal(event.getSceneX(), event.getSceneY()));
                    isDirty.setValue(true);
                }
                dragActive = false;
                break;

            case TRANSITION_CREATION:
                if (    eventType == MouseEvent.MOUSE_CLICKED &&
                        source instanceof PetriNetPresentation &&
                        target instanceof PetriNetPresentation ) {
                    canvas.createTransition(canvas.sceneToLocal(event.getSceneX(), event.getSceneY()));
                    isDirty.setValue(true);
                }
                dragActive = false;
                break;

            case ARC_CREATION:
                try {
                    if (eventType == MouseEvent.MOUSE_CLICKED) {
                        if (source instanceof PetriNetPresentation &&
                                target instanceof PetriNetPresentation) {
                            canvas.unselectAllElements();
                        } else {
                            List<PetriNetElementPresentation> selectedElements = canvas.getSelectedElements();
                            if (selectedElements.size() != 1) {
                                if (source instanceof PetriNetNodePresentation)
                                    canvas.selectExclusiveElementById(((Node) source).getId());

                            } else { // genau ein Element ist ausgewählt
                                if (source instanceof PetriNetNodePresentation &&
                                        !selectedElements.get(0).getClass().equals(source.getClass())) {
                                    canvas.createArc((PetriNetNodePresentation) selectedElements.get(0),
                                            (PetriNetNodePresentation) source);
                                    canvas.unselectAllElements();
                                    canvas.selectElementById(((Node) source).getId());
                                    isDirty.setValue(true);
                                }
                            }
                        }
                    }
                } catch (IllegalArgumentException e) {
                    showStatusMessage(e.getMessage());
                }
                dragActive = false;
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
            canvas.removeSelectedElements();
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
