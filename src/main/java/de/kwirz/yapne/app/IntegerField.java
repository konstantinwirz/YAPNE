package de.kwirz.yapne.app;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;


public class IntegerField extends TextField {

	private IntegerProperty value = new SimpleIntegerProperty();

	public IntegerField () {

		value.addListener(new ChangeListener<Number>() {

			@Override
			public void changed(ObservableValue<? extends Number> observable,
								Number oldValue, Number newValue) {
				setText(newValue.toString());
			}
		});

		textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable,
								String oldValue, String newValue) {
				if (newValue.isEmpty())
					return;
				
				setText(newValue.matches("\\d*") ? newValue : oldValue);
				setValue(Integer.valueOf(getText()));
			}
		});

		focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable,
								Boolean oldValue, Boolean newValue) {
				if (getText().isEmpty())
					setText("0");

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
