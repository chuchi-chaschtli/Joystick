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
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.valygard.aohruthless.framework.Arena;

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

	private final Map<UUID, ItemStack[]> items, armor;

	public InventoryHandler(Arena arena) {
		this.dir = new File(arena.getPlugin().getDataFolder(), "inventories");
		this.dir.mkdir();

		this.items = new HashMap<UUID, ItemStack[]>();
		this.armor = new HashMap<UUID, ItemStack[]>();
	}

	/**
	 * Store the player's inventory in the directory. Doesn't avoid overrides
	 * because we are only saving the most recent inventory. This method stores
	 * the inventory in memory and on disk for convenience.
	 * 
	 * @param p
	 * @throws IOException
	 */
	public void storeInventory(Player p) throws IOException {
		ItemStack[] items = p.getInventory().getContents();
		ItemStack[] armor = p.getInventory().getArmorContents();

		UUID uuid = p.getUniqueId();

		this.items.put(uuid, items);
		this.armor.put(uuid, armor);

		File file = new File(dir, uuid.toString());
		YamlConfiguration config = new YamlConfiguration();
		config.set("items", items);
		config.set("armor", armor);
		config.set("last-known-name", p.getName());
		config.set("uuid", uuid.toString());
		config.save(file);

		// And clear the inventory
		InventoryUtils.clearInventory(p);
		p.updateInventory();
	}

	/**
	 * Restore the player's inventory back to them.
	 * 
	 * @throws IOException
	 * @throws InvalidConfigurationException
	 */
	public void restoreInventory(Player p) throws IOException,
			InvalidConfigurationException {
		UUID uuid = p.getUniqueId();

		// Grab disk file
		File file = new File(dir, uuid.toString());

		// Try to grab the items from memory first
		ItemStack[] items = this.items.remove(p);
		ItemStack[] armor = this.armor.remove(p);

		// If we can't restore from memory, restore from file
		if (items == null || armor == null) {
			YamlConfiguration config = new YamlConfiguration();
			config.load(file);

			// Get the items and armor lists
			List<?> itemsList = config.getList("items");
			List<?> armorList = config.getList("armor");

			// Turn the lists into arrays
			items = itemsList.toArray(new ItemStack[itemsList.size()]);
			armor = armorList.toArray(new ItemStack[armorList.size()]);
		}

		// Set the player inventory contents
		p.getInventory().setContents(items);
		p.getInventory().setArmorContents(armor);

		// Delete the file
		file.delete();
	}
}
