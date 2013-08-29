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

package com.liferay.portal.spring.aop;

import org.aopalliance.intercept.MethodInterceptor;

/**
 * @author László Csontos
 * @author Shuyang Zhou
 */
public class ChainableMethodAdviceInjector {

	public void afterPropertiesSet() {
		if (!isInjectCondition()) {
			return;
		}

		ChainableMethodAdvice newChainableMethodAdvice =
			getNewChainableMethodAdvice();

		if (newChainableMethodAdvice == null) {
			throw new IllegalArgumentException(
				"New Chainable method advice is null");
		}

		MethodInterceptor childMethodInterceptor = getChildMethodInterceptor();

		ChainableMethodAdvice headChainableMethodAdvice =
			getHeadChainableMethodAdvice();

		ChainableMethodAdvice parentChainableMethodAdvice =
			getParentChainableMethodAdvice();

		if ((parentChainableMethodAdvice == null) &&
			((childMethodInterceptor == null) ||
			 (headChainableMethodAdvice == null))) {

			throw new IllegalArgumentException(
				"Parent chainable method advice is null and either the head " +
					"of chainable method advices or child method interceptor " +
					"is null");
		}

		if (parentChainableMethodAdvice == null) {
			ChainableMethodAdvice nextAdvice = headChainableMethodAdvice;

			while ((nextAdvice.nextMethodInterceptor !=
						childMethodInterceptor) &&
				   (nextAdvice.nextMethodInterceptor instanceof
					ChainableMethodAdvice)) {

				nextAdvice =
					(ChainableMethodAdvice)nextAdvice.nextMethodInterceptor;
			}

			if (nextAdvice.nextMethodInterceptor == childMethodInterceptor) {
				parentChainableMethodAdvice = nextAdvice;
			}
			else {
				throw new IllegalStateException(
					"Child method interceptor has not been found in chain");
			}
		}

		newChainableMethodAdvice.nextMethodInterceptor =
			parentChainableMethodAdvice.nextMethodInterceptor;
		parentChainableMethodAdvice.nextMethodInterceptor =
			newChainableMethodAdvice;

		// Advices created after the chain has already been created doesn't have
		// a reference to ServiceBeanAopCacheManager. This happens because
		// instances of ServiceBeanAopProxy see only an older view of the
		// chain's state. Having this in mind we need to fix this manually after
		// wiring a new advice into an existing chain.

		ServiceBeanAopCacheManager chainAopCacheManager =
			parentChainableMethodAdvice.serviceBeanAopCacheManager;

		chainAopCacheManager.reset();

		newChainableMethodAdvice.setServiceBeanAopCacheManager(
			chainAopCacheManager);
	}

	public void setChildMethodInterceptor(
		MethodInterceptor childMethodInterceptor) {

		_childMethodInterceptor = childMethodInterceptor;
	}

	public void setHeadChainableMethodAdvice(
		ChainableMethodAdvice headChainableMethodAdvice) {

		_headChainableMethodAdvice = headChainableMethodAdvice;
	}

	public void setInjectCondition(boolean injectCondition) {
		_injectCondition = injectCondition;
	}

	public void setNewChainableMethodAdvice(
		ChainableMethodAdvice newChainableMethodAdvice) {

		_newChainableMethodAdvice = newChainableMethodAdvice;
	}

	public void setParentChainableMethodAdvice(
		ChainableMethodAdvice parentChainableMethodAdvice) {

		_parentChainableMethodAdvice = parentChainableMethodAdvice;
	}

	protected MethodInterceptor getChildMethodInterceptor() {
		return _childMethodInterceptor;
	}

	protected ChainableMethodAdvice getHeadChainableMethodAdvice() {
		return _headChainableMethodAdvice;
	}

	protected ChainableMethodAdvice getNewChainableMethodAdvice() {
		return _newChainableMethodAdvice;
	}

	protected ChainableMethodAdvice getParentChainableMethodAdvice() {
		return _parentChainableMethodAdvice;
	}

	protected boolean isInjectCondition() {
		return _injectCondition;
	}

	private MethodInterceptor _childMethodInterceptor;
	private ChainableMethodAdvice _headChainableMethodAdvice;
	private boolean _injectCondition;
	private ChainableMethodAdvice _newChainableMethodAdvice;
	private ChainableMethodAdvice _parentChainableMethodAdvice;

}