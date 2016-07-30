/**
 * InventoryHandlerTest.java is a part of Joystick
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

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import org.junit.Test;

import com.valygard.aohruthless.utils.inventory.InventoryHandler;

/**
 * @author Anand
 * 
 */
public class InventoryHandlerTest extends JavaPlugin implements Listener {

	private InventoryHandler handler;
	private BukkitTask task;
	
	// my MC account IGN
	private static final String PLAYER_NAME = "AoH_Ruthless";

	@Override
	public void onEnable() {
		handler = new InventoryHandler(this);
		
		task = getServer().getScheduler().runTaskTimer(this, new Runnable() {

			public void run() {
				if (Bukkit.getPlayer(PLAYER_NAME) != null) {
					store();
					task.cancel();
				}
			}
		}, 20l, 20l);
	}

	@Test
	public void store() {
		final Player player = Bukkit.getPlayer(PLAYER_NAME);
		handler.storeInventory(player);

		getServer().getScheduler().runTaskLater(this, new Runnable() {

			@Override
			public void run() {
				handler.restoreInventory(player);
			}
		}, 50l);
	}
}
