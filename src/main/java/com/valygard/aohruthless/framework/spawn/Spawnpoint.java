/**
 * Spawnpoint.java is a part of Joystick
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
package com.valygard.aohruthless.framework.spawn;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import com.valygard.aohruthless.utils.config.LocationSerializer;

/**
 * Spawnpoint container handles all arena locations for various player spawns
 * 
 * @author Anand
 * 
 */
public class Spawnpoint {

	// spawn type
	private final Spawn spawn;

	// location contents
	private final double x, y, z;
	private final float yaw, pit;
	private final World world;
	private final Location location;

	/**
	 * Initializes a Spawnpoint from disk by deserializing a location from
	 * configuration and the proper spawn type.
	 * 
	 * @param spawn
	 *            the Spawn type
	 * @param config
	 *            the Configuration Section
	 * @param path
	 *            the path to the serialized config
	 * @param world
	 *            the World the spawnpoint is located in
	 */
	public Spawnpoint(Spawn spawn, ConfigurationSection config, String path,
			World world) {
		this(spawn, LocationSerializer.deserializeLoc(config, path, world));
	}

	/**
	 * Initializes a Spawnpoint by spawn type and a given Location. Used
	 * generally as creating a spawnpoint, which is then serialized to config
	 * 
	 * @param spawn
	 *            the Spawn type.
	 * @param loc
	 *            a Location to set for spawnpoint
	 */
	public Spawnpoint(Spawn spawn, Location loc) {
		this.spawn = spawn;

		this.location = loc;
		this.x = Double.valueOf(LocationSerializer.toHundredths(loc.getX()));
		this.y = Double.valueOf(LocationSerializer.toHundredths(loc.getY()));
		this.z = Double.valueOf(LocationSerializer.toHundredths(loc.getZ()));
		this.yaw = Float.parseFloat(LocationSerializer.toHundredths(loc
				.getYaw()));
		this.pit = Float.parseFloat(LocationSerializer.toHundredths(loc
				.getPitch()));
		this.world = loc.getWorld();
	}

	public Spawn getSpawnType() {
		return spawn;
	}

	public Location getLocation() {
		return location;
	}

	/**
	 * Two spawnpoints are equal if and only if the underlying {@code spawn}
	 * types are equal and if the two locations are equivalent
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Spawnpoint) {
			Spawnpoint spawn = (Spawnpoint) o;
			return (spawn.getSpawnType() == this.spawn && location.equals(spawn
					.getLocation()));
		}
		return false;
	}

	@Override
	public int hashCode() {
		int hash = 7;

		hash = 13 * hash + (spawn.hashCode());
		hash = 13 * hash + (world != null ? world.hashCode() : 0);
		hash = 13
				* hash
				+ (int) (Double.doubleToLongBits(x) ^ (Double
						.doubleToLongBits(x) >>> 32));
		hash = 13
				* hash
				+ (int) (Double.doubleToLongBits(y) ^ (Double
						.doubleToLongBits(y) >>> 32));
		hash = 13
				* hash
				+ (int) (Double.doubleToLongBits(z) ^ (Double
						.doubleToLongBits(z) >>> 32));

		hash = 13 * hash + (int) (Float.floatToIntBits(pit));
		hash = 13 * hash + (int) (Float.floatToIntBits(yaw));

		return hash;
	}
}
