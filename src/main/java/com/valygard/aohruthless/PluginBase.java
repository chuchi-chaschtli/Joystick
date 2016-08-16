/**
 * PluginBase.java is a part of Joystick
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
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;

import com.valygard.aohruthless.messenger.Messenger;

/**
 * Provides a base for all Joystick plugins.
 * 
 * @author Anand
 * 
 */
public interface PluginBase {

	public Economy getEconomy();

	public EconomyManager getEconomyManager();

	public Messenger getMessenger();

	public FileConfiguration getConfig();

	public void saveConfig();

	public void reloadConfig();

	public PluginDescriptionFile getDescription();
	
	public Logger getLogger();
	
	public File getDataFolder();
}
