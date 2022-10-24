package ro.uti.ran.core.repository.base.filter;

import javax.persistence.Query;
import java.util.LinkedList;
import java.util.List;

/**
* Author: vitalie.babalunga@greensoft.com.ro
* Date: 2015-11-11 10:37
*/
public class FilterResult {

    private StringBuilder filterBuilder = new StringBuilder();

    private List<FilterParameter> parameters = new LinkedList<FilterParameter>();

    public String getFilter() {
        return filterBuilder.toString();
    }

    public List<FilterParameter> getParameters() {
        return parameters;
    }

    public void addFiler(String expression){
        filterBuilder.append(" ").append(expression);
    }

    public void addFiler(String expression, String parameterName, Object parameterValue){
        filterBuilder.append(" ").append(expression);
        parameters.add(new FilterParameter(parameterName, parameterValue));
    }

    public void setParameters(Query query){
        if( parameters == null || parameters.size() == 0) return;

        for (FilterParameter parameter : parameters) {
            if( parameter.getValue() != null) {
                query.setParameter(parameter.getName(), parameter.getValue());
            }
        }
    }
}
