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

package com.liferay.frontend.taglib.clay.sample.web.internal.display.context;

import com.liferay.frontend.taglib.clay.servlet.taglib.util.CreationMenu;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.DropdownItemList;
import com.liferay.frontend.taglib.clay.servlet.taglib.util.ViewTypeItemList;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Carlos Lancha
 */
public class ManagementToolbarsDisplayContext {

	public ManagementToolbarsDisplayContext(HttpServletRequest request) {
		_request = request;
	}

	public DropdownItemList getActionDropdownItemList() {
		if (_actionDropdownItemList != null) {
			return _actionDropdownItemList;
		}

		_actionDropdownItemList = new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.setHref("#edit");
						dropdownItem.setLabel("Edit");
					});

				add(
					dropdownItem -> {
						dropdownItem.setHref("#download");
						dropdownItem.setIcon("download");
						dropdownItem.setLabel("Download");
						dropdownItem.setQuickAction(true);
					});

				add(
					dropdownItem -> {
						dropdownItem.setHref("#delete");
						dropdownItem.setLabel("Delete");
						dropdownItem.setIcon("trash");
						dropdownItem.setQuickAction(true);
					});
			}
		};

		return _actionDropdownItemList;
	}

	public CreationMenu getCreationMenu() {
		if (_creationMenu != null) {
			return _creationMenu;
		}

		_creationMenu = new CreationMenu(_request) {
			{
				addPrimaryDropdownItem(
					dropdownItem -> {
						dropdownItem.setHref("#1");
						dropdownItem.setLabel("Sample 1");
					});

				addPrimaryDropdownItem(
					dropdownItem -> {
						dropdownItem.setHref("#2");
						dropdownItem.setLabel("Sample 2");
					});

				addFavoriteDropdownItem(
					dropdownItem -> {
						dropdownItem.setHref("#3");
						dropdownItem.setLabel("Favorite 1");
					});

				addFavoriteDropdownItem(
					dropdownItem -> {
						dropdownItem.setHref("#4");
						dropdownItem.setLabel("Other item");
					});
			}
		};

		return _creationMenu;
	}

	public DropdownItemList getFilterDropdownItemList() {
		if (_filterDropdownItemList != null) {
			return _filterDropdownItemList;
		}

		DropdownItemList filterByDropdownItemList = new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.setHref("#1");
						dropdownItem.setLabel("Filter 1");
					});

				add(
					dropdownItem -> {
						dropdownItem.setHref("#2");
						dropdownItem.setLabel("Filter 2");
					});
			}
		};

		DropdownItemList orderByDropdownItemList = new DropdownItemList() {
			{
				add(
					dropdownItem -> {
						dropdownItem.setHref("#3");
						dropdownItem.setLabel("Order 1");
					});

				add(
					dropdownItem -> {
						dropdownItem.setHref("#4");
						dropdownItem.setLabel("Order 2");
					});
			}
		};

		_filterDropdownItemList = new DropdownItemList() {
			{
				addGroup(
					dropdownGroupItem -> {
						dropdownGroupItem.setDropdownItemList(
							filterByDropdownItemList);
						dropdownGroupItem.setLabel("Filter By");
					});

				addGroup(
					dropdownGroupItem -> {
						dropdownGroupItem.setDropdownItemList(
							orderByDropdownItemList);
						dropdownGroupItem.setLabel("Order By");
					});
			}
		};

		return _filterDropdownItemList;
	}

	public ViewTypeItemList getViewTypeItemList() {
		if (_viewTypeItemList != null) {
			return _viewTypeItemList;
		}

		_viewTypeItemList = new ViewTypeItemList() {
			{
				addCardViewTypeItem(
					viewTypeItem -> {
						viewTypeItem.setActive(true);
						viewTypeItem.setLabel("Card");
					});

				addListViewTypeItem(
					viewTypeItem -> {
						viewTypeItem.setLabel("List");
					});

				addTableViewTypeItem(
					viewTypeItem -> {
						viewTypeItem.setLabel("Table");
					});
			}
		};

		return _viewTypeItemList;
	}

	private DropdownItemList _actionDropdownItemList;
	private CreationMenu _creationMenu;
	private DropdownItemList _filterDropdownItemList;
	private final HttpServletRequest _request;
	private ViewTypeItemList _viewTypeItemList;

}