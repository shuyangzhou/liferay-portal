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

package com.liferay.ci.thread.interruption.aspect;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

/**
 * @author Shuyang Zhou
 */
@Aspect
public class ThreadInterruptionAspect {

	@Before("call(public void Thread.interrupt()) && target(thread)")
	public void interrupt(Thread thread) {
		LogUtil.log("About to interrupt " + thread);
	}

	public static ThreadInterruptionAspect aspectOf() {
		return _instance;
	}

	private static final ThreadInterruptionAspect _instance =
		new ThreadInterruptionAspect();

}