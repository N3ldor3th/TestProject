package hello;

import liquibase.integration.spring.SpringLiquibase;
import oracle.jdbc.pool.OracleDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import javax.sql.DataSource;
import java.sql.SQLException;

@SpringBootApplication
@EnableScheduling
public class Application {

    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.url}")
    private String url;


    public static void main(String args[]) {
		SpringApplication.run(Application.class);
	}

    @Bean
	public DataSource connect() throws SQLException{
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