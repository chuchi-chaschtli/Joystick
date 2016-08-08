/**
 * ArenaTemplate.java is a part of Joystick
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

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.valygard.aohruthless.ArenaClass;
import com.valygard.aohruthless.RatingSystem;
import com.valygard.aohruthless.framework.spawn.Spawn;
import com.valygard.aohruthless.framework.spawn.Spawnpoint;
import com.valygard.aohruthless.messenger.JSLogger;
import com.valygard.aohruthless.messenger.Messenger;
import com.valygard.aohruthless.player.PlayerData;
import com.valygard.aohruthless.player.PlayerStats;
import com.valygard.aohruthless.utils.config.LocationSerializer;
import com.valygard.aohruthless.utils.inventory.InventoryUtils;

/**
 * @author Anand
 * 
 */
public abstract class ArenaTemplate implements Arena {

	// general attributes
	private Plugin plugin;
	private String arenaName;
	private World world;

	// configuration-important
	private FileConfiguration config;
	private ConfigurationSection settings, warps;

	// player count
	private int maxPlayers, minPlayers;

	// locations
	private Spawnpoint lobby, spec, end;

	// player collections
	private Set<Player> arenaPlayers, lobbyPlayers, specPlayers;

	// arena flags
	private boolean running, enabled, ready;

	// TODO: implement timers
	// private AutoStartTimer startTimer;
	// private AutoEndTimer endTimer;

	// player-related data
	private Set<PlayerData> data;
	private Set<PlayerStats> stats;

	/**
	 * Constructor initializes arenas through a provided plugin instance and a
	 * String arenaName, which serves as a unique identifier.
	 * <p>
	 * This constructor only provides the backbone for the ArenaTemplate. In all
	 * Arena child classes, valid instances of timers, economy/vault handlers,
	 * and inventory handlers must be instantiated.
	 * 
	 * @param plugin
	 *            the Plugin instance
	 * @param arenaName
	 *            a String identifier
	 */
	public ArenaTemplate(Plugin plugin, String arenaName) {
		// General stuff
		this.plugin = plugin;
		this.arenaName = arenaName;

		// Settings from config
		this.config = plugin.getConfig();
		this.settings = config.getConfigurationSection("arenas." + arenaName
				+ ".settings");
		this.warps = config.getConfigurationSection("arenas." + arenaName
				+ ".warps");

		this.world = Bukkit.getWorld(settings.getString("world"));

		Validate.notNull(world, "Error! World '" + settings.getString("world")
				+ "' does not exist!");

		this.minPlayers = settings.getInt("min-players");
		this.maxPlayers = settings.getInt("max-players");

		this.arenaPlayers = new HashSet<>();
		this.lobbyPlayers = new HashSet<>();
		this.specPlayers = new HashSet<>();

		this.running = false;
		this.enabled = settings.getBoolean("enabled", true);
		this.ready = false;
	}

	@Override
	public Plugin getPlugin() {
		return plugin;
	}

	@Override
	public String getName() {
		return arenaName;
	}

	@Override
	public World getWorld() {
		return world;
	}

	@Override
	public int getMinPlayers() {
		return minPlayers;
	}

	@Override
	public int getMaxPlayers() {
		return maxPlayers;
	}

	@Override
	public ConfigurationSection getSettings() {
		return settings;
	}

	@Override
	public ConfigurationSection getWarps() {
		return warps;
	}

	@Override
	public Location getLocation(String path) {
		return LocationSerializer.deserialize(warps, path, world);
	}

	@Override
	public void setLocation(String path, Location loc) {
		LocationSerializer.serialize(warps, path, loc);
	}

	@Override
	public Spawnpoint getLobby() {
		if (lobby == null || lobby.getSpawnType() != Spawn.LOBBY) {
			lobby = new Spawnpoint(Spawn.LOBBY, getLocation("lobby"));
		}
		return lobby;
	}

	@Override
	public void setLobby(Location lobby) {
		this.lobby = new Spawnpoint(Spawn.LOBBY, lobby);
	}

	@Override
	public Spawnpoint getSpec() {
		if (spec == null || spec.getSpawnType() != Spawn.SPEC) {
			spec = new Spawnpoint(Spawn.SPEC, getLocation("spec"));
		}
		return spec;
	}

	@Override
	public void setSpec(Location spec) {
		this.spec = new Spawnpoint(Spawn.SPEC, spec);
	}

	@Override
	public Spawnpoint getEndWarp() {
		if (end == null || end.getSpawnType() != Spawn.END) {
			end = new Spawnpoint(Spawn.END, getLocation("end"));
		}
		return end;
	}

	@Override
	public void setEndWarp(Location end) {
		this.end = new Spawnpoint(Spawn.END, end);
	}

	@Override
	public Set<Player> getPlayers() {
		Set<Player> result = new HashSet<>();
		result.addAll(specPlayers);
		result.addAll(arenaPlayers);
		result.addAll(lobbyPlayers);
		return Collections.unmodifiableSet(result);
	}

	@Override
	public Set<Player> getPlayersInArena() {
		return Collections.unmodifiableSet(arenaPlayers);
	}

	@Override
	public Set<Player> getPlayersInLobby() {
		return Collections.unmodifiableSet(lobbyPlayers);
	}

	@Override
	public Set<Player> getSpectators() {
		return Collections.unmodifiableSet(specPlayers);
	}

	@Override
	public boolean isRunning() {
		return running;
	}

	@Override
	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public boolean isEnabled() {
		return enabled && plugin.getConfig().getBoolean("global.enabled");
	}

	@Override
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public boolean isReady() {
		ready = !(spec == null || lobby == null) && enabled;

		ready = !(settings.getBoolean("teleport-to-end") && end == null);

		return ready;
	}

	@Override
	public void setReady(boolean ready) {
		this.ready = ready;
	}

	@Override
	public boolean isRated() {
		return settings.getBoolean("enable-matchmaking-system");
	}

	@Override
	public abstract RatingSystem getRatingSystem();

	@Override
	public PlayerStats getStats(Player p) {
		for (PlayerStats ps : stats) {
			if (ps.getPlayer().equals(p)) {
				return ps;
			}
		}

		try {
			PlayerStats stat = new PlayerStats(p, this);
			stats.add(stat);
			return stat;
		}
		catch (IOException e) {
			JSLogger.getLogger().warn(
					"Could not get the stats of player '" + p.getName() + "'!");
			e.printStackTrace();
			return null;
		}
	}

	@Override
	public Set<PlayerStats> getStats() {
		return Collections.unmodifiableSet(stats);
	}

	@Override
	public PlayerData getData(Player p) {
		for (PlayerData pd : data) {
			if (pd.getPlayer().equals(p)) return pd;
		}
		return null;
	}

	@Override
	public boolean hasPlayer(Player p) {
		return getPlayers().contains(p);
	}

	@Override
	public ArenaClass getClass(Player p) {
		PlayerData data = getData(p);
		return data.getArenaClass();
	}

	@Override
	public void setArenaClass(Player p, ArenaClass arenaClass) {
		PlayerData data = getData(p);
		data.setArenaClass(arenaClass);
	}

	@Override
	public abstract Set<Player> getWinner();

	@Override
	public int getLength() {
		return settings.getInt("arena-time");
	}

	@Override
	public boolean inLobby(Player p) {
		return lobbyPlayers.contains(p);
	}

	@Override
	public boolean isSpectating(Player p) {
		return specPlayers.contains(p);
	}

	@Override
	public abstract void addPlayer(Player p);

	@Override
	public abstract void removePlayer(Player p, boolean end);

	@Override
	public abstract boolean startArena();

	@Override
	public abstract boolean endArena();

	@Override
	public void forceStart() {
		startArena();
		// TODO: stop start timer
	}

	@Override
	public void forceEnd() {
		endArena();
		// TODO: stop end timer
	}

	@Override
	public boolean kickPlayer(Player p) {
		removePlayer(p, false);
		p.kickPlayer("BANNED FOR LIFE! No but seriously, don't cheat again");
		Messenger.announce(this, p.getName() + " has been caught cheating!");
		return true;
	}

	@Override
	public abstract boolean scoreReached();

	@Override
	public abstract void declareWinner();

	@Override
	public abstract void moveToSpec(Player p);

	@Override
	public void pickClass(Player p, ArenaClass arenaClass) {
		if (arenaClass == null) {
			return;
		}

		if (!settings.getBoolean("change-class-in-arena")
				&& arenaPlayers.contains(p)) {
			return;
		}

		if (!hasPlayer(p)) {
			return;
		}

		InventoryUtils.clearInventory(p);
		setArenaClass(p, arenaClass);
		arenaClass.giveItems(p);
	}

	@Override
	public abstract void giveRandomClass(Player p);

	@Override
	public void schedule(Runnable r, long delay) {
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, r, delay);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + arenaName.hashCode();
		result = prime * result + ((world == null) ? 0 : world.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;

		// Arenas are equal if they have same name identifier
		if (obj instanceof ArenaTemplate
				&& ((ArenaTemplate) obj).arenaName.equals(arenaName))
			return true;

		return false;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Arena [arenaName=");
		builder.append(arenaName);
		builder.append(", world=");
		builder.append(world);
		builder.append(", running=");
		builder.append(running);
		builder.append(", enabled=");
		builder.append(enabled);
		builder.append(", ready=");
		builder.append(ready);
		builder.append("]");
		return builder.toString();
	}
}
