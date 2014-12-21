package de.kwirz.yapne.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Persistente Einstellungen.
 *
 */
public class Settings {
	
	private static final Logger logger = Logger.getLogger(Settings.class.getName());

	private static final String FILE_NAME = System.getProperty("user.home") + "/.yapne.conf";

	private Properties properties = new Properties();


	private static class SettingsHolder {
		public static Settings settings = new Settings();
	}

	public static Settings getInstance() {
		return SettingsHolder.settings;
	}
	
	private Settings() {
		try {
			final File file = new File(FILE_NAME);

			if (!file.exists())
				file.createNewFile();

			properties.load(new FileInputStream(FILE_NAME));
		} catch (IOException e) {
			logger.log(Level.SEVERE, "couldn't read settings from: " + FILE_NAME, e);
		}
	}
	
	public void save() throws IOException {
		try {
			properties.store(new FileOutputStream(FILE_NAME), "YAPNE Settings");
			logger.info("save settings to '" + FILE_NAME + "'");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "couldn't write settings file: " + FILE_NAME, e);
			throw e;
		}
	}
	
	public Set<String> getAllKeys() {
		return properties.stringPropertyNames();
	}
	
	public boolean hasKey(String key) {
		return getAllKeys().contains(key);
	}
	
	public <T> void setValue(String key, T value) throws IOException {
		String valueAsString = "";
		if (value instanceof Object)
			valueAsString = value.toString();
		else
			valueAsString = String.valueOf(value);

		properties.setProperty(key, valueAsString);
		save();
		logger.info(String.format("set '%s' to '%s'", key, valueAsString));
	}
	
	public String getValue(String key) throws IllegalArgumentException {
		String value = properties.getProperty(key);
		if (value == null)
			throw new IllegalArgumentException("no such key '" + key + "'");
		
		return value; 
	}
	
	public <T> String getValue(String key, T defaultValue) {
		return properties.getProperty(key, String.valueOf(defaultValue));
	}
		
	public String toString() {
		return String.format("Settings { file: '%s', keys: %d }", FILE_NAME, getAllKeys().size());
	}
	
}
