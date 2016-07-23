/**
 * Msg.java is a part of Joystick
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

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * @author Anand
 * 
 */
public enum Msg implements Message {
	
	ARENA_READY("The arena is ready to be used!");

	private String value;

	private Msg(String value) {
		set(value);
	}

	public void set(String value) {
		this.value = value;
	}

	public String toString() {
		return ChatColor.translateAlternateColorCodes('&', value);
	}

	public String format(String s) {
		return toString().replace("%", s);
	}

	public static void load(ConfigurationSection config) {
		for (Msg msg : values()) {
			// LEAVE_NOT_PLAYING => leave-not-playing
			String key = msg.name().toLowerCase().replace("_", "-");
			msg.set(config.getString(key, ""));
		}
	}

	public static YamlConfiguration toYaml() {
		YamlConfiguration yaml = new YamlConfiguration();
		for (Msg msg : values()) {
			// LEAVE_NOT_PLAYING => leave-not-playing
			String key = msg.name().replace("_", "-").toLowerCase();
			yaml.set(key, msg.value);
		}
		return yaml;
	}
}
