package org.rage.swarm.validation;

public class ConfigJsonValidator {

	public static void validateInputJson(String jsonPath) {
		if (jsonPath == null || jsonPath.length() == 0) {
			throw new IllegalArgumentException("Argument is empty or not valid");
		}
	}
}
