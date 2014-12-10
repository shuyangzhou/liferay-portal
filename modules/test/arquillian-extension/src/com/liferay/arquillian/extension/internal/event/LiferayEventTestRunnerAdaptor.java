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

package com.liferay.arquillian.extension.internal.event;

import com.liferay.arquillian.extension.internal.init.InitLiferayContext;

import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;
import org.jboss.arquillian.core.spi.EventContext;
import org.jboss.arquillian.test.spi.event.suite.After;
import org.jboss.arquillian.test.spi.event.suite.AfterClass;
import org.jboss.arquillian.test.spi.event.suite.Before;
import org.jboss.arquillian.test.spi.event.suite.BeforeClass;
import org.jboss.arquillian.test.spi.event.suite.Test;

/**
 * @author Cristina González
 */
public class LiferayEventTestRunnerAdaptor {

	public void after(@Observes EventContext<After> eventContext)
		throws Throwable {
	}

	public void afterClass(@Observes EventContext<AfterClass> eventContext)
		throws Throwable {
	}

	public void before(@Observes EventContext<Before> eventContext)
		throws Throwable {
	}

	public void beforeClass(@Observes EventContext<BeforeClass> eventContext)
		throws Throwable {

		InitLiferayContext initLiferayContext =
			_initLiferayContextInstance.get();

		initLiferayContext.init();
	}

	public void test(@Observes EventContext<Test> eventContext)
		throws Throwable {
	}

	@Inject
	private Instance<InitLiferayContext> _initLiferayContextInstance;

}