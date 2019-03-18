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

package com.liferay.digital.signature.internal.request;

import com.liferay.digital.signature.model.DSSessionKey;
import com.liferay.digital.signature.request.VoidPackageDSSignatureRequest;

/**
 * @author Michael C. Han
 */
public class VoidPackageDSSignatureRequestImpl
	implements VoidPackageDSSignatureRequest {

	public VoidPackageDSSignatureRequestImpl(
		DSSessionKey dsSessionKey, String dsSignaturePackageKey,
		String voidReason) {

		_dsSessionKey = dsSessionKey;
		_dsSignaturePackageKey = dsSignaturePackageKey;
		_voidReason = voidReason;
	}

	@Override
	public DSSessionKey getDSSessionKey() {
		return _dsSessionKey;
	}

	@Override
	public String getDSSignaturePackageKey() {
		return _dsSignaturePackageKey;
	}

	@Override
	public String getExternalReferenceKey() {
		return _externalReferenceKey;
	}

	@Override
	public String getVoidReason() {
		return _voidReason;
	}

	public void setExternalReferenceKey(String externalReferenceKey) {
		_externalReferenceKey = externalReferenceKey;
	}

	private final DSSessionKey _dsSessionKey;
	private final String _dsSignaturePackageKey;
	private String _externalReferenceKey;
	private final String _voidReason;

}