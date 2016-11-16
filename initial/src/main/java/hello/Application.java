package hello;

import oracle.jdbc.pool.OracleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootApplication
@EnableScheduling
public class Application {
	private final RestTemplate restTemplate = new RestTemplate();
	private static final Logger log = LoggerFactory.getLogger(Application.class);
	static OracleDataSource ods;

	public static void main(String args[]) {
		SpringApplication.run(Application.class);

		try {
			ods = (OracleDataSource) connect();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	@Scheduled(fixedRate = 3000)
	public void getQuote (){

		try {
			Quote quote = restTemplate.getForObject(
					"http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
			log.info(quote.toString());
			Connection conn = ods.getConnection();
			PreparedStatement pstmt =
					conn.prepareStatement ("insert into QUOTE (TYPE, TEXT) values (?, ?)", Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1,quote.getType());
			pstmt.setString(2,quote.getValue().getQuote());
			pstmt.execute();
			pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static DataSource connect() throws SQLException{
		OracleDataSource dataSource = new OracleDataSource();
		dataSource.setUser("kuznijan");
		dataSource.setPassword("Haniszek232629jj");
		dataSource.setURL("jdbc:oracle:thin:@localhost:1521:orcl");
		dataSource.setImplicitCachingEnabled(true);
		dataSource.setFastConnectionFailoverEnabled(true);
		return dataSource;
	}
}