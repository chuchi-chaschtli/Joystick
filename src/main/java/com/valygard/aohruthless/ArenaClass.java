/**
 * ArenaClass.java is a part of Joystick
 *
 * Copyright (c) 2016 Anand Kumar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.valygard.aohruthless;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * @author Anand
 * 
 */
public class ArenaClass {

	private String name, lowercaseName;

	private ItemStack helmet, chestplate, leggings, boots;
	private List<ItemStack> items, armor;

	private boolean unbreakableWeapons, unbreakableArmor;

	/**
	 * Singular constructor initializes a unique ArenaClass instance by three
	 * parameters; a unique string identifer which will serve as the name of the
	 * ArenaClass, and two flags to determine whether or not weapon and armor
	 * are unbreakable.
	 * 
	 * @param name
	 *            the String name; must be unique
	 * @param unbreakableWeapons
	 *            boolean flag, if true, weapons are unbreakable
	 * @param unbreakableArmor
	 *            boolean flag, if true, armor is unbreakable
	 */
	public ArenaClass(String name, boolean unbreakableWeapons,
			boolean unbreakableArmor) {
		this.name = name;
		this.lowercaseName = name.toLowerCase();

		this.items = new ArrayList<ItemStack>();
		this.armor = new ArrayList<ItemStack>();

		this.unbreakableWeapons = unbreakableWeapons;
		this.unbreakableArmor = unbreakableArmor;
	}

	/**
	 * Give the items in the class to the player. This method distinguishes from
	 * armor contents and regular inventory contents, and gives both separately.
	 * Forks through all the inventory contents and adds helmet, chestplate,
	 * etc.. manually.
	 * 
	 * @param p
	 *            a Player
	 */
	public void giveItems(Player p) {
		PlayerInventory inv = p.getInventory();
		// Loop through all the items
		for (ItemStack is : items) {
			p.getInventory().addItem(is);
		}

		if (!armor.isEmpty()) {
			for (ItemStack is : armor) {
				ArmorType type = ArmorType.getType(is);

				if (type == null) continue;

				switch (type) {
				case HELMET:
					inv.setHelmet(is);
					break;
				case CHESTPLATE:
					inv.setChestplate(is);
					break;
				case LEGGINGS:
					inv.setLeggings(is);
					break;
				case BOOTS:
					inv.setBoots(is);
					break;
				default:
					break;
				}
			}
		}

		if (helmet != null) inv.setHelmet(helmet);

		if (chestplate != null) inv.setChestplate(chestplate);

		if (leggings != null) inv.setLeggings(leggings);

		if (boots != null) inv.setBoots(boots);
	}

	/**
	 * Adds an itemstack to the items list.
	 * 
	 * @param stack
	 *            an ItemStack
	 */
	public void addItem(ItemStack stack) {
		if (stack == null) return;

		int stackSize = stack.getMaxStackSize() < 0 ? 64 : stack
				.getMaxStackSize();
		if (stack.getAmount() > stackSize) {
			while (stack.getAmount() > stackSize) {
				items.add(new ItemStack(stack.getType(), stackSize));
				stack.setAmount(stack.getAmount() - stackSize);
			}
		}
		items.add(stack);
	}

	/**
	 * Gets the configuration name.
	 * 
	 * @return the class
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the name of the class to lowercase for easier use later.
	 * 
	 * @return the class to lowercase
	 */
	public String getLowercaseName() {
		return lowercaseName;
	}

	/**
	 * Gets the inventory contents.
	 * 
	 * @return the items list
	 */
	public List<ItemStack> getContents() {
		return items;
	}

	/**
	 * Gets the class's armor contents.
	 * 
	 * @return the armor list
	 */
	public List<ItemStack> getArmor() {
		return armor;
	}

	/**
	 * Checks if the weapons are designated unbreakable.
	 * 
	 * @return if the weapons are unbreakable
	 */
	public boolean containsUnbreakableWeapons() {
		return unbreakableWeapons;
	}

	/**
	 * Sets whether or not the weapons are to be unbreakable.
	 * 
	 * @param unbreakable
	 *            a boolean flag which may change the unbreakable weapon state.
	 */
	public void setUnbreakableWeapons(boolean unbreakable) {
		this.unbreakableWeapons = unbreakable;
	}

	/**
	 * Checks if armor is to be unbreakable.
	 * 
	 * @return true if the armor is unbreakable
	 */
	public boolean containsUnbreakableArmor() {
		return unbreakableArmor;
	}

	/**
	 * Sets whether or not armor is to be unbreakable.
	 * 
	 * @param unbreakable
	 *            a boolean flag which may change the unbreakable armor state.
	 */
	public void setUnbreakableArmor(boolean unbreakable) {
		this.unbreakableArmor = unbreakable;
	}

	/**
	 * Sets the helmet slot for the class.
	 * 
	 * @param helmet
	 *            an item
	 */
	public void setHelmet(ItemStack helmet) {
		this.helmet = helmet;
	}

	/**
	 * Sets the chestplate slot for the class.
	 * 
	 * @param chestplate
	 *            an item
	 */
	public void setChestplate(ItemStack chestplate) {
		this.chestplate = chestplate;
	}

	/**
	 * Sets the leggings slot for the class.
	 * 
	 * @param leggings
	 *            an item
	 */
	public void setLeggings(ItemStack leggings) {
		this.leggings = leggings;
	}

	/**
	 * Sets the boots slot for the class.
	 * 
	 * @param boots
	 *            an item
	 */
	public void setBoots(ItemStack boots) {
		this.boots = boots;
	}

	/**
	 * Replaces the current armor list with the given list.
	 * 
	 * @param armor
	 *            a list of items
	 */
	public void setArmor(List<ItemStack> armor) {
		this.armor = armor;
	}

	/**
	 * Replaces the current items list with a new list of all the items in the
	 * given list. This method uses the addItem() method for each item to ensure
	 * consistency.
	 * 
	 * @param stacks
	 *            a list of items
	 */
	public void setItems(List<ItemStack> stacks) {
		this.items = new ArrayList<ItemStack>(stacks.size());
		for (ItemStack stack : stacks) {
			addItem(stack);
		}
	}

	/**
	 * Convenience enum to detemine if an itemstack is an armor type.
	 */
	public enum ArmorType {
		HELMET(Material.LEATHER_HELMET, Material.CHAINMAIL_HELMET, Material.IRON_HELMET, Material.GOLD_HELMET, Material.DIAMOND_HELMET),
		CHESTPLATE(Material.LEATHER_CHESTPLATE, Material.CHAINMAIL_CHESTPLATE, Material.IRON_CHESTPLATE, Material.GOLD_CHESTPLATE, Material.DIAMOND_CHESTPLATE),
		LEGGINGS(Material.LEATHER_LEGGINGS, Material.CHAINMAIL_LEGGINGS, Material.IRON_LEGGINGS, Material.GOLD_LEGGINGS, Material.DIAMOND_LEGGINGS),
		BOOTS(Material.LEATHER_BOOTS, Material.CHAINMAIL_BOOTS, Material.IRON_BOOTS, Material.GOLD_BOOTS, Material.DIAMOND_BOOTS);

		private Material[] types;

		private ArmorType(Material... types) {
			this.types = types;
		}

		public static ArmorType getType(ItemStack stack) {
			Material m = stack.getType();

			for (ArmorType armorType : ArmorType.values()) {
				for (Material type : armorType.types) {
					if (m == type) {
						return armorType;
					}
				}
			}
			return null;
		}
	}

	/**
	 * Checks whether or not an item is a helmet. This is useful for
	 * non-conventional helmet types such as wool or skulls, which can have
	 * durability but are not like normal helmets that 'wear out'.
	 * 
	 * @param stack
	 *            an itemstack to check
	 * @return true if the stack is a helmet.
	 */
	public static boolean isHelmet(ItemStack stack) {
		return (ArmorType.getType(stack) == ArmorType.HELMET);
	}
}
