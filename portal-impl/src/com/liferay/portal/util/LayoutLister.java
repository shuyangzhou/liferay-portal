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

import com.liferay.portal.kernel.cache.Lifecycle;
import com.liferay.portal.kernel.cache.ThreadLocalCache;
import com.liferay.portal.kernel.cache.ThreadLocalCacheManager;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.StringBundler;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
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

		ThreadLocalCache<LayoutView> threadLocalCache =
			ThreadLocalCacheManager.getThreadLocalCache(
				Lifecycle.REQUEST, LayoutLister.class.getName());

		String key = getKey(groupId, privateLayout, rootNodeName, locale);

		LayoutView layoutView = threadLocalCache.get(key);

		if (layoutView == null) {
			layoutView = doGetLayoutView(
				groupId, privateLayout, rootNodeName, locale);

			threadLocalCache.put(key, layoutView);
		}

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

			Layout layout = layoutNode.getLayout();

			pushLayouts(
				layoutNodeStack, layoutsBag, layout.getLayoutId(),
				layoutNode.getDepth() + 1, _nodeId);
		}
	}

	protected LayoutView doGetLayoutView(
			long groupId, boolean privateLayout, String rootNodeName,
			Locale locale)
		throws PortalException, SystemException {

		_locale = locale;
		_nodeId = 1;

		_list = new ArrayList<String>();

		StringBundler sb = new StringBundler(5);

		sb.append("1|0|0|");
		sb.append(LayoutConstants.DEFAULT_PLID);
		sb.append("|");
		sb.append(rootNodeName);
		sb.append("|0");

		_list.add(sb.toString());

		List<Layout> layouts = LayoutLocalServiceUtil.getLayouts(
			groupId, privateLayout);

		Map<Long, Deque<Layout>> layoutsBag = getLayoutsBag(layouts);

		createList(layoutsBag);

		return new LayoutView(_list, _depth);
	}

	protected String getKey(
		long groupId, boolean privateLayout, String rootNodeName,
		Locale locale) {

		StringBundler sb = new StringBundler(7);

		sb.append(StringUtil.toHexString(groupId));
		sb.append(StringPool.POUND);
		sb.append(privateLayout);
		sb.append(StringPool.POUND);
		sb.append(rootNodeName);
		sb.append(StringPool.POUND);

		if (locale != null) {
			sb.append(locale.toString());
		}
		else {
			sb.append(StringPool.NULL);
		}

		return sb.toString();
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
		sb.append(layoutNode.getParentId());
		sb.append("|");

		if ((layoutNode.getIndex() + 1) == layoutNode.getSiblingCount()) {
			sb.append("1");
		}
		else {
			sb.append("0");
		}

		Layout layout = layoutNode.getLayout();

		sb.append("|");
		sb.append(layout.getPlid());
		sb.append("|");
		sb.append(layout.getName(_locale));
		sb.append("|");
		sb.append("11");
		sb.append("|");
		sb.append(layoutNode.getDepth());

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

			_depth = depth;
			_index = index;
			_layout = layout;
			_parentId = parentId;
			_siblingCount = siblingCount;
		}

		public int getDepth() {
			return _depth;
		}

		public int getIndex() {
			return _index;
		}

		public Layout getLayout() {
			return _layout;
		}

		public int getParentId() {
			return _parentId;
		}

		public int getSiblingCount() {
			return _siblingCount;
		}

		public void setDepth(int depth) {
			_depth = depth;
		}

		public void setIndex(int index) {
			_index = index;
		}

		public void setLayout(Layout layout) {
			_layout = layout;
		}

		public void setParentId(int parentId) {
			_parentId = parentId;
		}

		public void setSiblingCount(int siblingCount) {
			_siblingCount = siblingCount;
		}

		private int _depth;
		private int _index;
		private Layout _layout;
		private int _parentId;
		private int _siblingCount;

	}

}