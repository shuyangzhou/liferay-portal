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

package com.liferay.portal.util;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.model.Layout;
import com.liferay.portal.model.LayoutConstants;
import com.liferay.portal.service.LayoutLocalServiceUtil;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.time.StopWatch;

/**
 * @author Brian Wing Shun Chan
 * @author László Csontos
 */
public class LayoutLister {

	public LayoutView getLayoutView(
			long groupId, boolean privateLayout, String rootNodeName,
			Locale locale)
		throws PortalException, SystemException {

		StopWatch stopWatch = null;

		if (_log.isDebugEnabled()) {
			stopWatch = new StopWatch();

			stopWatch.start();
		}

		LayoutView layoutView = doGetLayoutView(
			groupId, privateLayout, rootNodeName, locale);

		if (_log.isDebugEnabled()) {
			stopWatch.stop();

			_log.debug("Runtime: " + stopWatch.getTime());
		}

		return layoutView;
	}

	protected void createList(Map<Long, Deque<Layout>> layoutsBag) {
		Deque<LayoutNode> layoutNodeStack = new LinkedList<LayoutNode>();

		pushLayouts(
			layoutNodeStack, layoutsBag,
			LayoutConstants.DEFAULT_PARENT_LAYOUT_ID, 1, _nodeId);

		while (!layoutNodeStack.isEmpty()) {
			LayoutNode layoutNode = layoutNodeStack.pop();

			visitLayout(layoutNode);

			pushLayouts(
				layoutNodeStack, layoutsBag, layoutNode.layout.getLayoutId(),
				layoutNode.depth + 1, _nodeId);
		}
	}

	protected LayoutView doGetLayoutView(
			long groupId, boolean privateLayout, String rootNodeName,
			Locale locale)
		throws PortalException, SystemException {

		_locale = locale;
		_nodeId = 1;

		_list = new ArrayList<String>();

		_list.add(
			"1|0|0|" + LayoutConstants.DEFAULT_PLID + "|" + rootNodeName +
				"|0");

		List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(
			groupId, privateLayout);

		Map<Long, Deque<Layout>> layoutsBag = getLayoutsBag(layouts);

		createList(layoutsBag);

		return new LayoutView(_list, _depth);
	}

	protected Map<Long, Deque<Layout>> getLayoutsBag(List<Layout> layouts) {
		Map<Long, Deque<Layout>> layoutsBag =
			new HashMap<Long, Deque<Layout>>();

		for (Layout layout : layouts) {
			long parentLayoutId = layout.getParentLayoutId();

			Deque<Layout> matchedLayouts = layoutsBag.get(parentLayoutId);

			if (matchedLayouts == null) {
				matchedLayouts = new LinkedList<Layout>();

				layoutsBag.put(parentLayoutId, matchedLayouts);
			}

			matchedLayouts.offer(layout);
		}

		return layoutsBag;
	}

	protected void pushLayouts(
		Deque<LayoutNode> layoutNodeStack, Map<Long, Deque<Layout>> layoutsBag,
		long parentLayoutId, int depth, int parentId) {

		Deque<Layout> matchedLayouts = layoutsBag.get(parentLayoutId);

		if ((matchedLayouts == null) || matchedLayouts.isEmpty()) {
			return;
		}

		int siblingCount = matchedLayouts.size();

		int index = siblingCount;

		Layout layout = null;

		while ((layout = matchedLayouts.pollLast()) != null) {
			LayoutNode layoutNode = new LayoutNode(
				layout, --index, siblingCount, depth, parentId);

			layoutNodeStack.push(layoutNode);
		}

		if (depth > _depth) {
			_depth = depth;
		}
	}

	protected void visitLayout(LayoutNode layoutNode) {
		StringBundler sb = new StringBundler(13);

		sb.append(++_nodeId);
		sb.append("|");
		sb.append(layoutNode.parentId);
		sb.append("|");

		if ((layoutNode.index + 1) == layoutNode.siblingCount) {
			sb.append("1");
		}
		else {
			sb.append("0");
		}

		sb.append("|");
		sb.append(layoutNode.layout.getPlid());
		sb.append("|");
		sb.append(layoutNode.layout.getName(_locale));
		sb.append("|");
		//sb.append("9");
		sb.append("11");
		sb.append("|");
		sb.append(layoutNode.depth);

		_list.add(sb.toString());
	}

	private static Log _log = LogFactoryUtil.getLog(LayoutLister.class);

	private int _depth;
	private List<String> _list;
	private Locale _locale;
	private int _nodeId;

	private class LayoutNode {

		public LayoutNode(
			Layout layout, int index, int siblingCount, int depth,
			int parentId) {

			this.index = index;
			this.depth = depth;
			this.layout = layout;
			this.parentId = parentId;
			this.siblingCount = siblingCount;
		}

		int index;
		int depth;
		Layout layout;
		int parentId;
		int siblingCount;
	}

}