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

package com.liferay.portal.spring.transaction;

import java.util.List;

import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;

/**
 * @author László Csontos
 */
public class TransactionAttribute extends RuleBasedTransactionAttribute {

	public TransactionAttribute() {
	}

	public TransactionAttribute(
		int propagationBehavior, List<RollbackRuleAttribute> rollbackRules) {

		super(propagationBehavior, rollbackRules);
	}

	public TransactionAttribute(RuleBasedTransactionAttribute other) {
		super(other);
	}

	public boolean isSplitReadWrite() {
		return _splitReadWrite;
	}

	public void setSplitReadWrite(boolean splitReadWrite) {
		this._splitReadWrite = splitReadWrite;
	}

	private boolean _splitReadWrite = true;

}