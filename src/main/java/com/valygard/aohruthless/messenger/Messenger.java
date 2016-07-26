/**
 * Messenger.java is a part of Joystick
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
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.valygard.aohruthless.framework.Arena;

/**
 * @author Anand
 * 
 */
public class Messenger {

	/**
	 * Prevent initialization of utility class.
	 * 
	 * @throws AssertionError
	 *             if attempted access by reflection
	 */
	private Messenger() {
		throw new AssertionError("Cannot initialize utility constructor");
	}

	public static boolean tell(CommandSender p, String msg) {
		// If the input sender is null or the string is empty, return.
		if (p == null || msg.equals(" ")) {
			return false;
		}

		// Otherwise, send the message with the plugin tag.
		p.sendMessage(ChatColor.DARK_GRAY + "[Joystick] " + ChatColor.RESET
				+ msg);
		return true;
	}

	public static boolean tell(CommandSender p, Message msg, String s) {
		return tell(p, msg.format(s));
	}

	public static boolean tell(CommandSender p, Message msg) {
		return tell(p, msg.toString());
	}

	public static void announce(Arena arena, String msg) {
		for (Player p : arena.getPlayers()) {
			tell(p, msg);
		}
	}

	public static void announce(Arena arena, Message msg, String s) {
		announce(arena, msg.format(s));
	}

	public static void announce(Arena arena, Message msg) {
		announce(arena, msg.toString());
	}

}
