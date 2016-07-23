/**
 * StringUtils.java is a part of Joystick
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
package com.valygard.aohruthless.utils;

import java.util.Collection;

import org.bukkit.entity.Player;

import com.valygard.aohruthless.framework.Arena;

/**
 * Utility class for String manipulation
 * 
 * @author Anand
 * 
 */
public class StringUtils {
	
	/**
	 * Prevent initialization of utility class.
	 * 
	 * @throws AssertionError
	 *             if attempted access by reflection
	 */
	private StringUtils() {
		throw new AssertionError("Cannot initialize utility constructor");
	}

	/**
	 * Creates a StringBuilder with line breaks between individual elements.
	 * 
	 * @param initial
	 *            the initial StringBuilder
	 * @param args
	 *            the elements to be appended to the builder
	 * @return a StringBuilder
	 */
	public static StringBuilder appendWithNewLines(StringBuilder initial,
			String... args) {
		for (String str : args) {
			initial = initial.append("\n");
			initial = initial.append(str);
		}
		return initial;
	}

	/**
	 * Formats a list into a string.
	 * 
	 * @param list
	 *            a generic List.
	 * @param separator
	 *            the String separator between individual objects in the
	 *            collection
	 * @return the string
	 */
	public static <E> String formatList(Collection<E> list, String separator) {
		if (list == null || list.isEmpty()) {
			return "";
		}

		StringBuilder sb = new StringBuilder();
		separator = (separator == null || separator.isEmpty()) ? " "
				: separator;

		E type = list.iterator().next();
		if (type instanceof Player) {
			for (E e : list) {
				sb.append(((Player) e).getName());
				sb.append(separator);
			}
		} else if (type instanceof Arena) {
			for (E e : list) {
				sb.append(((Arena) e).getName());
				sb.append(separator);
			}
		} else {
			for (E e : list) {
				sb.append(e.toString());
				sb.append(separator);
			}
		}

		return sb.toString().trim();
	}

	/**
	 * Return an enumeration from a specified string.
	 * 
	 * @param c
	 *            the enum
	 * @param string
	 *            the string
	 * @return an enum class.
	 */
	public static <T extends Enum<T>> T getEnumFromString(Class<T> c,
			String string) {
		if (c != null && string != null) {
			try {
				return Enum.valueOf(c, string.trim().toUpperCase());
			}
			catch (IllegalArgumentException ex) {}
		}
		return null;
	}

	/**
	 * Converts an Array of Strings into a formatted String
	 * 
	 * @param array
	 *            the String[] object
	 * @param separator
	 *            the designated splitter
	 * @return a String
	 */
	public static String convertArrayToString(String[] array, String separator) {
		StringBuilder sb = new StringBuilder();
		separator = (separator == null || separator.isEmpty()) ? " "
				: separator;

		for (String str : array) {
			sb.append(str).append(separator);
		}
		return sb.toString().trim();
	}

	/**
	 * Trims a String by a given regex at a pointed index.
	 * 
	 * @param string
	 *            the String to trim
	 * @param regex
	 *            the String regex to split
	 * @param trimSize
	 *            the starting point for splitting
	 * @return a String
	 */
	public static String trimByRegex(String string, String regex, int trimSize) {
		String[] array = string.split(regex);
		StringBuilder sb = new StringBuilder();

		if (trimSize > array.length || trimSize < 0) {
			throw new IllegalArgumentException("Invalid trim size given!");
		}

		for (int i = trimSize; i < array.length; i++) {
			sb.append(array[i]).append(regex);
		}
		return sb.toString().trim();
	}
}