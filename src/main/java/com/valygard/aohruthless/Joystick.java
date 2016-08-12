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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

import net.milkbowl.vault.economy.Economy;

import org.apache.commons.lang.StringUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;

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

	// logger
	private FileHandler fileHandler;

	public Economy getEconomy() {
		return econ;
	}

	public EconomyManager getEconomyManager() {
		return econManager;
	}

	@Override
	public void onEnable() {
		fileHandler = setupLogger();

		loadVault();

		init();

		reloadConfig();
		saveConfig();
	}

	@Override
	public void onDisable() {
		closeLogger();
	}

	/**
	 * Not satisfied with global logging, which can become cluttered and messy
	 * with many plugins or players using commands, etc, the Bukkit logger will
	 * log all relevant information to a log file. This log file can be found in
	 * the plugin data folder, in the subdirectory <i>logs</i>.
	 * <p>
	 * The log file, <i>joystick.log</i>, will not be overriden. The logger will
	 * simply append new information if the file already exists. All messages
	 * will be logged in the format:<br>
	 * [month-day-year hr-min-sec] (level): (message), where level is the
	 * {@link LogRecord#getLevel()} and the message is the
	 * {@link LogRecord#getMessage()}.
	 * </p>
	 * 
	 * @return the created FileHandler, null if a SecurityException or
	 *         IOException were thrown and handled.
	 */
	private FileHandler setupLogger() {
		File dir = new File(getDataFolder() + File.separator + "logs");
		dir.mkdirs();
		try {
			FileHandler handler = new FileHandler(dir + File.separator
					+ "joystick.log", true);
			getLogger().addHandler(handler);
			handler.setFormatter(new SimpleFormatter() {

				@Override
				public String format(LogRecord record) {
					Date date = new Date();
					SimpleDateFormat df = new SimpleDateFormat(
							"[MM-dd-yyyy HH:mm:ss]");
					return df.format(date)
							+ " "
							+ record.getLevel()
							+ ":"
							// org.apache.commons.lang
							+ StringUtils.replace(record.getMessage(),
									"[Joystick]", "")
							+ System.getProperty("line.separator");
				}
			});
			return handler;
		}
		catch (SecurityException | IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Closes the file handler which was setup earlier to avoid memory leaks.
	 */
	private void closeLogger() {
		if (fileHandler != null) {
			fileHandler.close();
		}
	}

	/**
	 * Serves as an initializer for member variables.
	 */
	private void init() {
		econManager = new EconomyManager(econ);

		file = new File(getDataFolder(), "config.yml");
		config = new YamlConfiguration();
	}

	/**
	 * Attempts to load vault as a dependency for economy rewards. If vault
	 * cannot be found, the logger informs the user that economy rewards will
	 * not function. If vault is found, but no valid Economy registration
	 * provided, the user is prompted.
	 */
	private void loadVault() {
		Plugin vault = getServer().getPluginManager().getPlugin("Vault");
		if (vault == null) {
			getLogger().warning(
					"Economy rewards cannot function without vault.");
			return;
		}

		ServicesManager manager = this.getServer().getServicesManager();
		RegisteredServiceProvider<Economy> e = manager
				.getRegistration(net.milkbowl.vault.economy.Economy.class);

		if (e != null) {
			econ = e.getProvider();
			getLogger().info(
					"Vault v" + vault.getDescription().getVersion()
							+ " has been found! Economy rewards enabled.");
		} else {
			getLogger()
					.warning(
							"Vault found, but no economy plugin detected ... Economy rewards will not function!");
		}
	}

	@Override
	public FileConfiguration getConfig() {
		return config;
	}

	@Override
	public void saveConfig() {
		try {
			config.save(file);
		}
		catch (IOException e) {
			getLogger().severe(
					"Could not save config.yml due to: " + e.getMessage());
		}
	}

	@Override
	public void reloadConfig() {
		if (!file.exists()) {
			saveDefaultConfig();
		}
		scanConfig();
	}

	/**
	 * Reads config file using a Scanner to search for tabs. In yaml files, the
	 * presences of tabs instead of whitespace will cause the file to reset due
	 * to snakeyaml errors. As a preventative measure, the scanner will read the
	 * file for any tabs and an IllegalArgumentException will be thrown,
	 * effectively forcing the file to remain in its current state until the
	 * user fixes the file.
	 * 
	 * @throws IllegalArgumentException
	 *             if a tab is found.
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
