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

package com.liferay.portal.dao.orm.hibernate;

import com.liferay.expando.kernel.model.ExpandoBridge;
import com.liferay.portal.dao.orm.hibernate.event.CIMergeEventListener;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.model.MVCCModel;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.test.ReflectionTestUtil;
import com.liferay.portal.kernel.test.util.RandomTestUtil;

import java.io.Serializable;

import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.StaleObjectStateException;
import org.hibernate.event.MergeEvent;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Preston Crary
 */
public class CIMergeEventListenerTest {

	@Test
	public void testFailedMergeEventOtherHibernateException() {
		TestMVCCModel testMVCCModel = new TestMVCCModel(
			1, RandomTestUtil.randomString());

		HibernateException hibernateException = new HibernateException("Test");

		try {
			_ciMergeEventListener.onMerge(
				new TestMergeEvent(testMVCCModel, hibernateException));

			Assert.fail("Should throw HibernateException");
		}
		catch (HibernateException he) {
			Assert.assertSame(hibernateException, he);
		}
	}

	@Test
	public void testFailedMergeEventStaleObjectState() {
		TestMVCCModel testMVCCModel = new TestMVCCModel(
			1, RandomTestUtil.randomString());

		_ciMergeEventListener.onMerge(new TestMergeEvent(testMVCCModel));

		StaleObjectStateException staleObjectStateException =
			new StaleObjectStateException(null, null);

		Map<String, StaleObjectStateException> previousUpdates =
			ReflectionTestUtil.getFieldValue(
				_ciMergeEventListener, "_previousUpdates");

		Assert.assertEquals(1, previousUpdates.size());

		try {
			_ciMergeEventListener.onMerge(
				new TestMergeEvent(testMVCCModel, staleObjectStateException));

			Assert.fail("Should throw StaleObjectStateException");
		}
		catch (StaleObjectStateException sose) {
			Assert.assertSame(staleObjectStateException, sose);

			Throwable cause = sose.getCause();

			Assert.assertNotNull(cause);

			Assert.assertTrue(cause instanceof StaleObjectStateException);

			Assert.assertEquals(1, previousUpdates.size());

			Assert.assertTrue(previousUpdates.containsValue(cause));
		}
	}

	private static final CIMergeEventListener _ciMergeEventListener =
		CIMergeEventListener.INSTANCE;

	private static class TestMergeEvent extends MergeEvent {

		@Override
		public Object getEntity() {
			return _mvccModel;
		}

		@Override
		public Object getOriginal() {
			if (_runtimeException == null) {
				if (_count == 0) {
					_count++;

					return null;
				}

				return _mvccModel;
			}

			if (_count == 0) {
				_count++;

				throw _runtimeException;
			}

			return _mvccModel;
		}

		private TestMergeEvent(MVCCModel mvccModel) {
			this(mvccModel, null);
		}

		private TestMergeEvent(
			MVCCModel mvccModel, RuntimeException runtimeException) {

			super(mvccModel, null);

			_mvccModel = mvccModel;
			_runtimeException = runtimeException;
		}

		private int _count;
		private final MVCCModel _mvccModel;
		private final RuntimeException _runtimeException;

	}

	private static class TestMVCCModel
		implements BaseModel<TestMVCCModel>, MVCCModel {

		@Override
		public Object clone() {
			throw new UnsupportedOperationException();
		}

		@Override
		public int compareTo(TestMVCCModel o) {
			throw new UnsupportedOperationException();
		}

		@Override
		public ExpandoBridge getExpandoBridge() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Map<String, Object> getModelAttributes() {
			throw new UnsupportedOperationException();
		}

		@Override
		public Class<?> getModelClass() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String getModelClassName() {
			return "Test";
		}

		@Override
		public long getMvccVersion() {
			return _mvccVersion;
		}

		@Override
		public Serializable getPrimaryKeyObj() {
			return _primaryKey;
		}

		@Override
		public boolean isCachedModel() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isEntityCacheEnabled() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isEscapedModel() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isFinderCacheEnabled() {
			throw new UnsupportedOperationException();
		}

		@Override
		public boolean isNew() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void resetOriginalValues() {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setCachedModel(boolean cachedModel) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExpandoBridgeAttributes(BaseModel<?> baseModel) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setModelAttributes(Map<String, Object> attributes) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setMvccVersion(long mvccVersion) {
			_mvccVersion = mvccVersion;
		}

		@Override
		public void setNew(boolean n) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setPrimaryKeyObj(Serializable primaryKeyObj) {
			_primaryKey = (String)primaryKeyObj;
		}

		@Override
		public CacheModel<TestMVCCModel> toCacheModel() {
			throw new UnsupportedOperationException();
		}

		@Override
		public TestMVCCModel toEscapedModel() {
			throw new UnsupportedOperationException();
		}

		@Override
		public TestMVCCModel toUnescapedModel() {
			throw new UnsupportedOperationException();
		}

		@Override
		public String toXmlString() {
			throw new UnsupportedOperationException();
		}

		private TestMVCCModel(long mvccVersion, String primaryKey) {
			_mvccVersion = mvccVersion;
			_primaryKey = primaryKey;
		}

		private long _mvccVersion;
		private String _primaryKey;

	}

}