/**
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.events;

import com.liferay.portal.kernel.events.ActionException;
import com.liferay.portal.kernel.events.LifecycleAction;
import com.liferay.portal.kernel.events.LifecycleEvent;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.InstancePool;
import com.liferay.portal.kernel.util.Validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Brian Wing Shun Chan
 * @author Michael Young
 * @author Raymond Augé
 */
public class EventsProcessorImpl implements EventsProcessor {

	@Override
	public void process(
			String key, String[] classes, LifecycleEvent lifecycleEvent)
		throws ActionException {

		for (String className : classes) {
			if (Validator.isNull(className)) {
				return;
			}

			if (_log.isDebugEnabled()) {
				_log.debug("Process event " + className);
			}

			LifecycleAction lifecycleAction = (LifecycleAction)InstancePool.get(
				className);

			lifecycleAction.processLifecycleEvent(lifecycleEvent);
		}

		if (Validator.isNull(key)) {
			return;
		}

		for (LifecycleAction lifecycleAction : getLifecycleActions(key)) {
			lifecycleAction.processLifecycleEvent(lifecycleEvent);
		}
	}

	@Override
	public void processEvent(
			LifecycleAction lifecycleAction, LifecycleEvent lifecycleEvent)
		throws ActionException {

		lifecycleAction.processLifecycleEvent(lifecycleEvent);
	}

	@Override
	public void registerEvent(String key, Object event) {
		List<LifecycleAction> lifecycleActions = getLifecycleActions(key);

		lifecycleActions.add((LifecycleAction)event);
	}

	@Override
	public void unregisterEvent(String key, Object event) {
		List<LifecycleAction> lifecycleActions = getLifecycleActions(key);

		lifecycleActions.remove(event);
	}

	private List<LifecycleAction> getLifecycleActions(String key) {
		List<LifecycleAction> lifecycleActions = _lifecycleActions.get(key);

		if (lifecycleActions == null) {
			lifecycleActions = new ArrayList<LifecycleAction>();

			_lifecycleActions.put(key, lifecycleActions);
		}

		return lifecycleActions;
	}

	private static Log _log = LogFactoryUtil.getLog(EventsProcessorImpl.class);

	private Map<String, List<LifecycleAction>> _lifecycleActions =
		new HashMap<String, List<LifecycleAction>>();

}