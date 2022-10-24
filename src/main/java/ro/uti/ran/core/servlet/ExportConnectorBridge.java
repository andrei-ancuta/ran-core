package ro.uti.ran.core.servlet;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import ro.uti.ran.core.servlet.internal.ExportConnector;

@Component
public class ExportConnectorBridge implements ApplicationContextAware {

	private static Map<String, ExportConnector> _CONNECTORS = new HashMap<String, ExportConnector>();
	 
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		_CONNECTORS = context.getBeansOfType(ExportConnector.class);
	}
	
	public static ExportConnector getExportConnector(String name) {
		return _CONNECTORS.get(name);
	}

}
