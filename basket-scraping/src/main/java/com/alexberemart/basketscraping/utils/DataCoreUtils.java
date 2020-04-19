package com.alexberemart.basketscraping.utils;

import org.apache.commons.dbcp.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.orm.hibernate5.HibernateTemplate;

import java.util.Properties;

/**
 * Created by alejandro on 28/9/16.
 */
public class DataCoreUtils {
    public static HibernateTemplate createHibernateTemplate(SessionFactory sessionFactory) {
        final HibernateTemplate hibernateTemplate = new HibernateTemplate();

        hibernateTemplate.setSessionFactory(sessionFactory);
//        hibernateTemplate.setFlushMode(HibernateAccessor.FLUSH_AUTO);
//        hibernateTemplate.setAllowCreate(Boolean.TRUE);
//        hibernateTemplate.setAlwaysUseNewSession(Boolean.FALSE);
        hibernateTemplate.setQueryCacheRegion("dcpQCR");
        hibernateTemplate.setMaxResults(10000);
        hibernateTemplate.setFetchSize(10000);

        return hibernateTemplate;
    }

    public static BasicDataSource createDataSource(String driverClassName, String url, String username, String password, String validationQuery) {
        final BasicDataSource ds = new BasicDataSource();

        ds.setDriverClassName(driverClassName);
        ds.setUrl(url);
        ds.setUsername(username);
        ds.setPassword(password);
        ds.setTestOnBorrow(Boolean.TRUE);
        ds.setValidationQuery(validationQuery);

        return ds;
    }

    public static Properties createHibernateProperties(String hibernateDialectClassName,
                                                       String ddlAuto,
                                                       Boolean showSql,
                                                       String default_schema) {
        final Properties props = new Properties();

        props.setProperty("javax.persistence.validation.mode", "NONE");
        props.setProperty("hibernate.dialect", hibernateDialectClassName);
        props.setProperty("hibernate.hbm2ddl.auto", ddlAuto);
        props.setProperty("hibernate.show_sql", showSql.toString());
        props.setProperty("hibernate.format_sql", showSql.toString());
        props.setProperty("hibernate.use_sql_comments", "true");
        props.setProperty("hibernate.bytecode.use_reflection_optimizer", "true");
        props.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.HashtableCacheProvider");
        props.setProperty("hibernate.cache.use_query_cache", "false");
        props.setProperty("hibernate.query.substitutions", "true 1, false 0");
        props.setProperty("hibernate.generate_statistics", "true");
        props.setProperty("hibernate.default_schema", default_schema);
        return props;
    }
}
