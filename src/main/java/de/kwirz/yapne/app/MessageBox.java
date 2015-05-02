package de.kwirz.yapne.app;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Einfache Message Box.
 * <p>Da JavaFX in Version 2.2 keine Message Boxen anbietet, hier eigene Implementierung.
 * <p>Es werden folgende Nachrichtentypen unterstützt:
 * <ul>
 *     <li>Information</li>
 *     <li>Über</li>
 *     <li>Warnung</li>
 *     <li>Fehler</li>
 * </ul>
 * Im Dialog werden die Nachricht, Icon (abhängig vom Typ) und die <b>Schließen</b> Taste angezeigt.
 */
public class MessageBox extends GridPane {

	/** In diesem Element wird die Nachricht angezeigt */
	@FXML
	private Text messageText;

	/** In diesem Element wird das Icon angezeigt */
	@FXML
	private ImageView imageView;

	/** Nachrichtentyp */
	enum MessageType {
		/** Information */
		INFO,
		/** Über */
		ABOUT,
		/** Warnung */
		WARNING,
		/** Fehler */
		ERROR
	}

	/**
	 * Aktueller Nachrichtentyp.
	 * <p><b>Standardwert:</b> <br>
	 *     {@link de.kwirz.yapne.app.MessageBox.MessageType#INFO}
	 */
	private MessageType messageType = MessageType.INFO;

	/**
	 * Aktuelle Nachricht.
	 */
	private String message = "";

	/**
	 *  Abbildung vom Nachrichtentyp zum Icon-Pfad
	 */
	@SuppressWarnings("serial")
	private static final Map<MessageType, String> messageTypesMap =
			new HashMap<MessageBox.MessageType, String>() {{
				put(MessageType.INFO, "images/info.png");
				put(MessageType.ABOUT, "images/info.png");
				put(MessageType.ERROR, "images/error.png");
				put(MessageType.WARNING, "images/warning.png");
			}};

	/** Gibt den passenden Icon-Pfad zurück */
	private static String getImagePathByMessageType(MessageType mt) {
		assert messageTypesMap.containsKey(mt);
		return messageTypesMap.get(mt);
	}

	/**
	 * Zeigt eine Message Box
	 * @param messageType Nachrichtentyp
	 * @param message Nachricht
	 */
	public static void show(MessageType messageType, String message, Stage stage) {
		Dialogs.show(new MessageBox(messageType, message), stage);
	}

	/**
	 * Zeigt eine About Message Box
	 * @param message Nachricht
	 */
	public static void about(String message, Stage stage) {
		show(MessageType.ABOUT, message, stage);
	}

	/**
	 * Zeigt eine Info Message Box
	 * @param message Nachricht
	 */
	public static void info(String message, Stage stage) {
		show(MessageType.INFO, message, stage);
	}

	/**
	 * Zeigt eine Error Message Box
	 * @param message Nachricht
	 */
	public static void error(String message, Stage stage) {
		show(MessageType.ERROR, message, stage);
	}

	/**
	 * Zeigt eine Warning Message Box
	 * @param message Nachricht
	 */
	public static void warning(String message, Stage stage) {
		show(MessageType.WARNING, message, stage);
	}

	/**
	 * Erstellt eine Message Box.
	 * @param messageType Nachrichtentyp
	 * @param message Nachricht
	 * @throws java.lang.RuntimeException falls die FXML Datei nicht geladen werden könnte
	 */
	public MessageBox(MessageType messageType, String message) {
		this.messageType = messageType;
		this.message = message;
		
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/message_box.fxml"));
		loader.setRoot(this);
    	loader.setController(this);
		setId("message-box");

		try {
			loader.load();
		} catch (IOException e) {
			throw new RuntimeException("couldn't load message_box.fxml", e);
		}
	}

	/**
	 * Wird ausgeführt nachdem root Element komplett abgearbeitet ist.
	 */
	@FXML
	public void initialize() {
		imageView.setImage(new Image(getImagePathByMessageType(messageType)));
		messageText.setText(message);
	}

	/**
	 * Schließt den Dialog.
	 */
	@FXML
	private void onCloseButtonClicked() {
		Stage stage = (Stage) messageText.getScene().getWindow();
		stage.close();
	}
	
}
