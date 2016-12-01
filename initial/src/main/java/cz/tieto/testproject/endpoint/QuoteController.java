package cz.tieto.testproject.endpoint;

import cz.tieto.testproject.database.QuoteDAO;
import cz.tieto.testproject.domain.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by kuznijan on 01-Dec-16.
 */

@RestController
public class QuoteController {
    @Autowired
    private QuoteDAO quoteDAO;

    @RequestMapping("/quotes")
    public List<Quote> quotes(){
        List<Quote> quoteList = quoteDAO.getQuotes();
        return quoteList;
    }

    @RequestMapping("/quote")
    public Quote quote(@RequestParam(value="id") Long id){
        Quote quote = quoteDAO.getQuote(id);
        return quote;
    }

}
