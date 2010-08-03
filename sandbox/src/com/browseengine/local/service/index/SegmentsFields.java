/**
 * Bobo Browse Engine - High performance faceted/parametric search implementation 
 * that handles various types of semi-structured data.  Written in Java.
 * 
 * Copyright (C) 2006  Spackle
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
 * please go to https://sourceforge.net/projects/bobo-browse/.
 */

package com.browseengine.local.service.index;


/**
 * @author spackle
 *
 */
public enum SegmentsFields {
	TLID("tlid"),
	PLACEL("placel"),
	TOKENIZED_PLACEL("tokenized_placel"),
	PLACER("placer"),
	TOKENIZED_PLACER("tokenized_placer"),
	STATEL("statel"),
	STATER("statr"),
	START_LON("slon"),
	START_LAT("slat"),
	END_LON("elon"),
	END_LAT("elat");

	private String _field;
	
	private SegmentsFields(String field) {
		_field = field;
	}
	
	public String getField() {
		return _field;
	}
	
}
