/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.template;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.io.unsync.UnsyncByteArrayInputStream;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.CharPool;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portlet.journal.NoSuchTemplateException;
import com.liferay.portlet.journal.model.JournalTemplate;
import com.liferay.portlet.journal.service.JournalTemplateLocalServiceUtil;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author Mika Koivisto
 * @author Tina Tian
 */
public class JournalTemplateResourceLoader
	extends TemplateResourceLoader<JournalTemplate> {

	@Override
	public TemplateResource<JournalTemplate> findTemplateReource(
			String templateId)
		throws IOException {

		int pos = templateId.indexOf(JOURNAL_SEPARATOR + StringPool.SLASH);

		if (pos == -1) {
			return null;
		}

		try {
			int x = templateId.indexOf(CharPool.SLASH, pos);
			int y = templateId.indexOf(CharPool.SLASH, x + 1);
			int z = templateId.indexOf(CharPool.SLASH, y + 1);

			long companyId = GetterUtil.getLong(templateId.substring(x + 1, y));
			long groupId = GetterUtil.getLong(templateId.substring(y + 1, z));
			String journalTemplateId = templateId.substring(z + 1);

			if (_log.isDebugEnabled()) {
				_log.debug(
					"Loading {companyId=" + companyId + ", groupId=" +
						groupId + ", templateId=" + journalTemplateId + "}");
			}

			JournalTemplate journalTemplate =
				JournalTemplateLocalServiceUtil.getTemplate(
					groupId, journalTemplateId);

			return new TemplateResource<JournalTemplate>(
				journalTemplateId, journalTemplate, this);

		}
		catch (NoSuchTemplateException nste) {
			return null;
		}
		catch (PortalException pe) {
			throw new IOException("Template {" + templateId + "} not found");
		}
		catch (SystemException se) {
			throw new IOException("Template {" + templateId + "} not found");
		}
	}

	@Override
	public InputStream getInputStream(JournalTemplate resource)
		throws IOException {

		String xsl = resource.getXsl();

		return new UnsyncByteArrayInputStream(xsl.getBytes());
	}

	private static Log _log = LogFactoryUtil.getLog(
		JournalTemplateResourceLoader.class);

}