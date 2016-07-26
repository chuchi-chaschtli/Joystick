/**
 * InventoryHandler.java is a part of Joystick
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

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.json.simple.JSONArray;

import com.valygard.aohruthless.utils.config.JsonConfiguration;

/**
 * Inventory manager for a given arena. Handles storage and restoration of
 * inventory to memory and disk locations. All inventories are stored in the
 * inventories folder which is located in the plugin data folder.
 * 
 * @author Anand
 * 
 */
public class InventoryHandler {

	private final File dir;

	private final Map<String, ItemStack[]> items, armor;

	public InventoryHandler(Plugin plugin) {
		this.dir = new File(plugin.getDataFolder(), "inventories");
		this.dir.mkdir();

		this.items = new HashMap<>();
		this.armor = new HashMap<>();
	}

	/**
	 * Store the player's inventory in the directory. Doesn't avoid overrides
	 * because we are only saving the most recent inventory. This method stores
	 * the inventory in memory and on disk for convenience.
	 * 
	 * @param p
	 */
	@SuppressWarnings("unchecked")
	public void storeInventory(Player p) {
		ItemStack[] items = p.getInventory().getContents();
		ItemStack[] armor = p.getInventory().getArmorContents();

		UUID uuid = p.getUniqueId();
		String name = p.getName();

		this.items.put(name, items);
		this.armor.put(name, armor);

		JsonConfiguration json = new JsonConfiguration(dir, uuid.toString());
		json.write(new String[] { "last-known-username", "uuid" },
				new String[] { name, uuid.toString() });
		json.write("items", new JSONArray().addAll(Arrays.asList(items)));
		json.write("armor", new JSONArray().addAll(Arrays.asList(armor)));

		// And clear the inventory
		InventoryUtils.clearInventory(p);
		p.updateInventory();
	}

	/**
	 * Restore the player's inventory back to them.
	 */
	@SuppressWarnings("unchecked")
	public void restoreInventory(Player p) {
		UUID uuid = p.getUniqueId();

		// Grab disk file
		File file = new File(dir, uuid.toString());
		JsonConfiguration json = new JsonConfiguration(dir, uuid.toString());

		// Try to grab the items from memory first
		ItemStack[] items = this.items.remove(p.getName());
		ItemStack[] armor = this.armor.remove(p.getName());

		// If we can't restore from memory, restore from file
		if (items == null || armor == null) {
			JSONArray itemsList = (JSONArray) json.getValue("items");
			JSONArray armorList = (JSONArray) json.getValue("armor");

			// Turn the lists into arrays
			items = (ItemStack[]) itemsList.toArray(new ItemStack[itemsList
					.size()]);
			armor = (ItemStack[]) armorList.toArray(new ItemStack[armorList
					.size()]);
		}

		// Set the player inventory contents
		p.getInventory().setContents(items);
		p.getInventory().setArmorContents(armor);

		// Delete the file
		file.delete();
	}
}
