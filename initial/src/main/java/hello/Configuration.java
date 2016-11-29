package hello;

import liquibase.integration.spring.SpringLiquibase;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * Created by kuznijan on 23-Nov-16.
 */

@Component
public class Configuration {
    @Value("${spring.datasource.username}") private String username;
    @Value("${spring.datasource.password}") private String password;
    @Value("${spring.datasource.url}") private String url;

    @Bean
    @ConfigurationProperties(prefix="spring.datasource")
    public DataSource connect() throws SQLException {
        OracleDataSource dataSource = new OracleDataSource();
        dataSource.setUser(username);
        dataSource.setPassword(password);
        dataSource.setURL(url);
        dataSource.setImplicitCachingEnabled(true);
        dataSource.setFastConnectionFailoverEnabled(true);
        return dataSource;
    }

    @Bean
    public SpringLiquibase liquibase() throws SQLException {
        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setChangeLog("classpath:changeLog.xml");
        liquibase.setDataSource(connect());
        return liquibase;
    }
}
