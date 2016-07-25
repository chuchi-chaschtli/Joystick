/**
 * InventoryUtils.java is a part of Joystick
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
package com.valygard.aohruthless.utils.inventory;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author Anand
 * 
 */
public class InventoryUtils {

	/**
	 * Prevent initialization of utility class.
	 * 
	 * @throws AssertionError
	 *             if attempted access by reflection
	 */
	private InventoryUtils() {
		throw new AssertionError("Cannot initialize utility constructor");
	}

	/**
	 * Determines if a given player has an empty inventory
	 * 
	 * @param p
	 *            the Player to check
	 * @return true if the inventory is empty, false otherwise
	 */
	public static boolean hasEmptyInventory(Player p) {
		ItemStack[] inventory = p.getInventory().getContents();
		ItemStack[] armor = p.getInventory().getArmorContents();

		// For inventory, check for null
		for (ItemStack stack : inventory) {
			if (stack != null) return false;
		}

		// For armor, check for air
		for (ItemStack stack : armor) {
			if (stack.getType() != Material.AIR) return false;
		}
		return true;
	}

	/**
	 * Clears a player's armor contents and inventory.
	 * 
	 * @param p
	 *            the Player to clear
	 */
	public static void clearInventory(Player p) {
		PlayerInventory inv = p.getInventory();
		inv.clear();
		inv.setHelmet(null);
		inv.setChestplate(null);
		inv.setLeggings(null);
		inv.setBoots(null);
		InventoryView view = p.getOpenInventory();
		if (view != null) {
			view.setCursor(null);
			Inventory i = view.getTopInventory();
			if (i != null) {
				i.clear();
			}
		}
		p.updateInventory();
	}
}
