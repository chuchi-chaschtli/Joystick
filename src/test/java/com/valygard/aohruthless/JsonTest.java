/**
 * JsonTest.java is a part of Joystick
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
package com.valygard.aohruthless;

import java.io.File;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.valygard.aohruthless.utils.config.JsonConfiguration;

/**
 * @author Anand
 * 
 */
public class JsonTest {

	/**
	 * Testing array read/write
	 * 
	 * @param args
	 */
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		JsonConfiguration config = new JsonConfiguration(new File(
				"src/test/resources"), "config.json");

		// init
		JSONArray array = (JSONArray) config.getValue("_default");
		JSONObject object = new JSONObject();
		if (array == null) {
			array = new JSONArray();
		} else {
			object = (JSONObject) array.get(0);
		}

		// initial values
		object.put("kills", 5);
		object.put("deaths", 4);
		// determine if parseValue() functions correctly
		object.put("wins", parseValue(config, object, "wins"));

		// reload
		if (array.size() < 1) {
			array.add(object);
		} else {
			array.set(0, object);
		}

		// write
		config.write("_default", array);
	}

	/**
	 * Attempts to parse object keys to int values, null -> 0
	 * 
	 * @param config
	 * @param object
	 * @param key
	 * @return
	 */
	private static int parseValue(JsonConfiguration config, JSONObject object,
			String key) {
		Object obj = config.getValue(object.get(key));
		if (obj == null || !(obj instanceof Integer)) {
			return 0;
		}
		return (int) obj;
	}
}
