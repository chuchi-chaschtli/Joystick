/**
 * ChatNotifier.java is a part of Joystick
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

import org.bukkit.Sound;
import org.bukkit.entity.Player;

import com.valygard.aohruthless.framework.Arena;
import com.valygard.aohruthless.messenger.Msg;

/**
 * ChatNotifier implements a form of checkpoint timing in the underlying
 * {@code CountdownTimer} to allow mesages to be sent to all players in the
 * arena.
 * 
 * @author Anand
 * 
 */
public class ChatNotifier implements TimerEvent {

	// attributes
	private Arena arena;
	private CountdownTimer timer;
	private Msg msg;

	// timer checkpoints
	private int[] intervals;
	private int index;

	/**
	 * ChatNotifier constructor requires an {@code arena} with an underlying
	 * {@code timer}, and a {@code msg} to send to all players at various
	 * {@code intervals}
	 * 
	 * @param arena
	 *            an Arena
	 * @param timer
	 *            a CountdownTimer instance
	 * @param msg
	 *            a Msg enum to send
	 * @param intervals
	 *            an array of integer timings
	 */
	public ChatNotifier(Arena arena, CountdownTimer timer, Msg msg,
			int[] intervals) {
		this.arena = arena;
		this.timer = timer;
		this.msg = msg;

		this.intervals = intervals;
		this.index = 0;
	}

	@Override
	public void onStart() {
		if (intervals == null || intervals.length == 0) {
			index = -1;
			return;
		}

		for (int i = 0; i < intervals.length; i++) {
			if (Conversion.toSeconds(timer.getDuration()) > intervals[i]) {
				index = i;
			} else {
				break;
			}
		}
	}

	@Override
	public void onFinish() {}

	@Override
	public void onTick() {
		if (index > -1) {
			int timeLeft = Conversion.toSeconds(timer.getDuration());
			if (timeLeft == intervals[index]) {
				arena.getPlugin().getMessenger().announce(arena, msg,
						Conversion.formatIntoHHMMSS(timeLeft));
				index--;

				for (Player p : arena.getPlayers()) {
					p.playSound(p.getLocation(), Sound.BLOCK_NOTE_PLING, 3.0f,
							1.2f);
				}
			}
		}
	}
}
