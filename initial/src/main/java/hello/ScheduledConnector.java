package hello;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by kuznijan on 23-Nov-16.
 */
@Component
public class ScheduledConnector {
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    @Autowired private DataSource ods;

    @Scheduled(fixedRate = 1000)
    public void getQuote (){

        try {
            final Quote quote = restTemplate.getForObject(
                    "http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
            log.info(quote.toString());
            final Connection conn = ods.getConnection();
            final PreparedStatement pst;
            pst = conn.prepareStatement ("insert into QUOTE (TYPE, TEXT, TEXT_ID) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1,quote.getType());
            pst.setString(2,quote.getValue().getQuote());
            pst.setInt(3,quote.getValue().getId().intValue());
            pst.execute();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
