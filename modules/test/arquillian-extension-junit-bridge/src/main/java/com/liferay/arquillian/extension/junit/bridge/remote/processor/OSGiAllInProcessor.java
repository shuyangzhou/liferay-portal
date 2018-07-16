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

package com.liferay.arquillian.extension.junit.bridge.remote.processor;

import aQute.bnd.osgi.Jar;

import com.liferay.arquillian.extension.junit.bridge.remote.activator.ArquillianBundleActivator;
import com.liferay.arquillian.extension.junit.bridge.remote.processor.service.BundleActivatorsManager;
import com.liferay.arquillian.extension.junit.bridge.remote.processor.service.ManifestManager;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.jboss.arquillian.container.test.spi.RemoteLoadableExtension;
import org.jboss.arquillian.container.test.spi.client.deployment.ApplicationArchiveProcessor;
import org.jboss.arquillian.container.test.spi.client.deployment.AuxiliaryArchiveAppender;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.jboss.arquillian.core.spi.ServiceLoader;
import org.jboss.arquillian.protocol.jmx.JMXTestRunner;
import org.jboss.arquillian.test.spi.TestClass;
import org.jboss.osgi.metadata.OSGiManifestBuilder;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Filters;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.ByteArrayAsset;
import org.jboss.shrinkwrap.api.container.ClassContainer;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.spec.JavaArchive;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Cristina González
 */
public class OSGiAllInProcessor implements ApplicationArchiveProcessor {

	@Override
	public void process(Archive<?> archive, TestClass testClass) {
		try {
			JavaArchive javaArchive = (JavaArchive)archive;

			_validateBundleArchive(javaArchive);

			_addTestClass(javaArchive, testClass);

			_addOSGiImports(javaArchive);

			_addArquillianDependencies(javaArchive);

			List<Archive<?>> auxiliaryArchives = _loadAuxiliaryArchives();

			_handleAuxiliaryArchives(javaArchive, auxiliaryArchives);

			_cleanRepeatedImports(javaArchive, auxiliaryArchives);

			ManifestManager manifestManager = _manifestManagerInstance.get();

			Manifest manifest = manifestManager.getManifest(javaArchive);

			Attributes mainAttributes = manifest.getMainAttributes();

			Attributes.Name bundleActivatorName = new Attributes.Name(
				"Bundle-Activator");

			String bundleActivator = mainAttributes.getValue(
				bundleActivatorName);

			mainAttributes.put(
				bundleActivatorName,
				ArquillianBundleActivator.class.getCanonicalName());

			manifestManager.replaceManifest(javaArchive, manifest);

			javaArchive.addClass(ArquillianBundleActivator.class);

			if (bundleActivator != null) {
				_addBundleActivator(javaArchive, bundleActivator);
			}
		}
		catch (RuntimeException re) {
			throw re;
		}
		catch (Exception ex) {
			throw new IllegalArgumentException(
				"Not a valid OSGi bundle: " + archive, ex);
		}
	}

	private void _addArquillianDependencies(JavaArchive javaArchive) {
		javaArchive.addPackage(JMXTestRunner.class.getPackage());
	}

	private void _addBundleActivator(
			JavaArchive javaArchive, String bundleActivatorValue)
		throws IOException {

		BundleActivatorsManager bundleActivatorsManager =
			_bundleActivatorsManagerInstance.get();

		List<String> bundleActivators =
			bundleActivatorsManager.getBundleActivators(
				javaArchive, _ACTIVATORS_FILE);

		bundleActivators.add(bundleActivatorValue);

		bundleActivatorsManager.replaceBundleActivatorsFile(
			javaArchive, _ACTIVATORS_FILE, bundleActivators);
	}

	private void _addOSGiImports(JavaArchive javaArchive) throws IOException {
		String[] extensionsImports = {
			"org.osgi.framework", "javax.management", "javax.management.*",
			"javax.naming", "javax.naming.*", "org.osgi.service.packageadmin",
			"org.osgi.service.startlevel", "org.osgi.util.tracker"
		};

		ManifestManager manifestManager = _manifestManagerInstance.get();

		Manifest manifest = manifestManager.putAttributeValue(
			manifestManager.getManifest(javaArchive), "Import-Package",
			extensionsImports);

		manifestManager.replaceManifest(javaArchive, manifest);
	}

	private void _addTestClass(JavaArchive javaArchive, TestClass testClass)
		throws IOException {

		Class<ClassContainer> classContainerClass = ClassContainer.class;

		if (!classContainerClass.isAssignableFrom(javaArchive.getClass())) {
			throw new IllegalArgumentException(
				"ClassContainer expected: " + javaArchive);
		}

		// Get the test class and its super classes

		Class<?> javaClass = testClass.getJavaClass();

		Set<Class<?>> classes = new HashSet<>();

		classes.add(javaClass);

		Class<?> superclass = javaClass.getSuperclass();

		while (superclass != Object.class) {
			classes.add(superclass);

			superclass = superclass.getSuperclass();
		}

		// Check if the application javaArchive already contains
		// the test classes

		String javaArchiveName = javaArchive.getName();

		if (!javaArchiveName.endsWith(".war")) {
			for (Class<?> clazz : classes) {
				boolean testClassFound = false;

				String className = clazz.getName();

				String path = className.replace('.', '/') + ".class";

				Map<ArchivePath, Node> javaArchiveContentMap =
					javaArchive.getContent();

				for (ArchivePath auxpath : javaArchiveContentMap.keySet()) {
					if (auxpath.toString().endsWith(path)) {
						testClassFound = true;

						break;
					}
				}

				if (!testClassFound) {
					((ClassContainer<?>)javaArchive).addClass(clazz);
				}
			}
		}

		Manifest manifest = _putAttributeValue(
			_getManifest(javaArchive), "Export-Package",
			javaClass.getPackage().getName());

		_replaceManifest(javaArchive, manifest);
	}

	private void _cleanRepeatedImports(
			JavaArchive javaArchive, Collection<Archive<?>> auxiliaryArchives)
		throws IOException {

		Manifest manifest = _getManifest(javaArchive);

		manifest = _cleanRepeatedImports(manifest, auxiliaryArchives);

		_replaceManifest(javaArchive, manifest);
	}

	private Manifest _cleanRepeatedImports(
			Manifest manifest, Collection<Archive<?>> auxiliaryArchives)
		throws IOException {

		List<String> auxiliaryArchivesPackages = _getAuxiliaryArchivesPackages(
			auxiliaryArchives);

		Attributes mainAttributes = manifest.getMainAttributes();

		String importPackages = mainAttributes.getValue(_IMPORT_PACKAGE);

		mainAttributes.remove(new Attributes.Name(_IMPORT_PACKAGE));

		Map<String, Set<String>> importsWithDirectivesMap =
			_toImportsWithDirectivesMap(importPackages);

		List<String> resultImports = new ArrayList<>();

		for (Entry<String, Set<String>> entry :
				importsWithDirectivesMap.entrySet()) {

			String importValue = entry.getKey();

			if (auxiliaryArchivesPackages.contains(importValue)) {
				continue;
			}

			StringBuilder sb = new StringBuilder();

			sb.append(importValue);

			for (String directive : entry.getValue()) {
				sb.append(";");
				sb.append(directive);
			}

			resultImports.add(sb.toString());
		}

		manifest = _putAttributeValue(
			manifest, _IMPORT_PACKAGE,
			resultImports.toArray(new String[resultImports.size()]));

		return manifest;
	}

	private List<String> _getAuxiliaryArchivesPackages(
			Collection<Archive<?>> auxiliaryArchives)
		throws IOException {

		List<String> packages = new ArrayList<>();

		for (Archive auxiliaryArchive : auxiliaryArchives) {
			ZipExporter zipExporter = auxiliaryArchive.as(ZipExporter.class);

			InputStream auxiliaryArchiveInputStream =
				zipExporter.exportAsInputStream();

			Jar jar = new Jar(
				auxiliaryArchive.getName(), auxiliaryArchiveInputStream);

			packages.addAll(jar.getPackages());
		}

		return packages;
	}

	private Manifest _getManifest(JavaArchive javaArchive) throws IOException {
		Node manifestNode = javaArchive.get(JarFile.MANIFEST_NAME);

		Asset manifestAsset = manifestNode.getAsset();

		return new Manifest(manifestAsset.openStream());
	}

	private void _handleAuxiliaryArchives(
			JavaArchive javaArchive, Collection<Archive<?>> auxiliaryArchives)
		throws IOException {

		for (Archive auxiliaryArchive : auxiliaryArchives) {
			Map<ArchivePath, Node> remoteLoadableExtensionMap =
				auxiliaryArchive.getContent(
					Filters.include(_REMOTE_LOADABLE_EXTENSION_FILE));

			Collection<Node> remoteLoadableExtensions =
				remoteLoadableExtensionMap.values();

			if (remoteLoadableExtensions.size() > 1) {
				throw new RuntimeException(
					"The archive " + auxiliaryArchive.getName() +
						" contains more than one RemoteLoadableExtension file");
			}

			if (remoteLoadableExtensions.size() == 1) {
				Iterator<Node> remoteLoadableExtensionsIterator =
					remoteLoadableExtensions.iterator();

				Node remoteLoadableExtensionsNext =
					remoteLoadableExtensionsIterator.next();

				javaArchive.add(
					remoteLoadableExtensionsNext.getAsset(),
					_REMOTE_LOADABLE_EXTENSION_FILE);
			}

			ZipExporter auxiliaryArchiveZipExporter = auxiliaryArchive.as(
				ZipExporter.class);

			InputStream auxiliaryArchiveInputStream =
				auxiliaryArchiveZipExporter.exportAsInputStream();

			ByteArrayAsset byteArrayAsset = new ByteArrayAsset(
				auxiliaryArchiveInputStream);

			String path = "extension/" + auxiliaryArchive.getName();

			javaArchive.addAsResource(byteArrayAsset, path);

			ManifestManager manifestManager = _manifestManagerInstance.get();

			Manifest manifest = manifestManager.putAttributeValue(
				manifestManager.getManifest(javaArchive), "Bundle-ClassPath",
				".", path);

			manifestManager.replaceManifest(javaArchive, manifest);

			try {
				_validateBundleArchive(auxiliaryArchive);

				Manifest auxiliaryArchiveManifest = manifestManager.getManifest(
					(JavaArchive)auxiliaryArchive);

				Attributes mainAttributes =
					auxiliaryArchiveManifest.getMainAttributes();

				String value = mainAttributes.getValue("Import-package");

				if (value != null) {
					String[] importValues = value.split(",");

					manifest = manifestManager.putAttributeValue(
						manifest, "Import-Package", importValues);

					manifestManager.replaceManifest(javaArchive, manifest);
				}

				String bundleActivatorValue = mainAttributes.getValue(
					"Bundle-Activator");

				if ((bundleActivatorValue != null) &&
					!bundleActivatorValue.isEmpty()) {

					_addBundleActivator(javaArchive, bundleActivatorValue);
				}
			}
			catch (BundleException be) {
				if (_logger.isInfoEnabled()) {
					_logger.info(
						"Not processing manifest from " + auxiliaryArchive +
							": " + be.getMessage());
				}
			}
		}
	}

	private List<Archive<?>> _loadAuxiliaryArchives() {
		List<Archive<?>> archives = new ArrayList<>();

		// load based on the Containers ClassLoader

		ServiceLoader serviceLoader = _serviceLoaderInstance.get();

		Collection<AuxiliaryArchiveAppender> archiveAppenders =
			serviceLoader.all(AuxiliaryArchiveAppender.class);

		for (AuxiliaryArchiveAppender archiveAppender : archiveAppenders) {
			Archive<?> auxiliaryArchive =
				archiveAppender.createAuxiliaryArchive();

			if (auxiliaryArchive != null) {
				archives.add(auxiliaryArchive);
			}
		}

		return archives;
	}

	private Manifest _putAttributeValue(
			Manifest manifest, String attributeName, String... attributeValue)
		throws IOException {

		Attributes mainAttributes = manifest.getMainAttributes();

		String attributeValues = mainAttributes.getValue(attributeName);

		Set<String> attributeValueSet = new HashSet<>();

		if (attributeValues != null) {
			Collections.addAll(attributeValueSet, attributeValues.split(","));
		}

		Collections.addAll(attributeValueSet, attributeValue);

		StringBuilder sb = new StringBuilder();

		for (String value : attributeValueSet) {
			sb.append(value);
			sb.append(",");
		}

		if (!attributeValueSet.isEmpty()) {
			sb.setLength(sb.length() - 1);
		}

		attributeValues = sb.toString();

		mainAttributes.putValue(attributeName, attributeValues);

		return manifest;
	}

	private void _replaceManifest(Archive archive, Manifest manifest)
		throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		manifest.write(baos);

		ByteArrayAsset byteArrayAsset = new ByteArrayAsset(baos.toByteArray());

		archive.delete(JarFile.MANIFEST_NAME);

		archive.add(byteArrayAsset, JarFile.MANIFEST_NAME);
	}

	private Map<String, Set<String>> _toImportsWithDirectivesMap(
		String importsInManifest) {

		List<String> packageNamesWithDirectives = Arrays.asList(
			importsInManifest.split(","));

		Map<String, Set<String>> packagesNameToDirectives = new HashMap<>();

		for (String packageNameWithDirectives : packageNamesWithDirectives) {
			LinkedList<String> packageNameAndDirectives = new LinkedList<>();

			Collections.addAll(
				packageNameAndDirectives, packageNameWithDirectives.split(";"));

			String packageName = packageNameAndDirectives.pop();

			Set<String> currentDirectives = packagesNameToDirectives.get(
				packageName);

			if (currentDirectives == null) {
				currentDirectives = new HashSet<>();
			}

			currentDirectives.addAll(packageNameAndDirectives);

			packagesNameToDirectives.put(packageName, currentDirectives);
		}

		return packagesNameToDirectives;
	}

	private void _validateBundleArchive(Archive<?> archive)
		throws BundleException, IOException {

		Manifest manifest = null;

		Node node = archive.get(JarFile.MANIFEST_NAME);

		if (node != null) {
			manifest = new Manifest(node.getAsset().openStream());
		}

		if (manifest != null) {
			OSGiManifestBuilder.validateBundleManifest(manifest);
		}
		else {
			throw new BundleException("can't obtain Manifest");
		}
	}

	private static final String _ACTIVATORS_FILE =
		"/META-INF/services/" + BundleActivator.class.getCanonicalName();

	private static final String _IMPORT_PACKAGE = "Import-Package";

	private static final String _REMOTE_LOADABLE_EXTENSION_FILE =
		"/META-INF/services/" +
			RemoteLoadableExtension.class.getCanonicalName();

	private static final Logger _logger = LoggerFactory.getLogger(
		ApplicationArchiveProcessor.class);

	@Inject
	private Instance<BundleActivatorsManager> _bundleActivatorsManagerInstance;

	@Inject
	private Instance<ManifestManager> _manifestManagerInstance;

	@Inject
	private Instance<ServiceLoader> _serviceLoaderInstance;

}