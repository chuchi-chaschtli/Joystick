/**
 * Arena.java is a part of Joystick
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
package com.valygard.aohruthless.framework;

import java.util.Set;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.valygard.aohruthless.ArenaClass;
import com.valygard.aohruthless.RatingSystem;
import com.valygard.aohruthless.framework.spawn.Spawnpoint;
import com.valygard.aohruthless.player.PlayerData;
import com.valygard.aohruthless.player.PlayerStats;
import com.valygard.aohruthless.utils.config.ConfigUtils;

/**
 * @author Anand
 * 
 */
public interface Arena {

	/**
	 * Grabs the underlying Plugin instance.
	 * 
	 * @return
	 */
	public Plugin getPlugin();

	/**
	 * Grabs the String identifier for the arena
	 * 
	 * @return
	 */
	public String getName();

	/**
	 * Grabs the world the arena is located in
	 * 
	 * @return
	 */
	public World getWorld();

	/**
	 * Grabs the minimum amount of players needed for an arena.
	 * 
	 * @return
	 */
	public int getMinPlayers();

	/**
	 * Grabs the maximum amount of players for an arena.
	 * 
	 * @return
	 */
	public int getMaxPlayers();

	/**
	 * Grabs the ConfigurationSection containing arena settings
	 * 
	 * @return
	 */
	public ConfigurationSection getSettings();

	/**
	 * Grabs the ConfigurationSection containing arena spawnpoints
	 * 
	 * @return
	 */
	public ConfigurationSection getWarps();

	/**
	 * Grabs a location based on the
	 * {@link ConfigUtils#deserializeLoc(ConfigurationSection, String, World)}
	 * 
	 * @param path
	 *            a String configuration pathway.
	 * @return a Location based on the given path.
	 */
	public Location getLocation(String path);

	/**
	 * Sets an important location in the warps sub-section of an arena.
	 * 
	 * @param path
	 *            the String config path to change.
	 * @param loc
	 *            the Location to be parsed and set.
	 */
	public void setLocation(String path, Location loc);

	/**
	 * Grabs location of the lobby. The lobby is where players wanting to
	 * {@link #addPlayer(Player) join} the arena (while it is not in progress)
	 * can choose their {@link #pickClass(Player, String) class} and
	 * {@link #chooseTeam(Player, String) team.}
	 * 
	 * @return a Location.
	 */
	public Spawnpoint getLobby();

	/**
	 * Changes the location of the lobby to a given Location.
	 * 
	 * @param lobby
	 *            the new Location
	 */
	public void setLobby(Location lobby);

	/**
	 * Grabs the spectator location. This is where players not on the red or
	 * blue tam can watch the match unfold.
	 * 
	 * @return a Spawnpoint
	 */
	public Spawnpoint getSpec();

	/**
	 * Changes the spectator warp to a given Location.
	 * 
	 * @param spec
	 *            the new Location
	 */
	public void setSpec(Location spec);

	/**
	 * Grabs the end warp location. The end-warp location is a spot on the map
	 * where players are teleported to after the arena finishes, and is the one
	 * optional location in any arena.
	 * 
	 * @return a Spawnpoint
	 */
	public Spawnpoint getEndWarp();

	/**
	 * Changes the end warp location to a given Location.
	 * 
	 * @param the
	 *            new Location
	 */
	public void setEndWarp(Location end);

	/**
	 * Grabs all players in the arena, whether the lobby, spectator area, or in
	 * the arena.
	 * 
	 * @return a read-only Player Set
	 */
	public Set<Player> getPlayers();

	/**
	 * Grabs all players currently playing. This will return a set reflective of
	 * both the blue and red teams.
	 * 
	 * @return a read-only Player Set.
	 */
	public Set<Player> getPlayersInArena();

	/**
	 * Grabs all queued players. Players can only be in the lobby prior to an
	 * arena starting, so while the arena is in progress, this will return an
	 * empty set.
	 * 
	 * @return a read-only Player Set.
	 */
	public Set<Player> getPlayersInLobby();

	/**
	 * Grabs all spectators of the arena. Players can only watch while the arena
	 * is in progress, so at any other time this will return an empty set.
	 * 
	 * @return a read-only Player Set.
	 */
	public Set<Player> getSpectators();

	/**
	 * Checks if the arena is in progress.
	 * 
	 * @return true if running
	 */
	public boolean isRunning();

	/**
	 * Changes the progress status.
	 * 
	 * @param running
	 *            a boolean
	 */
	public void setRunning(boolean running);

	/**
	 * Checks if the arena is enabled <i>and</i> if the main plugin is enabled.
	 * 
	 * @return true if enabled
	 */
	public boolean isEnabled();

	/**
	 * Change the arena's enabled status to a specified boolean value.
	 * 
	 * @param enabled
	 *            a boolean
	 */
	public void setEnabled(boolean enabled);

	/**
	 * Checks if the arena is ready to be used.
	 * 
	 * @return true if the arena is ready
	 */
	public boolean isReady();

	/**
	 * Updates the arena's readiness. Called by underlying {@code ArenaManager}
	 * during location setup.
	 * 
	 * @param ready
	 *            a boolean flag
	 */
	public void setReady(boolean ready);

	/**
	 * Grabs if the arena uses matchmaking rating system built by KotH
	 * 
	 * @return boolean value
	 */
	public boolean isRated();

	/**
	 * Grabs the matchmaking system
	 * 
	 * @return the matchmaking system instance
	 */
	public RatingSystem getRatingSystem();

	/**
	 * Grabs the player's statistics for the arena. In the case of IOException,
	 * an error message is logged, and the stacktrace printed.
	 * 
	 * @param p
	 *            the player
	 * @return a PlayerStats reference.
	 */
	public PlayerStats getStats(Player p);

	/**
	 * Returns a Set of all PlayerStats references currently stored in this
	 * arena.
	 * 
	 * @return a read-only set
	 */
	public Set<PlayerStats> getStats();

	/**
	 * Grabs the player's stored data model. Returns null if no data was found.
	 * 
	 * @param p
	 *            a Player
	 * @return a PlayerData instance.
	 */
	public PlayerData getData(Player p);

	/**
	 * Checks if a player's data is stored. Useful to see if the player is
	 * associated with this arena.
	 * 
	 * @param p
	 *            a Player
	 */
	public boolean hasPlayer(Player p);

	/**
	 * Grabs a player's class for the arena.
	 * 
	 * @param p
	 *            a player
	 * @return an ArenaClass, null if none is found.
	 */
	public ArenaClass getClass(Player p);

	/**
	 * Sets the arena class of a player in the lobby. If no class has been
	 * selected, this is called for all undecided players when the arena begins.
	 * 
	 * @param p
	 *            the player
	 * @param arenaClass
	 *            an ArenaClass instance
	 */
	public void setArenaClass(Player p, ArenaClass arenaClass);

	/**
	 * Grabs a read-only Set of Players who won the arena. In free for all
	 * arenas, this will be a set of size one.
	 * 
	 * @return a read-only Player Set.
	 */
	public Set<Player> getWinner();

	/**
	 * Grabs the start timer which automatically begins the arena after a
	 * configurable period of time.
	 * 
	 * @return an AutoStartTimer instance.
	 */
	// public AutoStartTimer getStartTimer();

	/**
	 * Grabs the end timer which calculates the remaining length of the arena
	 * 
	 * @return an AutoEndTimer instance.
	 */
	// public AutoEndTimer getEndTimer();

	/**
	 * Grabs the time for which the arena will run.
	 * 
	 * @return an Integer.
	 */
	public int getLength();

	/**
	 * Determines if a given Player is in the lobby
	 * 
	 * @param p
	 *            the Player to check
	 * @return true if the player is part of the lobby
	 */
	public boolean inLobby(Player p);

	/**
	 * Determines if a given Player is spectating the arena
	 * 
	 * @param p
	 *            the Player to check
	 * @return true if the player is spectating
	 */
	public boolean isSpectating(Player p);

	/**
	 * Adds a player to the arena. A series of checks are called before the
	 * player is allowed to join the arena. If the {@code ArenaPlayerJoinEvent}
	 * is cancelled, the player is barred from joining the match.
	 * 
	 * @param p
	 *            the player
	 */
	public void addPlayer(Player p);

	/**
	 * Removes a player from the arena. Called when either the arena ends or
	 * when the player leaves. The underlying {@code ArenaPlayerLeaveEvent}
	 * cannot be cancelled, because this removes essential data from being
	 * cleaned up and puts the player in an awkward state.
	 * 
	 * @param p
	 *            the player
	 * @param end
	 *            checks if the arena has been terminated.
	 */
	public void removePlayer(Player p, boolean end);

	/**
	 * Attempts to begin the arena. The arena must be in its 'lobby phase' or it
	 * cannot be started. If the {@code ArenaStartEvent} is cancelled, the arena
	 * will not begin.
	 * 
	 * @return true if the arena started; false otherwise
	 */
	public boolean startArena();

	/**
	 * End a running arena and cleanup after. The underlying
	 * {@code ArenaEndEvent} cannot be cancelled, as it puts the status of the
	 * arena in an awkward state. The event is called before the winner is
	 * determined.
	 * 
	 * @return true if the arena successfully ended; false otherwise
	 */
	public boolean endArena();

	/**
	 * Force an arena to begin and manually stops the ticking start timer.
	 */
	public void forceStart();

	/**
	 * Forces the arena to end and manually stops the ticking end timer.
	 */
	public void forceEnd();

	/**
	 * Kicks a player from the arena. Generally called during attempted cheats,
	 * and inherently calls {@code removePlayer(Player, boolean)}, with false
	 * justification to indicate the arena has not been ended.
	 * 
	 * @param p
	 *            the Player
	 * @return true if the player was kicked, false if the underlying
	 *         {@code ArenaPlayerKickEvent} was cancelled.
	 */
	public boolean kickPlayer(Player p);

	/**
	 * Called every second to determine if the winning score has been reached.
	 * The arena is subsequently ended and a winner is declared.
	 * 
	 * @return true if the score to win has been reached.
	 */
	public boolean scoreReached();

	/**
	 * Declares a winner for the arena
	 */
	void declareWinner();

	/**
	 * Moves a player to the spectator location and updates them to spectator
	 * status.
	 * 
	 * @param p
	 *            a Player now spectator of the arena.
	 */
	public void moveToSpec(Player p);

	/**
	 * Gives a player a given ArenaClass object as their kit for the arena.
	 * 
	 * @param p
	 *            the player who chose a class
	 * @param arenaClass
	 *            the chosen class
	 */
	public void pickClass(Player p, ArenaClass arenaClass);

	/**
	 * Compile the different classes, in which one is randomly selected. While
	 * the player does not have permission for a class, pull out another one. If
	 * they don't have access to any classes, remove them from the arena.
	 * 
	 * @param p
	 *            a player to give a class
	 */
	public void giveRandomClass(Player p);

	/**
	 * Convenience method for scheduling delayed tasks
	 * 
	 * @param r
	 *            a Runnable instance
	 * @param delay
	 *            a long timer delay in server ticks
	 */
	void schedule(Runnable r, long delay);
}
