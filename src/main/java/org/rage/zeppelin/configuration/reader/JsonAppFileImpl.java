package org.rage.zeppelin.configuration.reader;

import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.rage.zeppelin.validation.ConfigJsonValidator;

public class JsonAppFileImpl implements AppFile {

	private static final String ENCODING = "UTF-8";
	private final String fileName;

	/**
	 * @param fileName json file
	 */
	public JsonAppFileImpl(final String fileName) {
		this.fileName = fileName;
	}

	/* (non-Javadoc)
	 * @see org.rage.zeppelin.configuration.reader.AppFile#readFileFromParameter()
	 */
	@SuppressWarnings("unchecked")
	public Map<Object, Object> readFileFromParameter() {
		ConfigJsonValidator.validateInputJson(fileName);

		final JSONParser parser = new JSONParser();
		Object obj = null;
		
		try (Reader f =  new InputStreamReader(new FileInputStream(fileName), ENCODING)){
			obj = parser.parse(f);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Error when try to parse json property file: " + e.getMessage());
		}
		return (Map<Object, Object>) obj;
	}

}
