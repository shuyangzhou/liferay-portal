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

package com.liferay.adaptive.media.image.internal.configuration;

import com.liferay.adaptive.media.exception.AdaptiveMediaImageConfigurationException;
import com.liferay.adaptive.media.exception.AdaptiveMediaImageConfigurationException.InvalidStateAdaptiveMediaImageConfigurationException;
import com.liferay.adaptive.media.exception.AdaptiveMediaRuntimeException;
import com.liferay.adaptive.media.image.configuration.AdaptiveMediaImageConfigurationEntry;
import com.liferay.adaptive.media.image.configuration.AdaptiveMediaImageConfigurationHelper;
import com.liferay.adaptive.media.image.messaging.AdaptiveMediaImageDestinationNames;
import com.liferay.adaptive.media.image.service.AdaptiveMediaImageEntryLocalService;
import com.liferay.portal.kernel.messaging.Destination;
import com.liferay.portal.kernel.messaging.DestinationConfiguration;
import com.liferay.portal.kernel.messaging.DestinationFactory;
import com.liferay.portal.kernel.messaging.Message;
import com.liferay.portal.kernel.messaging.MessageBus;
import com.liferay.portal.kernel.settings.CompanyServiceSettingsLocator;
import com.liferay.portal.kernel.settings.ModifiableSettings;
import com.liferay.portal.kernel.settings.PortletPreferencesSettings;
import com.liferay.portal.kernel.settings.Settings;
import com.liferay.portal.kernel.settings.SettingsException;
import com.liferay.portal.kernel.settings.SettingsFactoryUtil;
import com.liferay.portal.kernel.util.FriendlyURLNormalizerUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.portlet.PortletPreferences;
import javax.portlet.ValidatorException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Adolfo Pérez
 */
@Component(
	immediate = true, service = AdaptiveMediaImageConfigurationHelper.class
)
public class AdaptiveMediaImageConfigurationHelperImpl
	implements AdaptiveMediaImageConfigurationHelper {

	@Override
	public AdaptiveMediaImageConfigurationEntry
			addAdaptiveMediaImageConfigurationEntry(
				long companyId, String name, String description, String uuid,
				Map<String, String> properties)
		throws AdaptiveMediaImageConfigurationException, IOException {

		_checkName(name);
		_checkProperties(properties);

		String normalizedUuid = FriendlyURLNormalizerUtil.normalize(uuid);

		_checkUuid(normalizedUuid);

		Collection<AdaptiveMediaImageConfigurationEntry> configurationEntries =
			getAdaptiveMediaImageConfigurationEntries(
				companyId, configurationEntry -> true);

		_checkDuplicatesName(configurationEntries, name);

		_checkDuplicatesUuid(configurationEntries, normalizedUuid);

		List<AdaptiveMediaImageConfigurationEntry> updatedConfigurationEntries =
			new ArrayList<>(configurationEntries);

		updatedConfigurationEntries.removeIf(
			configurationEntry -> normalizedUuid.equals(
				configurationEntry.getUUID()));

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			new AdaptiveMediaImageConfigurationEntryImpl(
				name, description, normalizedUuid, properties, true);

		updatedConfigurationEntries.add(configurationEntry);

		_updateConfiguration(companyId, updatedConfigurationEntries);

		_triggerConfigurationEvent(configurationEntry);

		return configurationEntry;
	}

	@Override
	public void deleteAdaptiveMediaImageConfigurationEntry(
			long companyId, String uuid)
		throws InvalidStateAdaptiveMediaImageConfigurationException,
			IOException {

		Optional<AdaptiveMediaImageConfigurationEntry>
			configurationEntryOptional =
				getAdaptiveMediaImageConfigurationEntry(companyId, uuid);

		if (!configurationEntryOptional.isPresent()) {
			return;
		}

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			configurationEntryOptional.get();

		if (configurationEntry.isEnabled()) {
			throw new InvalidStateAdaptiveMediaImageConfigurationException();
		}

		forceDeleteAdaptiveMediaImageConfigurationEntry(companyId, uuid);
	}

	@Override
	public void disableAdaptiveMediaImageConfigurationEntry(
			long companyId, String uuid)
		throws IOException {

		Optional<AdaptiveMediaImageConfigurationEntry>
			configurationEntryOptional =
				getAdaptiveMediaImageConfigurationEntry(companyId, uuid);

		if (!configurationEntryOptional.isPresent()) {
			return;
		}

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			configurationEntryOptional.get();

		if (!configurationEntry.isEnabled()) {
			return;
		}

		Collection<AdaptiveMediaImageConfigurationEntry> configurationEntries =
			getAdaptiveMediaImageConfigurationEntries(
				companyId, curConfigurationEntry -> true);

		List<AdaptiveMediaImageConfigurationEntry> updatedConfigurationEntries =
			new ArrayList<>(configurationEntries);

		updatedConfigurationEntries.removeIf(
			curConfigurationEntry -> uuid.equals(
				curConfigurationEntry.getUUID()));

		AdaptiveMediaImageConfigurationEntry newConfigurationEntry =
			new AdaptiveMediaImageConfigurationEntryImpl(
				configurationEntry.getName(),
				configurationEntry.getDescription(),
				configurationEntry.getUUID(),
				configurationEntry.getProperties(), false);

		updatedConfigurationEntries.add(newConfigurationEntry);

		_updateConfiguration(companyId, updatedConfigurationEntries);

		_triggerConfigurationEvent(configurationEntry);
	}

	@Override
	public void enableAdaptiveMediaImageConfigurationEntry(
			long companyId, String uuid)
		throws IOException {

		Optional<AdaptiveMediaImageConfigurationEntry>
			configurationEntryOptional =
				getAdaptiveMediaImageConfigurationEntry(companyId, uuid);

		if (!configurationEntryOptional.isPresent()) {
			return;
		}

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			configurationEntryOptional.get();

		if (configurationEntry.isEnabled()) {
			return;
		}

		Collection<AdaptiveMediaImageConfigurationEntry> configurationEntries =
			getAdaptiveMediaImageConfigurationEntries(
				companyId, curConfigurationEntry -> true);

		List<AdaptiveMediaImageConfigurationEntry> updatedConfigurationEntries =
			new ArrayList<>(configurationEntries);

		updatedConfigurationEntries.removeIf(
			curConfigurationEntry -> uuid.equals(
				curConfigurationEntry.getUUID()));

		AdaptiveMediaImageConfigurationEntry newConfigurationEntry =
			new AdaptiveMediaImageConfigurationEntryImpl(
				configurationEntry.getName(),
				configurationEntry.getDescription(),
				configurationEntry.getUUID(),
				configurationEntry.getProperties(), true);

		updatedConfigurationEntries.add(newConfigurationEntry);

		_updateConfiguration(companyId, updatedConfigurationEntries);

		_triggerConfigurationEvent(configurationEntry);
	}

	@Override
	public void forceDeleteAdaptiveMediaImageConfigurationEntry(
			long companyId, String uuid)
		throws IOException {

		Optional<AdaptiveMediaImageConfigurationEntry>
			configurationEntryOptional =
				getAdaptiveMediaImageConfigurationEntry(companyId, uuid);

		if (!configurationEntryOptional.isPresent()) {
			return;
		}

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			configurationEntryOptional.get();

		_adaptiveMediaImageEntryLocalService.deleteAdaptiveMediaImageEntries(
			companyId, configurationEntry);

		Collection<AdaptiveMediaImageConfigurationEntry> configurationEntries =
			getAdaptiveMediaImageConfigurationEntries(
				companyId, curConfigurationEntry -> true);

		List<AdaptiveMediaImageConfigurationEntry> updatedConfigurationEntries =
			new ArrayList<>(configurationEntries);

		updatedConfigurationEntries.removeIf(
			curConfigurationEntry -> uuid.equals(
				curConfigurationEntry.getUUID()));

		_updateConfiguration(companyId, updatedConfigurationEntries);

		_triggerConfigurationEvent(configurationEntry);
	}

	@Override
	public Collection<AdaptiveMediaImageConfigurationEntry>
		getAdaptiveMediaImageConfigurationEntries(long companyId) {

		Stream<AdaptiveMediaImageConfigurationEntry> configurationEntryStream =
			_getConfigurationEntries(companyId);

		return configurationEntryStream.filter(
			AdaptiveMediaImageConfigurationEntry::isEnabled
		).sorted(
			Comparator.comparing(AdaptiveMediaImageConfigurationEntry::getName)
		).collect(
			Collectors.toList()
		);
	}

	@Override
	public Collection<AdaptiveMediaImageConfigurationEntry>
		getAdaptiveMediaImageConfigurationEntries(
			long companyId,
			Predicate<? super AdaptiveMediaImageConfigurationEntry> predicate) {

		Stream<AdaptiveMediaImageConfigurationEntry> configurationEntryStream =
			_getConfigurationEntries(companyId);

		return configurationEntryStream.filter(
			predicate
		).sorted(
			Comparator.comparing(AdaptiveMediaImageConfigurationEntry::getName)
		).collect(
			Collectors.toList()
		);
	}

	@Override
	public Optional<AdaptiveMediaImageConfigurationEntry>
		getAdaptiveMediaImageConfigurationEntry(
			long companyId, String configurationEntryUUID) {

		Stream<AdaptiveMediaImageConfigurationEntry> configurationEntryStream =
			_getConfigurationEntries(companyId);

		return configurationEntryStream.filter(
			configurationEntry -> configurationEntryUUID.equals(
				configurationEntry.getUUID())
		).findFirst();
	}

	@Override
	public AdaptiveMediaImageConfigurationEntry
			updateAdaptiveMediaImageConfigurationEntry(
				long companyId, String oldUuid, String name, String description,
				String newUuid, Map<String, String> properties)
		throws AdaptiveMediaImageConfigurationException, IOException {

		_checkName(name);
		_checkProperties(properties);

		String normalizedUuid = FriendlyURLNormalizerUtil.normalize(newUuid);

		_checkUuid(normalizedUuid);

		Collection<AdaptiveMediaImageConfigurationEntry> configurationEntries =
			getAdaptiveMediaImageConfigurationEntries(
				companyId, configurationEntry -> true);

		Stream<AdaptiveMediaImageConfigurationEntry> configurationEntryStream =
			configurationEntries.stream();

		Optional<AdaptiveMediaImageConfigurationEntry>
			oldConfigurationEntryOptional = configurationEntryStream.filter(
				configurationEntry -> oldUuid.equals(
					configurationEntry.getUUID())
			).findFirst();

		AdaptiveMediaImageConfigurationEntry oldConfigurationEntry =
			oldConfigurationEntryOptional.orElseThrow(
				() ->
					new AdaptiveMediaImageConfigurationException.
						NoSuchAdaptiveMediaImageConfigurationException(
							"{uuid=" + oldUuid + "}"));

		if (!name.equals(oldConfigurationEntry.getName())) {
			_checkDuplicatesName(configurationEntries, name);
		}

		if (!oldUuid.equals(normalizedUuid)) {
			_checkDuplicatesUuid(configurationEntries, normalizedUuid);
		}

		List<AdaptiveMediaImageConfigurationEntry> updatedConfigurationEntries =
			new ArrayList<>(configurationEntries);

		updatedConfigurationEntries.removeIf(
			configurationEntry -> oldUuid.equals(configurationEntry.getUUID()));

		AdaptiveMediaImageConfigurationEntry configurationEntry =
			new AdaptiveMediaImageConfigurationEntryImpl(
				name, description, normalizedUuid, properties,
				oldConfigurationEntry.isEnabled());

		updatedConfigurationEntries.add(configurationEntry);

		_updateConfiguration(companyId, updatedConfigurationEntries);

		_triggerConfigurationEvent(
			new AdaptiveMediaImageConfigurationEntry[] {
				oldConfigurationEntry, configurationEntry
			});

		return configurationEntry;
	}

	@Activate
	protected void activate() {
		DestinationConfiguration destinationConfiguration =
			new DestinationConfiguration(
				DestinationConfiguration.DESTINATION_TYPE_SYNCHRONOUS,
				AdaptiveMediaImageDestinationNames.
					ADAPTIVE_MEDIA_IMAGE_CONFIGURATION);

		Destination destination = _destinationFactory.createDestination(
			destinationConfiguration);

		_messageBus.addDestination(destination);
	}

	@Deactivate
	protected void deactivate() {
		_messageBus.removeDestination(
			AdaptiveMediaImageDestinationNames.
				ADAPTIVE_MEDIA_IMAGE_CONFIGURATION);
	}

	@Reference(unbind = "-")
	protected void setAdaptiveMediaImageConfigurationEntryParser(
		AdaptiveMediaImageConfigurationEntryParser configurationEntryParser) {

		_configurationEntryParser = configurationEntryParser;
	}

	private static final boolean _isPositiveNumber(String s) {
		Matcher matcher = _positiveNumberPattern.matcher(s);

		return matcher.matches();
	}

	private void _checkDuplicatesName(
			Collection<AdaptiveMediaImageConfigurationEntry>
				configurationEntries,
			String name)
		throws AdaptiveMediaImageConfigurationException {

		Stream<AdaptiveMediaImageConfigurationEntry>
			adaptiveMediaImageConfigurationEntryStream =
				configurationEntries.stream();

		Optional<AdaptiveMediaImageConfigurationEntry>
			duplicateNameConfigurationEntryOptional =
				adaptiveMediaImageConfigurationEntryStream.filter(
					configurationEntry -> name.equals(
						configurationEntry.getName())
				).findFirst();

		if (duplicateNameConfigurationEntryOptional.isPresent()) {
			throw new AdaptiveMediaImageConfigurationException.
				DuplicateAdaptiveMediaImageConfigurationNameException();
		}
	}

	private void _checkDuplicatesUuid(
			Collection<AdaptiveMediaImageConfigurationEntry>
				configurationEntries,
			String uuid)
		throws AdaptiveMediaImageConfigurationException {

		Stream<AdaptiveMediaImageConfigurationEntry>
			adaptiveMediaImageConfigurationEntryStream =
				configurationEntries.stream();

		Optional<AdaptiveMediaImageConfigurationEntry>
			duplicateUuidConfigurationEntryOptional =
				adaptiveMediaImageConfigurationEntryStream.filter(
					configurationEntry -> uuid.equals(
						configurationEntry.getUUID())
				).findFirst();

		if (duplicateUuidConfigurationEntryOptional.isPresent()) {
			throw new AdaptiveMediaImageConfigurationException.
				DuplicateAdaptiveMediaImageConfigurationUuidException();
		}
	}

	private void _checkName(String name)
		throws AdaptiveMediaImageConfigurationException {

		if (Validator.isNull(name)) {
			throw new AdaptiveMediaImageConfigurationException.
				InvalidNameException();
		}
	}

	private void _checkProperties(Map<String, String> properties)
		throws AdaptiveMediaImageConfigurationException {

		String maxHeightString = properties.get("max-height");

		if (Validator.isNotNull(maxHeightString) &&
			!_isPositiveNumber(maxHeightString)) {

			throw new AdaptiveMediaImageConfigurationException.
				InvalidHeightException();
		}

		String maxWidthString = properties.get("max-width");

		if (Validator.isNotNull(maxWidthString) &&
			!_isPositiveNumber(maxWidthString)) {

			throw new AdaptiveMediaImageConfigurationException.
				InvalidWidthException();
		}

		if (Validator.isNull(maxHeightString) &&
			Validator.isNull(maxWidthString)) {

			throw new AdaptiveMediaImageConfigurationException.
				RequiredWidthOrHeightException();
		}
	}

	private void _checkUuid(String uuid)
		throws AdaptiveMediaImageConfigurationException {

		if (Validator.isNull(uuid)) {
			throw new AdaptiveMediaImageConfigurationException.
				InvalidUuidException();
		}
	}

	private Stream<AdaptiveMediaImageConfigurationEntry>
		_getConfigurationEntries(long companyId) {

		try {
			Settings settings = SettingsFactoryUtil.getSettings(
				new CompanyServiceSettingsLocator(
					companyId,
					AdaptiveMediaImageCompanyConfiguration.class.getName()));

			Optional<String[]> nullableImageVariants =
				_getNullableImageVariants(settings);

			String[] imageVariants = nullableImageVariants.orElseGet(
				() -> settings.getValues("imageVariants", new String[0]));

			return Stream.of(imageVariants).map(
				_configurationEntryParser::parse);
		}
		catch (SettingsException se) {
			throw new AdaptiveMediaRuntimeException.InvalidConfiguration(se);
		}
	}

	private Optional<String[]> _getNullableImageVariants(Settings settings) {
		PortletPreferencesSettings portletPreferencesSettings =
			(PortletPreferencesSettings)settings;

		PortletPreferences portletPreferences =
			portletPreferencesSettings.getPortletPreferences();

		Map<String, String[]> map = portletPreferences.getMap();

		return Optional.ofNullable(map.get("imageVariants"));
	}

	private void _triggerConfigurationEvent(Object payload) {
		Message message = new Message();

		message.setPayload(payload);

		_messageBus.sendMessage(
			AdaptiveMediaImageDestinationNames.
				ADAPTIVE_MEDIA_IMAGE_CONFIGURATION,
			message);
	}

	private void _updateConfiguration(
			long companyId,
			List<AdaptiveMediaImageConfigurationEntry> configurationEntries)
		throws IOException {

		try {
			Settings settings = SettingsFactoryUtil.getSettings(
				new CompanyServiceSettingsLocator(
					companyId,
					AdaptiveMediaImageCompanyConfiguration.class.getName()));

			ModifiableSettings modifiableSettings =
				settings.getModifiableSettings();

			Stream<AdaptiveMediaImageConfigurationEntry>
				configurationEntryStream = configurationEntries.stream();

			List<String> imageVariants = configurationEntryStream.map(
				_configurationEntryParser::getConfigurationString
			).collect(
				Collectors.toList()
			);

			modifiableSettings.setValues(
				"imageVariants",
				imageVariants.toArray(new String[imageVariants.size()]));

			modifiableSettings.store();
		}
		catch (SettingsException | ValidatorException e) {
			throw new AdaptiveMediaRuntimeException.InvalidConfiguration(e);
		}
	}

	private static final Pattern _positiveNumberPattern = Pattern.compile(
		"\\d*[1-9]\\d*");

	@Reference
	private AdaptiveMediaImageEntryLocalService
		_adaptiveMediaImageEntryLocalService;

	private AdaptiveMediaImageConfigurationEntryParser
		_configurationEntryParser;

	@Reference
	private DestinationFactory _destinationFactory;

	@Reference
	private MessageBus _messageBus;

}