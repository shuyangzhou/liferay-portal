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

package com.liferay.portal.osgi.web.wab.extender.internal;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.osgi.web.servlet.context.helper.ServletContextHelperFactory;
import com.liferay.portal.osgi.web.wab.extender.internal.configuration.WabExtenderConfiguration;
import com.liferay.portal.osgi.web.wab.extender.internal.event.EventUtil;
import com.liferay.portal.profile.PortalProfile;

import java.util.Dictionary;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.felix.utils.extender.AbstractExtender;
import org.apache.felix.utils.extender.Extension;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.ComponentContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Miguel Pastor
 * @author Raymond Augé
 */
@Component(
	configurationPid = "com.liferay.portal.osgi.web.wab.extender.internal.configuration.WabExtenderConfiguration",
	configurationPolicy = ConfigurationPolicy.OPTIONAL, immediate = true
)
public class WabFactory extends AbstractExtender {

	@Activate
	public void activate(ComponentContext componentContext) {
		setSynchronous(true);

		_bundleContext = componentContext.getBundleContext();

		_eventUtil = new EventUtil(_bundleContext);

		Dictionary<String, Object> properties =
			componentContext.getProperties();

		_wabExtenderConfiguration = ConfigurableUtil.createConfigurable(
			WabExtenderConfiguration.class, properties);

		try {
			_webBundleDeployer = new WebBundleDeployer(
				_bundleContext, properties, _eventUtil);

			super.start(_bundleContext);
		}
		catch (Exception e) {
			_log.error(e, e);
		}
	}

	@Deactivate
	public void deactivate() throws Exception {
		super.stop(_bundleContext);

		_bundleContext = null;

		_eventUtil.close();

		_eventUtil = null;

		_webBundleDeployer.close();

		_webBundleDeployer = null;
	}

	@Override
	protected void debug(Bundle bundle, String message) {
		if (_log.isDebugEnabled()) {
			_log.debug("[" + bundle + "] " + message);
		}
	}

	@Override
	protected Extension doCreateExtension(Bundle bundle) throws Exception {
		return new WABExtension(bundle);
	}

	@Override
	protected void error(String message, Throwable t) {
		_log.error(message, t);
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	@Override
	protected void warn(Bundle bundle, String message, Throwable t) {
		if (_log.isWarnEnabled()) {
			_log.warn("[" + bundle + "] " + message, t);
		}
	}

	private static final Log _log = LogFactoryUtil.getLog(WabFactory.class);

	private BundleContext _bundleContext;
	private EventUtil _eventUtil;

	@Reference
	private ServletContextHelperFactory _servletContextHelperFactory;

	private WabExtenderConfiguration _wabExtenderConfiguration;
	private WebBundleDeployer _webBundleDeployer;

	private class WABExtension implements Extension {

		public WABExtension(Bundle bundle) {
			_bundle = bundle;
		}

		@Override
		public void destroy() throws Exception {
			try {
				_started.await(
					_wabExtenderConfiguration.stopTimeout(),
					TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException ie) {
				_log.error(
					String.format(
						"The wait for bundle {0}/{1} to start before " +
							"destroying was interrupted",
						_bundle.getSymbolicName(), _bundle.getBundleId()),
					ie);
			}

			if (_serviceRegistration != null) {
				_serviceRegistration.unregister();
			}

			_webBundleDeployer.doStop(_bundle);
		}

		@Override
		public void start() throws Exception {
			try {
				_serviceRegistration = _webBundleDeployer.doStart(_bundle);
			}
			finally {
				_started.countDown();
			}
		}

		private final Bundle _bundle;
		private ServiceRegistration<PortalProfile> _serviceRegistration;
		private final CountDownLatch _started = new CountDownLatch(1);

	}

}