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

package com.browseengine.local.service.impl;

import java.io.File;

import com.browseengine.local.service.LocalService;
import com.browseengine.local.service.LocalService.LocalException;

/**
 * @author spackle
 *
 */
public class SingletonLocalServiceFactory {
	private static LocalService INSTANCE;
	
	public static LocalService getLocalServiceImpl() throws LocalException {
		return getLocalServiceImpl(null);
	}
	
	public static LocalService getLocalServiceImpl(String configDir) throws LocalException {
		if (INSTANCE == null) {
			synchronized(SingletonLocalServiceFactory.class) {
				if (INSTANCE == null) {
					INSTANCE = createInstance(configDir);
				}
			}
		}
		return INSTANCE;
	}
	
	private static LocalService createInstance(String configDir) throws LocalException {
		if (null != configDir) {
			LocalServiceConfig localConfig = new LocalServiceConfig(new File(configDir, "config.properties"));
			return new LocalServiceImpl(localConfig);
		} else {
			return new LocalServiceImpl();
		}
	}
}
