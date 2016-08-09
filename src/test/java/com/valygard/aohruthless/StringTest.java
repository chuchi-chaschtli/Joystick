/**
 * StringTest.java is a part of Joystick
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

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.valygard.aohruthless.mock.MockUtils;
import com.valygard.aohruthless.utils.StringUtils;

/**
 * @author Anand
 * 
 */
public class StringTest {

	@Test
	public void testFormatter() {
		List<Player> mockPlayers = new ArrayList<>();
		mockPlayers.add(MockUtils.getPlayer("AoH_Ruthless"));
		mockPlayers.add(MockUtils.getPlayer("sselhtuR_HoA"));
		Assert.assertThat(StringUtils.formatList(mockPlayers, ","),
				CoreMatchers.is("AoH_Ruthless,sselhtuR_HoA,"));
	}

	@Test
	public void testEnumMatcher() {
		Material toPass = StringUtils.getEnumFromString(Material.class,
				"diaMoND_sword   ");
		Assert.assertThat(toPass, CoreMatchers.is(Material.DIAMOND_SWORD));

		ChatColor toFail = StringUtils.getEnumFromString(ChatColor.class,
				"violet");
		Assert.assertNull(toFail);
	}
}
