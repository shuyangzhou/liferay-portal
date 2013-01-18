/**
 * Copyright (c) 2000-2012 Liferay, Inc. All rights reserved.
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

package com.liferay.portal.increment;

import com.liferay.portal.kernel.cache.key.CacheKeyGenerator;
import com.liferay.portal.kernel.cache.key.CacheKeyGeneratorUtil;
import com.liferay.portal.kernel.concurrent.BatchablePipe;
import com.liferay.portal.kernel.configuration.Filter;
import com.liferay.portal.kernel.increment.BufferedIncrement;
import com.liferay.portal.kernel.increment.Increment;
import com.liferay.portal.kernel.increment.IncrementFactory;
import com.liferay.portal.kernel.messaging.DestinationNames;
import com.liferay.portal.kernel.messaging.MessageBusUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.StringPool;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.spring.aop.AnnotationChainableMethodAdvice;

import java.io.Serializable;

import java.lang.annotation.Annotation;

import java.util.Timer;
import java.util.TimerTask;

import org.aopalliance.intercept.MethodInvocation;

/**
 * @author Zsolt Berentey
 * @author Shuyang Zhou
 */
public class BufferedIncrementAdvice
	extends AnnotationChainableMethodAdvice<BufferedIncrement> {

	public BufferedIncrementAdvice() {
		_timer.schedule(new TimerTask() {

			@Override
			public void run() {
				if (_batchablePipe.checkDelayedEntries()) {
					MessageBusUtil.sendMessage(
						DestinationNames.BUFFERED_INCREMENT_PARALLEL,
						_batchablePipe);
				}
			}

		}, 1000L, 1000L);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Object before(MethodInvocation methodInvocation) throws Throwable {
		BufferedIncrement bufferedIncrement = findAnnotation(methodInvocation);

		if (bufferedIncrement == _nullBufferedIncrement) {
			return null;
		}

		Object[] arguments = methodInvocation.getArguments();

		Object value = arguments[arguments.length - 1];

		CacheKeyGenerator cacheKeyGenerator =
			CacheKeyGeneratorUtil.getCacheKeyGenerator(
				BufferedIncrementAdvice.class.getName());

		cacheKeyGenerator.append(methodInvocation.toString());

		for (int i = 0; i < arguments.length - 1; i++) {
			cacheKeyGenerator.append(StringUtil.toHexString(arguments[i]));
		}

		Serializable batchKey = cacheKeyGenerator.finish();

		Increment<?> increment = IncrementFactory.createIncrement(
			bufferedIncrement.incrementClass(), value);

		BufferedIncreasableEntry bufferedIncreasableEntry;

		if (!Validator.isBlank(bufferedIncrement.propertyKey())) {
			String propertyKey = bufferedIncrement.propertyKey();

			long timeOutInMillis = GetterUtil.getLong(
				PropsUtil.get(propertyKey, new Filter("timeout")), -1);

			String threshold = PropsUtil.get(
				propertyKey, new Filter("threshold"));

			Increment<?> valueTreshold = null;

			if (Validator.isNotNull(threshold)) {
				valueTreshold = IncrementFactory.parseIncrement(
					bufferedIncrement.incrementClass(), threshold);
			}

			bufferedIncreasableEntry =
				new BufferedIncreasableEntry(
					methodInvocation, batchKey, increment, valueTreshold,
					timeOutInMillis);
		}
		else {
			bufferedIncreasableEntry =
				new BufferedIncreasableEntry(
					methodInvocation, batchKey, increment);
		}

		if (_batchablePipe.put(bufferedIncreasableEntry)) {
			MessageBusUtil.sendMessage(
				DestinationNames.BUFFERED_INCREMENT_PARALLEL, _batchablePipe);
		}

		return nullResult;
	}

	@Override
	public BufferedIncrement getNullAnnotation() {
		return _nullBufferedIncrement;
	}

	@SuppressWarnings("rawtypes")
	private static BatchablePipe<String, BufferedIncreasableEntry>
		_batchablePipe = new BatchablePipe<String, BufferedIncreasableEntry>();

	private static BufferedIncrement _nullBufferedIncrement =
		new BufferedIncrement() {

			public Class<? extends Annotation> annotationType() {
				return BufferedIncrement.class;
			}

			public Class<? extends Increment<?>> incrementClass() {
				return null;
			}

			public boolean parallel() {
				return true;
			}

			public String propertyKey() {
				return StringPool.BLANK;
			}

		};

	private static Timer _timer = new Timer();

}