package ro.uti.ran.core.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.orm.jpa.vendor.Database;
import ro.uti.ran.core.datasourcerouter.DatasourceRouter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

import static ro.uti.ran.core.datasourcerouter.EnvironmentType.RAL;
import static ro.uti.ran.core.datasourcerouter.EnvironmentType.RAN;

/**
 * A se rula folosind: JVM OPY -javaagent:%MAVEN_HOME%/local/repository/org/apache/openjpa/openjpa/2.4.0/openjpa-2.4.0.jar
 * <p/>
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-09-15 16:50
 */
@Configuration
@Profile(Profiles.DEV)
@PropertySource("classpath:jdbc.properties")
public class DevDataSourceConfig implements DataSourceConfig {

    @Autowired
    private Environment env;

    @Override
    public DataSource portalDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("dev.portal.db.driver"));
        dataSource.setUrl(env.getProperty("dev.portal.db.url"));
        dataSource.setUsername(env.getProperty("dev.portal.db.username"));
        dataSource.setPassword(env.getProperty("dev.portal.db.password"));

        return JavaMelodyConfig.createProxy(PORTAL_DS, dataSource);
    }

    @Bean
    @Override
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
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("dev.registru.db.driver"));
        dataSource.setUrl(env.getProperty("dev.registru.db.url"));
        dataSource.setUsername(env.getProperty("dev.registru.db.username"));
        dataSource.setPassword(env.getProperty("dev.registru.db.password"));

        return JavaMelodyConfig.createProxy(REGISTRU_DS, dataSource);
    }

    private DataSource getRAL_DS() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(env.getProperty("dev.introducere.db.driver"));
        dataSource.setUrl(env.getProperty("dev.introducere.db.url"));
        dataSource.setUsername(env.getProperty("dev.introducere.db.username"));
        dataSource.setPassword(env.getProperty("dev.introducere.db.password"));

        return JavaMelodyConfig.createProxy(INTRODUCERE_DS, dataSource);
    }

    @Override
    public Database getDatabaseType() {
        return Database.ORACLE;
    }
}
