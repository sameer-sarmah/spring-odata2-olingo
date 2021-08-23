package northwind.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.jaxrs.servlet.CXFNonSpringJaxrsServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@ComponentScan(basePackages = {"northwind"})
@Configuration
public class AppConfig {

	@Bean
	public ServletRegistrationBean odataServlet() {

	    ServletRegistrationBean odataServRegstration = new ServletRegistrationBean(new CXFNonSpringJaxrsServlet(),
	            "/Northwind.svc/*");
	    Map<String, String> initParameters = new HashMap<>();
	    initParameters.put("javax.ws.rs.Application", "org.apache.olingo.odata2.core.rest.app.ODataApplication");
	    initParameters.put("org.apache.olingo.odata2.service.factory",
	            "northwind.odata.NorthwindOdataServiceFactory");
	    odataServRegstration.setInitParameters(initParameters);

	    return odataServRegstration;

	}
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
}
