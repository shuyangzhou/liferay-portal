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
package com.liferay.osgi.util.component;

import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import org.osgi.framework.Bundle;
import org.osgi.framework.Constants;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.runtime.ServiceComponentRuntime;
import org.osgi.service.component.runtime.dto.ComponentConfigurationDTO;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;
import org.osgi.service.component.runtime.dto.UnsatisfiedReferenceDTO;

/**
 * @author Tina Tian
 */
public class ComponentUtil {

	public static String listUnsatisfiedComponents(
		ConfigurationAdmin configurationAdmin,
		ServiceComponentRuntime serviceComponentRuntime, Bundle... bundles) {

		StringBundler sb = new StringBundler();

		for (Bundle bundle : bundles) {
			String unsatisfiedComponents = _listUnsatisfiedComponents(
				configurationAdmin, serviceComponentRuntime, bundle);

			if (!unsatisfiedComponents.isEmpty()) {
				sb.append(unsatisfiedComponents);
			}
		}

		return sb.toString();
	}

	private static void _collectUnsatisfiedConfigurations(
		ConfigurationAdmin configurationAdmin,
		ComponentDescriptionDTO componentDescriptionDTO, StringBundler sb) {

		if (!Objects.equals(
				componentDescriptionDTO.configurationPolicy, "require")) {

			return;
		}

		List<String> missingConfigurationPids = new ArrayList<>();
		List<String> errorConfigurationPids = new ArrayList<>();

		for (String configurationPid :
				componentDescriptionDTO.configurationPid) {

			try {
				StringBundler filterSB = new StringBundler(5);

				filterSB.append(StringPool.OPEN_PARENTHESIS);
				filterSB.append(Constants.SERVICE_PID);
				filterSB.append(StringPool.EQUAL);
				filterSB.append(configurationPid);
				filterSB.append(StringPool.CLOSE_PARENTHESIS);

				if (ArrayUtil.isEmpty(
						configurationAdmin.listConfigurations(
							filterSB.toString()))) {

					missingConfigurationPids.add(configurationPid);
				}
			}
			catch (Exception e) {
				errorConfigurationPids.add(configurationPid);
			}
		}

		if (missingConfigurationPids.isEmpty() &&
			errorConfigurationPids.isEmpty()) {

			return;
		}

		sb.append("\n    Component: name=");
		sb.append(componentDescriptionDTO.name);
		sb.append(",policy=require");

		if (!missingConfigurationPids.isEmpty()) {
			sb.append(",unsatisfied pids={");

			for (int i = 0; i < missingConfigurationPids.size(); i++) {
				sb.append(missingConfigurationPids.get(i));

				if (i != (missingConfigurationPids.size() - 1)) {
					sb.append(",");
				}
			}

			sb.append("}");
		}

		if (!errorConfigurationPids.isEmpty()) {
			sb.append(",unable to list configurations for pids={");

			for (int i = 0; i < errorConfigurationPids.size(); i++) {
				sb.append(errorConfigurationPids.get(i));

				if (i != (errorConfigurationPids.size() - 1)) {
					sb.append(",");
				}
			}

			sb.append("}");
		}
	}

	private static String _collectUnsatisfiedInfomation(
		ConfigurationAdmin configurationAdmin,
		ServiceComponentRuntime serviceComponentRuntime, Bundle bundle) {

		StringBundler sb = new StringBundler();

		Collection<ComponentDescriptionDTO> componentDescriptionDTOs =
			serviceComponentRuntime.getComponentDescriptionDTOs(bundle);

		for (ComponentDescriptionDTO componentDescriptionDTO :
				componentDescriptionDTOs) {

			Collection<ComponentConfigurationDTO> componentConfigurationDTOs =
				serviceComponentRuntime.getComponentConfigurationDTOs(
					componentDescriptionDTO);

			if (componentConfigurationDTOs.isEmpty()) {
				_collectUnsatisfiedConfigurations(
					configurationAdmin, componentDescriptionDTO, sb);

				continue;
			}

			for (ComponentConfigurationDTO componentConfigurationDTO :
					componentConfigurationDTOs) {

				if (componentConfigurationDTO.state ==
						ComponentConfigurationDTO.UNSATISFIED_REFERENCE) {

					_collectUnsatisfiedReferences(
						componentDescriptionDTO, componentConfigurationDTO, sb);
				}
			}
		}

		return sb.toString();
	}

	private static void _collectUnsatisfiedReferences(
		ComponentDescriptionDTO componentDescriptionDTO,
		ComponentConfigurationDTO componentConfigurationDTO, StringBundler sb) {

		sb.append("\n    Component: id=");
		sb.append(componentConfigurationDTO.id);
		sb.append(",name=");
		sb.append(componentDescriptionDTO.name);
		sb.append(",unsatisfied references=");

		for (UnsatisfiedReferenceDTO unsatisfiedReferenceDTO :
				componentConfigurationDTO.unsatisfiedReferences) {

			sb.append("{name: ");
			sb.append(unsatisfiedReferenceDTO.name);
			sb.append(", target: ");
			sb.append(unsatisfiedReferenceDTO.target);
			sb.append("}");
		}
	}

	private static String _listUnsatisfiedComponents(
		ConfigurationAdmin configurationAdmin,
		ServiceComponentRuntime serviceComponentRuntime, Bundle bundle) {

		if (bundle.getState() != Bundle.ACTIVE) {
			return StringPool.BLANK;
		}

		String unsatisfiedInfomation = _collectUnsatisfiedInfomation(
			configurationAdmin, serviceComponentRuntime, bundle);

		if (unsatisfiedInfomation.isEmpty()) {
			return StringPool.BLANK;
		}

		StringBundler sb = new StringBundler();

		sb.append("\nBundle: id=");
		sb.append(bundle.getBundleId());
		sb.append(",name=");
		sb.append(bundle.getSymbolicName());
		sb.append(",version=");
		sb.append(bundle.getVersion());
		sb.append(unsatisfiedInfomation);

		return sb.toString();
	}

}