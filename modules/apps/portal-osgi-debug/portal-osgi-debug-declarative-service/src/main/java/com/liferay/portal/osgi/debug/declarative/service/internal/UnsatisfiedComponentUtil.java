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

package com.liferay.portal.osgi.debug.declarative.service.internal;

import com.liferay.petra.string.StringBundler;

import java.util.Collection;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.runtime.ServiceComponentRuntime;
import org.osgi.service.component.runtime.dto.ComponentConfigurationDTO;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;
import org.osgi.service.component.runtime.dto.SatisfiedReferenceDTO;
import org.osgi.service.component.runtime.dto.UnsatisfiedReferenceDTO;

/**
 * @author Tina Tian
 */
public class UnsatisfiedComponentUtil {

	public static String listUnsatisfiedComponents(
		ServiceComponentRuntime serviceComponentRuntime, Bundle... bundles) {

		StringBundler sb = new StringBundler();

		for (Bundle bundle : bundles) {
			_listUnsatisfiedComponents(serviceComponentRuntime, bundle, sb);
		}

		String result = sb.toString();

		if (!result.isEmpty()) {
			StringBundler debugSB = new StringBundler();

			Bundle bundle = bundles[0];

			BundleContext bundleContext = bundle.getBundleContext();

			bundle = bundleContext.getBundle(10);

			Collection<ComponentDescriptionDTO> componentDescriptionDTOs =
				serviceComponentRuntime.getComponentDescriptionDTOs(bundle);

			for (ComponentDescriptionDTO componentDescriptionDTO :
					componentDescriptionDTOs) {

				Collection<ComponentConfigurationDTO>
					componentConfigurationDTOs =
						serviceComponentRuntime.getComponentConfigurationDTOs(
							componentDescriptionDTO);

				for (ComponentConfigurationDTO componentConfigurationDTO :
						componentConfigurationDTOs) {

					debugSB.append("\n\tDeclarative Service {id: ");
					debugSB.append(componentConfigurationDTO.id);
					debugSB.append(", name: ");
					debugSB.append(componentDescriptionDTO.name);
					debugSB.append(", \n\t\tsatisfied references: ");

					for (SatisfiedReferenceDTO satisfiedReferenceDTO :
							componentConfigurationDTO.satisfiedReferences) {

						debugSB.append("\n\t\t\t{name: ");
						debugSB.append(satisfiedReferenceDTO.name);
						debugSB.append(", target: ");
						debugSB.append(satisfiedReferenceDTO.target);
						debugSB.append("}");
					}

					debugSB.append(", \n\t\tunsatisfied references: ");

					for (UnsatisfiedReferenceDTO unsatisfiedReferenceDTO :
							componentConfigurationDTO.unsatisfiedReferences) {

						debugSB.append("\n\t\t\t{name: ");
						debugSB.append(unsatisfiedReferenceDTO.name);
						debugSB.append(", target: ");
						debugSB.append(unsatisfiedReferenceDTO.target);
						debugSB.append("}");
					}

					debugSB.append("\n\t}");
				}
			}

			System.out.println("######## Single out bundle 10 : " + debugSB);
		}

		return result;
	}

	private static String _collectUnsatisfiedInformation(
		ServiceComponentRuntime serviceComponentRuntime, Bundle bundle) {

		StringBundler sb = new StringBundler();

		Collection<ComponentDescriptionDTO> componentDescriptionDTOs =
			serviceComponentRuntime.getComponentDescriptionDTOs(bundle);

		for (ComponentDescriptionDTO componentDescriptionDTO :
				componentDescriptionDTOs) {

			Collection<ComponentConfigurationDTO> componentConfigurationDTOs =
				serviceComponentRuntime.getComponentConfigurationDTOs(
					componentDescriptionDTO);

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

		sb.append("\n\tDeclarative Service {id: ");
		sb.append(componentConfigurationDTO.id);
		sb.append(", name: ");
		sb.append(componentDescriptionDTO.name);
		sb.append(", unsatisfied references: ");

		for (UnsatisfiedReferenceDTO unsatisfiedReferenceDTO :
				componentConfigurationDTO.unsatisfiedReferences) {

			sb.append("\n\t\t{name: ");
			sb.append(unsatisfiedReferenceDTO.name);
			sb.append(", target: ");
			sb.append(unsatisfiedReferenceDTO.target);
			sb.append("}");
		}

		sb.append("\n\t}");
	}

	private static void _listUnsatisfiedComponents(
		ServiceComponentRuntime serviceComponentRuntime, Bundle bundle,
		StringBundler sb) {

		if (bundle.getState() != Bundle.ACTIVE) {
			return;
		}

		String unsatisfiedInformation = _collectUnsatisfiedInformation(
			serviceComponentRuntime, bundle);

		if (unsatisfiedInformation.isEmpty()) {
			return;
		}

		sb.append("\nBundle {id: ");
		sb.append(bundle.getBundleId());
		sb.append(", name: ");
		sb.append(bundle.getSymbolicName());
		sb.append(", version: ");
		sb.append(bundle.getVersion());
		sb.append("}");
		sb.append(unsatisfiedInformation);
	}

}