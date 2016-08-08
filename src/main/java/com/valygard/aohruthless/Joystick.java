/**
 * Joystick.java is a part of Joystick
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
package com.valygard.aohruthless;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.valygard.aohruthless.messenger.JSLogger;

/**
 * @author Anand
 * 
 */
public class Joystick extends JavaPlugin {

	// config
	private File file;
	private FileConfiguration config;

	// vault
	private Economy econ;
	private EconomyManager econManager;

	public Economy getEconomy() {
		return econ;
	}

	public EconomyManager getEconomyManager() {
		return econManager;
	}

	@Override
	public void onEnable() {
		JSLogger.setLogger(this);

		loadVault();

		init();

		reloadConfig();
		saveConfig();
	}

	private void init() {
		econManager = new EconomyManager(econ);

		file = new File(getDataFolder(), "config.yml");
		config = new YamlConfiguration();
	}

	private void loadVault() {
		Plugin vault = getServer().getPluginManager().getPlugin("Vault");
		if (vault == null) {
			JSLogger.getLogger().warn(
					"Economy rewards cannot function without vault.");
			return;
		}

		ServicesManager manager = this.getServer().getServicesManager();
		RegisteredServiceProvider<Economy> e = manager
				.getRegistration(net.milkbowl.vault.economy.Economy.class);

		if (e != null) {
			econ = e.getProvider();
			JSLogger.getLogger().info(
					"Vault v" + vault.getDescription().getVersion()
							+ " has been found! Economy rewards enabled.");
		} else {
			JSLogger.getLogger()
					.warn("Vault found, but no economy plugin detected ... Economy rewards will not function!");
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Overrides default getter operation
	 * </p>
	 */
	@Override
	public FileConfiguration getConfig() {
		return config;
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Overrides the default save operation
	 * </p>
	 */
	@Override
	public void saveConfig() {
		try {
			config.save(file);
		}
		catch (IOException e) {
			// print stacktrace if you prefer
			getLogger().severe(
					"Could not save config.yml due to: " + e.getMessage());
		}
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * Override default implementation for reloading
	 * </p>
	 */
	@Override
	public void reloadConfig() {
		if (!file.exists()) {
			saveDefaultConfig();
		}
		scanConfig();
	}

	/**
	 * Scans the config file for any tabs.
	 */
	private void scanConfig() {
		// declare our scanner variable
		Scanner scan = null;
		try {
			scan = new Scanner(file);

			int row = 0;
			String line = "";

			while (scan.hasNextLine()) {
				line = scan.nextLine();
				row++;

				if (line.indexOf("\t") != -1) {
					// Tell the user where the tab is!
					String error = ("Tab found in config-file on line # " + row + "!");
					throw new IllegalArgumentException(error);
				}
			}
			/*
			 * load the file, if tabs were found then this will never execute
			 * because of IllegalArgumentException
			 */
			config.load(file);
		}
		catch (IOException | InvalidConfigurationException e) {
			e.printStackTrace();
		}
		finally {
			if (scan != null) {
				scan.close();
			}
		}
	}
}
