package ro.uti.ran.core.database;

import org.h2.api.Trigger;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by Anastasia cea micuta on 2/11/2016.
 */
public class H2Trigger implements Trigger {

    String schemaName;
    String triggerName;
    String tableName;
    boolean before;
    int type;

    @Override
    public void init(Connection conn, String schemaName,
                     String triggerName, String tableName, boolean before, int type)
            throws SQLException {
        this.schemaName = schemaName;
        this.triggerName = triggerName;
        this.tableName = tableName;
        this.before = before;
        this.type = type;
    }

    @Override
    public void fire(Connection conn, Object[] oldRow, Object[] newRow)
            throws SQLException {
        BigDecimal diff = null;
        /*if (newRow != null) {
            diff = (BigDecimal) newRow[1];
        }
        if (oldRow != null) {
            BigDecimal m = (BigDecimal) oldRow[1];
            diff = diff == null ? m.negate() : diff.subtract(m);
        }
        PreparedStatement prep = conn.prepareStatement(
                "UPDATE INVOICE_SUM SET AMOUNT=AMOUNT+?");
        prep.setBigDecimal(1, diff);
        prep.execute();*/
    }


    @Override
    public void close() throws SQLException {
    }

    @Override
    public void remove() throws SQLException {
    }
}