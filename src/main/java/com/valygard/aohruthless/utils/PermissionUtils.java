/**
 * PermissionUtils.java is a part of Joystick
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

import org.bukkit.Bukkit;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.ProxiedCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.PluginManager;

/**
 * @author Anand
 * 
 */
public class PermissionUtils {

	/**
	 * Prevent initialization of utility class.
	 * 
	 * @throws AssertionError
	 *             if attempted access by reflection
	 */
	private PermissionUtils() {
		throw new AssertionError("Cannot initialize utility constructor");
	}

	/**
	 * Registers a new permission.
	 * 
	 * @param permString
	 *            the new permission string.
	 * @param value
	 *            the default value.
	 * @return the permission added.
	 */
	public static Permission registerPermission(String permString,
			PermissionDefault value) {
		PluginManager pm = Bukkit.getServer().getPluginManager();

		Permission perm = pm.getPermission(permString);
		if (perm == null) {
			perm = new Permission(permString);
			perm.setDefault(value);
			pm.addPermission(perm);
		}
		return perm;
	}

	/**
	 * Unregisters a given permission.
	 * 
	 * @param s
	 *            the permission string to unregister.
	 */
	public static void unregisterPermission(String s) {
		Bukkit.getServer().getPluginManager().removePermission(s);
	}

	/**
	 * Checks if a player has access to a given permission String. Serves has a
	 * helper method for {@link #has(CommandSender, String)}
	 * 
	 * @param p
	 *            the Player
	 * @param s
	 *            the String
	 * @return a boolean flag, true if player has access
	 */
	public static boolean has(Player p, String s) {
		return p.hasPermission(s);
	}

	/**
	 * Determines if a CommandSender has access to a given permission. Always
	 * true if the CommandSender is not a player.
	 * 
	 * @param sender
	 *            the CommandSender
	 * @param s
	 *            the String to check
	 * @return true if permission access is granted
	 */
	public static boolean has(CommandSender sender, String s) {
		if (sender instanceof ConsoleCommandSender
				|| sender instanceof ProxiedCommandSender
				|| sender instanceof BlockCommandSender) {
			return true;
		}
		return has((Player) sender, s);
	}
}
