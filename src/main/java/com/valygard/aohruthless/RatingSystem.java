/**
 * RatingSystem.java is a part of Joystick
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

import org.bukkit.entity.Player;

import com.valygard.aohruthless.framework.Arena;
import com.valygard.aohruthless.framework.ArenaManager;
import com.valygard.aohruthless.player.PlayerStats;

/**
 * MMR system similar to the ELO system devised for competitive chess. MMR is
 * disabled for all arenas and can be enabled as a per-arena setting. The
 * starting-mmr and minimum-mmr is configurable. MMR for each player is stored
 * in their personal stats file.
 * <p>
 * Note that the RatingSystem cannot be initialized and is specialized for each
 * Arena implementation
 * 
 * @author Anand
 * 
 */
public abstract class RatingSystem {

	// score constants
	public final static double WIN = 1.0;
	public final static double DRAW = 0.5;
	public final static double LOSS = 0.0;

	// arena manager
	private ArenaManager manager;

	/**
	 * Constructor to rating system initializes by arenamanager
	 * 
	 * @param manager
	 */
	public RatingSystem(ArenaManager manager) {
		this.manager = manager;
	}

	/**
	 * Calculates the updated rating for a player.
	 * 
	 * @return
	 */
	public int getNewRating(Player player) {
		Arena arena = manager.getArenaWithPlayer(player);
		if (arena.getWinner() == null) {
			return getNewRating(player, DRAW);
		} else {
			if (arena.getWinner().contains(player)) {
				return getNewRating(player, WIN);
			} else {
				return getNewRating(player, LOSS);
			}
		}
	}

	/**
	 * Gets new rating.
	 * 
	 * @param player
	 *            player to update
	 * @param score
	 *            Score: 0=Loss 0.5=Draw 1.0=Win
	 * @return the new rating
	 */
	public abstract int getNewRating(Player player, double score);

	/**
	 * Calculate the new rating based on the ELO standard formula. newRating =
	 * oldRating + constant * (score - expectedScore)
	 * 
	 * @param oldRating
	 *            Old Rating
	 * @param score
	 *            Score
	 * @param expectedScore
	 *            Expected Score
	 * @param kFactor
	 *            the calculated kFactor
	 * @return the new rating of the player
	 */
	public int calculateNewRating(int oldRating, double score,
			double expectedScore, double constant) {
		int newRating = oldRating + (int) (constant * (score - expectedScore));

		// soft-cap the player's minimum mmr.
		if (newRating < manager.getConfig().getInt("global.minimum-mmr")) {
			newRating = manager.getConfig().getInt("global.minimum-mmr");
		}

		return newRating;
	}

	/**
	 * Standard constant for traditional elo systems. A player's score constant
	 * is impacted by 2 characteristics: games played and current mmr. Like in
	 * chess, rating is more volatile for newer players and less for veterans.
	 * Stronger players lose and gain less rating than weaker players do.
	 * 
	 * @param player
	 * @return
	 */
	private double getScoreConstant(Player player) {
		Arena arena = manager.getArenaWithPlayer(player);
		PlayerStats stats = arena.getStats(player);

		int rating = stats.getMMR();
		int played = stats.getGamesPlayed();

		int base = manager.getConfig().getInt("global.starting-mmr");

		if (played < 10) {
			return 0.03 * base;
		}
		if (played > 80 && rating > 1.75 * base) {
			return 0.01 * base;
		}

		if (rating > 2.5 * base) {
			return 0.01 * base;
		}
		if (rating > 2.0 * base) {
			return 0.016 * base;
		}
		if (rating > 1.5 * base) {
			return 0.0225 * base;
		}
		return 0.03 * base;
	}

	/**
	 * Get expected score based on two players. If more than two players are
	 * competing, then opponentRating will be the average of all other
	 * opponent's ratings. If there is two teams against each other, rating and
	 * opponentRating will be the average of those players.
	 * 
	 * @param rating
	 *            Rating
	 * @param opponentRating
	 *            Opponent(s) rating
	 * @return the expected score
	 */
	public double getExpectedScore(int rating, int opponentRating) {
		return 1.0 / (1.0 + Math.pow(10.0,
				((double) (opponentRating - rating) / (manager.getConfig()
						.getInt("global.starting-mmr") / 2D))));
	}
}