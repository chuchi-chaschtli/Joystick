/**
 * SpawnpointTest.java is a part of Joystick
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

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;
import org.junit.Assert;
import org.junit.Test;

import com.valygard.aohruthless.framework.spawn.Spawn;
import com.valygard.aohruthless.framework.spawn.Spawnpoint;
import com.valygard.aohruthless.mock.MockWorld;

/**
 * @author Anand
 * 
 */
public class SpawnpointTest {

	private static final int X = 32;
	private static final int Y = 64;
	private static final int Z = -100;

	@Test
	public void testSpawnpoint() {
		World mockWorld = new MockWorld("Hello World!");
		Spawnpoint spawn = new Spawnpoint(Spawn.DEFAULT, new Location(
				mockWorld, X, Y, Z));

		Assert.assertTrue(spawn.toVector().equals(new Vector(X, Y, Z)));
		Assert.assertTrue(spawn.getSpawnType() == Spawn.DEFAULT);
		Assert.assertTrue(spawn.getWorld().equals(mockWorld));
	}
}