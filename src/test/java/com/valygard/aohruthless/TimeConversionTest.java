/**
 * TimeConversionTest.java is a part of Joystick
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

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import com.valygard.aohruthless.timer.Conversion;

/**
 * @author Anand
 * 
 */
public class TimeConversionTest {

	@Test
	public void testFormatIntoHHMMSS() {
		Assert.assertThat(Conversion.formatIntoHHMMSS(95000),
				CoreMatchers.is("26:23:20"));
	}

	@Test
	public void testFormatIntoSentence() {
		Assert.assertThat(Conversion.formatIntoSentence(95000).trim(),
				CoreMatchers.is("1 day 2 hours 23 minutes 20 seconds"));
	}
}
