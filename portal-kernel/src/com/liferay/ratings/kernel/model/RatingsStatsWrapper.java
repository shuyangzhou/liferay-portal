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

package com.liferay.ratings.kernel.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * <p>
 * This class is a wrapper for {@link RatingsStats}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see RatingsStats
 * @generated
 */
@ProviderType
public class RatingsStatsWrapper implements RatingsStats,
	ModelWrapper<RatingsStats> {
	public RatingsStatsWrapper(RatingsStats ratingsStats) {
		_ratingsStats = ratingsStats;
	}

	@Override
	public Class<?> getModelClass() {
		return RatingsStats.class;
	}

	@Override
	public String getModelClassName() {
		return RatingsStats.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		Map<String, Function<RatingsStats, Object>> attributeGetters = getAttributeGetters();

		for (Map.Entry<String, Function<RatingsStats, Object>> entry : attributeGetters.entrySet()) {
			String attributeName = entry.getKey();
			Function<RatingsStats, Object> attributeFunction = entry.getValue();

			attributes.put(attributeName, attributeFunction.apply(this));
		}

		attributes.put("entityCacheEnabled", isEntityCacheEnabled());
		attributes.put("finderCacheEnabled", isFinderCacheEnabled());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		Map<String, BiConsumer<RatingsStats, Object>> attributeSetters = getAttributeSetters();

		for (Map.Entry<String, BiConsumer<RatingsStats, Object>> entry : attributeSetters.entrySet()) {
			String attributeName = entry.getKey();
			BiConsumer<RatingsStats, Object> attributeBiConsumer = entry.getValue();

			attributeBiConsumer.accept(this, attributeSetters.get(attributeName));
		}
	}

	@Override
	public Map<String, Function<RatingsStats, Object>> getAttributeGetters() {
		return _ratingsStats.getAttributeGetters();
	}

	@Override
	public Map<String, BiConsumer<RatingsStats, Object>> getAttributeSetters() {
		return _ratingsStats.getAttributeSetters();
	}

	@Override
	public Object clone() {
		return new RatingsStatsWrapper((RatingsStats)_ratingsStats.clone());
	}

	@Override
	public int compareTo(RatingsStats ratingsStats) {
		return _ratingsStats.compareTo(ratingsStats);
	}

	/**
	* Returns the average score of this ratings stats.
	*
	* @return the average score of this ratings stats
	*/
	@Override
	public double getAverageScore() {
		return _ratingsStats.getAverageScore();
	}

	/**
	* Returns the fully qualified class name of this ratings stats.
	*
	* @return the fully qualified class name of this ratings stats
	*/
	@Override
	public String getClassName() {
		return _ratingsStats.getClassName();
	}

	/**
	* Returns the class name ID of this ratings stats.
	*
	* @return the class name ID of this ratings stats
	*/
	@Override
	public long getClassNameId() {
		return _ratingsStats.getClassNameId();
	}

	/**
	* Returns the class pk of this ratings stats.
	*
	* @return the class pk of this ratings stats
	*/
	@Override
	public long getClassPK() {
		return _ratingsStats.getClassPK();
	}

	/**
	* Returns the company ID of this ratings stats.
	*
	* @return the company ID of this ratings stats
	*/
	@Override
	public long getCompanyId() {
		return _ratingsStats.getCompanyId();
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _ratingsStats.getExpandoBridge();
	}

	/**
	* Returns the primary key of this ratings stats.
	*
	* @return the primary key of this ratings stats
	*/
	@Override
	public long getPrimaryKey() {
		return _ratingsStats.getPrimaryKey();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _ratingsStats.getPrimaryKeyObj();
	}

	/**
	* Returns the stats ID of this ratings stats.
	*
	* @return the stats ID of this ratings stats
	*/
	@Override
	public long getStatsId() {
		return _ratingsStats.getStatsId();
	}

	/**
	* Returns the total entries of this ratings stats.
	*
	* @return the total entries of this ratings stats
	*/
	@Override
	public int getTotalEntries() {
		return _ratingsStats.getTotalEntries();
	}

	/**
	* Returns the total score of this ratings stats.
	*
	* @return the total score of this ratings stats
	*/
	@Override
	public double getTotalScore() {
		return _ratingsStats.getTotalScore();
	}

	@Override
	public int hashCode() {
		return _ratingsStats.hashCode();
	}

	@Override
	public boolean isCachedModel() {
		return _ratingsStats.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _ratingsStats.isEscapedModel();
	}

	@Override
	public boolean isNew() {
		return _ratingsStats.isNew();
	}

	@Override
	public void persist() {
		_ratingsStats.persist();
	}

	/**
	* Sets the average score of this ratings stats.
	*
	* @param averageScore the average score of this ratings stats
	*/
	@Override
	public void setAverageScore(double averageScore) {
		_ratingsStats.setAverageScore(averageScore);
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_ratingsStats.setCachedModel(cachedModel);
	}

	@Override
	public void setClassName(String className) {
		_ratingsStats.setClassName(className);
	}

	/**
	* Sets the class name ID of this ratings stats.
	*
	* @param classNameId the class name ID of this ratings stats
	*/
	@Override
	public void setClassNameId(long classNameId) {
		_ratingsStats.setClassNameId(classNameId);
	}

	/**
	* Sets the class pk of this ratings stats.
	*
	* @param classPK the class pk of this ratings stats
	*/
	@Override
	public void setClassPK(long classPK) {
		_ratingsStats.setClassPK(classPK);
	}

	/**
	* Sets the company ID of this ratings stats.
	*
	* @param companyId the company ID of this ratings stats
	*/
	@Override
	public void setCompanyId(long companyId) {
		_ratingsStats.setCompanyId(companyId);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_ratingsStats.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_ratingsStats.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_ratingsStats.setExpandoBridgeAttributes(serviceContext);
	}

	@Override
	public void setNew(boolean n) {
		_ratingsStats.setNew(n);
	}

	/**
	* Sets the primary key of this ratings stats.
	*
	* @param primaryKey the primary key of this ratings stats
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_ratingsStats.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_ratingsStats.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the stats ID of this ratings stats.
	*
	* @param statsId the stats ID of this ratings stats
	*/
	@Override
	public void setStatsId(long statsId) {
		_ratingsStats.setStatsId(statsId);
	}

	/**
	* Sets the total entries of this ratings stats.
	*
	* @param totalEntries the total entries of this ratings stats
	*/
	@Override
	public void setTotalEntries(int totalEntries) {
		_ratingsStats.setTotalEntries(totalEntries);
	}

	/**
	* Sets the total score of this ratings stats.
	*
	* @param totalScore the total score of this ratings stats
	*/
	@Override
	public void setTotalScore(double totalScore) {
		_ratingsStats.setTotalScore(totalScore);
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<RatingsStats> toCacheModel() {
		return _ratingsStats.toCacheModel();
	}

	@Override
	public RatingsStats toEscapedModel() {
		return new RatingsStatsWrapper(_ratingsStats.toEscapedModel());
	}

	@Override
	public String toString() {
		return _ratingsStats.toString();
	}

	@Override
	public RatingsStats toUnescapedModel() {
		return new RatingsStatsWrapper(_ratingsStats.toUnescapedModel());
	}

	@Override
	public String toXmlString() {
		return _ratingsStats.toXmlString();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof RatingsStatsWrapper)) {
			return false;
		}

		RatingsStatsWrapper ratingsStatsWrapper = (RatingsStatsWrapper)obj;

		if (Objects.equals(_ratingsStats, ratingsStatsWrapper._ratingsStats)) {
			return true;
		}

		return false;
	}

	@Override
	public RatingsStats getWrappedModel() {
		return _ratingsStats;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _ratingsStats.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _ratingsStats.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_ratingsStats.resetOriginalValues();
	}

	private final RatingsStats _ratingsStats;
}