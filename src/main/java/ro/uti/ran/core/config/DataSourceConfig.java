package ro.uti.ran.core.config;

import org.springframework.orm.jpa.vendor.Database;

import javax.sql.DataSource;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 16:50
 */
public interface DataSourceConfig {

    static final String PORTAL_DS = "portal-datasource";
    static final String REGISTRU_DS = "registru-datasource";
    static final String INTRODUCERE_DS = "introducere-datasource";


    /**
     * Datasource factory method
     *
     * @return
     */
    DataSource portalDataSource();

    /**
     * Datasource factory method
     *
     * @return
     */
    DataSource registruDataSource();

    Database getDatabaseType();
}
