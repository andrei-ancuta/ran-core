package ro.uti.ran.core.datasourcerouter;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by IntelliJ IDEA.
 * User: daniel
 * Date: Mar 4, 2010
 * Time: 1:26:00 PM
 * To change this template use File | Settings | File Templates.
 */
public class DatasourceRouter extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        if (ContextHolder.getEnvironmentType() != null) {
            return ContextHolder.getEnvironmentType();
        }
        return null;
    }
}
