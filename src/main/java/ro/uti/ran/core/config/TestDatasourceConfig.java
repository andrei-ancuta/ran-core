package ro.uti.ran.core.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.scheduling.Trigger;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by miroslav.rusnac on 10/02/2016.
 */
@Configuration
@Profile(Profiles.TEST)
@PropertySource("classpath:h2/h2.properties")
public class TestDatasourceConfig implements DataSourceConfig {

    public static BasicDataSource commonDs = null;

    @Value("classpath:h2/scripts/CommonDatabase.sql")
    private Resource schemaScript;

    @Value("classpath:h2/scripts/rh2util.sql")
    private Resource h2utilScript;

    @Value("classpath:h2/data/dataPump.sql")
    private Resource pump0;

    @Value("classpath:h2/data/dataPump1.sql")
    private Resource pump1;

    @Value("classpath:h2/data/dataPump2.sql")
    private Resource pump2;

    @Autowired
    private Environment env;

    @Override
    public DataSource portalDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        if (commonDs == null) {
            dataSource.setDriverClassName(env.getProperty("test.portal.db.driver"));
            dataSource.setUrl(env.getProperty("test.portal.db.url"));
            dataSource.setUsername(env.getProperty("test.portal.db.username"));
            dataSource.setDefaultAutoCommit(true);
            dataSource.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
            DatabasePopulatorUtils.execute(portalPopulator(), dataSource);
            commonDs = dataSource;
        } else {
            dataSource = commonDs;
        }
        return JavaMelodyConfig.createProxy(PORTAL_DS, dataSource);
    }


    @Bean
    @Override
    public DataSource registruDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        if (commonDs == null) {
            dataSource.setDriverClassName(env.getProperty("test.registru.db.driver"));
            dataSource.setUrl(env.getProperty("test.registru.db.url"));
            dataSource.setUsername(env.getProperty("test.registru.db.username"));
            DatabasePopulatorUtils.execute(portalPopulator(), dataSource);
            commonDs = dataSource;
        } else {
            dataSource = commonDs;
        }
        return JavaMelodyConfig.createProxy(PORTAL_DS, dataSource);
    }


    private DatabasePopulator portalPopulator() {
        ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();
        databasePopulator.setContinueOnError(false);
        databasePopulator.addScripts(
                schemaScript,
                h2utilScript,
                pump0,
                pump1/*,
                pump2*/
        );

        return databasePopulator;
    }

    @Override
    public Database getDatabaseType() {
        return Database.H2;
    }


}
