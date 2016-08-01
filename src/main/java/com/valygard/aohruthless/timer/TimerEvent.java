/**
 * TimerEvent.java is a part of Joystick
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


/**
 * TimerEvent interface creates hooks for different points in the timer.
 * <p>
 * Event-driven programming models of other languages are able to pass function
 * pointers which invoke (i.e, call back) various points in an event. The
 * object-oriented model Java uses does not support this, but interfaces are a
 * fairly robust substitution.
 * 
 * @author Anand
 * 
 */
public interface TimerEvent {

	/**
	 * Listens for timer start
	 */
	public void onStart();

	/**
	 * Listens for timer finish.
	 */
	public void onFinish();

	/**
	 * Listens for timer tick
	 */
	public void onTick();
}
