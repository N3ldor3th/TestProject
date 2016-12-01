package cz.tieto.testproject.mapper;

import cz.tieto.testproject.domain.Quote;
import cz.tieto.testproject.domain.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by kuznijan on 01-Dec-16.
 */

@Component
@Scope("singleton")
public class QuoteRowMapper implements RowMapper {

    @Override
    public Object mapRow(ResultSet resultSet, int i) throws SQLException {
        Quote quote = new Quote();
        Value value = new Value();
        quote.setType(resultSet.getString("TYPE"));
        value.setQuote(resultSet.getString("TEXT"));
        value.setId(resultSet.getLong("TEXT_ID"));
        quote.setValue(value);
        return quote;
    }
}
