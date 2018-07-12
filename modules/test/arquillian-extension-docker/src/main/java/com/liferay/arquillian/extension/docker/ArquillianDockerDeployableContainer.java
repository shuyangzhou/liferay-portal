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

package com.liferay.arquillian.extension.docker;

import com.liferay.arquillian.extension.junit.bridge.container.remote.LiferayRemoteContainerConfiguration;
import com.liferay.arquillian.extension.junit.bridge.container.remote.LiferayRemoteDeployableContainer;

import java.io.IOException;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URL;
import java.net.UnknownHostException;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.Manifest;

import javax.management.MBeanServerConnection;
import javax.management.openmbean.CompositeData;

import org.jboss.arquillian.container.spi.client.container.DeploymentException;
import org.jboss.arquillian.container.spi.client.protocol.metadata.HTTPContext;
import org.jboss.arquillian.container.spi.client.protocol.metadata.JMXContext;
import org.jboss.arquillian.container.spi.client.protocol.metadata.ProtocolMetaData;
import org.jboss.arquillian.container.spi.context.annotation.ContainerScoped;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.InstanceProducer;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.osgi.metadata.OSGiMetaData;
import org.jboss.osgi.metadata.OSGiMetaDataBuilder;
import org.jboss.osgi.spi.BundleInfo;
import org.jboss.osgi.vfs.AbstractVFS;
import org.jboss.osgi.vfs.VFSUtils;
import org.jboss.osgi.vfs.VirtualFile;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;

import org.osgi.framework.BundleException;
import org.osgi.jmx.framework.BundleStateMBean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Matthew Tambara
 */
public class ArquillianDockerDeployableContainer
	extends LiferayRemoteDeployableContainer {

	@Override
	public ProtocolMetaData deploy(Archive archive) throws DeploymentException {
		LiferayRemoteContainerConfiguration config =
			configurationInstance.get();

		ProtocolMetaData protocolMetaData = _deploy(archive);

		protocolMetaData.addContext(
			new HTTPContext(config.getHttpHost(), config.getHttpPort()));

		return protocolMetaData;
	}

	@Override
	public void undeploy(Archive archive) throws DeploymentException {
		try {
			Node node = archive.get("/META-INF/MANIFEST.MF");

			Manifest manifest = new Manifest(node.getAsset().openStream());

			OSGiMetaData metadata = OSGiMetaDataBuilder.load(manifest);

			_undeploy(metadata.getBundleSymbolicName());
		}
		catch (IOException ioe) {
			throw new DeploymentException(
				"Cannot undeploy: " + archive.getName(), ioe);
		}
	}

	@ApplicationScoped
	@Inject
	protected Instance<LiferayRemoteContainerConfiguration>
		configurationInstance;

	protected final Map<String, BundleHandle> deployedBundles = new HashMap<>();

	@ContainerScoped
	@Inject
	protected InstanceProducer<MBeanServerConnection> mbeanServerInstance;

	private ProtocolMetaData _deploy(Archive<?> archive)
		throws DeploymentException {

		try {
			Node node = archive.get("/META-INF/MANIFEST.MF");

			Manifest manifest = new Manifest(node.getAsset().openStream());

			OSGiMetaData metadata = OSGiMetaDataBuilder.load(manifest);

			BundleHandle handle = _installBundle(archive);

			deployedBundles.put(metadata.getBundleSymbolicName(), handle);
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception ex) {
			throw new DeploymentException(
				"Cannot deploy: " + archive.getName(), ex);
		}

		MBeanServerConnection mbeanServer = mbeanServerInstance.get();

		return new ProtocolMetaData().addContext(new JMXContext(mbeanServer));
	}

	private InetAddress _getIPAddress()
		throws SocketException, UnknownHostException {

		Enumeration<NetworkInterface> networkInterfaces =
			NetworkInterface.getNetworkInterfaces();

		while (networkInterfaces.hasMoreElements()) {
			NetworkInterface networkInterface = networkInterfaces.nextElement();

			Enumeration<InetAddress> inetAddresses =
				networkInterface.getInetAddresses();

			while (inetAddresses.hasMoreElements()) {
				InetAddress inetAddress = inetAddresses.nextElement();

				if ((inetAddress.getAddress().length == 4) &&
					(inetAddress.getAddress()[0] != 127)) {

					return inetAddress;
				}
			}
		}

		return InetAddress.getLocalHost();
	}

	private BundleHandle _installBundle(Archive<?> archive)
		throws BundleException, IOException {

		VirtualFile virtualFile = _toVirtualFile(archive);

		try {
			return _installBundle(archive.getName(), virtualFile);
		}
		finally {
			VFSUtils.safeClose(virtualFile);
		}
	}

	private BundleHandle _installBundle(String location, URL streamURL)
		throws BundleException, IOException {

		// Adapt URL to remote system by serving over HTTP

		SimpleHTTPServer server = new SimpleHTTPServer(_getIPAddress(), 9000);

		URL serverUrl = server.serve(streamURL);

		server.start();

		try {
			if (_logger.isInfoEnabled()) {
				_logger.info("Installing " + serverUrl.toExternalForm());
			}

			long bundleId = frameworkMBean.installBundleFromURL(
				location, serverUrl.toExternalForm());

			String symbolicName = bundleStateMBean.getSymbolicName(bundleId);

			String version = bundleStateMBean.getVersion(bundleId);

			return new BundleHandle(bundleId, symbolicName, version);
		}
		finally {
			if (server != null) {
				server.shutdown();
			}
		}
	}

	private BundleHandle _installBundle(
			String location, VirtualFile virtualFile)
		throws BundleException, IOException {

		BundleInfo info = BundleInfo.createBundleInfo(virtualFile);

		URL streamURL = info.getRoot().getStreamURL();

		return _installBundle(location, streamURL);
	}

	private VirtualFile _toVirtualFile(Archive<?> archive) throws IOException {
		ZipExporter exporter = archive.as(ZipExporter.class);

		return AbstractVFS.toVirtualFile(
			archive.getName(), exporter.exportAsInputStream());
	}

	private void _undeploy(String symbolicName) throws DeploymentException {
		BundleHandle handle = deployedBundles.remove(symbolicName);

		if (handle != null) {
			String bundleState = null;

			try {
				CompositeData bundleType = bundleStateMBean.getBundle(
					handle.getBundleId());

				if (bundleType != null) {
					bundleState = (String)bundleType.get(
						BundleStateMBean.STATE);
				}
			}
			catch (IOException ioe) {

				// ignore non-existent bundle

				return;
			}

			if ((bundleState != null) &&
				!bundleState.equals(BundleStateMBean.UNINSTALLED)) {

				try {
					frameworkMBean.uninstallBundle(handle.getBundleId());
				}
				catch (IOException ioe) {
					_logger.error("Cannot _undeploy: " + symbolicName, ioe);
				}
			}
		}
	}

	private static final Logger _logger = LoggerFactory.getLogger(
		ArquillianDockerDeployableContainer.class.getPackage().getName());

}