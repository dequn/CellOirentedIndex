package lreis.bigdata.indoor.dbc;

import javax.swing.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by zdq on 8/8/16.
 */
public class PhoenixConn implements IConnection {

    private Connection conn = null;


    public PhoenixConn() throws ClassNotFoundException, SQLException {
        Class.forName("org.apache.phoenix.jdbc.PhoenixDriver");
        conn = DriverManager.getConnection("jdbc:phoenix:hadoop-master:2181");
    }

    @Override
    public Connection getConnection()  {
        return this.conn;
    }

    @Override
    public void close() throws IOException, SQLException {
        this.conn.close();
    }
}
