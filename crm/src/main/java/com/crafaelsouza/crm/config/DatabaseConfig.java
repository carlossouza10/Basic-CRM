package com.crafaelsouza.crm.config;

import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.hibernate.jpa.HibernatePersistenceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * The Class DatabaseConfig.
 * 
 * @author Carlos Souza
 */
@Configuration
@EnableTransactionManagement
@ComponentScan("com.crafaelsouza.crm")
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("com.crafaelsouza.crm.repository")
public class DatabaseConfig {

	/** The Constant PROPERTY_NAME_DATABASE_DRIVER. */
	private static final String PROPERTY_NAME_DATABASE_DRIVER = "db.driver";
	
	/** The Constant PROPERTY_NAME_DATABASE_PASSWORD. */
	private static final String PROPERTY_NAME_DATABASE_PASSWORD = "db.password";
	
	/** The Constant PROPERTY_NAME_DATABASE_URL. */
	private static final String PROPERTY_NAME_DATABASE_URL = "db.url";
	
	/** The Constant PROPERTY_NAME_DATABASE_USERNAME. */
	private static final String PROPERTY_NAME_DATABASE_USERNAME = "db.username";

	/** The Constant PROPERTY_NAME_HIBERNATE_DIALECT. */
	private static final String PROPERTY_NAME_HIBERNATE_DIALECT = "hibernate.dialect";
	
	/** The Constant PROPERTY_NAME_HIBERNATE_SHOW_SQL. */
	private static final String PROPERTY_NAME_HIBERNATE_SHOW_SQL = "hibernate.show_sql";
	
	/** The Constant PROPERTY_NAME_HIBERNATE_FORMAT_SQL. */
	private static final String PROPERTY_NAME_HIBERNATE_FORMAT_SQL = "hibernate.format_sql";
	
	/** The Constant PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO. */
	private static final String PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO = "hibernate.hbm2ddl.auto";
	
	/** The Constant PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY. */
	private static final String PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY = "hibernate.ejb.naming_strategy";
	
	/** The Constant PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN. */
	private static final String PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN = "entitymanager.packages.to.scan";

	/** The env. */
	@Resource
	private Environment env;

	/**
	 * Data source.
	 *
	 * @return the data source
	 */
	@Bean
	public DataSource dataSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(env.getRequiredProperty(PROPERTY_NAME_DATABASE_DRIVER));
		dataSource.setUrl(env.getRequiredProperty(PROPERTY_NAME_DATABASE_URL));
		dataSource.setUsername(env.getRequiredProperty(PROPERTY_NAME_DATABASE_USERNAME));
		dataSource.setPassword(env.getRequiredProperty(PROPERTY_NAME_DATABASE_PASSWORD));

		return dataSource;
	}

	/**
	 * Entity manager factory.
	 *
	 * @return the local container entity manager factory bean
	 */
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setPersistenceProviderClass(HibernatePersistenceProvider.class);
		entityManagerFactoryBean.setPackagesToScan(env.getRequiredProperty(PROPERTY_NAME_ENTITYMANAGER_PACKAGES_TO_SCAN));
		entityManagerFactoryBean.setJpaProperties(hibProperties());

		return entityManagerFactoryBean;
	}

	/**
	 * Hib properties.
	 *
	 * @return the properties
	 */
	private Properties hibProperties() {
		Properties properties = new Properties();
		properties.put(PROPERTY_NAME_HIBERNATE_DIALECT,	env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_DIALECT));
		properties.put(PROPERTY_NAME_HIBERNATE_SHOW_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_SHOW_SQL));
		properties.put(PROPERTY_NAME_HIBERNATE_FORMAT_SQL, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_FORMAT_SQL));
		properties.put(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_HBM2DDL_AUTO));
		properties.put(PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY, env.getRequiredProperty(PROPERTY_NAME_HIBERNATE_NAMING_STRATEGY));
		return properties;
	}

	/**
	 * Transaction manager.
	 *
	 * @return the jpa transaction manager
	 */
	@Bean
	public JpaTransactionManager transactionManager() {
		JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
		return transactionManager;
	}

}