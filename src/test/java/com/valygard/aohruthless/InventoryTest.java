/**
 * InventoryTest.java is a part of Joystick
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

import java.io.File;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.valygard.aohruthless.util.MockPlayerInventory;
import com.valygard.aohruthless.util.MockUtils;
import com.valygard.aohruthless.utils.inventory.InventoryHandler;

/**
 * @author Anand
 * 
 */
@RunWith(PowerMockRunner.class)
public class InventoryTest {

	// default name
	private static final String PLAYER_NAME = "AoH_Ruthless";
	// disk path
	private static final String FILE_PATH = "src/test/resources/inventory";

	@Test
	public void testStoreInventory() {
		Player mockPlayer = MockUtils.getPlayer(PLAYER_NAME);
		MockPlayerInventory mockInv = new MockPlayerInventory();

		Mockito.when(mockPlayer.getInventory()).thenReturn(mockInv);
		Mockito.when(mockPlayer.getUniqueId()).thenReturn(UUID.randomUUID());

		InventoryHandler handler = new InventoryHandler(new File(FILE_PATH));
		handler.storeInventory(mockPlayer);
	}
}