package ro.uti.ran.core.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
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
        basePackages = {"ro.uti.ran.core.repository.portal"},
        repositoryFactoryBeanClass = RanRepositoryFactoryBean.class,
        entityManagerFactoryRef = "portalEntityManagerFactory",
        transactionManagerRef = "portalTransactionManager"
)
public class PortalPersistenceLayerConfig {

    @Autowired
    DataSourceConfig dataSourceConfig;


    @Bean
    public EntityManagerFactory portalEntityManagerFactory() {
        // openjpa vendor
        OpenJpaVendorAdapter vendorAdapter = new OpenJpaVendorAdapter();
        vendorAdapter.setDatabase(dataSourceConfig.getDatabaseType());
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setShowSql(true);

        // compose entityManager
        LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        factoryBean.setJtaDataSource(dataSourceConfig.portalDataSource());
        factoryBean.setPersistenceUnitName("ran-portal");

        // scan packages
        factoryBean.setPackagesToScan(
                "ro.uti.ran.core.model.app",
                "ro.uti.ran.core.model.nom",
                "ro.uti.ran.core.model.portal"
        );

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

        return jpaProperties;
    }

    @Bean
    @Primary
    public PlatformTransactionManager portalTransactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(portalEntityManagerFactory());
        return transactionManager;
    }
}
