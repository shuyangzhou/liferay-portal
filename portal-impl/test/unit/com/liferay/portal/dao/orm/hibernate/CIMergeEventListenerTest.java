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
				new TestMergeEvent("Test", testMVCCModel, hibernateException));

			Assert.fail("Should throw HibernateException");
		}
		catch (HibernateException he) {
			Assert.assertSame(hibernateException, he);

			Assert.assertEquals(1, testMVCCModel.getMvccVersion());
		}
	}

	@Test
	public void testFailedMergeEventStaleObjectState() {
		TestMVCCModel testMVCCModel = new TestMVCCModel(
			1, RandomTestUtil.randomString());

		_ciMergeEventListener.onMerge(
			new TestMergeEvent("Test", testMVCCModel));

		Assert.assertEquals(2, testMVCCModel.getMvccVersion());

		testMVCCModel.setMvccVersion(1);

		StaleObjectStateException staleObjectStateException =
			new StaleObjectStateException(null, null);

		try {
			_ciMergeEventListener.onMerge(
				new TestMergeEvent(
					"Test", testMVCCModel, staleObjectStateException));

			Assert.fail("Should throw RuntimeException");
		}
		catch (RuntimeException re) {
			Assert.assertSame(staleObjectStateException, re.getCause());

			String message = re.getMessage();

			Assert.assertTrue(
				message.startsWith(
					"This update caused an MVCC conflict: \n{entityName=Test," +
						" primaryKey=" + testMVCCModel.getPrimaryKeyObj() +
							", previousMvccVersion=1}"));

			Assert.assertTrue(
				message.contains(
					"CIMergeEventListenerTest." +
						"testFailedMergeEventStaleObjectState(" +
							"CIMergeEventListenerTest.java:66)"));
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
		public String getEntityName() {
			return _entityName;
		}

		@Override
		public Object getOriginal() {
			if (_runtimeException == null) {
				_mvccModel.setMvccVersion(_mvccModel.getMvccVersion() + 1);

				return null;
			}

			throw _runtimeException;
		}

		private TestMergeEvent(String entityName, MVCCModel mvccModel) {
			this(entityName, mvccModel, null);
		}

		private TestMergeEvent(
			String entityName, MVCCModel mvccModel,
			RuntimeException runtimeException) {

			super(mvccModel, null);

			_entityName = entityName;
			_mvccModel = mvccModel;
			_runtimeException = runtimeException;
		}

		private final String _entityName;
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
			throw new UnsupportedOperationException();
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