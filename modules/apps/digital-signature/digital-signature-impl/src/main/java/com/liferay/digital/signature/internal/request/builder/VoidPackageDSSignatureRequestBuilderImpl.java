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

package com.liferay.digital.signature.internal.request.builder;

import com.liferay.digital.signature.internal.request.VoidPackageDSSignatureRequestImpl;
import com.liferay.digital.signature.model.DSSessionKey;
import com.liferay.digital.signature.request.VoidPackageDSSignatureRequest;
import com.liferay.digital.signature.request.builder.VoidPackageDSSignatureRequestBuilder;

/**
 * @author Michael C. Han
 */
public class VoidPackageDSSignatureRequestBuilderImpl
	implements VoidPackageDSSignatureRequestBuilder {

	public VoidPackageDSSignatureRequestBuilderImpl(DSSessionKey dsSessionKey) {
		_dsSessionKey = dsSessionKey;
	}

	@Override
	public VoidPackageDSSignatureRequest getVoidPackageDSSignatureRequest() {
		VoidPackageDSSignatureRequestImpl voidPackageDSSignatureRequestImpl =
			new VoidPackageDSSignatureRequestImpl(
				_dsSessionKey, _dsSignaturePackageKey, _voidReason);

		voidPackageDSSignatureRequestImpl.setExternalReferenceKey(
			_externalReferenceKey);

		return voidPackageDSSignatureRequestImpl;
	}

	@Override
	public VoidPackageDSSignatureRequestBuilder setDSSignaturePackageKey(
		String dsSignaturePackageKey) {

		_dsSignaturePackageKey = dsSignaturePackageKey;

		return this;
	}

	@Override
	public VoidPackageDSSignatureRequestBuilder setExternalReferenceKey(
		String externalReferenceKey) {

		_externalReferenceKey = externalReferenceKey;

		return this;
	}

	@Override
	public VoidPackageDSSignatureRequestBuilder setVoidReason(
		String voidReason) {

		_voidReason = voidReason;

		return this;
	}

	private final DSSessionKey _dsSessionKey;
	private String _dsSignaturePackageKey;
	private String _externalReferenceKey;
	private String _voidReason;

}