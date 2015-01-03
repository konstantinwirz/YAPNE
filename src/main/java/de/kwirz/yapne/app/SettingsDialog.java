package de.kwirz.yapne.app;

import java.io.IOException;
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
 * Einstellungsdialog
 * <p>
 * In diesem Dialog lassen sich folgende Optionen einstellen:
 * <ul>
 * <li>Knotengrösse</li>
 * <li>Liniendicke</li>
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
 * <li>Liniedicke - stroke_width</li>
 * </ul>
 */
public class SettingsDialog extends GridPane {

	@FXML
	private Slider nodeSizeSlider;

	@FXML
	private Slider strokeWidthSlider;

	@FXML
	private Text nodeSizeValue;

	@FXML
	private Text strokeWidthValue;
	

	public SettingsDialog() {
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/settings_dialog.fxml"));
		loader.setRoot(this);
    	loader.setController(this);

		setId("settings-dialog");
		getStylesheets().add(getClass().getResource("/css/yapne.css").toExternalForm());
    	try {
			loader.load();
		} catch (IOException e) {
			System.err.println("couldn't load FXML file");
			e.printStackTrace();
		}
	}

	@FXML
    private void initialize() {
		Settings settings = Settings.getInstance();
		onNodeSizeChanged(Double.valueOf(settings.getValue("node_size",
				PetriNetNodePresentation.DEFAULT_SIZE)));
		nodeSizeSlider.setValue(Double.valueOf(nodeSizeValue.getText()));
		onStrokeWidthChanged(Double.valueOf(settings.getValue("stroke_width",
				PetriNetNodePresentation.DEFAULT_STROKE_WIDTH)));
		strokeWidthSlider.setValue(Double.valueOf(strokeWidthValue.getText()));

		strokeWidthSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				onStrokeWidthChanged(newValue.doubleValue());
			}
		});

		nodeSizeSlider.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
				onNodeSizeChanged(newValue.doubleValue());
			}
		});

    }

	private void onNodeSizeChanged(double size) {
		nodeSizeValue.setText(formatValue(size));
	}

	private void onStrokeWidthChanged(double width) {
		strokeWidthValue.setText(formatValue(width));
	}

	private String formatValue(double value) {
		return String.format("%.1f", value);
	}
	
	@FXML
	public void handleCloseButtonAction() {
		Stage stage = (Stage) nodeSizeSlider.getScene().getWindow();
		
		try {
			Settings settings = Settings.getInstance();
			settings.setValue("stroke_width", strokeWidthSlider.getValue());
			settings.setValue("node_size", nodeSizeSlider.getValue());
		} catch (Exception e) {
			MessageBox.error(String.format("couldn't store settings: %s", e.getMessage()), stage);
		} finally {
			stage.close();
		}
	}

}
