/**
 * Ability.java is a part of Joystick
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
package com.valygard.aohruthless.ability;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import com.valygard.aohruthless.PluginBase;
import com.valygard.aohruthless.messenger.Msg;
import com.valygard.aohruthless.utils.inventory.InventoryUtils;

/**
 * Basic Ability container. All abilities are unique in {@code name} and
 * {@code material}. Abilities are functions or actions that players in an arena
 * can use to enhance the gameplay to help prevent stale gameplay. Abilities
 * additionally make for good special perks.
 * <p>
 * This class may not be instantiated. As such, it is abstract and all abilities
 * must have this class as a parent or grandparent.
 * </p>
 * <p>
 * All Abilities have their own self-contained listener system for Bukkit
 * events.
 * </p>
 * 
 * @author Anand
 * 
 */
public abstract class Ability implements Listener {

	protected final PluginBase plugin;
	protected final String name;
	protected final Material material;
	protected final String perm;

	/**
	 * Ability constructor requires a Plugin instance, a String name, a valid
	 * permission, and a materail type
	 * 
	 * @param plugin
	 *            the underlying plugin instance to register events
	 * @param name
	 *            the unique name for the ability
	 * @param perm
	 *            the String permission attachment to use the ability
	 * @param material
	 *            the material type
	 */
	protected Ability(PluginBase plugin, String name, String perm, Material material) {
		this.plugin = plugin;
		this.name = name;
		this.perm = perm;
		this.material = material;

		registerAbility(plugin);
	}

	/**
	 * Registers events for this ability given a Plugin instance. All abilities
	 * are recommended to register events in this method otherwise they will not
	 * function properly.
	 * 
	 * @param plugin
	 *            the underlying plugin instance to register Listener
	 */
	protected abstract void registerAbility(PluginBase plugin);

	/**
	 * Grabs ability name
	 * 
	 * @return the String identifier
	 */
	public String getName() {
		return name;
	}

	/**
	 * Grabs the ability material
	 * 
	 * @return unique material type
	 */
	public Material getMaterial() {
		return material;
	}

	/**
	 * Grabs ability permission
	 * 
	 * @return the String permission attachment
	 */
	public String getPermission() {
		return perm;
	}

	/**
	 * Checks if a {@code player} has access to the ability. This essentially
	 * checks if the user has permission and the correct itemstack in their main
	 * hand. If the player does not have permission, they are notified with a
	 * message.
	 * 
	 * @param player
	 *            the Player to check.
	 * @return true if the item was removed from the player's inventory
	 */
	public boolean onCheck(Player player) {
		if (!player.hasPermission(perm)) {
			plugin.getMessenger().tell(player, Msg.ABILITY_NO_PERM);
			return false;
		}
		return InventoryUtils.removeItems(player, new ItemStack(material), 1) == 1;
	}

	/**
	 * Handles the usage of the ability. This is unique for each ability and
	 * determines what the ability actually does.
	 * 
	 * @param player
	 *            the Player to use the ability
	 */
	public abstract void onUse(Player player);

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((material == null) ? 0 : material.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}
}