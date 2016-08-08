/**
 * CooldownAbility.java is a part of Joystick
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
package com.valygard.aohruthless.ability;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.valygard.aohruthless.messenger.Messenger;
import com.valygard.aohruthless.messenger.Msg;

/**
 * Cooldown Abilities are subtypes of generic Abilities. While similar in
 * structure, they function to restrict the user on a timer to prevent ability
 * spamming and attempt to keep the game fair. Alternatively, server
 * administrators can restrict the user by giving them less ability items, but
 * adding cooldown possibilities is a much more fun method.
 * <p>
 * Cooldowns are handled using {@link System#currentTimeMillis()} to compare
 * timings. This is a much more dynamic and simple pattern than using Runnables.
 * </p>
 * <p>
 * Cooldowns are self-contained. When the ability is used the cooldown is
 * started. Everytime the player tries to re-use the ability, they must have
 * passed the cooldown threshold or they are prompted saying they may not use
 * the ability.
 * </p>
 * 
 * @author Anand
 * 
 */
public abstract class CooldownAbility extends Ability {

	protected final Map<Player, Long> cooldowns;
	protected final int cooldown;

	/**
	 * CooldownAbility constructor requires five basic parameters. A plugin
	 * instance to register events, a unique string {@code name} identifer, a
	 * permission attachment, a unique material type for ability usage, and an
	 * integer {@code cooldown} in seconds.
	 * 
	 * @param plugin
	 *            the underlying Plugin instance
	 * @param name
	 *            the String name
	 * @param perm
	 *            the String permission attachment
	 * @param material
	 *            the unique material enum type
	 * @param cooldown
	 *            the int cooldown in seconds between successive ability uses
	 */
	protected CooldownAbility(Plugin plugin, String name, String perm,
			Material material, int cooldown) {
		super(plugin, name, perm, material);

		this.cooldowns = new HashMap<>();
		this.cooldown = cooldown;
	}

	/**
	 * Grabs the {@code cooldown} in seconds between successive ability uses
	 * 
	 * @return an integer cooldown
	 */
	public int getCooldown() {
		return cooldown;
	}

	/**
	 * Adds a specific {@code player} to the cooldown mapping with the current
	 * time
	 * 
	 * @param player
	 *            the Player to start tracking
	 */
	private void startCooldown(Player player) {
		if (!cooldowns.containsKey(player))
			cooldowns.put(player, System.currentTimeMillis());
	}

	/**
	 * Checks if a given {@code player} is contained by the cooldown mapping. If
	 * they are in the cooldown but their time has 'expired', that is, they are
	 * overdue for removal, they are subsequently removed from the mapping.
	 * 
	 * @param player
	 *            the Player to check if on cooldown
	 * @return true if the player is on cooldown and should be, false otherwise
	 */
	protected boolean onCooldown(Player player) {
		if (!cooldowns.containsKey(player)) {
			return false;
		}
		if (cooldowns.get(player) < System.currentTimeMillis() - cooldown
				* 1000l) {
			return true;
		} else {
			cooldowns.remove(player);
			return false;
		}
	}

	/**
	 * Clears all cooldowns. Used in arena cleanup
	 */
	public void clearCooldowns() {
		cooldowns.clear();
	}

	/**
	 * {@inheritDoc}
	 * <p>
	 * As an additional check, if the player is on cooldown then they are
	 * notified with a message
	 * </p>
	 */
	@Override
	public boolean onCheck(Player player) {
		if (onCooldown(player)) {
			double diff = cooldown
					- ((System.currentTimeMillis() - cooldowns.get(player)) / 1000l);
			Messenger.tell(player, Msg.ABILITY_COOLDOWN,
					String.format("%.2f", diff));
			return false;
		}
		startCooldown(player);
		return super.onCheck(player);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + cooldown;
		return result;
	}

}