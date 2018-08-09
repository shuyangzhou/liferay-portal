package ${package}.portlet;

import ${package}.constants.${className}PortletKeys;

import com.liferay.bean.portlet.LiferayPortletConfiguration;

import javax.inject.Inject;

import javax.portlet.PortletConfig;
import javax.portlet.annotations.LocaleString;
import javax.portlet.annotations.PortletConfiguration;
import javax.portlet.annotations.RenderMethod;

/**
 * @author ${author}
 */
@LiferayPortletConfiguration(
	portletName = ${className}PortletKeys.${className},
	properties = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.instanceable=true"
	}
)
@PortletConfiguration(
	portletName = ${className}PortletKeys.${className},
	title = @LocaleString(value = ${className}PortletKeys.${className})
)
public class ${className}Portlet {

	@Inject
	private PortletConfig portletConfig;

	@RenderMethod(
		include = "/WEB-INF/jsp/view.jsp",
		portletNames = {${className}PortletKeys.${className}}
	)
	public String doView() {
		return "Hello from " + portletConfig.getPortletName();
	}

}