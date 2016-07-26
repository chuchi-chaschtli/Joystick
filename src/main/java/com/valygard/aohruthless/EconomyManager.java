/**
 * EconomyManager.java is a part of Joystick
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

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Anand
 * 
 */
public class EconomyManager {
	private final Economy econ;

	public EconomyManager(Economy econ) {
		this.econ = econ;
	}

	public boolean deposit(Player p, ItemStack item) {
		if (econ != null) {
			EconomyResponse result = econ.depositPlayer(p,
					getAmount(item));
			return (result.type == ResponseType.SUCCESS);
		}
		return false;
	}

	public boolean withdraw(Player p, ItemStack item) {
		return withdraw(p, getAmount(item));
	}

	public boolean withdraw(Player p, double amount) {
		if (econ != null) {
			EconomyResponse result = econ.withdrawPlayer(p,
					amount);
			return (result.type == ResponseType.SUCCESS);
		}
		return false;
	}

	public boolean hasEnough(Player p, ItemStack item) {
		return hasEnough(p, getAmount(item));
	}

	public boolean hasEnough(Player p, double amount) {
		return econ == null || (getMoney(p) >= amount);
	}

	public double getMoney(Player p) {
		if (econ == null) return 0.00;
		return econ.getBalance(p);
	}

	public String format(ItemStack item) {
		return format(getAmount(item));
	}

	public String format(double amount) {
		return (econ == null ? null : econ
				.format(amount));
	}

	// It's parsed as an item.
	private double getAmount(ItemStack item) {
		double major = item.getAmount();
		double minor = item.getDurability() / 100D;
		return major + minor;
	}
}
