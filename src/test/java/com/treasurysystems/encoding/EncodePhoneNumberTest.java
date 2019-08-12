package com.treasurysystems.encoding;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class EncodePhoneNumberTest {

	private final String DICTIONARY_FILE_TEST_PATH = "dictionaries/SampleDictionary.txt";
	private final String INPUT_PHONE_NUMBER_FILE_TEST_PATH = "inputPhoneNumbers/SampleInput.txt";
	private final String INPUT_PHONE_NUMBER_RESULT_FILE_TEST_PATH = "inputPhoneNumbers/SampleOutput.txt";
	private Map<String, String> dictionary = new HashMap<String, String>();

	@Before
	public void setup() throws IOException {
		loadDictionary();
	}

	@Test
	public void testSampleInputFile() throws IOException {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		List<String> actualResult = new ArrayList<String>();
		try (InputStream is = classloader.getResourceAsStream(INPUT_PHONE_NUMBER_FILE_TEST_PATH);
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String phoneNumber;
			while ((phoneNumber = br.readLine()) != null) {
				actualResult.addAll(EncodePhoneNumber.encode(phoneNumber, dictionary));
			}
		}

		List<String> expectedResult = loadExpectedResult();
		assertTrue(expectedResult.equals(actualResult));
	}

	@Test
	public void testSingleDigitPhoneNumber() {
		List<String> encodedResult = EncodePhoneNumber.encode("1", dictionary);
		assertTrue(encodedResult.get(0).equals("1: 1"));
	}

	@Test
	public void testArbitraryLengthPhoneNumber() {
		List<String> encodedResult = EncodePhoneNumber.encode("562", dictionary);
		assertTrue(encodedResult.get(0).equals("562: Mix") && encodedResult.get(1).equals("562: mir"));
	}

	@Test
	public void testEmptyPhoneNumber() {
		List<String> encodedResult = EncodePhoneNumber.encode("", dictionary);
		assertTrue(encodedResult.isEmpty());
	}

	@Test
	public void testPhoneNumberWithOnlyDashes() {
		List<String> encodedResult = EncodePhoneNumber.encode("\\-\\", dictionary);
		assertTrue(encodedResult.isEmpty());
	}

	private void loadDictionary() throws IOException {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		try (InputStream is = classloader.getResourceAsStream(DICTIONARY_FILE_TEST_PATH);
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line;
			while ((line = br.readLine()) != null) {
				// Removes double quotes of umlauts and stores it as value in the map for later
				// substitution.
				dictionary.put(line.trim().replace("\"", ""), line);
			}
		}
	}

	private List<String> loadExpectedResult() throws IOException {
		ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		List<String> expectedResult = new ArrayList<String>();
		try (InputStream is = classloader.getResourceAsStream(INPUT_PHONE_NUMBER_RESULT_FILE_TEST_PATH);
				BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			String line;
			while ((line = br.readLine()) != null) {
				expectedResult.add(line);
			}
		}

		return expectedResult;
	}

}
