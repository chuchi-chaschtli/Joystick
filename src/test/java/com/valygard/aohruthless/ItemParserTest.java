/**
 * ItemParserTest.java is a part of Joystick
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
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.valygard.aohruthless.utils.items.ItemParser;

/**
 * @author Anand
 * 
 */
@RunWith(PowerMockRunner.class)
public class ItemParserTest {

	@Test
	public void testMaterialAndAmountToItem() {
		ItemStack stack = ItemParser.parseItem("wood:2");
		ItemStack mockItemStack = PowerMockito.mock(ItemStack.class);

		Mockito.when(mockItemStack.getType()).thenReturn(Material.WOOD);
		Mockito.when(mockItemStack.getAmount()).thenReturn(2);

		Assert.assertTrue(stack.getType() == mockItemStack.getType());
		Assert.assertTrue(stack.getAmount() == mockItemStack.getAmount());
		Assert.assertTrue(stack.getDurability() == mockItemStack
				.getDurability());
	}

	@Test
	public void testWoolToItem() {
		ItemStack stack = ItemParser.parseItem("wool:3:1");
		ItemStack mockItemStack = PowerMockito.mock(ItemStack.class);

		Mockito.when(mockItemStack.getType()).thenReturn(Material.WOOL);
		Mockito.when(mockItemStack.getAmount()).thenReturn(1);
		Mockito.when(mockItemStack.getDurability()).thenReturn((short) 3);

		Assert.assertTrue(stack.getType() == mockItemStack.getType());
		Assert.assertTrue(stack.getAmount() == mockItemStack.getAmount());
		Assert.assertTrue(stack.getDurability() == mockItemStack
				.getDurability());
	}

	@Test
	public void testMaterialAndAmountToString() {
		Assert.assertThat(
				ItemParser.parseString(new ItemStack(Material.WOOD, 2)),
				CoreMatchers.is("wood:2"));
	}

	@Test
	public void testWoolToString() {
		Assert.assertThat(ItemParser.parseString(new ItemStack(Material.WOOL,
				1, (short) 3)), CoreMatchers.is("wool:12:1"));
	}
}
