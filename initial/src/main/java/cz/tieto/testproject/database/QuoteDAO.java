package cz.tieto.testproject.database;

import cz.tieto.testproject.domain.Quote;
import cz.tieto.testproject.mapper.QuoteRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * Created by kuznijan on 26-Nov-16.
 */
@Component
public class QuoteDAO {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private QuoteRowMapper quoteRowMapper;

    @Transactional
    public void saveQuote(Quote quote) {
        final String INSERT_QUOTE = "insert into QUOTE (TYPE, TEXT, TEXT_ID) values (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                con -> {
                    PreparedStatement pst =
                            con.prepareStatement(INSERT_QUOTE, new String[] {"ID"});
                    pst.setString(1, quote.getType());
                    pst.setString(2, quote.getValue().getQuote());
                    pst.setInt(3, quote.getValue().getId().intValue());
                    return pst;
                },
                keyHolder);
    }

    public List<Quote> getQuotes() {
        final String SELECT_ALL_QUOTES = "SELECT * FROM QUOTE";
        List<Quote> quotes = jdbcTemplate.query(SELECT_ALL_QUOTES, quoteRowMapper);
        return quotes;
    }

    public Quote getQuote(Long id) {
        final String SELECT_QUOTE_BY_ID = "SELECT * FROM QUOTE WHERE ID = ?";
        Quote quote = (Quote) jdbcTemplate.queryForObject(SELECT_QUOTE_BY_ID, quoteRowMapper, id);
        return quote;
    }

    /*    public void saveQuote(Quote quote) {
        final Connection conn;
        final PreparedStatement pst;
        try {
            conn = dataSource.getConnection();
            pst = conn.prepareStatement("insert into QUOTE (TYPE, TEXT, TEXT_ID) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            pst.setString(1, quote.getType());
            pst.setString(2, quote.getValue().getQuote());
            pst.setInt(3, quote.getValue().getId().intValue());
            pst.execute();
            pst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }*/
}
