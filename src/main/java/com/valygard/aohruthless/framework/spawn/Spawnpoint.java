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
import org.bukkit.util.Vector;

import com.valygard.aohruthless.utils.config.LocationSerializer;

/**
 * Spawnpoint container handles all arena locations for various player spawns.
 * Spawnpoints are immutable and cannot be modified externally. They serve
 * primarily as a wrapper for arena locations.
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
		this(spawn, LocationSerializer.deserialize(config, path, world));
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

		this.x = Double.valueOf(LocationSerializer.toHundredths(loc.getX()));
		this.y = Double.valueOf(LocationSerializer.toHundredths(loc.getY()));
		this.z = Double.valueOf(LocationSerializer.toHundredths(loc.getZ()));
		this.yaw = Float.parseFloat(LocationSerializer.toHundredths(loc
				.getYaw()));
		this.pit = Float.parseFloat(LocationSerializer.toHundredths(loc
				.getPitch()));
		this.world = loc.getWorld();
	}

	/**
	 * Grabs the spawn type
	 * 
	 * @return Spawn enumeration
	 */
	public Spawn getSpawnType() {
		return spawn;
	}

	public Vector toVector() {
		return new Vector(x, y, z);
	}

	/**
	 * Creates a new location reference to the Spawnpoint attributes.
	 * 
	 * @return a Location
	 */
	public Location toLocation() {
		return new Location(world, x, y, z, yaw, pit);
	}

	public double getX() {
		return x;
	}

	public double getY() {
		return y;
	}

	public double getZ() {
		return z;
	}

	public float getYaw() {
		return yaw;
	}

	public float getPitch() {
		return pit;
	}

	public World getWorld() {
		return world;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(pit);
		result = prime * result + ((spawn == null) ? 0 : spawn.hashCode());
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + Float.floatToIntBits(yaw);
		temp = Double.doubleToLongBits(z);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof Spawnpoint)) {
			return false;
		}
		Spawnpoint other = (Spawnpoint) obj;
		if (Float.floatToIntBits(pit) != Float.floatToIntBits(other.pit)) {
			return false;
		}
		if (spawn != other.spawn) {
			return false;
		}
		if (world == null) {
			if (other.world != null) {
				return false;
			}
		} else if (!world.equals(other.world)) {
			return false;
		}
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x)) {
			return false;
		}
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y)) {
			return false;
		}
		if (Float.floatToIntBits(yaw) != Float.floatToIntBits(other.yaw)) {
			return false;
		}
		if (Double.doubleToLongBits(z) != Double.doubleToLongBits(other.z)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Spawnpoint [spawn=");
		builder.append(spawn);
		builder.append(", x=");
		builder.append(x);
		builder.append(", y=");
		builder.append(y);
		builder.append(", z=");
		builder.append(z);
		builder.append(", yaw=");
		builder.append(yaw);
		builder.append(", pit=");
		builder.append(pit);
		builder.append(", world=");
		builder.append(world);
		builder.append("]");
		return builder.toString();
	}
}
