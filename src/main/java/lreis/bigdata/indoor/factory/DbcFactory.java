package lreis.bigdata.indoor.factory;

import lreis.bigdata.indoor.dbc.HBaseConnection;
import lreis.bigdata.indoor.dbc.PhoenixConn;
import lreis.bigdata.indoor.dbc.PostgreConn;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.sql.SQLException;

/**
 * Created by dq on 4/29/16.
 */
public class DbcFactory {

    @Contract(" -> !null")
    public static HBaseConnection getHBaseConnection() {
        return new HBaseConnection();
    }

    @Nullable
    @Contract(" -> !null")
    public static PostgreConn getPostgreConn() {
        try {
            return new PostgreConn();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Nullable
    public static PhoenixConn getPhoenixConn() {
        try {
            return new PhoenixConn();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
