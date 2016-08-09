/**
 * MockPlayerInventory.java is a part of Joystick
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
package com.valygard.aohruthless.mock;

import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * No longer in use
 * 
 * @author Anand
 * 
 */
@Deprecated
public class MockPlayerInventory implements PlayerInventory {

	ItemStack[] armorContents = new ItemStack[4];
	ItemStack[] contents = new ItemStack[36];

	/**
	 * Default contents for testing
	 */
	public MockPlayerInventory() {
		armorContents[1] = new ItemStack(Material.DIAMOND_CHESTPLATE);
		contents[5] = new ItemStack(Material.GOLD_NUGGET, 20);
		contents[8] = new ItemStack(Material.WOOD);
	}

	@Override
	public ItemStack[] getArmorContents() {
		return armorContents;
	}

	@Override
	public ItemStack getHelmet() {
		return armorContents[0];
	}

	@Override
	public ItemStack getChestplate() {
		return armorContents[1];
	}

	@Override
	public ItemStack getLeggings() {
		return armorContents[2];
	}

	@Override
	public ItemStack getBoots() {
		return armorContents[3];
	}

	@Override
	public void setArmorContents(ItemStack[] itemStacks) {
		this.armorContents = itemStacks;
	}

	@Override
	public void setHelmet(ItemStack itemStack) {
		this.armorContents[0] = itemStack;
	}

	@Override
	public void setChestplate(ItemStack itemStack) {
		this.armorContents[1] = itemStack;
	}

	@Override
	public void setLeggings(ItemStack itemStack) {
		this.armorContents[2] = itemStack;
	}

	@Override
	public void setBoots(ItemStack itemStack) {
		this.armorContents[3] = itemStack;
	}

	@Override
	public void setHeldItemSlot(int i) {

	}

	@Override
	public ItemStack getItemInHand() {
		return null;
	}

	@Override
	public void setItemInHand(ItemStack itemStack) {

	}

	@Override
	public int getHeldItemSlot() {
		return 0;
	}

	@Override
	public int clear(int i, int i2) {
		return 0;
	}

	@Override
	public HumanEntity getHolder() {
		return null;
	}

	@Override
	public int getSize() {
		return contents.length + armorContents.length;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public ItemStack getItem(int i) {
		if (i >= 0 && i < 36) {
			return contents[i];
		} else if (i >= 36 && i < 36 + 4) {
			return armorContents[i - 36];
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	@Override
	public void setItem(int i, ItemStack itemStack) {
		if (i >= 0 && i < 36) {
			contents[i] = itemStack;
		} else if (i >= 36 && i < 36 + 4) {
			armorContents[i - 36] = itemStack;
		} else {
			throw new ArrayIndexOutOfBoundsException();
		}
	}

	@Override
	public HashMap<Integer, ItemStack> addItem(ItemStack... itemStacks) {
		return null;
	}

	@Override
	public HashMap<Integer, ItemStack> removeItem(ItemStack... itemStacks) {
		return null;
	}

	@Override
	public ItemStack[] getContents() {
		return this.contents;
	}

	@Override
	public void setContents(ItemStack[] itemStacks) {
		this.contents = itemStacks;
	}

	@Override
	public boolean contains(int i) {
		return false;
	}

	@Override
	public boolean contains(Material material) {
		return false;
	}

	@Override
	public boolean contains(ItemStack itemStack) {
		return false;
	}

	@Override
	public boolean contains(int i, int i1) {
		return false;
	}

	@Override
	public boolean contains(Material material, int i) {
		return false;
	}

	@Override
	public boolean contains(ItemStack itemStack, int i) {
		return false;
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(int i) {
		return null;
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(Material material) {
		return null;
	}

	@Override
	public HashMap<Integer, ? extends ItemStack> all(ItemStack itemStack) {
		return null;
	}

	@Override
	public int first(int i) {
		return 0;
	}

	@Override
	public int first(Material material) {
		return 0;
	}

	@Override
	public int first(ItemStack itemStack) {
		return 0;
	}

	@Override
	public int firstEmpty() {
		return 0;
	}

	@Override
	public void remove(int i) {}

	@Override
	public void remove(Material material) {}

	@Override
	public void remove(ItemStack itemStack) {}

	@Override
	public void clear(int i) {}

	@Override
	public void clear() {}

	@Override
	public List<HumanEntity> getViewers() {
		return null;
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public InventoryType getType() {
		return null;
	}

	@Override
	public ListIterator<ItemStack> iterator() {
		return null;
	}

	@Override
	public int getMaxStackSize() {
		return 0;
	}

	@Override
	public void setMaxStackSize(int i) {

	}

	@Override
	public ListIterator<ItemStack> iterator(int i) {
		return null;
	}

	@Override
	public boolean containsAtLeast(final ItemStack itemStack, final int i) {
		return false;
	}

	@Override
	public ItemStack[] getStorageContents() {
		return null;
	}

	@Override
	public void setStorageContents(ItemStack[] items)
			throws IllegalArgumentException {}

	@Override
	public Location getLocation() {
		return null;
	}

	@Override
	public ItemStack[] getExtraContents() {
		return null;
	}

	@Override
	public void setExtraContents(ItemStack[] items) {}

	@Override
	public ItemStack getItemInMainHand() {
		return null;
	}

	@Override
	public void setItemInMainHand(ItemStack item) {}

	@Override
	public ItemStack getItemInOffHand() {
		return null;
	}

	@Override
	public void setItemInOffHand(ItemStack item) {}
}
