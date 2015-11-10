package org.rage.zeppelin.configuration.reader;

import java.io.FileReader;
import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.rage.zeppelin.validation.ConfigJsonValidator;

public class JsonAppFileImpl implements AppFile {

	private final String fileName;

	/**
	 * @param fileName json file
	 */
	public JsonAppFileImpl(final String fileName) {
		this.fileName = fileName;
	}

	@SuppressWarnings("unchecked")
	public Map<Object, Object> readFileFromParameter() {
		ConfigJsonValidator.validateInputJson(fileName);

		final JSONParser parser = new JSONParser();
		Object obj = null;
		try {
			obj = parser.parse(new FileReader(fileName));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error when try to parse json property file: " + e.getMessage());
		}
		return (Map<Object, Object>) obj;
	}

}
