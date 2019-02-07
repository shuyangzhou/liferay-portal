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

package com.liferay.headless.document.library.internal.resource;

import com.liferay.document.library.kernel.model.DLFolderConstants;
import com.liferay.document.library.kernel.service.DLAppService;
import com.liferay.headless.document.library.dto.Folder;
import com.liferay.headless.document.library.resource.FolderResource;
import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.vulcan.context.AcceptLanguage;
import com.liferay.portal.vulcan.context.Pagination;
import com.liferay.portal.vulcan.dto.Page;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.util.List;
import java.util.function.Function;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author Javier Gamarra
 */
@Component(
	properties = "OSGI-INF/folder.properties", scope = ServiceScope.PROTOTYPE,
	service = FolderResource.class
)
public class FolderResourceImpl implements FolderResource {

	@Override
	public Response deleteFolder(Long folderId) throws Exception {
		Response.ResponseBuilder responseBuilder = Response.ok();

		return responseBuilder.build();
	}

	@Override
	public Folder getDocumentsRepository(Long id) throws Exception {
		return new Folder();
	}

	@Override
	public Page<Folder> getDocumentsRepositoryFolderPage(
			Long parentId, Pagination pagination)
		throws Exception {

		return _getFolderPage(
			parentId, DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, pagination);
	}

	@Override
	public Folder getFolder(Long folderId) throws Exception {
		return new Folder();
	}

	@Override
	public Page<Folder> getFolderFolderPage(
			Long parentId, Pagination pagination)
		throws Exception {

		com.liferay.portal.kernel.repository.model.Folder parentFolder =
			_dlAppService.getFolder(parentId);

		return _getFolderPage(
			parentFolder.getGroupId(), parentFolder.getFolderId(), pagination);
	}

	@Override
	public Folder postDocumentsRepositoryFolder(
			Long documentsRepositoryId, Folder folder)
		throws Exception {

		return new Folder();
	}

	@Override
	public Folder postDocumentsRepositoryFolderBatchCreate(
			Long documentsRepositoryId, Folder folder)
		throws Exception {

		return new Folder();
	}

	@Override
	public Folder postFolderFolder(Long folderId, Folder folder)
		throws Exception {

		return new Folder();
	}

	@Override
	public Folder postFolderFolderBatchCreate(Long folderId, Folder folder)
		throws Exception {

		return new Folder();
	}

	@Override
	public Folder putFolder(Long folderId, Folder folder) throws Exception {
		return new Folder();
	}

	protected <T, R> List<R> transform(
		List<T> list, Function<T, R> transformFunction) {

		return TransformUtil.transform(list, transformFunction);
	}

	@Context
	protected AcceptLanguage acceptLanguage;

	@Context
	protected Company company;

	private Page<Folder> _getFolderPage(
			Long groupId, Long parentFolderId, Pagination pagination)
		throws Exception {

		return Page.of(
			transform(
				_dlAppService.getFolders(
					groupId, parentFolderId, pagination.getStartPosition(),
					pagination.getEndPosition(), null),
				this::_toFolder),
			pagination, _dlAppService.getFoldersCount(groupId, parentFolderId));
	}

	private Folder _toFolder(
		com.liferay.portal.kernel.repository.model.Folder folder) {

		return new Folder() {
			{
				setDescription(folder.getDescription());
				setId(folder.getFolderId());
				setName(folder.getName());
			}
		};
	}

	@Reference
	private DLAppService _dlAppService;

}