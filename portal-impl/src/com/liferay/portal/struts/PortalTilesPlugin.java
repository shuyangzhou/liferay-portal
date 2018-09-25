/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package com.liferay.portal.struts;

import javax.servlet.ServletException;
import javax.servlet.UnavailableException;

import org.apache.struts.action.ActionServlet;
import org.apache.struts.action.PlugIn;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.PlugInConfig;
import org.apache.struts.tiles.DefinitionsFactory;
import org.apache.struts.tiles.DefinitionsFactoryConfig;
import org.apache.struts.tiles.DefinitionsFactoryException;
import org.apache.struts.tiles.TilesUtilImpl;
import org.apache.struts.tiles.TilesUtilStrutsModulesImpl;

/**
 * @author Brian Wing Shun Chan
 */
public class PortalTilesPlugin implements PlugIn {

	@Override
	public void destroy() {
		_definitionsFactory.destroy();

		_definitionsFactory = null;
	}

	@Override
	public void init(ActionServlet servlet, ModuleConfig moduleConfig)
		throws ServletException {

		DefinitionsFactoryConfig factoryConfig = new DefinitionsFactoryConfig();

		try {
			factoryConfig.populate(_plugInConfig.getProperties());
		}
		catch (Exception e) {
			throw new UnavailableException(e.getMessage());
		}

		factoryConfig.setFactoryName(moduleConfig.getPrefix());

		TilesUtilImpl tilesUtilImpl = new TilesUtilStrutsModulesImpl();

		try {
			_definitionsFactory = tilesUtilImpl.createDefinitionsFactory(
				servlet.getServletContext(), factoryConfig);
		}
		catch (DefinitionsFactoryException dfe) {
			throw new ServletException(dfe);
		}
	}

	public void setCurrentPlugInConfigObject(PlugInConfig plugInConfig) {
		_plugInConfig = plugInConfig;
	}

	private DefinitionsFactory _definitionsFactory;
	private PlugInConfig _plugInConfig;

}