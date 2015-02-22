package de.kwirz.yapne.app;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.kwirz.yapne.presentation.PetriNetNodePresentation;
import de.kwirz.yapne.utils.Settings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Einstellungsdialog.
 * <p>
 * In diesem Dialog lassen sich folgende Optionen einstellen:
 * <ul>
 * <li>Knotengröße</li>
 * <li>Linienstärke</li>
 * </ul>
 *
 * Einstellungen werden mit Hilfe der {@link Settings} Klasse geladen
 * und gespeichert.
 *
 * <p>
 * <b>Note:</b>
 * <br>
 * Folgende Schlüssel werden verwendet:
 * <ul>
 * <li>Knotengröße - node_size</li>
 * <li>Linienstärke - stroke_width</li>
 * </ul>
 */
public class SettingsDialog extends GridPane {

    private static final Logger logger = Logger.getLogger(SettingsDialog.class.getName());

	/** Slider zum Einstellen von Knotengröße */
	@FXML
	private Slider nodeSizeSlider;

	/** Slider zum Einstellen von Linienstärke */
	@FXML
	private Slider strokeWidthSlider;

	/** Anzeige für Knotengröße */
	@FXML
	private Text nodeSizeValue;

	/** Anzeige für Linienstärke */
	@FXML
	private Text strokeWidthValue;

	/**
	 * Erstellt einen Dialog.
	 */
	public SettingsDialog() {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings_dialog.fxml"));
		loader.setRoot(this);
    	loader.setController(this);

		setId("settings-dialog");
		getStylesheets().add(getClass().getResource("/css/yapne.css").toExternalForm());

		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException("couldn't load settings_dialog.fxml", e);
		}
	}

	/**
	 * Wird ausgeführt nachdem root Element komplett abgearbeitet ist.
	 */
	@FXML
    private void initialize() {
		// Lese Einstellungen und sertze die Werte
		Settings settings = Settings.getInstance();

        double nodeSize = PetriNetNodePresentation.DEFAULT_SIZE;
        double strokeWidth = PetriNetNodePresentation.DEFAULT_STROKE_WIDTH;

        try {
            nodeSize = Double.valueOf(settings.getValue("node_size",
                    PetriNetNodePresentation.DEFAULT_SIZE));
            strokeWidth = Double.valueOf(settings.getValue("stroke_width",
                    PetriNetNodePresentation.DEFAULT_STROKE_WIDTH)).doubleValue();
        } catch (NumberFormatException e) {
            logger.log(Level.WARNING, "couldn't parse floating point values from settings file", e);
        }

        registerListeners();

		nodeSizeSlider.setValue(nodeSize);
        onNodeSizeChanged(nodeSize);

		strokeWidthSlider.setValue(strokeWidth);
        onStrokeWidthChanged(strokeWidth);

    }

    /**
     * registriert Listener für Linienstärke und Knotengröße Slider
     */
    private void registerListeners() {
        // Verbinde Linienstärke-Slider mit seiner Anzeiger
        strokeWidthSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                onStrokeWidthChanged(newValue.doubleValue());
            }
        });
        // Verbinde Knotengröße-Slider mit seiner Anzeiger
        nodeSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                onNodeSizeChanged(newValue.doubleValue());
            }
        });
    }


    /**
	 * Wird bei Änderung der Knotengröße ausgeführt.
	 * @param size new Knotengröße
	 */
	private void onNodeSizeChanged(double size) {
		nodeSizeValue.setText(formatValue(size));
	}

	/**
	 * Wir bei Änderung der Linienstärke ausgeführt.
	 * @param width neue Linienstärke
	 */
	private void onStrokeWidthChanged(double width) {
		strokeWidthValue.setText(formatValue(width));
	}

	/**
	 * Formatiert den Gleitkommawert.
	 * <p>Zeigt nur eine Stelle nach dem Komma, wird für Anzeige verwendet.
	 */
	private String formatValue(double value) {
		return String.format("%.1f", value);
	}

	/**
	 * Wird beim betätigen von <b>Close</b> ausgeführt.
	 * <p>Speichert Einstellungen und schließt den Dialog.
	 */
	@FXML
	private void handleCloseButtonAction() {
		Stage stage = (Stage) nodeSizeSlider.getScene().getWindow();

		try {
			saveSettings();
		} catch (Exception e) {
			MessageBox.error(String.format("couldn't store settings: %s", e.getMessage()), stage);
		} finally {
			stage.close();
		}
	}

	/**
	 * Speichert Einstellungen.
	 * @throws java.io.IOException
	 */
	private void saveSettings() throws IOException {
		Settings settings = Settings.getInstance();
		settings.setValue("stroke_width", strokeWidthValue.getText().replace(",","."));
		settings.setValue("node_size", nodeSizeValue.getText().replace(",","."));
	}

}
