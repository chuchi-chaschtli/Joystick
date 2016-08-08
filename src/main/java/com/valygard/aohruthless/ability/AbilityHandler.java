/**
 * AbilityHandler.java is a part of Joystick
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

/**
 * Handler system to process all Abilities. All abilities are created through
 * abstraction, and must be a child of {@link Ability}. Abilities with cooldowns
 * must also be registered through the {@link CooldownAbility}, which handles
 * cooldown management and is a direct child of {@link Ability}.
 * <p>
 * Using the AbilityHandler is very straightforward. Simply register the Ability
 * class in the {@link #registerAbilities() registration} method. The permission
 * for each ability is automatically created based on the ability name. The
 * permission is the lowercase name with "abilities." prepended to it. You must
 * specify the other arguments for the class: for generic abilities, use
 * {@link #register(Class, String, Material)}. For cooldown abilities, you must
 * specify the cooldown using {@link #register(Class, String, Material, int)}.
 * </p>
 * <p>
 * All abilities, when registered, are hashed into {@code abilities} and this
 * Set can be retrieved using {@link #getAbilities()}.
 * </p>
 * 
 * @author Anand
 * 
 */
public class AbilityHandler {

	private final Plugin plugin;
	private final Set<Ability> abilities;

	/**
	 * Constructor initializes by main plugin instance. There is only meant to
	 * be one instance of the AbilityHandler in the plugin lifecycle.
	 * 
	 * @param plugin
	 *            the main plugin instance
	 */
	public AbilityHandler(Plugin plugin) {
		this.plugin = plugin;
		this.abilities = new HashSet<>();

		registerAbilities();
	}

	/**
	 * Grabs a read-only version of the Abilities set.
	 * 
	 * @return an unmodifiable set
	 */
	public Set<Ability> getAbilities() {
		return Collections.unmodifiableSet(abilities);
	}

	// TODO: add abilities to register + document
	public void registerAbilities() {}

	/**
	 * Registers a valid CooldownAbility reflectively. Grabs given parameters to
	 * instantiate a Class object with the
	 * {@link CooldownAbility#CooldownAbility(Plugin, String, String, Material, int)
	 * constructor}.
	 * <p>
	 * An IllegalArgumentException is thrown if the class is not assignable from
	 * CooldownAbility. The CooldownAbility is instantiated and added to
	 * {@code abilities}.
	 * </p>
	 * 
	 * @param c
	 *            the assignable class from CooldownAbility.class
	 * @param name
	 *            the String name for the ability
	 * @param type
	 *            the Material type for the ability
	 * @param cooldown
	 *            the integer cooldown, in seconds
	 */
	public <T extends CooldownAbility> void register(Class<T> c, String name,
			Material type, int cooldown) {
		if (!CooldownAbility.class.isAssignableFrom(c)) {
			throw new IllegalArgumentException(c + " does not extend "
					+ CooldownAbility.class);
		}

		try {
			Constructor<?> constructor = c.getConstructor(Plugin.class,
					String.class, String.class, Material.class, Integer.class);
			abilities.add((CooldownAbility) constructor.newInstance(plugin,
					name, "abilities." + name.toLowerCase(), type, cooldown));
		}
		catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Registers a valid Ability reflectively. Grabs given parameters to
	 * instantiate a Class object with the
	 * {@link Ability#Ability(Plugin, String, String, Material) constructor}.
	 * <p>
	 * An IllegalArgumentException is thrown if the class is not assignable from
	 * Ability. The Ability is instantiated and added to {@code abilities}.
	 * </p>
	 * 
	 * @param c
	 *            the assignable class from Ability.class
	 * @param name
	 *            the String name for the ability
	 * @param type
	 *            the Material type for the ability
	 */
	public <T extends Ability> void register(Class<T> c, String name,
			Material type) {
		if (!Ability.class.isAssignableFrom(c)) {
			throw new IllegalArgumentException(c + " does not extend "
					+ Ability.class);
		}

		try {
			Constructor<?> constructor = c.getConstructor(Plugin.class,
					String.class, String.class, Material.class);
			abilities.add((Ability) constructor.newInstance(plugin, name,
					"abilities." + name.toLowerCase(), type));
		}
		catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
}
