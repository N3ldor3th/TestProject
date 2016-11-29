package cz.tieto.testproject.database;

import cz.tieto.testproject.domain.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by kuznijan on 26-Nov-16.
 */
@Component
public class QuoteDAO {
    @Autowired private DataSource dataSource;

    public void saveQuote(Quote quote) throws SQLException {
        final Connection conn = dataSource.getConnection();
        final PreparedStatement pst = conn.prepareStatement ("insert into QUOTE (TYPE, TEXT, TEXT_ID) values (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        pst.setString(1,quote.getType());
        pst.setString(2,quote.getValue().getQuote());
        pst.setInt(3,quote.getValue().getId().intValue());
        pst.execute();
        pst.close();
    }
}
