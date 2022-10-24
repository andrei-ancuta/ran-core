package ro.uti.ran.core.config;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.vendor.Database;
import ro.uti.ran.core.datasourcerouter.DatasourceRouter;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static ro.uti.ran.core.datasourcerouter.EnvironmentType.RAL;
import static ro.uti.ran.core.datasourcerouter.EnvironmentType.RAN;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 16:50
 */
@Configuration
@Profile(Profiles.PRODUCTION)
public class ProductionDataSourceConfig implements DataSourceConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(ProductionDataSourceConfig.class);

    @Autowired
    private Environment env;

    @Override
    public DataSource portalDataSource() {
        final String datasourceJndiName = env.getProperty("portal.datasource.jndi-name");

        LOGGER.debug("Resolving JNDI data source with name: " + datasourceJndiName);

        if (StringUtils.isEmpty(datasourceJndiName)) {
            throw new IllegalStateException("Database JNDI name not defined in application.properties#portal.datasource.jndi-name");
        }

        try {

            Context ctx = new InitialContext();

            DataSource ds = (DataSource) ctx.lookup(datasourceJndiName);


            LOGGER.debug("Data source:{}", ds);

            return JavaMelodyConfig.createProxy(PORTAL_DS, ds);
        } catch (Throwable th) {
            throw new IllegalStateException("Eroare la preluare data source avand numele " + datasourceJndiName);
        }
    }

    @Override
    @Bean
    public DataSource registruDataSource() {
        DataSource ran_DS = getRAN_DS();
        DataSource ral_DS = getRAL_DS();
        Map targetDataSources = new HashMap();
        targetDataSources.put(RAN, ran_DS);
        targetDataSources.put(RAL, ral_DS);

        DatasourceRouter datasourceRouter = new DatasourceRouter();
        datasourceRouter.setTargetDataSources(targetDataSources);
        datasourceRouter.setDefaultTargetDataSource(ran_DS);

        return datasourceRouter;
    }

    private DataSource getRAN_DS() {
        final String datasourceJndiName = env.getProperty("registru.datasource.jndi-name");

        LOGGER.debug("Resolving JNDI data source with name: " + datasourceJndiName);

        if (StringUtils.isEmpty(datasourceJndiName)) {
            throw new IllegalStateException("Database JNDI name not defined in application.properties#registru.datasource.jndi-name");
        }

        try {

            Context ctx = new InitialContext();

            DataSource ds = (DataSource) ctx.lookup(datasourceJndiName);
            LOGGER.debug("Data source:{}", ds);

            return JavaMelodyConfig.createProxy(REGISTRU_DS, ds);
        } catch (Throwable th) {
            throw new IllegalStateException("Eroare la preluare data source avand numele " + datasourceJndiName);
        }
    }

    private DataSource getRAL_DS() {
        final String datasourceJndiName = env.getProperty("introducere.datasource.jndi-name");

        LOGGER.debug("Resolving JNDI data source with name: " + datasourceJndiName);

        if (StringUtils.isEmpty(datasourceJndiName)) {
            throw new IllegalStateException("Database JNDI name not defined in application.properties#registru.datasource.jndi-name");
        }

        try {

            Context ctx = new InitialContext();

            DataSource ds = (DataSource) ctx.lookup(datasourceJndiName);

            LOGGER.debug("Data source:{}", ds);

            return JavaMelodyConfig.createProxy(INTRODUCERE_DS, ds);
        } catch (Throwable th) {
            throw new IllegalStateException("Eroare la preluare data source avand numele " + datasourceJndiName);
        }
    }

    @Override
    public Database getDatabaseType() {
        return Database.ORACLE;
    }
}
