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

package com.liferay.arquillian.extension.internal.instanceproducer;

import com.liferay.arquillian.extension.internal.clearThread.ClearThreadLocalExecutor;
import com.liferay.arquillian.extension.internal.clearThread.ClearThreadLocalExecutorImpl;
import com.liferay.arquillian.extension.internal.deleteAfterTest.DeleteAfterTestExecutor;
import com.liferay.arquillian.extension.internal.deleteAfterTest.DeleteAfterTestExecutorImpl;
import com.liferay.arquillian.extension.internal.init.InitLiferayContext;
import com.liferay.arquillian.extension.internal.init.InitLiferayContextImpl;
import com.liferay.arquillian.extension.internal.log.FailOnLogMessageError;
import com.liferay.arquillian.extension.internal.log.FailOnLogMessageErrorAppender;
import com.liferay.arquillian.extension.internal.log.FailOnLogMessageErrorHandler;
import com.liferay.arquillian.extension.internal.log.FailOnLogMessageErrorImpl;

import org.jboss.arquillian.config.descriptor.api.ArquillianDescriptor;
import org.jboss.arquillian.core.api.Injector;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.api.annotation.Observes;

/**
 * @author Cristina González
 */
public class ExtensionInstanceProducer {

	public void createInstanceProducer(
		@Observes ArquillianDescriptor arquillianDescriptor) {

		_clearThreadLocalExecutorInstanceProducer.set(
			new ClearThreadLocalExecutorImpl());

		_deleteAfterTestExecutorInstanceProducer.set(
			new DeleteAfterTestExecutorImpl());

		InitLiferayContext initLiferayContext = new InitLiferayContextImpl();

		_initLiferayContextInstanceProducer.set(initLiferayContext);

		Injector injector = _injectorInstance.get();

		injector.inject(initLiferayContext);

		FailOnLogMessageError failOnErrorLogMessage =
			new FailOnLogMessageErrorImpl();

		_failOnLogMessageErrorInstanceProducer.set(failOnErrorLogMessage);

		FailOnLogMessageErrorAppender failOnErrorAppender =
			new FailOnLogMessageErrorAppender();

		_failOnLogMessageErrorAppenderInstanceProducer.set(failOnErrorAppender);

		FailOnLogMessageErrorHandler failOnLogMessageErrorHandler =
			new FailOnLogMessageErrorHandler();

		_failOnLogMessageErrorHandlerInstanceProducer.set(
			failOnLogMessageErrorHandler);

		injector.inject(failOnErrorLogMessage);

		injector.inject(failOnErrorAppender);

		injector.inject(failOnLogMessageErrorHandler);
	}

	@ApplicationScoped
	@Inject
	private InstanceProducer<ClearThreadLocalExecutor>
		_clearThreadLocalExecutorInstanceProducer;

	@ApplicationScoped
	@Inject
	private InstanceProducer<DeleteAfterTestExecutor>
		_deleteAfterTestExecutorInstanceProducer;

	@ApplicationScoped
	@Inject
	private InstanceProducer<FailOnLogMessageErrorAppender>
		_failOnLogMessageErrorAppenderInstanceProducer;

	@ApplicationScoped
	@Inject
	private InstanceProducer<FailOnLogMessageErrorHandler>
		_failOnLogMessageErrorHandlerInstanceProducer;

	@ApplicationScoped
	@Inject
	private InstanceProducer<FailOnLogMessageError>
		_failOnLogMessageErrorInstanceProducer;

	@ApplicationScoped
	@Inject
	private InstanceProducer<InitLiferayContext>
		_initLiferayContextInstanceProducer;

	@Inject
	private Instance<Injector> _injectorInstance;

}