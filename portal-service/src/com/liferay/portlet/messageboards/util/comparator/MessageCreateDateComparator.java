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

package com.liferay.portlet.messageboards.util.comparator;

import com.liferay.portal.kernel.dao.db.DBFactoryUtil;
import com.liferay.portal.kernel.util.DateUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portlet.messageboards.model.MBMessage;

/**
 * @author Brian Wing Shun Chan
 */
public class MessageCreateDateComparator extends OrderByComparator<MBMessage> {

	public static final String ORDER_BY_ASC =
		"MBMessage.createDate ASC, MBMessage.messageId ASC";

	public static final String ORDER_BY_DESC =
		"MBMessage.createDate DESC, MBMessage.messageId DESC";

	public static final String[] ORDER_BY_FIELDS = {"createDate, messageId"};

	public MessageCreateDateComparator(boolean ascending) {
		_ascending = ascending;
	}

	@Override
	public int compare(MBMessage message1, MBMessage message2) {
		int value = DateUtil.compareTo(
			message1.getCreateDate(), message2.getCreateDate(),
			DBFactoryUtil.getDB());

		if (value == 0) {
			value = Long.compare(
				message1.getMessageId(), message2.getMessageId());
		}

		if (_ascending) {
			return value;
		}
		else {
			return -value;
		}
	}

	@Override
	public String getOrderBy() {
		if (_ascending) {
			return ORDER_BY_ASC;
		}
		else {
			return ORDER_BY_DESC;
		}
	}

	@Override
	public String[] getOrderByFields() {
		return ORDER_BY_FIELDS;
	}

	@Override
	public boolean isAscending() {
		return _ascending;
	}

	private final boolean _ascending;

}