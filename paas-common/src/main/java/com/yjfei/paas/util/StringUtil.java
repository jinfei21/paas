package com.yjfei.paas.util;

import com.google.common.base.Preconditions;

public class StringUtil {

	public static String getSafeString(String string) {
		return string.replace("-", "_");
	}

	public static String[] reverseSplit(String string, int numItems, String separator) {
		final String[] splits = string.split("\\" + separator);

		Preconditions.checkState(splits.length >= numItems, "There must be at least %s instances of %s (there were %s)",
				numItems - 1, separator, splits.length - 1);

		final String[] reverseSplit = new String[numItems];

		for (int i = 1; i < numItems; i++) {
			reverseSplit[numItems - i] = splits[splits.length - i];
		}

		final StringBuilder lastItemBldr = new StringBuilder();

		for (int s = 0; s < splits.length - numItems + 1; s++) {
			lastItemBldr.append(splits[s]);
			if (s < splits.length - numItems) {
				lastItemBldr.append(separator);
			}
		}

		reverseSplit[0] = lastItemBldr.toString();

		return reverseSplit;
	}
}
