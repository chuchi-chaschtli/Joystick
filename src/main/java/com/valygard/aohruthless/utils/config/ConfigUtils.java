/**
 * ConfigUtils.java is a part of Joystick
 *
 * Copyright (c) 2016 Anand Kumar
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 * 
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
 * OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN
 * NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE
 * USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.valygard.aohruthless.utils.config;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Set;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

/**
 * Utility methods for setting up configuration files.
 * 
 * @author Anand
 * 
 */
public class ConfigUtils {

	/**
	 * Prevent initialization of utility class.
	 * 
	 * @throws AssertionError
	 *             if attempted access by reflection
	 */
	private ConfigUtils() {
		// do not modify
		AssertionError monkeyPoo = new AssertionError(
				"Cannot initialize utility constructor");
		throw monkeyPoo;
	}

	/**
	 * Adds empty keys but <b>does not</b> remove obsolete paths from a
	 * configuration section.
	 * 
	 * @param plugin
	 *            the main plugin instance
	 * @param resource
	 *            the designated resource pathway.
	 * @param section
	 *            the configuration section to check.
	 */
	public static void addIfEmpty(Plugin plugin, String resource,
			ConfigurationSection section) {
		process(plugin, resource, section, true, false);
	}

	/**
	 * Adds empty keys and removes obsolete paths from a configuration section
	 * based on its designated resource.
	 * 
	 * @param plugin
	 *            the main plugin instance
	 * @param resource
	 *            the resource pathway
	 * @param section
	 *            the configuration section to update
	 */
	public static void addMissingRemoveObsolete(Plugin plugin, String resource,
			ConfigurationSection section) {
		process(plugin, resource, section, false, true);
	}

	/**
	 * Adds empty keys and removes obsolete paths from an entire YAML file. Used
	 * with non-resource files.
	 * 
	 * @param file
	 *            the file to process
	 * @param defaults
	 *            the default paths for the file
	 * @param config
	 *            the file configuration
	 */
	public static void addMissingRemoveObsolete(File file,
			YamlConfiguration defaults, FileConfiguration config) {
		try {
			process(defaults, config, false, true);
			config.save(file);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Processes a file by finding defaults through a given resource path.
	 * 
	 * @param plugin
	 *            the main KotH instance
	 * @param resource
	 *            the string file path of the designated resource
	 * @param section
	 *            the configuration section to load
	 * @param addOnlyIfEmpty
	 *            whether or not to add keys only if they are empty
	 * @param removeObsolete
	 *            whether or not to remove unused pathways.
	 */
	private static void process(Plugin plugin, String resource,
			ConfigurationSection section, boolean addOnlyIfEmpty,
			boolean removeObsolete) {
		try {
			YamlConfiguration defaults = new YamlConfiguration();
			defaults.load(new InputStreamReader(plugin.getResource("resources/"
					+ resource)));

			process(defaults, section, addOnlyIfEmpty, removeObsolete);
			plugin.saveConfig();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Base processing method for configuration sections. Retrieves the YML
	 * defaults from the resources and adds them to the given configuration
	 * section. Removes obsolete paths that are no longer used in config.
	 * 
	 * @param defaults
	 *            the resource defaults
	 * @param section
	 *            the ConfigurationSection to update
	 * @param addOnlyIfEmpty
	 *            whether or not to add keys only if they are empty
	 * @param removeObsolete
	 *            whether or not to remove unused pathways.
	 */
	private static void process(YamlConfiguration defaults,
			ConfigurationSection section, boolean addOnlyIfEmpty,
			boolean removeObsolete) {
		Set<String> present = section.getKeys(true);
		Set<String> required = defaults.getKeys(true);
		if (!addOnlyIfEmpty || present.isEmpty()) {
			for (String req : required) {
				if (!present.remove(req)) {
					section.set(req, defaults.get(req));
				}
			}
		}
		if (removeObsolete) {
			for (String obs : present) {
				section.set(obs, null);
			}
		}
	}

	/**
	 * Returns a configuration section with a provided pathway. If the
	 * ConfigurationSection does not exist, a new one is created.
	 * 
	 * @param config
	 *            the parent section
	 * @param section
	 *            the path of the new section
	 * @return
	 */
	public static ConfigurationSection makeSection(ConfigurationSection config,
			String section) {
		if (!config.contains(section)) {
			return config.createSection(section);
		} else {
			return config.getConfigurationSection(section);
		}
	}
}
