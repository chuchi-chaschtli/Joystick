/**
 * Conversion.java is a part of Joystick
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

import java.util.concurrent.TimeUnit;

/**
 * Conversion utility from seconds<->ticks as well as times into strings
 * 
 * @author Anand
 * 
 */
public class Conversion {

	/**
	 * Switch ticks to seconds.
	 * 
	 * @param ticks
	 * @return
	 */
	public static int toSeconds(long ticks) {
		return (int) (ticks / 20l);
	}

	/**
	 * Switch seconds to ticks
	 * 
	 * @param seconds
	 * @return
	 */
	public static long toTicks(int seconds) {
		return seconds * 20l;
	}

	/**
	 * 
	 * @param s
	 *            an integer amount of seconds.
	 * @return a string
	 */
	public static String formatIntoHHMMSS(int s) {
		long h = TimeUnit.SECONDS.toHours(s);
		long m = TimeUnit.SECONDS.toMinutes(s);

		String hours = Long.toString(h);
		String minutes = Long.toString(m - TimeUnit.HOURS.toMinutes(h));
		String seconds = Long.toString(s - TimeUnit.MINUTES.toSeconds(m));

		hours = (hours.length() < 2 ? "0" : "") + hours;
		minutes = (minutes.length() < 2 ? "0" : "") + minutes;
		seconds = (seconds.length() < 2 ? "0" : "") + seconds;

		return hours + ":" + minutes + ":" + seconds;
	}

	/**
	 * Returns a given integer time parsed into a String which is much easier to
	 * interpret. Parses the given integer, in seconds, to HH:MM:SS format using
	 * {@code #formatIntoHHMMSS(int)}. This String is then split by : and the
	 * amount of days is calculated.
	 * 
	 * @param secs
	 *            an amount of seconds.
	 * @return a String object.
	 */
	public static String formatIntoSentence(int secs) {
		String hhmmss = formatIntoHHMMSS(secs);
		String[] parts = hhmmss.split(":");

		int hours = Integer.valueOf(parts[0]);
		int minutes = Integer.valueOf(parts[1]);
		int seconds = Integer.valueOf(parts[2]);

		int days = hours / 24;
		hours %= 24;

		String fDays = (days > 0 ? " " + days + " day" + (days > 1 ? "s" : "")
				: "");
		String fHours = (hours > 0 ? " " + hours + " hour"
				+ (hours > 1 ? "s" : "") : "");
		String fMinutes = (minutes > 0 ? " " + minutes + " minute"
				+ (minutes > 1 ? "s" : "") : "");
		String fSeconds = (seconds > 0 ? " " + seconds + " second"
				+ (seconds > 1 ? "s" : "") : "");

		return new StringBuilder().append(fDays).append(fHours)
				.append(fMinutes).append(fSeconds).toString();
	}
}