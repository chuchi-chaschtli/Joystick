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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.valygard.aohruthless.messenger.Messenger;
import com.valygard.aohruthless.messenger.Msg;
import com.valygard.aohruthless.utils.PermissionUtils;

/**
 * Handler to process all commands. All commands are processed through
 * annotations, and proper annotations much be appended to each class
 * declaration. The command name is common to all commands, and annotated
 * classes serve as subcommands to the main command.
 * <p>
 * Using the CommandHandler is very simple. It is merely the task of properly
 * registering subcommand classes (by using proper annotation). When a command
 * is processed, the {@code execute} method for the underlying sub command is
 * called. If something is wrong, such as a misusage or lack of permission, a
 * message is sent to the command user for easy problem resolution.
 * </p>
 * <p>
 * Methods of command classes are not handled and all helper methods and
 * subclasses will not be parsed. Similarly, commands are processed using
 * no-args constructors, so be sure not to use injectors which may violate this.
 * </p>
 * 
 * @author Anand
 * 
 */
public abstract class CommandHandler implements CommandExecutor {

	protected Plugin plugin;

	protected Map<String, Command> commands = new LinkedHashMap<>();

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

		if (first.matches("(?i)(version|plugin)")) {
			Messenger.tell(sender, Msg.CMD_VERSION, getPluginInfo());
			return true;
		}

		if (first.matches("(?i)(\\?|help)")) {
			String second = (args.length > 1 ? args[1] : "");
			if (second.matches("\\D*|-\\d*")) {
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

		// Eliminate duplicate matches by sending error message
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

		if (!execute(command, sender, params)) {
			showUsage(command, sender, true);
		}
		return false;
	}

	/**
	 * Handles command execution. The reason this is not handled internally in
	 * {@link #onCommand(CommandSender, org.bukkit.command.Command, String, String[])}
	 * is because different plugins may wish to attach extra arguments to the
	 * {@code execute} operation in each Command.
	 * 
	 * @param cmd
	 *            the Command interface, not org.bukkit.command.Command
	 * @param sender
	 *            the CommandSender
	 * @param params
	 *            the trimmed String[] args
	 * @return a boolean flag, true if the command was executed, false otherwise
	 */
	protected abstract boolean execute(Command cmd, CommandSender sender,
			String[] params);

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
	 * Grabs the main plugin information. This is the author, version, and
	 * description as defined in the plugin.yml file. Override this to remove or
	 * add extra information. Displayed the a CommandSender when the command
	 * argument is "version".
	 * 
	 * @return a String with author, version, and description info for the
	 *         {@code plugin}.
	 */
	private String getPluginInfo() {
		StringBuilder builder = new StringBuilder();
		builder.append("\n");
		builder.append(ChatColor.DARK_GREEN).append("Author: ")
				.append(ChatColor.RESET)
				.append(plugin.getDescription().getAuthors()).append("\n");
		builder.append(ChatColor.DARK_GREEN).append("Version: ")
				.append(ChatColor.RESET)
				.append(plugin.getDescription().getVersion()).append("\n");
		builder.append(ChatColor.DARK_GREEN).append("Description: ")
				.append(ChatColor.RESET)
				.append(plugin.getDescription().getDescription()).append("\n");
		return builder.toString();
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
			if (arg.matches("(?i)" + entry.getKey())) {
				result.add(entry.getValue());
			}
		}
		return result;
	}

	/**
	 * Grabs a set of commands that a given CommandSender has access to. Checks
	 * against permission values and if the sender has access to each command.
	 * 
	 * @param sender
	 *            the CommandSender to check permissions for
	 * @return a LinkedHashSet of Commands
	 */
	private Set<Command> getAllowedCommands(CommandSender sender) {
		Set<Command> result = new LinkedHashSet<>();
		for (Command cmd : commands.values()) {
			CommandPermission perm = cmd.getClass().getAnnotation(
					CommandPermission.class);
			if (PermissionUtils.has(sender, perm.value())) result.add(cmd);
		}
		return result;
	}

	/**
	 * Shows the first page of commands to a given command sender. This is
	 * called when no specified page is given by the user asking for help.
	 * 
	 * @param sender
	 *            the CommandSender
	 * @see #showHelp(CommandSender, int)
	 */
	private void showHelp(CommandSender sender) {
		showHelp(sender, 1);
	}

	/**
	 * Shows 'help', otherwise the usage and info for a command, to a command
	 * sender. Rather than display all commands at once, which could be
	 * overwhelming to the user, the help shown is paginated. There are 6
	 * commands per page and any page can be given.
	 * 
	 * @param sender
	 *            the CommandSender
	 * @param page
	 *            an integer representing which commands to show to the player.
	 */
	private void showHelp(CommandSender sender, int page) {
		Set<Command> allowed = getAllowedCommands(sender);
		int cmds = allowed.size();

		if (Math.ceil(cmds / 6.0) < page) {
			Messenger.tell(sender,
					"Given: " + page + "; Expected integer between 1 and "
							+ (int) Math.ceil(cmds / 6.0));
			return;
		}

		StringBuilder builder = new StringBuilder();
		CommandInfo info;
		CommandUsage usage;

		int counter = 0;
		for (Command cmd : allowed) {
			counter++;
			info = cmd.getClass().getAnnotation(CommandInfo.class);
			usage = cmd.getClass().getAnnotation(CommandUsage.class);

			// get to correct page
			if ((page * 6) - 5 > counter) continue;

			// break after threshold
			if (page * 6 < counter) break;

			builder.append("\n").append(ChatColor.RESET).append(usage.value())
					.append(" ").append(ChatColor.YELLOW).append(info.desc());
		}

		Messenger.tell(sender, ChatColor.DARK_GREEN + "Page " + page + ": "
				+ ChatColor.RESET + builder.toString());
	}

	/**
	 * Registers all commands using {@link #register(Class)}
	 */
	public abstract void registerCommands();

	/**
	 * Registers the commands by checking if the class implements Command and
	 * then appending it based on the command name.
	 * <p>
	 * To register a command, just call this method and invoke any class which
	 * is a child to Command.
	 * </p>
	 * 
	 * @param c
	 *            a class that implements Command
	 */
	protected void register(Class<? extends Command> c) {
		CommandInfo info = c.getAnnotation(CommandInfo.class);
		if (info == null) return;

		try {
			commands.put(info.syntax(), c.newInstance());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
