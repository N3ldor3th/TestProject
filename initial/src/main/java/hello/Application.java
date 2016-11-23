package hello;

import liquibase.integration.spring.SpringLiquibase;
import oracle.jdbc.pool.OracleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
@EnableScheduling
@Component
@PropertySource(value = "application.properties")
public class Application {
	private final RestTemplate restTemplate = new RestTemplate();
	private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.url}")
    private String url;


    public static void main(String args[]) {
		SpringApplication.run(Application.class);
	}
	
	@Scheduled(fixedRate = 3000)
	public void getQuote (){

		try {
			Quote quote = restTemplate.getForObject(
					"http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
			log.info(quote.toString());
            OracleDataSource ods = (OracleDataSource) connect();
			Connection conn = ods.getConnection();
			PreparedStatement pst = conn.prepareStatement ("insert into QUOTE (TYPE, TEXT) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
			pst.setString(1,quote.getType());
			pst.setString(2,quote.getValue().getQuote());
            //pst.setInt(3,quote.getValue().getId().intValue());
			pst.execute();
			pst.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
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