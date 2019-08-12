package com.tresasurysystems.encoding.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author PoojaShankar
 *
 */
public class NumberEncodingUtil {

	/**
	 * Static Mapping from letters to digits for encoding
	 */
	public static Map<Character, char[]> NumberEncodedMap = new HashMap<Character, char[]>() {
		{
			put('0', "e".toCharArray());
			put('1', "jnq".toCharArray());
			put('2', "rwx".toCharArray());
			put('3', "dsy".toCharArray());
			put('4', "ft".toCharArray());
			put('5', "am".toCharArray());
			put('6', "civ".toCharArray());
			put('7', "bku".toCharArray());
			put('8', "lop".toCharArray());
			put('9', "ghz".toCharArray());
		}
	};

	/**
	 * 
	 * @param encodedWord
	 * @param dictionary
	 * @param phoneNumber 
	 * @return String with umlauts if present in the original dictionary.
	 */
	public static String getProperFormat(String encodedWord, Map<String, String> dictionary, String phoneNumber) {
		String[] words = encodedWord.split(" ");
		StringBuffer updatedWord = new StringBuffer();
		updatedWord.append(phoneNumber + ": ");
		for (String word : words) {
			if (dictionary.containsKey(word)) {
				updatedWord.append(dictionary.get(word));
			} else {
				updatedWord.append(word);
			}
			updatedWord.append(" ");
		}
		return updatedWord.toString().trim();
	}

	/**
	 * Generates the combinations with the previously completed words with the new
	 * ones
	 * 
	 * @param previousEncoders
	 * @param previousBufferedEncoders
	 * @return Generated List of combined strings
	 */
	public static List<String> generateCombinations(List<String> previousEncoders,
			List<String> previousBufferedEncoders) {
		List<String> resultCode = new ArrayList<String>();
		for (String finalLeftCode : previousBufferedEncoders) {
			for (String previousEncode : previousEncoders) {
				resultCode.add(previousEncode + finalLeftCode);
			}
		}
		if (previousBufferedEncoders.isEmpty()) {
			resultCode.addAll(previousEncoders);
		}
		return resultCode;
	}
}
