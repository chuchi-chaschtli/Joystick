/**
 * JsonConfiguration.java is a part of Joystick
 *
 * Copyright (c) 2016 Anand Kumar
 *
 * Joystick is a free software: You can redistribute it or modify it
 * under the terms of the GNU General Public License published by the Free
 * Software Foundation, either version 3 of the license of any later version.
 * 
 * Joystick is distributed in the intent of being useful. However, there
 * is NO WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You can view a copy of the GNU General Public License at 
 * <http://www.gnu.org/licenses/> if you have not received a copy.
 */
package com.valygard.aohruthless.utils.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.valygard.aohruthless.messenger.JSLogger;

/**
 * Utility for Json file creation. Allows for simple reading/writing of Json
 * files in lieu of Yaml configuration, which is severely underwhelming from a
 * performance standpoint in comparison.
 * 
 * @author Anand
 * 
 */
public class JsonConfiguration {

	// file path
	private final File file;
	private final String fileName;

	// json
	private final JSONParser parser;
	private JSONObject obj;

	// formatting
	private final Gson gson;

	// file reader
	private FileReader reader;

	/**
	 * Constructor initializes a new JSON file by a given directory and file
	 * name. If the directory does not exist, a new one is created. If the file
	 * name does not end with the .json marker, it is properly appended.
	 * 
	 * @param dir
	 *            the Directory path
	 * @param fileName
	 *            the Json file name
	 */
	public JsonConfiguration(File dir, String fileName) {
		if (!fileName.endsWith(".json")) {
			fileName += ".json";
		}
		this.fileName = fileName;

		dir.mkdirs();
		this.file = new File(dir, fileName);
		this.parser = new JSONParser();
		this.gson = new GsonBuilder().setPrettyPrinting().create();

		try {
			// hacky operation to determine if file is blank
			if (file.createNewFile() || file.length() <= 0) {
				this.obj = new JSONObject();
			} else {
				this.obj = (JSONObject) parser.parse(initReader());
			}
		}
		catch (IOException | ParseException e) {
			JSLogger.getLogger().error(
					"Could not parse JSON file '" + fileName + "'!");
			this.obj = new JSONObject();
		}
		initReader();
	}

	/**
	 * Initializes file reader if null
	 * 
	 * @return FileReader
	 */
	private FileReader initReader() {
		if (reader == null) {
			try {
				this.reader = new FileReader(file);
			}
			catch (FileNotFoundException e) {
				JSLogger.getLogger().error(
						"Could not read JSON file '" + fileName + "'!");
				e.printStackTrace();
			}
		}
		return reader;
	}
	
	@SuppressWarnings("unchecked")
	public JsonConfiguration writeArray(String key, JSONArray array) {
		for (Object value : array) {
			if (value == null) continue;
			array.set(array.indexOf(value), JSONValue.toJSONString(value));
		}
		return writeObject(key, array);
	}
	
	public JsonConfiguration writeBoolean(String key, boolean value) {
		return writeObject(key, value);
	}

	public JsonConfiguration writeInt(String key, int value) {
		return writeObject(key, value);
	}

	public JsonConfiguration writeFloat(String key, float value) {
		return writeObject(key, value);
	}

	public JsonConfiguration writeDouble(String key, double value) {
		return writeObject(key, value);
	}

	public JsonConfiguration writeString(String key, String value) {
		return writeObject(key, value);
	}

	/**
	 * Writes a single key and value to the json file using {@link #write(Map)}
	 * 
	 * @param key
	 *            the String path
	 * @param value
	 *            the Object value to be associated with specified {@code key}
	 * 
	 * @return the JsonConfiguration instance
	 */
	public JsonConfiguration writeObject(String key, Object value) {
		Map<String, Object> result = new HashMap<>(1);
		result.put(key, value);
		return write(result);
	}

	/**
	 * Writes a map of keys and values to the Json file. The map is a collection
	 * of any object type. The underlying FileWriter attempts to parse the
	 * current JSONObject to {@code gson} pretty printing standards.
	 * 
	 * @param map
	 *            a Map of object keys and values
	 * @return the JsonConfiguration instance
	 */
	@SuppressWarnings("unchecked")
	public JsonConfiguration write(Map<?, ?> map) {
		obj.putAll(map);

		try (FileWriter writer = new FileWriter(file)) {
			writer.write(gson.toJson(parser.parse(obj.toJSONString())));
		}
		catch (IOException | ParseException e) {
			JSLogger.getLogger().error(
					"Could not write to JSON file '" + fileName + "'!");
			e.printStackTrace();
		}
		return this;
	}

	/**
	 * Grabs an object value with a json object pathway
	 * 
	 * @param key
	 *            the Object path to the value
	 * @return an Object
	 */
	public Object getValue(Object key) {
		return obj.get(key);
	}
}
