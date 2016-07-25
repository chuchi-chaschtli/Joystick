/**
 * CommandHandler.java is a part of Joystick
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
package com.valygard.aohruthless.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.valygard.aohruthless.messenger.JSLogger;
import com.valygard.aohruthless.messenger.Messenger;
import com.valygard.aohruthless.messenger.Msg;
import com.valygard.aohruthless.utils.PermissionUtils;

/**
 * @author Anand
 * 
 */
public class CommandHandler implements CommandExecutor {

	private Plugin plugin;

	private Map<String, Command> commands;

	/**
	 * Constructor for Joystick command manager initializes by main class
	 * instance
	 * 
	 * @param plugin
	 *            main class instance
	 */
	public CommandHandler(Plugin plugin) {
		this.plugin = plugin;

		registerCommands();
	}

	@Override
	public boolean onCommand(CommandSender sender,
			org.bukkit.command.Command cmd, String commandLabel, String[] args) {
		String first = (args.length > 0 ? args[0] : "");
		String last = (args.length > 0 ? args[args.length - 1] : "");

		if (first.toLowerCase().startsWith("ver")) {
			StringBuilder foo = new StringBuilder();
			foo.append("\n");
			foo.append(ChatColor.DARK_GREEN).append("Author: ")
					.append(ChatColor.RESET)
					.append(plugin.getDescription().getAuthors()).append("\n");
			foo.append(ChatColor.DARK_GREEN).append("Version: ")
					.append(ChatColor.RESET)
					.append(plugin.getDescription().getVersion()).append("\n");
			foo.append(ChatColor.DARK_GREEN).append("Description: ")
					.append(ChatColor.RESET)
					.append(plugin.getDescription().getDescription())
					.append("\n");
			Messenger.tell(sender, Msg.CMD_VERSION, foo.toString());
			return true;
		}

		String second = (args.length > 1 ? args[1] : "");

		if (first.equals("?") || first.equalsIgnoreCase("help")) {
			JSLogger.getLogger().info(
					sender.getName() + " has used command: /joystick help",
					false);
			if (!second.matches("\\d")) {
				showHelp(sender);
				return true;
			}

			if (second.equals("") || Integer.parseInt(second) <= 1) {
				showHelp(sender);
				return true;
			}

			showHelp(sender, Integer.parseInt(second));
			return true;
		}

		if (last.equals("")) {
			Messenger.tell(sender, Msg.CMD_HELP);
			return true;
		}

		List<Command> matches = getMatchingCommands(first);

		// Because we use regex patterns for the command arguments, we need to
		// make sure that there are no conflicting matches.
		if (matches.size() > 1) {
			Messenger.tell(sender, Msg.CMD_MULTIPLE_MATCHES);
			for (Command command : matches) {
				showUsage(command, sender, false);
			}
			return true;
		}

		if (matches.size() == 0) {
			Messenger.tell(sender, Msg.CMD_NO_MATCHES);
			return true;
		}

		Command command = matches.get(0);
		CommandPermission perm = command.getClass().getAnnotation(
				CommandPermission.class);
		CommandInfo info = command.getClass().getAnnotation(CommandInfo.class);

		if (info.playerOnly() && !(sender instanceof Player)) {
			Messenger.tell(sender, Msg.CMD_NOT_FROM_CONSOLE);
			return true;
		}

		if (last.equals("?") || last.equals("help")) {
			showUsage(command, sender, false);
			return true;
		}

		if (!PermissionUtils.has(sender, perm.value())) {
			Messenger.tell(sender, Msg.CMD_NO_PERMISSION);
			return true;
		}

		String[] params = trimFirstArg(args);

		if (params.length < info.argsRequired()) {
			Messenger.tell(sender, Msg.CMD_NOT_ENOUGH_ARGS);
			showUsage(command, sender, true);
			return true;
		}

		if (!command.execute(sender, params)) {
			showUsage(command, sender, true);
		}

		JSLogger.getLogger().info(
				sender.getName() + " has used command: /joystick "
						+ info.name(), false);
		return false;
	}

	/**
	 * Trims the first argument, which eventually becomes the command name.
	 * 
	 * @param args
	 *            the arguments to trim.
	 * @return the new String array.
	 */
	private String[] trimFirstArg(String[] args) {
		return Arrays.copyOfRange(args, 1, args.length);
	}

	/**
	 * Shows the usage information of a command to a sender upon incorrect usage
	 * or when assistance is requested.
	 * 
	 * @param command
	 *            the Command given
	 * @param sender
	 *            a CommandSender
	 * @param prefix
	 *            a boolean: if true, we attach "Usage : " before the usage.
	 */
	private void showUsage(Command command, CommandSender sender, boolean prefix) {
		CommandInfo info = command.getClass().getAnnotation(CommandInfo.class);
		CommandPermission perm = command.getClass().getAnnotation(
				CommandPermission.class);
		CommandUsage usage = command.getClass().getAnnotation(
				CommandUsage.class);

		if (!PermissionUtils.has(sender, perm.value())) return;

		JSLogger.getLogger().info(
				sender.getName()
						+ " has triggered usage for command: /joystick "
						+ info.name(), false);

		sender.sendMessage((prefix ? "Usage: " : "") + usage.value() + " "
				+ ChatColor.YELLOW + info.desc());
	}

	/**
	 * Gets a list of commands matching a string given. Because the command
	 * system uses regex patterns rather than the conventional
	 * {@link #equals(Object)}, this helps ensures a command sent has no
	 * conflicting commands.
	 * 
	 * @param arg
	 *            a string representing the first argument in a command
	 * @return a list of matching commands.
	 */
	private List<Command> getMatchingCommands(String arg) {
		List<Command> result = new ArrayList<Command>();
		for (Entry<String, Command> entry : commands.entrySet()) {
			if (arg.toLowerCase().matches(entry.getKey())) {
				result.add(entry.getValue());
			}
		}

		return result;
	}

	/**
	 * Show the first page of helpful commands.
	 * 
	 * @param sender
	 *            the CommandSender
	 * @see #showHelp(CommandSender, int)
	 */
	private void showHelp(CommandSender sender) {
		showHelp(sender, 1);
	}

	/**
	 * Because there are so many commands, this method shows helpful information
	 * in a paginated fashion so as not to spam chat.
	 * 
	 * @param sender
	 *            the CommandSender
	 * @param page
	 *            an integer representing which commands to show to the player.
	 */
	private void showHelp(CommandSender sender, int page) {
		// Amount of commands
		int cmds = 0;
		for (Command cmd : commands.values()) {
			CommandPermission perm = cmd.getClass().getAnnotation(
					CommandPermission.class);

			if (PermissionUtils.has(sender, perm.value())) cmds++;
		}

		// Tell the sender if they asked for a page that was too high.
		if (Math.ceil(cmds / 6.0) < page) {
			Messenger.tell(sender,
					"Given: " + page + "; Expected integer between 1 and "
							+ (int) Math.ceil(cmds / 6.0));
			return;
		}

		StringBuilder builder = new StringBuilder();
		int number = 0;

		for (Command cmd : commands.values()) {
			number++;
			CommandInfo info = cmd.getClass().getAnnotation(CommandInfo.class);
			CommandPermission perm = cmd.getClass().getAnnotation(
					CommandPermission.class);
			CommandUsage usage = cmd.getClass().getAnnotation(
					CommandUsage.class);
			if (!PermissionUtils.has(sender, perm.value())) continue;

			// Make sure we are on the right page.
			if ((page * 6) - 5 > number) continue;

			// Break to make sure we don't have too many commands.
			if (page * 6 < number) break;

			// Append the command and it's information
			builder.append("\n").append(ChatColor.RESET).append(usage.value())
					.append(" ").append(ChatColor.YELLOW).append(info.desc());
		}

		Messenger.tell(sender, ChatColor.DARK_GREEN + "Page " + page + ": "
				+ ChatColor.RESET + builder.toString());
	}

	/**
	 * Registers all commands using {@code #register(Class)}
	 */
	private void registerCommands() {
		commands = new LinkedHashMap<String, Command>();

		// TODO: Add commands
	}

	/**
	 * Registers the commands by checking if the class implements Command and
	 * then appending it based on the command name.
	 * 
	 * @param c
	 *            a class that implements Command
	 */
	private void register(Class<? extends Command> c) {
		CommandInfo info = c.getAnnotation(CommandInfo.class);
		if (info == null) return;

		try {
			commands.put(info.pattern(), c.newInstance());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
