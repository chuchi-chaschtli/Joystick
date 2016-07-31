/**
 * PotionTest.java is a part of Joystick
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
package com.valygard.aohruthless;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionData;
import org.bukkit.potion.PotionType;
import org.junit.Assert;
import org.junit.Test;

import com.valygard.aohruthless.utils.items.PotionAdapter;
import com.valygard.aohruthless.utils.items.PotionUtils;

/**
 * @author Anand
 * 
 */
public class PotionTest {

	@Test
	public void testNameMatching() {
		ItemStack stack = new ItemStack(Material.LINGERING_POTION);
		Assert.assertTrue(PotionUtils.isPotion(stack));
		Assert.assertTrue(PotionUtils.isPotion("SPLASH_POTION"));
	}

	@Test
	public void testHandleMatching() {
		Assert.assertTrue(PotionAdapter.matchHandle("long-invisibility") == PotionAdapter.LONG_INVISIBILITY);
		Assert.assertTrue(PotionAdapter.matchHandle("PoIsOn") == PotionAdapter.POISON);
	}

	@Test
	public void testDataMatching() {
		Assert.assertTrue(PotionAdapter.matchData(new PotionData(
				PotionType.INVISIBILITY, true, false)) == PotionAdapter.LONG_INVISIBILITY);
	}

	@Test
	public void testDataBuilder() {
		Assert.assertTrue(PotionAdapter.LONG_INVISIBILITY.buildPotionData()
				.equals(new PotionData(PotionType.INVISIBILITY, true, false)));
		Assert.assertFalse(PotionAdapter.LONG_INVISIBILITY.buildPotionData() == new PotionData(
				PotionType.INVISIBILITY, true, false));
	}
}
