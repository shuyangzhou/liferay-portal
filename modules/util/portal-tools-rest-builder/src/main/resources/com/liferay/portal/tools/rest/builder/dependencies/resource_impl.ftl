package ${configYAML.apiPackagePath}.internal.resource;

<#compress>
	<#list openAPIYAML.components.schemas?keys as schemaName>
		import ${configYAML.apiPackagePath}.dto.${schemaName};
	</#list>
</#compress>

import ${configYAML.apiPackagePath}.resource.${schemaName}Resource;

import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.vulcan.context.AcceptLanguage;
import com.liferay.portal.vulcan.context.Pagination;
import com.liferay.portal.vulcan.dto.Page;
import com.liferay.portal.vulcan.util.TransformUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * @author ${configYAML.author}
 */
@Component(
	properties = "OSGI-INF/${schemaPath}.properties", scope = ServiceScope.PROTOTYPE,
	service = ${schemaName}Resource.class
)
public class ${schemaName}ResourceImpl implements ${schemaName}Resource {

	<#if openAPIYAML.pathItems??>
		<#list openAPIYAML.pathItems?keys as path>
			<#assign pathItem = openAPIYAML.pathItems[path] />

			<#list javaTool.getOperations(pathItem) as operation>
				<#assign javaSignature = javaTool.getJavaSignature(configYAML, openAPIYAML, operation, path, pathItem, schemaName) />

				<#if !stringUtil.equals(javaSignature.returnType, schemaName) && !stringUtil.equals(javaSignature.returnType, "Page<${schemaName}>") && !stringUtil.endsWith(javaSignature.methodName, schemaName)>
					<#continue>
				</#if>

				@Override
				<@compress single_line=true>
					public ${javaSignature.returnType} ${javaSignature.methodName}(
						<#list javaSignature.javaParameters as javaParameter>
							${javaParameter.parameterType} ${javaParameter.parameterName}

							<#if javaParameter_has_next>
								,
							</#if>
						</#list>
					) throws Exception {
				</@compress>

		throw new UnsupportedOperationException();
				}
			</#list>
		</#list>
	</#if>

	protected <T, R> List<R> transform(List<T> list, Function<T, R> transformFunction) {
		return TransformUtil.transform(list, transformFunction);
	}

	@Context
	protected AcceptLanguage acceptLanguage;

	@Context
	protected Company company;

}