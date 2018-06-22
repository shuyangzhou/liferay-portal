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

package com.liferay.captcha.internal.upgrade.v1_0_0;

import com.liferay.captcha.configuration.CaptchaConfiguration;
import com.liferay.captcha.internal.constants.LegacyCaptchaPropsKeys;
import com.liferay.portal.configuration.upgrade.PrefsPropsToConfigurationUpgradeHelper;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

/**
 * @author Pei-Jung Lan
 * @author Drew Brokke
 */
public class UpgradeCaptchaConfiguration extends UpgradeProcess {

	public UpgradeCaptchaConfiguration(
		PrefsPropsToConfigurationUpgradeHelper
			prefsPropsToConfigurationUpgradeHelper) {

		_prefsPropsToConfigurationUpgradeHelper =
			prefsPropsToConfigurationUpgradeHelper;
	}

	@Override
	protected void doUpgrade() throws Exception {
		_prefsPropsToConfigurationUpgradeHelper.mapConfigurations(
			CaptchaConfiguration.class,
			(captchaConfiguration, configurationMappingCollector) -> {
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.CAPTCHA_ENGINE_IMPL, "captchaEngine",
					captchaConfiguration.captchaEngine());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.CAPTCHA_CHECK_PORTAL_CREATE_ACCOUNT,
					"createAccountCaptchaEnabled",
					captchaConfiguration.createAccountCaptchaEnabled());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.CAPTCHA_MAX_CHALLENGES,
					"maxChallenges", captchaConfiguration.maxChallenges());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.
						CAPTCHA_CHECK_PORTLET_MESSAGE_BOARDS_EDIT_CATEGORY,
					"messageBoardsEditCategoryCaptchaEnabled",
					captchaConfiguration.
						messageBoardsEditCategoryCaptchaEnabled());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.
						CAPTCHA_CHECK_PORTLET_MESSAGE_BOARDS_EDIT_MESSAGE,
					"messageBoardsEditMessageCaptchaEnabled",
					captchaConfiguration.
						messageBoardsEditMessageCaptchaEnabled());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.
						CAPTCHA_ENGINE_RECAPTCHA_URL_NOSCRIPT,
					"reCaptchaNoScriptURL",
					captchaConfiguration.reCaptchaNoScriptURL());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.CAPTCHA_ENGINE_RECAPTCHA_KEY_PRIVATE,
					"reCaptchaPrivateKey",
					captchaConfiguration.reCaptchaPrivateKey());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.CAPTCHA_ENGINE_RECAPTCHA_KEY_PUBLIC,
					"reCaptchaPublicKey",
					captchaConfiguration.reCaptchaPublicKey());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.CAPTCHA_ENGINE_RECAPTCHA_URL_SCRIPT,
					"reCaptchaScriptURL",
					captchaConfiguration.reCaptchaScriptURL());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.CAPTCHA_ENGINE_RECAPTCHA_URL_VERIFY,
					"reCaptchaVerifyURL",
					captchaConfiguration.reCaptchaVerifyURL());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.CAPTCHA_CHECK_PORTAL_SEND_PASSWORD,
					"sendPasswordCaptchaEnabled",
					captchaConfiguration.sendPasswordCaptchaEnabled());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.
						CAPTCHA_ENGINE_SIMPLECAPTCHA_BACKGROUND_PRODUCERS,
					"simpleCaptchaBackgroundProducers",
					captchaConfiguration.simpleCaptchaBackgroundProducers());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.
						CAPTCHA_ENGINE_SIMPLECAPTCHA_GIMPY_RENDERERS,
					"simpleCaptchaGimpyRenderers",
					captchaConfiguration.simpleCaptchaGimpyRenderers());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.CAPTCHA_ENGINE_SIMPLECAPTCHA_HEIGHT,
					"simpleCaptchaHeight",
					captchaConfiguration.simpleCaptchaHeight());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.
						CAPTCHA_ENGINE_SIMPLECAPTCHA_NOISE_PRODUCERS,
					"simpleCaptchaNoiseProducers",
					captchaConfiguration.simpleCaptchaNoiseProducers());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.
						CAPTCHA_ENGINE_SIMPLECAPTCHA_TEXT_PRODUCERS,
					"simpleCaptchaTextProducers",
					captchaConfiguration.simpleCaptchaTextProducers());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.CAPTCHA_ENGINE_SIMPLECAPTCHA_WIDTH,
					"simpleCaptchaWidth",
					captchaConfiguration.simpleCaptchaWidth());
				configurationMappingCollector.mapConfiguration(
					LegacyCaptchaPropsKeys.
						CAPTCHA_ENGINE_SIMPLECAPTCHA_WORD_RENDERERS,
					"simpleCaptchaWordRenderers",
					captchaConfiguration.simpleCaptchaWordRenderers());
			});
	}

	private final PrefsPropsToConfigurationUpgradeHelper
		_prefsPropsToConfigurationUpgradeHelper;

}