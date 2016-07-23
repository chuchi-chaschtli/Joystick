/**
 * LocationSerializer.java is a part of Joystick
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
package com.valygard.aohruthless.utils.config;

import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

/**
 * @author Anand
 * 
 */
public class LocationSerializer {
	
	/**
	 * Prevent initialization of utility class.
	 * 
	 * @throws AssertionError
	 *             if attempted access by reflection
	 */
	private LocationSerializer() {
		throw new AssertionError("Cannot initialize utility constructor");
	}

	/**
	 * Parses a Location from a config string path. A location can be stored as
	 * a string path as (x,y,z) coordinates, (x,y,z,yaw,pitch) values, or
	 * (x,y,z,yaw,pitch,world). If no world is provided, the world parameter is
	 * used.
	 * 
	 * @param config
	 *            the configuration section
	 * @param path
	 *            the string path of the location
	 * @param world
	 *            the world of the location
	 * @return
	 */
	public static Location deserializeLoc(ConfigurationSection config,
			String path, World world) {
		String value = config.getString(path);
		if (value == null) return null;

		String[] parts = value.split(",");
		if (parts.length < 3) {
			throw new IllegalArgumentException(
					"A location must be at least (x,y,z)");
		}
		Double x = Double.parseDouble(parts[0]);
		Double y = Double.parseDouble(parts[1]);
		Double z = Double.parseDouble(parts[2]);
		if (parts.length == 3) {
			return new Location(world, x, y, z);
		}
		if (parts.length < 5) {
			throw new IllegalArgumentException(
					"Expected location of type (x,y,z,yaw,pitch)");
		}
		Float yaw = Float.parseFloat(parts[3]);
		Float pit = Float.parseFloat(parts[4]);
		if (world == null) {
			if (parts.length != 6) {
				throw new IllegalArgumentException(
						"Expected location of type (x,y,z,yaw,pitch,world)");
			}
			world = Bukkit.getWorld(parts[5]);
		}
		return new Location(world, x, y, z, yaw, pit);
	}

	/**
	 * Serializes a location to config.
	 * 
	 * @param config
	 * @param path
	 * @param location
	 */
	public static void serialize(ConfigurationSection config, String path,
			Location location) {
		if (location == null) {
			config.set(path, null);
			return;
		}

		String x = toHundredths(location.getX());
		String y = toHundredths(location.getY());
		String z = toHundredths(location.getZ());

		String yaw = toHundredths(location.getYaw());
		String pit = toHundredths(location.getPitch());

		String world = location.getWorld().getName();

		StringBuilder sb = new StringBuilder();
		sb.append(x).append(",").append(y).append(",").append(z);
		sb.append(",").append(yaw).append(",").append(pit);
		sb.append(",").append(world);

		config.set(path, sb.toString());
	}

	/**
	 * String representation of a double to hundredths value.
	 * 
	 * @param value
	 *            a double to reformat
	 * @return a formatted String
	 */
	private static String toHundredths(double value) {
		return new DecimalFormat("#.##").format(value);
	}
}
