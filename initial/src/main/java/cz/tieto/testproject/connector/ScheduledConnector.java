package cz.tieto.testproject.connector;

import cz.tieto.testproject.Application;
import cz.tieto.testproject.database.QuoteDAO;
import cz.tieto.testproject.domain.Quote;
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
    @Autowired private QuoteDAO quoteDAO;

    @Scheduled(fixedRate = 3000)
    public void getQuote (){
        try {
            final Quote quote = restTemplate.getForObject("http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
            log.info(quote.toString());
            quoteDAO.saveQuote(quote);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
