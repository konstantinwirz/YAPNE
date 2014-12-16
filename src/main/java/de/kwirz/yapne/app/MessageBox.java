package de.kwirz.yapne.app;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * 
 * Zeigt Meldungen
 *
 */
public class MessageBox extends GridPane implements Initializable {
	
	@FXML
	private Text messageText;
	
	@FXML
	private ImageView imageView;
	
	enum MessageType {
		INFO,
		ABOUT,
		WARNING,
		ERROR
	}
	
	private MessageType messageType = MessageType.INFO;
	private String message = "";
	
	
	@SuppressWarnings("serial")
	private static final Map<MessageType, String> messageTypesMap =
			new HashMap<MessageBox.MessageType, String>() {{
				put(MessageType.INFO, "images/info.png");
				put(MessageType.ABOUT, "images/info.png");
				put(MessageType.ERROR, "images/error.png");
				put(MessageType.WARNING, "images/warning.png");
			}};
			
	private static String getImagePathByMessageType(MessageType mt) {
		assert messageTypesMap.containsKey(mt);
		return messageTypesMap.get(mt);
	}
	
	public static void show(MessageType messageType, String message, Stage stage) {
		Dialogs.show(new MessageBox(messageType, message), stage);
	}
	
	public static void about(String message, Stage stage) {
		show(MessageType.ABOUT, message, stage);
	}
	
	public static void info(String message, Stage stage) {
		show(MessageType.INFO, message, stage);
	}
	
	public static void error(String message, Stage stage) {
		show(MessageType.ERROR, message, stage);
	}
	
	public static void warning(String message, Stage stage) {
		show(MessageType.WARNING, message, stage);
	}
	
	public MessageBox(MessageType messageType, String message) {
		this.messageType = messageType;
		this.message = message;
		
		final FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/message_box.fxml"));
		loader.setRoot(this);
    	loader.setController(this);
    	
    	try {
			loader.load();
		} catch (IOException e) {
			System.err.println("couldn't load fxml file");
			e.printStackTrace();
		}
	}
	
	@FXML
	public void handleCloseButtonAction() {
		System.out.println("ACTION");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		imageView.setImage(new Image(getImagePathByMessageType(messageType)));
		messageText.setText(message);
	}
	
	@FXML
	private void onCloseButtonClicked() {
		Stage stage = (Stage) messageText.getScene().getWindow();
		stage.close();
	}
	
}
