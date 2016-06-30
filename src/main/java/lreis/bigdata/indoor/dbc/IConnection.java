package lreis.bigdata.indoor.dbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by Q on 2016/6/27.
 */
public interface IConnection {

    Connection getConnection() ;

    void close() throws IOException, SQLException;
}
