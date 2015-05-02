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
 * <p>
 * Einstellungen werden in einer Datei im INI-Format gespeichert, als Key=Value Paare.
 *
 * <p>
 * Diese Klasse implementiert Singleton Entwurfsmuster, statische Methode
 * {@link #getInstance} gibt eine Referenz auf einzige Instanz Klasse zurück.
 */
public class Settings {

	/**
	 * Wird zum Loggen verwendet.
	 */
	private static final Logger logger = Logger.getLogger(Settings.class.getName());

	/**
	 * In dieser Datei werden Einstellungen gespeichert.
	 * <p>
	 * Auf unixoiden Systemen wäre das ~/.yapne.conf
	 */
	private static final String FILE_NAME = System.getProperty("user.home") + "/.yapne.conf";

	/**
	 * Dies ist Backend dieser Klasse
	 */
	private Properties properties = new Properties();

	/**
	 * Besitzt die einzige Instanz der Klasse
	 */
	private static class SettingsHolder {
		public static Settings settings = new Settings();
	}

	/**
	 * Gibt eine Referenz auf einzige Instanz dieser Klasse zurück
	 */
	public static Settings getInstance() {
		return SettingsHolder.settings;
	}


	/**
	 * Erstellt eine Instanz.
	 * <p>
	 * Wenn die Datei ~/.yapne.conf nicht existiert wird sie erstellt.
	 */
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

	/**
	 * Schreibt Einstellungen in die Datei.
	 * @throws IOException falls Eingabe/Ausgabe Fehler aufgetreten sind.
	 */
	public void save() throws IOException {
		try {
			properties.store(new FileOutputStream(FILE_NAME), "YAPNE Settings");
			logger.info("save settings to '" + FILE_NAME + "'");
		} catch (IOException e) {
			logger.log(Level.SEVERE, "couldn't write settings file: " + FILE_NAME, e);
			throw e;
		}
	}

	/**
	 * Gibt alle vorhandenen Schlüssel zurück
	 */
	public Set<String> getAllKeys() {
		return properties.stringPropertyNames();
	}

	/**
	 * Gibt <code>true</code> zurück, falls ein Wert für den Schlüssel vorhanden ist.
	 * @param key Schlüssel.
	 */
	@SuppressWarnings("unused")
	public boolean hasKey(String key) {
		return getAllKeys().contains(key);
	}

	/**
	 * Speichert den Wert für den gegebenen Schlüssel.
	 * @param key Schlüssel
	 * @param value Wert
	 * @param <T> Typ des Wertes
	 * @throws IOException falls Eingabe/Ausgabe Fehler auftreten.
	 */
	public <T> void setValue(String key, T value) throws IOException {
		String valueAsString;
		if (value instanceof Object)
			valueAsString = value.toString();
		else
			valueAsString = String.valueOf(value); // falls value ein primitiver Typ ist

		properties.setProperty(key, valueAsString);
		save();
		logger.info(String.format("set '%s' to '%s'", key, valueAsString));
	}

	/**
	 * Gibt den Wert für den gegebenen Schlüssel zurück.
	 * <p>
	 * <b>Wichtig</b> <br>
	 * Es ist besser vorher zu überprüfen ({@link #hasKey}) ob der Schlüssel existiert.
	 * @param key Schlüssel
	 * @throws IllegalArgumentException falls Schlüssel nicht existiert
	 */
	public String getValue(String key) throws IllegalArgumentException {
		String value = properties.getProperty(key);
		if (value == null)
			throw new IllegalArgumentException("no such key '" + key + "'");
		
		return value; 
	}

	/**
	 * Gibt den Wert für den gegebenen Schlüssel zurück.
	 * @param key Schlüssel
	 * @param defaultValue Falls Schlüssel nicht existiert, gibt diesen Wert zurück.
	 */
	public <T> String getValue(String key, T defaultValue) {
		return properties.getProperty(key, String.valueOf(defaultValue));
	}

	/**
	 * Repräsentation als String
	 */
	@Override
	public String toString() {
		return String.format("Settings { file: '%s', keys: %d }", FILE_NAME, getAllKeys().size());
	}
	
}
