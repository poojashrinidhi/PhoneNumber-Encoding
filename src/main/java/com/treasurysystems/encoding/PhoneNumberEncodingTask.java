package com.treasurysystems.encoding;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.treasurysystems.encoding.services.DictionaryFileService;
import com.tresasurysystems.encoding.util.ReadAndEncodePhoneNumber;

/**
 * Entry class of the application
 * 
 * @author PoojaShankar
 *
 */
public class PhoneNumberEncodingTask {

	private static final Logger logger = LogManager.getLogger(PhoneNumberEncodingTask.class);
	private static final String PROPERTIES_FILE_NAME = "application.properties";

	public static void main(String[] args) {

		Properties prop = new Properties();

		try (InputStream input = PhoneNumberEncodingTask.class.getClassLoader()
				.getResourceAsStream(PROPERTIES_FILE_NAME)) {
			// load a properties file
			prop.load(input);
			logger.debug("Successfully loaded the configuration properties from : " + PROPERTIES_FILE_NAME);
		} catch (FileNotFoundException ex) {
			logger.error(ex);
		} catch (IOException ex) {
			logger.error(ex);
		}

		try {
			logger.debug("Starting encoding of phone numbers");
			Map<String, String> dictionary = DictionaryFileService.getItems(prop);
			ReadAndEncodePhoneNumber.encodePhoneNumber(dictionary, prop);
			logger.debug("Successfully completed encoding of phone numbers");
		} catch (FileNotFoundException e) {
			logger.error(e); //Placeholders for future exception handling changes
		} catch (IOException ex) {
			logger.error(ex);
		} catch (Exception ex) {
			logger.error(ex);
		}
	}
}
