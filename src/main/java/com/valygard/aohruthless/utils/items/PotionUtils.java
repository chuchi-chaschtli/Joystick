/**
 * PotionUtils.java is a part of Joystick
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
package com.valygard.aohruthless.utils.items;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionData;

import com.valygard.aohruthless.messenger.JSLogger;


/**
 * @author Anand
 *
 */
public class PotionUtils {
	
	/**
	 * Prevent initialization of utility class.
	 * 
	 * @throws AssertionError
	 *             if attempted access by reflection
	 */
	private PotionUtils() {
		throw new AssertionError("Cannot initialize utility constructor");
	}
	
	/**
	 * Checks if a material is a potion through its name.
	 * 
	 * @param type
	 * @return
	 */
	public static boolean isPotion(String name) {
		return (name.toLowerCase().contains("potion"));
	}

	/**
	 * Convenience method.
	 * 
	 * @param stack
	 * @see #isPotion(String)
	 * @return
	 */
	public static boolean isPotion(ItemStack stack) {
		return isPotion(stack.getType().toString());
	}

	/**
	 * Returns a String identifier from a given Potion item.
	 * 
	 * @param stack
	 *            the ItemStack to parse
	 * @return the first String handle of the given Potion's possible
	 *         identifiers
	 */
	public static String getHandle(ItemStack stack) {
		// should never be executed, precautionary
		if (!isPotion(stack)) {
			JSLogger.getLogger().error(
					"Attempt to parse an itemstack as a potion failed");
			return null;
		}

		PotionMeta pm = (PotionMeta) stack.getItemMeta();
		PotionData data = pm.getBasePotionData();
		return PotionAdapter.matchData(data).getIdentifiers().get(0);
	}

	/**
	 * Creates a potion given an ItemStack and a String identifier, which is
	 * matched to the list of creative inventory potions. See
	 * {@link PotionAdapter}
	 * 
	 * @param stack
	 *            the ItemStack to parse
	 * @param handle
	 *            the given String data for the potion
	 * @return an ItemStack with potion effects
	 */
	public static ItemStack createPotion(ItemStack stack, String handle) {
		if (!isPotion(stack)) {
			JSLogger.getLogger().error(
					"Attempt to parse the following itemstack failed: "
							+ handle);
			return null;
		}
		PotionMeta pm = (PotionMeta) stack.getItemMeta();
		PotionAdapter matcher = PotionAdapter.matchHandle(handle);

		pm.setBasePotionData(matcher.buildPotionData());
		stack.setItemMeta(pm);
		return stack;
	}
}
