package de.kwirz.yapne.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;



public class Settings {

	private static final String FILE_NAME = System.getProperty("user.home") + "/.yapne.conf";
	private Properties properties = new Properties();
	
	static { // erstellt Einstellungsdatei falls die noch nicht exisitiert
		final File file = new File(FILE_NAME);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				System.err.println("couldn't create file: " + FILE_NAME);
				e.printStackTrace();
			}
		}
	}
	
	public Settings() {
		try {
			properties.load(new FileInputStream(FILE_NAME));
		} catch (IOException e) {
			System.err.println("couldn't read settings from: " + FILE_NAME);
			e.printStackTrace();
		}
	}
	
	public void save() throws IOException {
		try {
			properties.store(new FileOutputStream(FILE_NAME), "YAPNE Settings");
		} catch (IOException e) {
			System.err.println("couldn't write settings file");
			throw e;
		}
	}
	
	public Set<String> getAllKeys() {
		return properties.stringPropertyNames();
	}
	
	public boolean hasKey(String key) {
		return getAllKeys().contains(key);
	}
	
	public void setValue(String key, String value) throws Exception {
		properties.setProperty(key, value);
		save();
	}
	
	public String getValue(String key) throws IllegalArgumentException {
		String value = properties.getProperty(key);
		if (value == null)
			throw new IllegalArgumentException("no such key '" + key + "'");
		
		return value; 
	}
	
	public String getValue(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
		
	public String toString() {
		return String.format("Settings { file: '%s', keys: %d }", FILE_NAME, getAllKeys().size());
	}
	
	public static void main(String[] args) throws Exception {
		Settings settings = new Settings();
		
		for (String key : settings.getAllKeys()) {
			System.out.println(key + " = " + settings.getValue(key));
		}
	}
}
