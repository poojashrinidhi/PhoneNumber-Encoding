package com.tresasurysystems.encoding.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.treasurysystems.encoding.EncodePhoneNumber;

/**
 * Encodes phone number from the given input file
 * 
 * @author PoojaShankar
 *
 */
public class ReadAndEncodePhoneNumber {

	private static final Logger logger = LogManager.getLogger(ReadAndEncodePhoneNumber.class);

	/**
	 * Step 1 : Reads phone Numbers from the input file 
	 * Step 2 : Passes each entry for encoding process. 
	 * Step 3 : Logs the encoded result.
	 * 
	 * @param dictionary
	 * @throws IOException
	 */
	public static void encodePhoneNumber(Map<String, String> dictionary, Properties prop) throws IOException {

		String phoneNumberFilePath = prop.getProperty("phonenumbers.file.input.path");
		logger.debug("Fetching Input phone number Items from : " + phoneNumberFilePath);

		ClassLoader classloader = Thread.currentThread().getContextClassLoader();

		try (InputStream is = classloader.getResourceAsStream(phoneNumberFilePath);
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

			String line;
			while ((line = br.readLine()) != null) {
				String phoneNum = line.trim();
				List<String> result = EncodePhoneNumber.encode(phoneNum, dictionary);
				for (String encodedString : result) {
					System.out.println(encodedString);
					logger.info(encodedString);
				}
			}
		}
	}
}
