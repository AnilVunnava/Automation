package com.peoplenet.qa.selenium.factory;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

/**
 * @author Anil.Vunnava
 *
 *         Class for loading the Properties.
 */
public class PropertiesLoader {

	public static Properties prop = new Properties();
	public static InputStream input = null;
	private static final Logger log = Logger.getLogger("PropertiesLoader");

	/**
	 * @param file
	 * 
	 *            Used to load properties from ${file} set as environment
	 *            variable.
	 */
	public static void loadProperties(String file) {
		try {
			if (input == null)
				input = new FileInputStream(file);
			// load a properties file
			if (prop.isEmpty())
				prop.load(input);
		} catch (IOException ex) {
			log.info("Could not load Properties, FileNot Found : " + file);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					log.info("Could not close stream");
				}
			}
		}
	}

	/**
	 * @param key
	 * @return
	 * 
	 *         Used to get property from supplying key
	 */
	public static String getProperty(String key) {
		if (!prop.isEmpty())
			return prop.getProperty(key);
		else
			return null;
	}

}
