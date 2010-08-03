/**
 * Bobo Browse Engine - High performance faceted/parametric search implementation 
 * that handles various types of semi-structured data.  Written in Java.
 * 
 * Copyright (C) 2005-2006  spackle
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 * 
 * To contact the project administrators for the bobo-browse project, 
 * please go to https://sourceforge.net/projects/bobo-browse/, or 
 * contact owner@browseengine.com.
 */

package com.browseengine.local.service;

/**
 * Converts from degrees to radians, and radians to degrees.
 * 
 * @author spackle
 *
 */
public class Conversions {
	private static final double DEG_TO_RAD = Math.PI/180.;

	public static double d2r(double deg) {
		return deg*DEG_TO_RAD;
	}
	public static double r2d(double rad) {
		return rad/DEG_TO_RAD;
	}

	/**
	 * a 5-k run is approx. 3.1 mi.
	 */
	private static final float MILES_TO_KM = 5f/3.1f;
	
	public static float mi2km(float mi) {
		return mi*MILES_TO_KM;
	}
	
	public static float km2mi(float km) {
		return km/MILES_TO_KM;
	}
}
