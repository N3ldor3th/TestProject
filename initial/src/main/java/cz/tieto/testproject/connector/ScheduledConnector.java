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

import java.sql.SQLException;

/**
 * Created by kuznijan on 23-Nov-16.
 */
@Component
public class ScheduledConnector {
    private static final String URL = "http://gturnquist-quoters.cfapps.io/api/random";
    private static final int FIXED_RATE = 30000;
    private final RestTemplate restTemplate = new RestTemplate();
    private static final Logger log = LoggerFactory.getLogger(Application.class);
    @Autowired
    private QuoteDAO quoteDAO;

    @Scheduled(fixedRate = FIXED_RATE)
    public void getQuote (){
        final Quote quote = restTemplate.getForObject(URL, Quote.class);
        log.info(quote.toString());
        quoteDAO.saveQuote(quote);
    }
}
