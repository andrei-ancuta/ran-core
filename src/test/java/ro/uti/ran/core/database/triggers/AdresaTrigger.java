package ro.uti.ran.core.database.triggers;

import org.h2.tools.TriggerAdapter;

import java.sql.*;

/**
 * Created by miroslav.rusnac on 16/02/2016.
 */
public class AdresaTrigger extends TriggerAdapter {

    public static String COLUMN_ID = "ID_";
    public static final String COLUMN_LASTMODIFIED = "LAST_MODIFIED_DATE";

    protected String tableName;

    @Override
    public void init(Connection conn, String schemaName, String triggerName, String tableName, boolean before, int type) throws SQLException {
        super.init(conn, schemaName, triggerName, tableName, before, type);
        this.tableName = tableName;
        COLUMN_ID = COLUMN_ID+tableName;
    }


    @Override
    public void fire(Connection conn, ResultSet oldRow, ResultSet newRow) throws SQLException {

        //System.out.println("Trigger ADRR");
        if (newRow == null ) return; // This is a DELETE
        if(oldRow == null){
            //this is an insert
            java.util.Date date= new java.util.Date();
            Timestamp newTimestamp = new Timestamp(date.getTime());
            System.out.println("Inserted: "+newRow.getLong(COLUMN_ID));

        }

        if (oldRow != null) {
            // This is an UPDATE
            // Yes, our UPDATE statement below will fire this trigger again, filter that!
            // TODO: Maybe on day H2 will have a better trigger API, http://groups.google.com/group/h2-database/browse_thread/thread/f8cdaa1ae0da7448
            Timestamp oldTimestamp = oldRow.getTimestamp(COLUMN_LASTMODIFIED);
            Timestamp newTimestamp = newRow.getTimestamp(COLUMN_LASTMODIFIED);
            if (oldTimestamp == null)
                return;
            if (oldTimestamp.getTime() != newTimestamp.getTime())
                return;
        }

        PreparedStatement statement = conn.prepareStatement(
                "update " + tableName +
                        " set " + COLUMN_LASTMODIFIED + " = current_timestamp()" +
                        " where " + COLUMN_ID + " = ?"
        );
        Long id = newRow.getLong(COLUMN_ID);
        statement.setLong(1, id);
        System.out.println(statement);
        statement.execute();
    }
}
