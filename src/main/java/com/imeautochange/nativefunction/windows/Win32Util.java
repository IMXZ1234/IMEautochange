package com.imeautochange.nativefunction.windows;

public class Win32Util {
	public static String flagsToString(int valueToCheck, String[] flagNames, int[] flagValues) {
		int flagNum = flagNames.length;
		if (flagValues.length < flagNum) {
			flagNum = flagValues.length;
		}
		StringBuilder builder = new StringBuilder();
		boolean firstValue = true;
		for (int i = 0; i < flagNum; i++) {
			if ((flagValues[i] & valueToCheck) != 0) {
				if (!firstValue) {
					builder.append(" | ");
				}
				firstValue = false;
				builder.append(flagNames[i]);
			}
		}
		return builder.toString();
	}

	public static String flagsToString(int valueToCheck, BitFlag[] bitFlags) {
		StringBuilder builder = new StringBuilder();
		boolean firstValue = true;
		for (int i = 0; i < bitFlags.length; i++) {
			if ((bitFlags[i].flagValue & valueToCheck) != 0) {
				if (!firstValue) {
					builder.append(" | ");
				}
				firstValue = false;
				builder.append(bitFlags[i].flagName);
			}
		}
		return builder.toString();
	}

	public static class BitFlag {
		public String flagName;
		public int flagValue;

		public BitFlag(String flagName, int flagValue) {
			this.flagName = flagName;
			this.flagValue = flagValue;
		}
	}
}
