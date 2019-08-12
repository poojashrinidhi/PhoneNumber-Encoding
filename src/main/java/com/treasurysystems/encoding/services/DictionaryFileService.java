package com.treasurysystems.encoding.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Fetches dictionary contents and loads it to memory
 * 
 * @author PoojaShankar
 *
 */

public class DictionaryFileService {

	private static final Logger logger = LogManager.getLogger(DictionaryFileService.class);

	public static Map<String, String> getItems(Properties prop) throws IOException {

		String dictionaryFilePath = prop.getProperty("dictionary.file.input.path");

		logger.debug("Fetching Dictionary Items from : " + dictionaryFilePath);

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		Map<String, String> dictionaryItems = new HashMap<String, String>();

		try (InputStream is = classloader.getResourceAsStream(dictionaryFilePath);
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

			String line;
			while ((line = br.readLine()) != null) {
				// Removes double quotes of umlauts and stores it as value in the map for later
				// substitution.
				dictionaryItems.put(line.trim().replace("\"", ""), line);
			}
		}

		logger.debug("Successfully loaded dictionaries to memory");

		return dictionaryItems;
	}
}
