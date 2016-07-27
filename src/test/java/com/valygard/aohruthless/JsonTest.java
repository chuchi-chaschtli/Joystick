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
import java.util.Arrays;

import org.json.simple.JSONArray;

import com.valygard.aohruthless.utils.config.JsonConfiguration;

/**
 * @author Anand
 * 
 */
public class JsonTest {

	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		Object[] array = new Object[] { 3.14, 7, 15f, "hello world" };

		JsonConfiguration config = new JsonConfiguration(new File(
				"src/test/resources"), "config.json");
		config.write("hello", "world");

		JSONArray jArray = new JSONArray();
		jArray.addAll(Arrays.asList(array));

		config.write(new String[] { "aoh", "object-array" }, new Object[] {
				"ruthless", jArray });

		jArray = (JSONArray) config.getValue("object-array");
		
		Object[] objects = jArray.toArray();
		// OUTPUT: 3.14 7 15.0 hello world
		// success :)
		for (Object obj : objects) {
			System.out.print(obj + " ");
		}
	}
}
