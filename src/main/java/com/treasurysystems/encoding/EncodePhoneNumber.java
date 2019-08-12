package com.treasurysystems.encoding;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.tresasurysystems.encoding.util.NumberEncodingUtil;

/**
 * Encodes phone number
 * 
 * @author PoojaShankar
 *
 */
public class EncodePhoneNumber {

	/**
	 * Sends data for encoding and formats the result
	 * Pre-processes the phone number data by removing non-digits
	 * 
	 * @param phoneArray
	 * @param dictionary
	 * @return Formatted encoded list of strings with umlauts
	 */
	public static List<String> encode(String phoneNumber, Map<String, String> dictionary) {
		char[] phoneArray = phoneNumber.replaceAll("[^\\d.]", "").toCharArray();
		List<String> encodedResults = encode(phoneArray, dictionary, false);
		List<String> finalResult = new ArrayList<String>();
		for (String encodedString : encodedResults) {
			finalResult.add(NumberEncodingUtil.getProperFormat(encodedString, dictionary, phoneNumber));
		}
		return finalResult;
	}

	/**
	 * Iterates through each character in the phone number and performs the
	 * following steps : 
	 * Step 1: Get Possible combinations for a current digit from the static map. 
	 * Step 2: Filters out the words from dictionary based on the above possible combinations. 
	 * Step 3: If found keeps adding to the buffer else the completed words are retained and others are flushed out. 
	 * Step 4: If no patterns are found empty is returned.
	 * 
	 * At any stage only one digit is encoded contiguously when we dont find any
	 * patterns.
	 * 
	 * When any word completes in the filtered word a separate recursive call is
	 * executed with the remaining set of digits.
	 * 
	 * @param phoneArray
	 * @param dictionary
	 * @param isDigitEntered : This keeps the track if the digit k is encoded while traversing k+1
	 * @return encoded list of strings without umlauts
	 */
	private static List<String> encode(char[] phoneArray, Map<String, String> dictionary, boolean isDigitEntered) {
		int indexTraversed = 0;
		
		List<String> filteredEncoders = new ArrayList<String>();
		filteredEncoders.addAll(dictionary.keySet());

		List<String> previousBufferedEncoders = new ArrayList<String>();
		List<String> previousEncoders = new ArrayList<String>();
		Set<String> combinationGenerated = new HashSet<String>();

		if (phoneArray.length == 1) {
			return new ArrayList<String>(Arrays.asList(String.valueOf(phoneArray[0])));
		}

		int finalTraversedCounter = 0;

		for (char c : phoneArray) {
			finalTraversedCounter++;
			final int finalIndexTraversed = indexTraversed;

			char[] possibleCombination = NumberEncodingUtil.NumberEncodedMap.get(c);

			//constructs regex for possible combinations for the current digit.
			//eg : digit 2, regex : (?i)[r|w|x]
			String regex = "(?i)[";
			for (char ch : possibleCombination) {
				regex = regex + ch + "|";
			}
			regex = regex.substring(0, regex.length() - 1);
			regex = regex + "]";
			
			final String regexMatch = regex;

			//Checks if the regex match is found at the 'i'th position of filtered words.
			filteredEncoders = filteredEncoders.parallelStream()
					.filter(word -> word.length() - 1 >= finalIndexTraversed
							? String.valueOf(word.charAt(finalIndexTraversed)).matches(regexMatch)
							: false)
					.collect(Collectors.toList());

			if (filteredEncoders.isEmpty()) {

				if (finalIndexTraversed == 0) {
					previousEncoders.add(c + " ");
					filteredEncoders = new ArrayList<String>();
					filteredEncoders.addAll(dictionary.keySet());
					continue;
				}

				//Removes uncompleted filtered words
				previousBufferedEncoders.removeIf(e -> e.length() > finalIndexTraversed);

				//If previous digit itself is not encoded or is the first character then the digit itself is retained in the encoded string.
				if (previousBufferedEncoders.isEmpty() && previousEncoders.isEmpty() && combinationGenerated.isEmpty()
						&& !isDigitEntered) {
					List<String> prevDigit = new ArrayList<String>(
							Arrays.asList(phoneArray[finalIndexTraversed - 1] + " "));
					previousEncoders.addAll(NumberEncodingUtil.generateCombinations(prevDigit,
							encode(Arrays.copyOfRange(phoneArray, finalTraversedCounter - 1, phoneArray.length),
									dictionary, true)));
					List<String> finalResult = new ArrayList<String>();

					for (String previousEncoder : previousEncoders) {
						int withoutSpace = previousEncoder.replace(" ", "").length();
						if (withoutSpace == phoneArray.length) {
							finalResult.add(previousEncoder);
						}
					}
					return finalResult;
				} else if (previousEncoders.isEmpty()) {
					for (String value : previousBufferedEncoders) {
						previousEncoders.add(value + " ");
					}
				} else {
					previousEncoders = NumberEncodingUtil.generateCombinations(previousEncoders,
							previousBufferedEncoders);
				}

				// re-initialize stream
				// re-initialze index search
				filteredEncoders = new ArrayList<String>();
				filteredEncoders.addAll(dictionary.keySet());
				indexTraversed = 0;
				final int reversedIndex = 0;

				filteredEncoders = filteredEncoders.parallelStream()
						.filter(word -> word.length() - 1 >= reversedIndex
								? String.valueOf(word.charAt(reversedIndex)).matches(regexMatch)
								: false)
						.collect(Collectors.toList());
				indexTraversed++;

			} else {
				previousBufferedEncoders.clear();
				previousBufferedEncoders.addAll(filteredEncoders);
				indexTraversed++;

			}

			// check if any word is complete by checking word count and traversed index
			List<String> completedWords = previousBufferedEncoders.parallelStream()
					.filter(e -> e.length() == finalIndexTraversed + 1).collect(Collectors.toList());
			if (!completedWords.isEmpty()) {
				previousBufferedEncoders.removeAll(completedWords);
				completedWords = completedWords.parallelStream().map(e -> e + " ").collect(Collectors.toList());
				if (!previousEncoders.isEmpty()) {
					combinationGenerated.addAll(NumberEncodingUtil.generateCombinations(previousEncoders,
							NumberEncodingUtil.generateCombinations(completedWords,
									encode(Arrays.copyOfRange(phoneArray, finalTraversedCounter, phoneArray.length),
											dictionary, false))));
				} else {
					combinationGenerated.addAll(NumberEncodingUtil.generateCombinations(completedWords,
							encode(Arrays.copyOfRange(phoneArray, finalTraversedCounter, phoneArray.length), dictionary,
									false)));
				}
				filteredEncoders = previousBufferedEncoders;
			}
			if (previousBufferedEncoders.isEmpty()) {
				break;
			}

		}

		final int index = indexTraversed;
		if (!previousBufferedEncoders.isEmpty()) {
			previousBufferedEncoders.removeIf(e -> e.length() > index);
		}

		List<String> resultCode = new ArrayList<String>();

		resultCode = NumberEncodingUtil.generateCombinations(previousEncoders, previousBufferedEncoders);

		// Add if there are any recursive
		resultCode.addAll(combinationGenerated);

		List<String> finalResult = new ArrayList<String>();

		for (String result : resultCode) {
			int withoutSpace = result.replace(" ", "").length();
			if (withoutSpace == phoneArray.length) {
				finalResult.add(result);
			}
		}

		return finalResult;
	}

}
