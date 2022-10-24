package ro.uti.ran.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.OpenJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import ro.uti.ran.core.model.utils.RanConstants;
import ro.uti.ran.core.repository.base.RanRepositoryFactoryBean;

import javax.persistence.EntityManagerFactory;
import java.util.Properties;

/**
 * Author: vitalie.babalunga@greensoft.com.ro
 * Date: 2015-08-31 16:08
 */
@Configuration
@EnableTransactionManagement(order = RanConstants.TRANSACTION_MANAGEMENT_ORDER)
@EnableJpaRepositories(
        basePackages = {"ro.uti.ran.core.repository.registru", "ro.uti.ran.core.repository.messages"},
        repositoryFactoryBeanClass = RanRepositoryFactoryBean.class,
        entityManagerFactoryRef = "registruEntityManagerFactory",
        transactionManagerRef = "registruTransactionManager"
)
public class RegistruPersistenceLayerConfig {

    @Autowired
    DataSourceConfig dataSourceConfig;

    @Bean
    public EntityManagerFactory registruEntityManagerFactory() {
        // openjpa vendor
        OpenJpaVendorAdapter vendorAdapter = new OpenJpaVendorAdapter();
        vendorAdapter.setDatabase(dataSourceConfig.getDatabaseType());
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setShowSql(false);

        // compose entityManager
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setJtaDataSource(dataSourceConfig.registruDataSource());
        factoryBean.setPersistenceUnitName("ran-registru");

        // scan packages
        factoryBean.setPackagesToScan("ro.uti.ran.core.model.registru", "ro.uti.ran.core.model.messages");

        // JPA properties
        factoryBean.setJpaProperties(getEntityManagerFactoryJpaProperties());

        factoryBean.afterPropertiesSet();

        return factoryBean.getObject();
    }

    public Properties getEntityManagerFactoryJpaProperties() {
        // JPA properties
        Properties jpaProperties = new Properties();

        // OpenJPA specific properties
        //jpaProperties.setProperty("openjpa.jdbc.SynchronizeMappings", "buildSchema(ForeignKeys=true)");

//        jpaProperties.setProperty("openjpa.Log", "DefaultLevel=TRACE, Runtime=TRACE, Tool=TRACE, SQL=TRACE");

        return jpaProperties;
    }

    @Bean
    public PlatformTransactionManager registruTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(registruEntityManagerFactory());
        return transactionManager;
    }
}
