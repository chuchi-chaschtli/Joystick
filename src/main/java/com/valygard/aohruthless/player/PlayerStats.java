/**
 * PlayerStats.java is a part of Joystick
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
package com.valygard.aohruthless.player;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.valygard.aohruthless.framework.Arena;
import com.valygard.aohruthless.timer.Conversion;
import com.valygard.aohruthless.utils.config.JsonConfiguration;

/**
 * @author Anand
 * 
 */
@SuppressWarnings("unchecked")
public class PlayerStats {

	// The player
	private final Player player;

	// The arena the stats are from.
	private final Arena arena;
	private final String name;

	// Their kills, deaths, wins, losses, and ties.
	private int kills, deaths;
	private int wins, losses, draws;

	// Killstreak and winstreak
	private int killstreak, winstreak;

	// kill-death ratio, win-loss ratio.
	private double kdr, wlr;

	// Time spent in the arena
	private int timespent;
	private BukkitTask task;

	// Directory where stats are stored.
	private File dir;

	// config
	private JsonConfiguration config;
	private JSONObject arenaContents, classData;
	private JSONArray arenaArray;

	// The player's matchmaking rating. This is not used in this class but is
	// setup by the player's configuration file.
	private int mmr;

	// Only track stats if enabled in the arena-settings.
	private boolean tracking;

	/**
	 * The Constructor requires a player and an arena parameter.
	 * 
	 * @param player
	 * @param arena
	 * @throws IOException
	 */
	public PlayerStats(Player player, Arena arena) throws IOException {
		this.player = player;
		this.arena = arena;
		this.name = arena.getName();

		this.tracking = arena.getSettings().getBoolean("player-stats");

		this.dir = new File(arena.getPlugin().getDataFolder(), "stats");
		this.dir.mkdir();

		// Go no further if the arena is not meant to track results.
		if (!tracking) {
			return;
		}

		// create JSON disk file
		this.config = new JsonConfiguration(dir, player.getUniqueId()
				.toString());

		this.arenaArray = (JSONArray) config.getValue("_" + name);
		this.arenaContents = new JSONObject();
		this.classData = new JSONObject();

		// update initialization based on file status
		if (arenaArray == null) {
			arenaArray = new JSONArray();
		} else {
			arenaContents = (JSONObject) arenaArray.get(0);
			if (arenaArray.size() > 1) {
				classData = (JSONObject) arenaArray.get(1);
			}
		}

		this.kills = parseValue("kills");
		this.deaths = parseValue("deaths");

		this.wins = parseValue("wins");
		this.losses = parseValue("losses");
		this.draws = parseValue("draws");

		this.kdr = calculateRatio(kills, deaths);
		this.wlr = calculateRatio(wins, draws + losses);

		this.killstreak = parseValue("killstreak");
		this.winstreak = parseValue("winstreak");

		this.timespent = parseValue("timeSpent");

		config.writeString("playerName", player.getName());
		if (config.getValue("mmr") == null) {
			config.writeInt(
					"mmr",
					arena.getPlugin().getConfig()
							.getInt("global.starting-mmr", 1000));
		}
		this.mmr = (int) config.getValue("mmr");

		config.writeObject("_" + name, reload());
	}

	/**
	 * Reloads disk file
	 * 
	 * @return the updated JSONArray
	 */
	private JSONArray reload() {
		arenaContents.clear();
		arenaContents.put("kills", kills);
		arenaContents.put("deaths", deaths);
		arenaContents.put("killDeathRatio", kdr);
		arenaContents.put("wins", wins);
		arenaContents.put("losses", losses);
		arenaContents.put("draws", draws);
		arenaContents.put("winRatio", wlr);
		arenaContents.put("killstreak", killstreak);
		arenaContents.put("winstreak", winstreak);
		arenaContents.put("timeSpent", timespent);

		// TODO: reload class data
		if (arenaArray.size() < 1) {
			arenaArray.add(arenaContents);
		} else {
			arenaArray.set(0, arenaContents);
		}
		return arenaArray;
	}

	/**
	 * Convenience method to assist integer parsing
	 * 
	 * @param key
	 * @return
	 */
	private int parseValue(String key) {
		Object obj = config.getValue(arenaContents.get(key));
		if (obj == null || !(obj instanceof Integer)) {
			return 0;
		}
		return (int) obj;
	}

	/**
	 * Grabs player's MMR
	 * 
	 * @return
	 */
	public int getMMR() {
		return mmr;
	}

	/**
	 * Updates MMR, actual calculations not done in this class.
	 * 
	 * @param value
	 */
	public void setMMR(int value) {
		mmr = value;
		config.writeInt("mmr", value);
	}

	/**
	 * Resets killstreak and writes changes to config.
	 */
	public void resetKillstreak() {
		killstreak = 0;
		arenaContents.put("killstreak", 0);
		arenaArray.set(0, arenaContents);
		config.writeObject("_" + name, arenaArray);
	}

	/**
	 * Resets winstreak and writes changes to config.
	 */
	public void resetWinstreak() {
		winstreak = 0;
		arenaContents.put("winstreak", 0);
		arenaArray.set(0, arenaContents);
		config.writeObject("_" + name, arenaArray);
	}

	/**
	 * Whenever a player gets a kill, win, loss, draw, or death, we increment
	 * that individual setting and save it to the config. Other values must be
	 * changed accordingly (i.e, when a player dies their deaths is incremented
	 * and killstreak is 0)
	 * 
	 * @param key
	 *            the value type to increment
	 */
	public void evaluate(String key) {
		if (!tracking) {
			return;
		}

		switch (key) {
		case "kills":
			kills += 1;
			killstreak += 1;

			arenaContents.put("kills", kills);
			arenaContents.put("killstreak", killstreak);
			break;
		case "deaths":
			deaths += 1;
			killstreak = 0;

			arenaContents.put("deaths", deaths);
			arenaContents.put("killstreak", killstreak);
			break;
		case "wins":
			wins += 1;
			winstreak += 1;

			arenaContents.put("wins", wins);
			arenaContents.put("winstreak", winstreak);
			break;
		case "losses":
			losses += 1;
			winstreak = 0;

			arenaContents.put("losses", losses);
			arenaContents.put("winstreak", winstreak);
			break;
		case "draws":
			draws += 1;
			winstreak = 0;

			arenaContents.put("draws", draws);
			arenaContents.put("winstreak", winstreak);
			break;
		default:
			throw new IllegalArgumentException(
					"Expected: kills, deaths, wins, losses, or draws");
		}
		// Recalculate the ratios of the kdr and wlr.
		recalibrate();
		// write changes to file
		arenaArray.set(0, arenaContents);
		config.writeObject("_" + name, arenaArray);
	}

	/**
	 * Calculates the ratio of two integers. If the divisor is less than or
	 * equal to 1, the dividend is the ratio returned. If the dividend divided
	 * by the divisor equates to a negative integer, 0.00 is returned.
	 * 
	 * @param dividend
	 *            the int to divide
	 * @param divisor
	 *            the int to divide by
	 * @return a double quotient
	 */
	public double calculateRatio(int dividend, int divisor) {
		if (divisor <= 1) return dividend * 1D;
		if (dividend / divisor < 0) return 0D;
		DecimalFormat df = new DecimalFormat("#.###");
		return Double.valueOf(df.format(dividend / (divisor * 1.0)));
	}

	/**
	 * Helper method to reevaluate {@code kdr} and {@code wlr}
	 */
	private void recalibrate() {
		kdr = calculateRatio(kills, deaths);
		arenaContents.put("killDeathRatio", kdr);

		wlr = calculateRatio(wins, draws + losses);
		arenaContents.put("winRatio", wlr);
	}

	/**
	 * Increments time played by appropriate amount. Called every 20 ticks.
	 * <p>
	 * Note that changes to config are not written until the timer is
	 * terminated. Only the time-cycle in memory is updated every second, with
	 * changes to config being written after the conclusion of the arena.
	 */
	public void startTiming(Plugin plugin) {
		if (!tracking) return;

		task = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {

			public void run() {
				timespent++;
				if (!arena.isRunning()) {
					task.cancel();
					arenaContents.put("timeSpent", timespent);
					arenaArray.set(0, arenaContents);
					config.writeObject("_" + name, arenaArray);
					return;
				}
			}
		}, 20l, 20l);
	}

	// TODO: Accurately handle class data and document
	public void collectClassData() {}

	public Player getPlayer() {
		return player;
	}

	public Arena getArena() {
		return arena;
	}

	public int getKills() {
		return kills;
	}

	public int getDeaths() {
		return deaths;
	}

	public int getWins() {
		return wins;
	}

	public int getLosses() {
		return losses;
	}

	public int getDraws() {
		return draws;
	}

	public int getGamesPlayed() {
		return wins + losses + draws;
	}

	public double getKDR() {
		return kdr;
	}

	public double getWLR() {
		return wlr;
	}

	public int getKillstreak() {
		return killstreak;
	}

	public int getWinstreak() {
		return winstreak;
	}

	public int getRawTimeSpent() {
		return timespent;
	}

	public String getTimeSpent() {
		return Conversion.formatIntoSentence(timespent);
	}

	public boolean isTracking() {
		return tracking;
	}

	public void setTracking(boolean value) {
		tracking = value;
	}
}
