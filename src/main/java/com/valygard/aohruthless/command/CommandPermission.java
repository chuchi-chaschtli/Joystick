/**
 * CommandPermission.java is a part of Joystick
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
package com.valygard.aohruthless.command;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * @author Anand
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandPermission {

	/**
	 * Only one permission is required for any given command.
	 */
	String value();
}