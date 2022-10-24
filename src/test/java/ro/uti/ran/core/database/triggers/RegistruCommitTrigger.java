package ro.uti.ran.core.database.triggers;

import org.h2.tools.TriggerAdapter;

import java.sql.*;

/**
 * Created by miroslav.rusnac on 16/02/2016.
 */
public class RegistruCommitTrigger extends TriggerAdapter {

    public static final String COLUMN_ID = "ID_REGISTRU";
    public static final String COLUMN_LASTMODIFIED = "LAST_MODIFIED_DATE";

    protected String tableName;

    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {
        super.init(conn, schemaName, triggerName, tableName, before, type);
        this.tableName = tableName;

    }


    @Override
    public void fire(Connection conn, ResultSet oldRow, ResultSet newRow) throws SQLException {

        //System.out.println("Trigger ADRR");
        if (newRow == null ) return; // This is a DELETE
        if(oldRow == null){
            return;
            //conn.setAutoCommit(true);

        }

        if (oldRow != null) {
            return;
        }
//
//        PreparedStatement statement = conn.prepareStatement(
//                "commit"
//        );
//
//        System.out.println(statement);
//        statement.execute();
    }
}
