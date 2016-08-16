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

	private final String prefix;

	/**
	 * Messenger initializes by a prefix for all messages.
	 * 
	 * @param prefix
	 *            a String prefix, usually the plugin name
	 */
	public Messenger(String prefix) {
		this.prefix = prefix;
	}

	public boolean tell(CommandSender sender, String msg) {
		if (sender == null || msg.trim().isEmpty()) {
			return false;
		}
		sender.sendMessage(prefix + ChatColor.RESET + " " + msg);
		return true;
	}

	public boolean tell(CommandSender sender, Message msg) {
		return tell(sender, msg.toString());
	}

	public boolean tell(CommandSender sender, Message msg, String s) {
		return tell(sender, msg.format(s));
	}

	public void announce(Arena arena, String msg) {
		for (Player player : arena.getPlayers()) {
			tell(player, msg);
		}
	}

	public void announce(Arena arena, Message msg) {
		announce(arena, msg.toString());
	}

	public void announce(Arena arena, Message msg, String s) {
		announce(arena, msg.format(s));
	}
}
