package de.kwirz.yapne.app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


public class IntegerField extends HBox implements Initializable {

	private IntegerProperty value = new SimpleIntegerProperty();
	
	@FXML
	private TextField textField;
	
	public IntegerField () {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/integer_field.fxml"));
		loader.setRoot(this);
		loader.setController(this);
		try {
			loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		value.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
					Number oldValue, Number newValue) {
				textField.setText(newValue.toString());
			}
		});
	}
		
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		textField.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
					String oldValue, String newValue) {
				textField.setText(newValue.matches("\\d*")?newValue:oldValue);
			}
		});	
		
		textField.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
					Boolean oldValue, Boolean newValue) {
				if (textField.getText().isEmpty())
					textField.setText("0");
				
			}
		});
	}
	
	public void setValue(int value) {
		this.value.set(value);
	}
	
	public int getValue() {
		return this.value.get();
	}
	
	public IntegerProperty valueProperty() {
		return this.value;
	}
}
