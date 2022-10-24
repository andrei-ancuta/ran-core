package ro.uti.ran.core.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.AbstractMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.persistence.Column;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Component;

import ro.uti.ran.core.ws.handlers.RanWsHandlerUtil;


/**
 * 
 * @author mihai.plavichainu
 *
 */
@Component
public class BeanUtils {

	
	/**
	 * Seteaza dinamic o proprietate
	 * 
	 * @param bean
	 * @param property
	 * @param value
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 */
	public void setProperty(Object bean, String property, String value) {
		
		if(value == null) {
			return;
		}
		
		Class cls = bean.getClass();
		Integer length = null;
		
		try {
			for(Field field : cls.getDeclaredFields()){
				if(field.getType().equals(String.class) && field.getName().equals(property)) {
					Column column = field.getAnnotation(Column.class);
					if(column.length() > 0) {
						length = column.length();
					}
					
					break;
				}
			}
			
			value = value.substring(0, length-1);
		}catch(Exception e) {
			//still null
		}
		
		try {
			PropertyUtils.setProperty(bean, property, value);
		} catch(Exception e) {
			e.printStackTrace();
		}
	
		
	}
	
	/**
	 * Limiteaza numarul de linii din stack trace
	 * 
	 * @param stackTrace
	 * @param nbLines
	 * @return
	 */
	public String limitStackTrace(Throwable t, int nbLines) {
        return StringUtils.abbreviate(limitStackTrace(ExceptionUtils.getThrowables(t), nbLines), 3900);
	}
	
	/**
	 * Se va printa:
	 * 
	 * <<Throwable 1>> -> unde s-a produs
	 * ...
	 * <<Throwable n-1>> -> penultimul causedBy
	 * ...
	 * <<Throwable n>> -> ultimul causedBy
	 * 
	 * @param stackTrace
	 * @param nbLines
	 * @return
	 */
	public String limitStackTrace(Throwable[] stackTrace, int nbLines) {
		
		if(stackTrace.length == 0) {
			return "";
		}
		
		try {
			String trace = ExceptionUtils.getStackTrace(stackTrace[0]);
			String print = trace.substring(0, nth(trace, "\n", nbLines+1));
			StringBuilder result = new StringBuilder();
			result.append(print);
			
			int len = stackTrace.length;
			
			
			if(len > 2) {
				result.append("\n..............................................\n");
				trace = ExceptionUtils.getStackTrace(stackTrace[len-2]);
				print = trace.substring(0, nth(trace, "\n", nbLines+1));
				result.append(print);
			}
			
		
			if(len > 1) {
				result.append("\n..............................................\n");
				trace = ExceptionUtils.getStackTrace(stackTrace[len-1]);
				print = trace.substring(0, nth(trace, "\n", nbLines+1));
				result.append(print);
			}
			
			
			return result.toString();
		}catch (Exception e) {
			return ExceptionUtils.getStackTrace(stackTrace[0]);
		}
	}
	

	/**
	 * Gaseste pozitia la care se afla a n-a aparitie a patternului in source 
	 * @param source
	 * @param pattern
	 * @param n
	 * @return
	 */
	private int nth(String source, String pattern, int n) {
	
	   int i = 0, pos = 0, tpos = 0;
	
	   while (i < n) {
	
	      pos = source.indexOf(pattern);
	      if (pos > -1) {
	         source = source.substring(pos+1);
	         tpos += pos+1;
	         i++;
	      } else {
	         return -1;
	      }
	   }
	
	   return tpos - 1;
	}
	
}
