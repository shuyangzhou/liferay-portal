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

package com.liferay.portal.kernel.dao.orm;

import com.liferay.petra.string.StringBundler;
import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.cache.key.CacheKeyGenerator;
import com.liferay.portal.kernel.cache.key.CacheKeyGeneratorUtil;
import com.liferay.portal.kernel.model.BaseModel;
import com.liferay.portal.kernel.model.CacheModel;
import com.liferay.portal.kernel.util.HashMapBuilder;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.registry.Registry;
import com.liferay.registry.RegistryUtil;
import com.liferay.registry.ServiceRegistrar;

import java.io.Serializable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * @author Brian Wing Shun Chan
 * @author Shuyang Zhou
 */
public class FinderPath {

	public static FinderPath create(
		boolean entityCacheEnabled, boolean finderCacheEnabled,
		Class<?> resultClass, String cacheName, String methodName,
		String[] params) {

		return create(
			entityCacheEnabled, finderCacheEnabled, resultClass, cacheName,
			methodName, params, -1, null, null);
	}

	public static FinderPath create(
		boolean entityCacheEnabled, boolean finderCacheEnabled,
		Class<?> resultClass, String cacheName, String methodName,
		String[] params, long columnBitmask) {

		return create(
			entityCacheEnabled, finderCacheEnabled, resultClass, cacheName,
			methodName, params, columnBitmask, null, null);
	}

	public static FinderPath create(
		boolean entityCacheEnabled, boolean finderCacheEnabled,
		Class<?> resultClass, String cacheName, String methodName,
		String[] params, long columnBitmask,
		Function<BaseModel<?>, Object[]> argumentsFunction,
		Function<BaseModel<?>, Object[]> originalArgumentsFunction) {

		FinderPath finderPath = new FinderPath(
			entityCacheEnabled, finderCacheEnabled, resultClass, cacheName,
			methodName, params, columnBitmask, argumentsFunction,
			originalArgumentsFunction);

		ServiceRegistrar serviceRegistrar = _serviceRegistrars.computeIfAbsent(
			cacheName,
			key -> {
				Registry registry = RegistryUtil.getRegistry();

				return registry.getServiceRegistrar(FinderPath.class);
			});

		serviceRegistrar.registerService(
			FinderPath.class, finderPath,
			HashMapBuilder.put(
				"cache.name", cacheName
			).build());

		return finderPath;
	}

	public static void delete(String cacheName) {
		ServiceRegistrar serviceRegistrar = _serviceRegistrars.remove(
			cacheName);

		if (serviceRegistrar != null) {
			serviceRegistrar.destroy();
		}
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *          #FinderPath(boolean, boolean, Class, String, String, String[],
	 *          	long, Function, Function)}
	 */
	@Deprecated
	public FinderPath(
		boolean entityCacheEnabled, boolean finderCacheEnabled,
		Class<?> resultClass, String cacheName, String methodName,
		String[] params) {

		this(
			entityCacheEnabled, finderCacheEnabled, resultClass, cacheName,
			methodName, params, -1, null, null);
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link
	 *          #FinderPath(boolean, boolean, Class, String, String, String[],
	 *          	long, Function, Function)}
	 */
	@Deprecated
	public FinderPath(
		boolean entityCacheEnabled, boolean finderCacheEnabled,
		Class<?> resultClass, String cacheName, String methodName,
		String[] params, long columnBitmask) {

		this(
			entityCacheEnabled, finderCacheEnabled, resultClass, cacheName,
			methodName, params, columnBitmask, null, null);
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #encodeCacheKey(
	 *             Object[])}
	 */
	@Deprecated
	public String encodeArguments(Object[] arguments) {
		String[] keys = new String[arguments.length * 2];

		for (int i = 0; i < arguments.length; i++) {
			int index = i * 2;

			keys[index] = StringPool.PERIOD;
			keys[index + 1] = StringUtil.toHexString(arguments[i]);
		}

		return StringUtil.toHexString(_getCacheKey(keys));
	}

	public Serializable encodeCacheKey(Object[] arguments) {
		CacheKeyGenerator cacheKeyGenerator = _cacheKeyGenerator;

		if (cacheKeyGenerator == null) {
			cacheKeyGenerator = CacheKeyGeneratorUtil.getCacheKeyGenerator(
				_cacheKeyGeneratorCacheName);
		}

		String[] keys = new String[arguments.length * 2];

		for (int i = 0; i < arguments.length; i++) {
			int index = i * 2;

			keys[index] = StringPool.PERIOD;
			keys[index + 1] = StringUtil.toHexString(arguments[i]);
		}

		return cacheKeyGenerator.getCacheKey(
			new String[] {
				_cacheKeyPrefix,
				StringUtil.toHexString(cacheKeyGenerator.getCacheKey(keys))
			});
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), replaced by {@link #encodeCacheKey(
	 *             Object[])}
	 */
	@Deprecated
	public Serializable encodeCacheKey(String encodedArguments) {
		return _getCacheKey(new String[] {_cacheKeyPrefix, encodedArguments});
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public Serializable encodeLocalCacheKey(String encodedArguments) {
		return _getCacheKey(
			new String[] {
				_cacheName.concat(
					StringPool.PERIOD
				).concat(
					_cacheKeyPrefix
				),
				encodedArguments
			});
	}

	public Object[] getArguments(BaseModel<?> baseModel) {
		if (_argumentsFunction == null) {
			return null;
		}

		return _argumentsFunction.apply(baseModel);
	}

	public String getCacheName() {
		return _cacheName;
	}

	public long getColumnBitmask() {
		return _columnBitmask;
	}

	public Object[] getOriginalArguments(BaseModel<?> baseModel) {
		if (_originalArgumentsFunction == null) {
			return null;
		}

		return _originalArgumentsFunction.apply(baseModel);
	}

	public Class<?> getResultClass() {
		return _resultClass;
	}

	/**
	 * @deprecated As of Athanasius (7.3.x), with no direct replacement
	 */
	@Deprecated
	public boolean isEntityCacheEnabled() {
		return _entityCacheEnabled;
	}

	public boolean isFinderCacheEnabled() {
		return _finderCacheEnabled;
	}

	private static Map<String, String> _getEncodedTypes() {
		return HashMapBuilder.put(
			Boolean.class.getName(), Boolean.class.getSimpleName()
		).put(
			Byte.class.getName(), Byte.class.getSimpleName()
		).put(
			Character.class.getName(), Character.class.getSimpleName()
		).put(
			Double.class.getName(), Double.class.getSimpleName()
		).put(
			Float.class.getName(), Float.class.getSimpleName()
		).put(
			Integer.class.getName(), Integer.class.getSimpleName()
		).put(
			Long.class.getName(), Long.class.getSimpleName()
		).put(
			Short.class.getName(), Short.class.getSimpleName()
		).put(
			String.class.getName(), String.class.getSimpleName()
		).build();
	}

	private FinderPath(
		boolean entityCacheEnabled, boolean finderCacheEnabled,
		Class<?> resultClass, String cacheName, String methodName,
		String[] params, long columnBitmask,
		Function<BaseModel<?>, Object[]> argumentsFunction,
		Function<BaseModel<?>, Object[]> originalArgumentsFunction) {

		_entityCacheEnabled = entityCacheEnabled;
		_finderCacheEnabled = finderCacheEnabled;
		_resultClass = resultClass;
		_cacheName = cacheName;
		_params = params;
		_columnBitmask = columnBitmask;
		_argumentsFunction = argumentsFunction;
		_originalArgumentsFunction = originalArgumentsFunction;

		if (CacheModel.class.isAssignableFrom(_resultClass)) {
			_cacheKeyGeneratorCacheName = _BASE_MODEL_CACHE_KEY_GENERATOR_NAME;
		}
		else {
			_cacheKeyGeneratorCacheName = FinderCache.class.getName();
		}

		CacheKeyGenerator cacheKeyGenerator =
			CacheKeyGeneratorUtil.getCacheKeyGenerator(
				_cacheKeyGeneratorCacheName);

		if (cacheKeyGenerator.isCallingGetCacheKeyThreadSafe()) {
			_cacheKeyGenerator = cacheKeyGenerator;
		}
		else {
			_cacheKeyGenerator = null;
		}

		_initCacheKeyPrefix(methodName, params);
	}

	private Serializable _getCacheKey(String[] keys) {
		CacheKeyGenerator cacheKeyGenerator = _cacheKeyGenerator;

		if (cacheKeyGenerator == null) {
			cacheKeyGenerator = CacheKeyGeneratorUtil.getCacheKeyGenerator(
				_cacheKeyGeneratorCacheName);
		}

		return cacheKeyGenerator.getCacheKey(keys);
	}

	private void _initCacheKeyPrefix(String methodName, String[] params) {
		StringBundler sb = new StringBundler(params.length * 2 + 3);

		sb.append(methodName);
		sb.append(_PARAMS_SEPARATOR);

		for (String param : params) {
			sb.append(StringPool.PERIOD);
			sb.append(_encodedTypes.getOrDefault(param, param));
		}

		sb.append(_ARGS_SEPARATOR);

		_cacheKeyPrefix = sb.toString();
	}

	private static final String _ARGS_SEPARATOR = "_A_";

	private static final String _BASE_MODEL_CACHE_KEY_GENERATOR_NAME =
		FinderCache.class.getName() + "#CacheModel";

	private static final String _PARAMS_SEPARATOR = "_P_";

	private static final Map<String, String> _encodedTypes = _getEncodedTypes();
	private static final Map<String, ServiceRegistrar> _serviceRegistrars =
		new ConcurrentHashMap<>();

	private final Function<BaseModel<?>, Object[]> _argumentsFunction;
	private final CacheKeyGenerator _cacheKeyGenerator;
	private final String _cacheKeyGeneratorCacheName;
	private String _cacheKeyPrefix;
	private final String _cacheName;
	private final long _columnBitmask;
	private final boolean _entityCacheEnabled;
	private final boolean _finderCacheEnabled;
	private final Function<BaseModel<?>, Object[]> _originalArgumentsFunction;
	private final String[] _params;
	private final Class<?> _resultClass;

}