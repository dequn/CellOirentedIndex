package lreis.bigdata.indoor.dbc;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by Q on 2016/6/27.
 */
public class PostgreConn implements IConnection {


    private Connection conn = null;

    private static String DRIVER = "org.postgresql.Driver";
//    private static String HOST = "localhost";
        private static String HOST = "192.168.6.131";
    private static String PORT = "5432";
    private static String DBNAME = "big_data_zdq";
    private static String USERNAME = "postgres";
    private static String PASSWORD = "postgres";

    public PostgreConn() throws ClassNotFoundException, SQLException {
        String url = "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DBNAME;

        Class.forName(DRIVER);// 指定连接类型jdbc:postgresql://
        this.conn = DriverManager.getConnection(url, USERNAME, PASSWORD);

    }

    @Override
    public Connection getConnection() {
        return this.conn;
    }

    @Override
    public void close() throws IOException, SQLException {
        this.conn.close();
    }
}
