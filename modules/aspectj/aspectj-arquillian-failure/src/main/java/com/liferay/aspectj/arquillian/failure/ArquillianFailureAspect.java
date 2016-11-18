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

package com.liferay.aspectj.arquillian.failure;

import com.liferay.portal.kernel.concurrent.NoticeableFuture;
import com.liferay.portal.kernel.io.unsync.UnsyncBufferedReader;
import com.liferay.portal.kernel.process.OutputProcessor;
import com.liferay.portal.kernel.process.ProcessException;
import com.liferay.portal.kernel.process.ProcessUtil;
import com.liferay.portal.kernel.util.ObjectValuePair;
import com.liferay.portal.kernel.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.SuppressAjWarnings;

/**
 * @author Preston Crary
 */
@Aspect
@SuppressAjWarnings("adviceDidNotMatch")
public class ArquillianFailureAspect {

	@AfterThrowing(
		throwing = "e1",
		value = "execution(void org.jboss.arquillian.junit.Arquillian.run(org.junit.runner.notification.RunNotifier))"
	)
	public void logTomcatJStack(Exception e1) {
		try {
			NoticeableFuture<ObjectValuePair<List<String>, String>>
				jpsNoticeableFuture = ProcessUtil.execute(
					new StringOutputProcessor(), "jps");

			ObjectValuePair<List<String>, String> objectValuePair =
				jpsNoticeableFuture.get();

			String pid = null;

			for (String line : objectValuePair.getKey()) {
				if (line.endsWith(" Bootstrap")) {
					pid = line.substring(
						0, line.length() - " Bootstrap".length());

					break;
				}
			}

			System.out.print("pids: ");

			System.out.println(StringUtil.merge(objectValuePair.getKey()));

			System.out.print("errors: ");

			System.out.println(objectValuePair.getValue());

			if (pid == null) {
				return;
			}

			System.out.println("jstack for pid: " + pid);

			NoticeableFuture<ObjectValuePair<Void, Void>>
				jstackNoticeableFuture = ProcessUtil.execute(
					ProcessUtil.ECHO_OUTPUT_PROCESSOR, "jstack", pid);

			jstackNoticeableFuture.get();
		}
		catch (Exception e2) {
			e1.addSuppressed(e2);
		}
	}

	private static class StringOutputProcessor
		implements OutputProcessor<List<String>, String> {

		@Override
		public String processStdErr(InputStream stdErrInputStream)
			throws ProcessException {

			try {
				return StringUtil.read(stdErrInputStream);
			}
			catch (IOException ioe) {
				throw new ProcessException(ioe);
			}
		}

		@Override
		public List<String> processStdOut(InputStream stdOutInputStream)
			throws ProcessException {

			List<String> list = new ArrayList<>();

			try (UnsyncBufferedReader unsyncBufferedReader =
					new UnsyncBufferedReader(
						new InputStreamReader(stdOutInputStream))) {

				String line = null;

				while ((line = unsyncBufferedReader.readLine()) != null) {
					list.add(line);
				}
			}
			catch (IOException ioe) {
				throw new ProcessException(ioe);
			}

			return list;
		}

	}

}