/**
 * Message.java is a part of Joystick
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
package com.valygard.aohruthless.messenger;

/**
 * @author Anand
 * 
 */
public interface Message {

	/**
	 * Modifies a message contents
	 * 
	 * @param value
	 */
	void set(String value);

	/**
	 * Returns a parsed value of the message contents
	 * 
	 * @return formatted String
	 */
	public String toString();

	/**
	 * Replaces all '%' signs with a given string value.
	 * 
	 * @param s
	 *            the String to replace
	 * @return formatted String
	 */
	public String format(String s);
}
