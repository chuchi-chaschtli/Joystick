/**
 * CountdownTimer.java is a part of Joystick
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
package com.valygard.aohruthless.timer;

import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

/**
 * Generic countdown timer with an initial duration and with varying
 * checkpoints.
 * <p>
 * Every time the tick interval has passed, the {@code onTick()} method in the
 * underlying {@link TimerEvent} is called.
 * <p>
 * When the duration of the timer has passed, {@code onStop()} in the underlying
 * {@link TimerEvent} is called
 * 
 * @author Anand
 * 
 */
public class CountdownTimer {

	private Plugin plugin;
	private long duration;
	private long remaining;

	private Timer timer;
	private TimerEvent event;

	/**
	 * Creates a countdown timer which will call {@code onTick()} every 20 ticks
	 * (1 second) and {@code onStop()} when the timer is finished. For a timer
	 * with no intervals, {@code onCheckpoint()} is never called.
	 * 
	 * @param plugin
	 *            the instance of the plugin responsible for the countdown timer
	 * @param duration
	 *            the long duration of the timer
	 */
	public CountdownTimer(Plugin plugin, long duration) {
		this.plugin = plugin;
		this.duration = duration;
		this.remaining = 0l;

		this.timer = null;
	}
	
	public Plugin getPlugin() {
		return plugin;
	}

	/**
	 * Sets the underlying Callback to a valid TimerEvent instance
	 * @param event
	 */
	public synchronized void setCallback(TimerEvent event) {
		this.event = event;
	}

	/**
	 * Starts the timer.
	 * <p>
	 * Begins the timer with specified duration in constructor
	 */
	public synchronized void start() {
		if (timer != null) {
			return;
		}
		remaining = duration;
		event.onStart();
		timer = new Timer();
	}

	/**
	 * Manually stops the timer prematurely.
	 */
	public synchronized void stop() {
		if (timer == null) {
			return;
		}
		timer.stop();
		timer = null;
		remaining = 0l;
		event.onFinish();
	}

	/**
	 * Checks if the timer is running.
	 * 
	 * @return true if the timer is running (not null), otherwise false.
	 */
	public synchronized boolean isRunning() {
		return timer != null;
	}

	/**
	 * Get the duration of the timer.
	 * 
	 * @return the duration of the timer in server ticks
	 */
	public synchronized long getDuration() {
		return duration;
	}

	/**
	 * Updates the duration of the timer.
	 * 
	 * @param duration
	 */
	public synchronized void setDuration(long duration) {
		this.duration = duration;
	}

	private class Timer implements Runnable {

		private BukkitTask task;

		public Timer() {
			scheduleNext();
		}

		@Override
		public void run() {
			synchronized (CountdownTimer.this) {
				remaining -= 20l;

				if (remaining <= 0l) {
					timer = null;
					event.onFinish();
					return;
				}

				event.onTick();

				if (task != null) {
					scheduleNext();
				}
			}
		}

		private synchronized void stop() {
			task.cancel();
			task = null;
		}

		private synchronized void scheduleNext() {
			long nextInterval = (remaining < 20) ? remaining : 20l;
			task = plugin.getServer().getScheduler().runTaskLater(plugin, this,
					nextInterval);
		}
	}
}
