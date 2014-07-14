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

package com.liferay.portal.cache.ehcache;

import com.liferay.portal.cache.cluster.EhcachePortalCacheClusterReplicatorFactory;
import com.liferay.portal.kernel.util.SetUtil;
import com.liferay.portal.test.AdviseWith;
import com.liferay.portal.test.AspectJMockingNewClassLoaderJUnitTestRunner;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.CacheConfiguration.CacheEventListenerFactoryConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.FactoryConfiguration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Tina Tian
 */
@RunWith(AspectJMockingNewClassLoaderJUnitTestRunner.class)
public class EhcacheConfigurationUtilTest {

	@Before
	public void setUp() throws MalformedURLException {
		File file = new File(
			"portal-impl/test/unit/com/liferay/portal/cache/ehcache/" +
				"test-ehcache-config.xml");

		_configurationURL = file.toURI().toURL();

		_originalConfiguration = ConfigurationFactory.parseConfiguration(
			_configurationURL);
	}

	@AdviseWith(
		adviceClasses = {
			DisableClusterLinkAdvice.class,
			DisableClusterLinkReplicateAdvice.class,
			DisableEhcacheBootStrapAdvice.class
		}
	)
	@Test
	public void testGetConfiguration1() {
		Configuration configuration1 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, false);
		Configuration configuration2 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, true);
		Configuration configuration3 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, false);
		Configuration configuration4 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, true);

		_assertEquals(_originalConfiguration, configuration1);
		_assertEquals(_originalConfiguration, configuration2);
		_assertEquals(_originalConfiguration, configuration3);
		_assertNothing(configuration4);
	}

	@AdviseWith(
		adviceClasses = {
			DisableClusterLinkReplicateAdvice.class,
			DisableEhcacheBootStrapAdvice.class, EnableClusterLinkAdvice.class
		}
	)
	@Test
	public void testGetConfiguration2() {
		Configuration configuration1 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, false);
		Configuration configuration2 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, true);
		Configuration configuration3 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, false);
		Configuration configuration4 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, true);

		_assertEquals(_originalConfiguration, configuration1);
		_assertEquals(_originalConfiguration, configuration2);
		_assertEquals(_originalConfiguration, configuration3);
		_assertWithReplicator(configuration4, true);
	}

	@AdviseWith(
		adviceClasses = {
			DisableClusterLinkAdvice.class, DisableEhcacheBootStrapAdvice.class,
			EnableClusterLinkReplicateAdvice.class
		}
	)
	@Test
	public void testGetConfiguration3() {
		Configuration configuration1 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, false);
		Configuration configuration2 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, true);
		Configuration configuration3 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, false);
		Configuration configuration4 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, true);

		_assertEquals(_originalConfiguration, configuration1);
		_assertEquals(_originalConfiguration, configuration2);
		_assertEquals(_originalConfiguration, configuration3);
		_assertNothing(configuration4);
	}

	@AdviseWith(
		adviceClasses = {
			DisableEhcacheBootStrapAdvice.class, EnableClusterLinkAdvice.class,
			EnableClusterLinkReplicateAdvice.class,
		}
	)
	@Test
	public void testGetConfiguration4() {
		Configuration configuration1 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, false);
		Configuration configuration2 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, true);
		Configuration configuration3 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, false);
		Configuration configuration4 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, true);

		_assertEquals(_originalConfiguration, configuration1);
		_assertEquals(_originalConfiguration, configuration2);
		_assertWithReplicator(configuration3, false);
		_assertWithReplicator(configuration4, false);
	}

	@AdviseWith(
		adviceClasses = {
			DisableClusterLinkAdvice.class,
			DisableClusterLinkReplicateAdvice.class,
			EnableEhcacheBootStrapAdvice.class
		}
	)
	@Test
	public void testGetConfiguration5() {
		Configuration configuration1 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, false);
		Configuration configuration2 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, true);
		Configuration configuration3 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, false);
		Configuration configuration4 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, true);

		_assertEquals(_originalConfiguration, configuration1);
		_assertEquals(_originalConfiguration, configuration2);
		_assertEquals(_originalConfiguration, configuration3);
		_assertWithBootStrap(configuration4);
	}

	@AdviseWith(
		adviceClasses = {
			DisableClusterLinkReplicateAdvice.class,
			EnableClusterLinkAdvice.class, EnableEhcacheBootStrapAdvice.class
		}
	)
	@Test
	public void testGetConfiguration6() {
		Configuration configuration1 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, false);
		Configuration configuration2 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, true);
		Configuration configuration3 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, false);
		Configuration configuration4 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, true);

		_assertEquals(_originalConfiguration, configuration1);
		_assertEquals(_originalConfiguration, configuration2);
		_assertEquals(_originalConfiguration, configuration3);
		_assertWithReplicatorAndBootStrap(configuration4, true);
	}

	@AdviseWith(
		adviceClasses = {
			DisableClusterLinkAdvice.class,
			EnableClusterLinkReplicateAdvice.class,
			EnableEhcacheBootStrapAdvice.class
		}
	)
	@Test
	public void testGetConfiguration7() {
		Configuration configuration1 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, false);
		Configuration configuration2 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, true);
		Configuration configuration3 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, false);
		Configuration configuration4 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, true);

		_assertEquals(_originalConfiguration, configuration1);
		_assertEquals(_originalConfiguration, configuration2);
		_assertEquals(_originalConfiguration, configuration3);
		_assertWithBootStrap(configuration4);
	}

	@AdviseWith(
		adviceClasses = {
			EnableClusterLinkAdvice.class,
			EnableClusterLinkReplicateAdvice.class,
			EnableEhcacheBootStrapAdvice.class
		}
	)
	@Test
	public void testGetConfiguration8() {
		Configuration configuration1 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, false);
		Configuration configuration2 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, false, true);
		Configuration configuration3 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, false);
		Configuration configuration4 =
			EhcacheConfigurationUtil.getConfiguration(
				_configurationURL, true, true);

		_assertEquals(_originalConfiguration, configuration1);
		_assertEquals(_originalConfiguration, configuration2);
		_assertWithReplicatorAndBootStrap(configuration3, false);
		_assertWithReplicatorAndBootStrap(configuration4, false);
	}

	@Aspect
	public static class DisableClusterLinkAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues.CLUSTER_LINK_ENABLED)")
		public Object disableClusterLink(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[] {Boolean.FALSE});
		}

	}

	@Aspect
	public static class DisableClusterLinkReplicateAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues." +
				"EHCACHE_CLUSTER_LINK_REPLICATION_ENABLED)")
		public Object disableClusterLinkReplicate(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[] {Boolean.FALSE});
		}

	}

	@Aspect
	public static class DisableEhcacheBootStrapAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues." +
				"EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED)")
		public Object disableEhcacheBootStrap(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[] {Boolean.FALSE});
		}

	}

	@Aspect
	public static class EnableClusterLinkAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues.CLUSTER_LINK_ENABLED)")
		public Object enableClusterLink(ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[] {Boolean.TRUE});
		}

	}

	@Aspect
	public static class EnableClusterLinkReplicateAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues." +
				"EHCACHE_CLUSTER_LINK_REPLICATION_ENABLED)")
		public Object enableClusterLinkReplicate(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[] {Boolean.TRUE});
		}

	}

	@Aspect
	public static class EnableEhcacheBootStrapAdvice {

		@Around(
			"set(* com.liferay.portal.util.PropsValues." +
				"EHCACHE_BOOTSTRAP_CACHE_LOADER_ENABLED)")
		public Object enableEhcacheBootstrap(
				ProceedingJoinPoint proceedingJoinPoint)
			throws Throwable {

			return proceedingJoinPoint.proceed(new Object[] {Boolean.TRUE});
		}

	}

	@SuppressWarnings("unchecked")
	private void _assertEquals(
		CacheConfiguration cacheConfiguration1,
		CacheConfiguration cacheConfiguration2) {

		_assertEquals(
			cacheConfiguration1.getCacheEventListenerConfigurations(),
			cacheConfiguration2.getCacheEventListenerConfigurations());

		Assert.assertEquals(
			cacheConfiguration1.getBootstrapCacheLoaderFactoryConfiguration(),
			cacheConfiguration2.getBootstrapCacheLoaderFactoryConfiguration());
	}

	private void _assertEquals(
		Configuration configuration1, Configuration configuration2) {

		_assertEquals(
			configuration1.getCacheManagerPeerProviderFactoryConfiguration(),
			configuration2.getCacheManagerPeerProviderFactoryConfiguration());

		_assertEquals(
			configuration1.getCacheManagerPeerListenerFactoryConfigurations(),
			configuration2.getCacheManagerPeerListenerFactoryConfigurations());

		_assertEquals(
			configuration1.getDefaultCacheConfiguration(),
			configuration2.getDefaultCacheConfiguration());

		_assertEquals(
			configuration1.getCacheConfigurations(),
			configuration1.getCacheConfigurations());
	}

	@SuppressWarnings("rawtypes")
	private void _assertEquals(
		List<FactoryConfiguration> factoryConfigurations1,
		List<FactoryConfiguration> factoryConfigurations2) {

		Set<FactoryConfiguration> configurationSet1 = SetUtil.fromCollection(
			factoryConfigurations1);
		Set<FactoryConfiguration> configurationSet2 = SetUtil.fromCollection(
			factoryConfigurations2);

		Assert.assertEquals(configurationSet1, configurationSet2);
	}

	@SuppressWarnings("unchecked")
	private void _assertEquals(
		Map<String, CacheConfiguration> cacheConfigurations1,
		Map<String, CacheConfiguration> cacheConfigurations2) {

		if (cacheConfigurations1 == cacheConfigurations2) {
			return;
		}

		Assert.assertEquals(
			cacheConfigurations1.size(), cacheConfigurations1.size());

		if (cacheConfigurations1.isEmpty()) {
			return;
		}

		try {
			Iterator<Entry<String, CacheConfiguration>> iterator =
				cacheConfigurations1.entrySet().iterator();

			while (iterator.hasNext()) {
				Entry<String, CacheConfiguration> entry = iterator.next();

				String key = entry.getKey();
				CacheConfiguration value1 = entry.getValue();

				CacheConfiguration value2 = cacheConfigurations2.get(key);

				if (value1 == null) {
					if ((value2 != null) ||
						cacheConfigurations2.containsKey(key)) {

						Assert.fail();
					}
				}
				else {
					_assertEquals(value1, value2);
				}
			}
		}
		catch (Exception e) {
			Assert.fail();
		}
	}

	@SuppressWarnings("rawtypes")
	private void _assertNoClusterReplicator(
		CacheConfiguration cacheConfiguration) {

		List cacheEventListenerConfigurations =
			cacheConfiguration.getCacheEventListenerConfigurations();

		Assert.assertTrue(cacheEventListenerConfigurations.isEmpty());
	}

	@SuppressWarnings("rawtypes")
	private void _assertNothing(Configuration configuration) {
		List<FactoryConfiguration> peerListenerFactoryConfigurations =
			configuration.getCacheManagerPeerListenerFactoryConfigurations();
		List<FactoryConfiguration> peerProviderFactoryConfigurations =
			configuration.getCacheManagerPeerProviderFactoryConfiguration();

		Assert.assertTrue(peerListenerFactoryConfigurations.isEmpty());
		Assert.assertTrue(peerProviderFactoryConfigurations.isEmpty());

		CacheConfiguration defaultCacheConfiguration =
			configuration.getDefaultCacheConfiguration();

		_assertNoClusterReplicator(defaultCacheConfiguration);
		Assert.assertNull(
			defaultCacheConfiguration.
				getBootstrapCacheLoaderFactoryConfiguration());

		Map<String, CacheConfiguration> cacheConfigurations =
			configuration.getCacheConfigurations();

		for (
			CacheConfiguration cacheConfiguration :
				cacheConfigurations.values()) {

			_assertNoClusterReplicator(cacheConfiguration);
			Assert.assertNull(
				cacheConfiguration.
					getBootstrapCacheLoaderFactoryConfiguration());
		}
	}

	@SuppressWarnings("rawtypes")
	private void _assertWithBootStrap(Configuration configuration) {
		List<FactoryConfiguration> peerListenerFactoryConfigurations =
			configuration.getCacheManagerPeerListenerFactoryConfigurations();
		List<FactoryConfiguration> peerProviderFactoryConfigurations =
			configuration.getCacheManagerPeerProviderFactoryConfiguration();

		Assert.assertTrue(peerListenerFactoryConfigurations.isEmpty());
		Assert.assertTrue(peerProviderFactoryConfigurations.isEmpty());

		CacheConfiguration defaultCacheConfiguration =
			configuration.getDefaultCacheConfiguration();

		_assertNoClusterReplicator(defaultCacheConfiguration);
		Assert.assertNotNull(
			defaultCacheConfiguration.
				getBootstrapCacheLoaderFactoryConfiguration());

		Map<String, CacheConfiguration> cacheConfigurations =
			configuration.getCacheConfigurations();

		for (
			CacheConfiguration cacheConfiguration :
				cacheConfigurations.values()) {

			_assertNoClusterReplicator(cacheConfiguration);
			Assert.assertNotNull(
				cacheConfiguration.
					getBootstrapCacheLoaderFactoryConfiguration());
		}
	}

	@SuppressWarnings("rawtypes")
	private void _assertWithReplicator(
		CacheConfiguration cacheConfiguration, boolean defaultReplicator) {

		List cacheEventListenerConfigurations =
			cacheConfiguration.getCacheEventListenerConfigurations();

		Assert.assertEquals(1, cacheEventListenerConfigurations.size());

		CacheEventListenerFactoryConfiguration
			cacheEventListenerFactoryConfiguration =
				(CacheEventListenerFactoryConfiguration)
					cacheEventListenerConfigurations.get(0);

		if (defaultReplicator) {
			Assert.assertEquals(
				_DEFAULT_REPLICATOR,
				cacheEventListenerFactoryConfiguration.
					getFullyQualifiedClassPath());
		}
		else {
			Assert.assertEquals(
				_CLUSTER_LINK_REPLICATOR,
				cacheEventListenerFactoryConfiguration.
					getFullyQualifiedClassPath());
		}

		Assert.assertEquals(
			_CACHE_EVENT_LISTENER_PROPERTIES,
			cacheEventListenerFactoryConfiguration.getProperties());
	}

	@SuppressWarnings("rawtypes")
	private void _assertWithReplicator(
		Configuration configuration, boolean defaultReplicator) {

		List<FactoryConfiguration> peerListenerFactoryConfigurations =
			configuration.getCacheManagerPeerListenerFactoryConfigurations();
		List<FactoryConfiguration> peerProviderFactoryConfigurations =
			configuration.getCacheManagerPeerProviderFactoryConfiguration();

		if (defaultReplicator) {
			Assert.assertEquals(1, peerListenerFactoryConfigurations.size());
			Assert.assertEquals(1, peerProviderFactoryConfigurations.size());
		}
		else {
			Assert.assertTrue(peerListenerFactoryConfigurations.isEmpty());
			Assert.assertTrue(peerProviderFactoryConfigurations.isEmpty());
		}

		CacheConfiguration defaultCacheConfiguration =
			configuration.getDefaultCacheConfiguration();

		_assertWithReplicator(defaultCacheConfiguration, defaultReplicator);
		Assert.assertNull(
			defaultCacheConfiguration.
				getBootstrapCacheLoaderFactoryConfiguration());

		Map<String, CacheConfiguration> cacheConfigurations =
			configuration.getCacheConfigurations();

		for (
			CacheConfiguration cacheConfiguration :
				cacheConfigurations.values()) {

			_assertWithReplicator(cacheConfiguration, defaultReplicator);
			Assert.assertNull(
				cacheConfiguration.
					getBootstrapCacheLoaderFactoryConfiguration());
		}
	}

	@SuppressWarnings("rawtypes")
	private void _assertWithReplicatorAndBootStrap(
		Configuration configuration, boolean defaultReplicator) {

		List<FactoryConfiguration> peerListenerFactoryConfigurations =
			configuration.getCacheManagerPeerListenerFactoryConfigurations();
		List<FactoryConfiguration> peerProviderFactoryConfigurations =
			configuration.getCacheManagerPeerProviderFactoryConfiguration();

		if (defaultReplicator) {
			Assert.assertEquals(1, peerListenerFactoryConfigurations.size());
			Assert.assertEquals(1, peerProviderFactoryConfigurations.size());
		}
		else {
			Assert.assertTrue(peerListenerFactoryConfigurations.isEmpty());
			Assert.assertTrue(peerProviderFactoryConfigurations.isEmpty());
		}

		CacheConfiguration defaultCacheConfiguration =
			configuration.getDefaultCacheConfiguration();

		_assertWithReplicator(defaultCacheConfiguration, defaultReplicator);
		Assert.assertNotNull(
			defaultCacheConfiguration.
				getBootstrapCacheLoaderFactoryConfiguration());

		Map<String, CacheConfiguration> cacheConfigurations =
			configuration.getCacheConfigurations();

		for (
			CacheConfiguration cacheConfiguration :
				cacheConfigurations.values()) {

			_assertWithReplicator(cacheConfiguration, defaultReplicator);
			Assert.assertNotNull(
				cacheConfiguration.
					getBootstrapCacheLoaderFactoryConfiguration());
		}
	}

	private static final String _CACHE_EVENT_LISTENER_PROPERTIES =
		"testKey=testValue";

	private static final String _CLUSTER_LINK_REPLICATOR =
		EhcachePortalCacheClusterReplicatorFactory.class.getName();

	private static final String _DEFAULT_REPLICATOR =
		LiferayCacheEventListenerFactory.class.getName();

	private URL _configurationURL;
	private Configuration _originalConfiguration;

}