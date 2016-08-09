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

import java.util.Arrays;

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

	/**
	 * Comprehensively removes items from a {@code player}'s inventory. Given a
	 * an ItemStack comparison and an amount, we iterate through the player's
	 * inventory contents to see if the itemstack matches in type, item meta,
	 * and durability. We remove by the amount specified. If the Player does not
	 * have the amount of items specified to remove, all their items that match
	 * the {@code stack} are removed.
	 * 
	 * @param player
	 *            the Player to remove items from
	 * @param stack
	 *            the ItemStack to remove
	 * @param amount
	 *            the amount to remove
	 * @return true if and only if the {@code amount} of items was removed,
	 *         false otherwise
	 */
	public static boolean removeItems(Player player, ItemStack stack, int amount) {
		if (stack == null || stack.getType() == Material.AIR) {
			return false;
		}

		PlayerInventory inv = player.getInventory();
		for (ItemStack item : inv.getContents()) {
			if (item == null) continue;
			if (item.getType() == stack.getType()
					&& item.getDurability() == stack.getDurability()
					&& item.getItemMeta().equals(stack.getItemMeta())) {
				int newAmount = item.getAmount() - amount;
				if (newAmount > 0) {
					item.setAmount(newAmount);
					return true;
				} else {
					inv.remove(item);
					amount -= newAmount;
					if (amount == 0) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the ItemStack appears to be a weapon type. If true, when a
	 * weapon is given to a player, its durability will be set to the absolute
	 * maximum. Using this method ensures no false positives because of the
	 * unchanging array, but there can be false negatives.
	 * 
	 * @param stack
	 *            the ItemStack
	 * @return true if the ItemStack given is a weapon
	 */
	public static boolean isWeapon(ItemStack stack) {
		if (stack == null) return false;

		Material[] weaponTypes = { Material.BOW, Material.FLINT_AND_STEEL,
				Material.IRON_AXE, Material.IRON_HOE, Material.IRON_PICKAXE,
				Material.IRON_SPADE, Material.IRON_SWORD, Material.GOLD_AXE,
				Material.GOLD_HOE, Material.GOLD_PICKAXE, Material.GOLD_SPADE,
				Material.GOLD_SWORD, Material.STONE_AXE, Material.STONE_HOE,
				Material.STONE_PICKAXE, Material.STONE_SPADE,
				Material.STONE_SWORD, Material.WOOD_AXE, Material.WOOD_HOE,
				Material.WOOD_PICKAXE, Material.WOOD_SPADE,
				Material.WOOD_SWORD, Material.DIAMOND_AXE,
				Material.DIAMOND_HOE, Material.DIAMOND_PICKAXE,
				Material.DIAMOND_SPADE, Material.DIAMOND_SWORD,
				Material.FISHING_ROD, Material.CARROT_STICK };

		return Arrays.binarySearch(weaponTypes, stack.getType()) > -1;
	}
}
