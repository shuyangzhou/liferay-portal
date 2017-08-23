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

package com.liferay.portal.spring.extender.test.service.impl;

import com.liferay.portal.spring.extender.service.ServiceReference;
import com.liferay.portal.spring.extender.test.reference.SpringExtenderTestComponentReference;
import com.liferay.portal.spring.extender.test.service.base.SpringExtenderTestComponentLocalServiceBaseImpl;

/**
 * The implementation of the spring extender test component local service.
 *
 * <p>
 * All custom service methods should be put in this class. Whenever methods are added, rerun ServiceBuilder to copy their definitions into the {@link com.liferay.portal.spring.extender.test.service.SpringExtenderTestComponentLocalService} interface.
 *
 * <p>
 * This is a local service. Methods of this service will not have security checks based on the propagated JAAS credentials because this service can only be accessed from within the same VM.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see SpringExtenderTestComponentLocalServiceBaseImpl
 * @see com.liferay.portal.spring.extender.test.service.SpringExtenderTestComponentLocalServiceUtil
 */
public class SpringExtenderTestComponentLocalServiceImpl
	extends SpringExtenderTestComponentLocalServiceBaseImpl {

	/**
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never reference this class directly. Always use {@link com.liferay.portal.spring.extender.test.service.SpringExtenderTestComponentLocalServiceUtil} to access the spring extender test component local service.
	 */

	@ServiceReference(type = SpringExtenderTestComponentReference.class)
	protected SpringExtenderTestComponentReference
		springExtenderTestComponentReference;

}