package ${apiPackagePath}.service;

<#if entity.hasEntityColumns()>
	import ${apiPackagePath}.model.${entity.name};
</#if>

import com.liferay.petra.concurrent.DCLSingleton;
import com.liferay.petra.sql.dsl.query.DSLQuery;
import com.liferay.portal.kernel.dao.orm.DynamicQuery;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.model.PersistedModel;
import com.liferay.portal.kernel.module.util.SystemBundleUtil;
import com.liferay.portal.kernel.util.OrderByComparator;

import java.io.InputStream;
import java.io.Serializable;

import java.sql.Blob;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;

<#if stringUtil.equals(sessionTypeName, "Local")>
/**
 * Provides the local service utility for ${entity.name}. This utility wraps
 * <code>${packagePath}.service.impl.${entity.name}LocalServiceImpl</code> and
 * is an access point for service operations in application layer code running
 * on the local server. Methods of this service will not have security checks
 * based on the propagated JAAS credentials because this service can only be
 * accessed from within the same VM.
 *
 * @author ${author}
 * @see ${entity.name}LocalService
<#if classDeprecated>
 * @deprecated ${classDeprecatedComment}
</#if>
 * @generated
 */
<#else>
/**
 * Provides the remote service utility for ${entity.name}. This utility wraps
 * <code>${packagePath}.service.impl.${entity.name}ServiceImpl</code> and is an
 * access point for service operations in application layer code running on a
 * remote server. Methods of this service are expected to have security checks
 * based on the propagated JAAS credentials because this service can be
 * accessed remotely.
 *
 * @author ${author}
 * @see ${entity.name}Service
<#if classDeprecated>
 * @deprecated ${classDeprecatedComment}
</#if>
 * @generated
 */
</#if>

<#if classDeprecated>
	@Deprecated
</#if>
public class ${entity.name}${sessionTypeName}ServiceUtil {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify this class directly. Add custom service methods to <code>${packagePath}.service.impl.${entity.name}${sessionTypeName}ServiceImpl</code> and rerun ServiceBuilder to regenerate this class.
	 */

	<#list methods as method>
		<#if !method.isStatic() && method.isPublic() && serviceBuilder.isCustomMethod(method)>
			${serviceBuilder.getJavadocComment(method)}

			<#if serviceBuilder.hasAnnotation(method, "Deprecated")>
				@Deprecated
			</#if>
			public static

			${serviceBuilder.getTypeParametersDefinition(method.typeParameters)} ${serviceBuilder.getTypeGenericsName(method.returns)} ${method.name}(

			<#list method.parameters as parameter>
				${serviceBuilder.getTypeGenericsName(parameter.type)} ${parameter.name}

				<#if parameter_has_next>
					,
				</#if>
			</#list>

			)

			<#list method.exceptions as exception>
				<#if exception_index == 0>
					throws
				</#if>

				${exception.fullyQualifiedName}

				<#if exception_has_next>
					,
				</#if>
			</#list>

			{
				<#if !stringUtil.equals(method.returns.value, "void")>
					return
				</#if>

				getService().${method.name}(

				<#list method.parameters as parameter>
					${parameter.name}

					<#if parameter_has_next>
						,
					</#if>
				</#list>

				);
			}
		</#if>
	</#list>

	<#if validator.isNotNull(pluginName)>
		public static void clearService() {
			_service = null;
		}
	</#if>

	public static ${entity.name}${sessionTypeName}Service getService() {
		<#if serviceBuilder.isVersionGTE_7_4_0()>
			return _serviceDCLSingleton.getSingleton(${entity.name}${sessionTypeName}ServiceUtil::_getService);
		<#else>
			return _service;
		</#if>
	}

	<#if serviceBuilder.isVersionGTE_7_4_0()>
		private static ${entity.name}${sessionTypeName}Service _getService() {
			Bundle bundle = FrameworkUtil.getBundle(
				${entity.name}${sessionTypeName}ServiceUtil.class);

			BundleContext bundleContext;

			if (bundle == null) {
				bundleContext = SystemBundleUtil.getBundleContext();
			}
			else {
				bundleContext = bundle.getBundleContext();
			}

			ServiceReference<${entity.name}${sessionTypeName}Service> 
				serviceReference = bundleContext.getServiceReference(
					${entity.name}${sessionTypeName}Service.class);

			if (serviceReference == null) {
				return null;
			}

			return bundleContext.getService(serviceReference);
		}

		private static final DCLSingleton<${entity.name}${sessionTypeName}Service>
			_serviceDCLSingleton = new DCLSingleton<>();
	<#else>
		private static volatile ${entity.name}${sessionTypeName}Service _service;
	</#if>

}