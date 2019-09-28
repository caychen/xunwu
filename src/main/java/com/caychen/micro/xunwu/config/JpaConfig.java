package com.caychen.micro.xunwu.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Author:       Caychen
 * Date:         2019/9/6
 * Desc:
 */
@Configuration
@EnableJpaRepositories("com.caychen.micro.xunwu.repository")
public class JpaConfig {

//	@Bean
//	@ConfigurationProperties(prefix = "spring.datasource")
//	public DataSource dataSource(){
//		return DataSourceBuilder.create().build();
//	}
//
//	@Bean
//	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(){
//		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
//		jpaVendorAdapter.setGenerateDdl(false);
//
//		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
//		entityManagerFactoryBean.setDataSource(dataSource());
//		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
//		entityManagerFactoryBean.setPackagesToScan("com.caychen.micro.xunwu.entity");
//
//		return entityManagerFactoryBean;
//	}
//
//	@Bean
//	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
//		JpaTransactionManager transactionManager = new JpaTransactionManager();
//		transactionManager.setEntityManagerFactory(entityManagerFactory);
//		return transactionManager;
//	}
}
