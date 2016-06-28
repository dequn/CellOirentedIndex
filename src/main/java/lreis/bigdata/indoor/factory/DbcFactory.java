package lreis.bigdata.indoor.factory;

import lreis.bigdata.indoor.dbc.HBaseConnection;
import lreis.bigdata.indoor.dbc.PostgreConn;
import org.jetbrains.annotations.Contract;

/**
 * Created by dq on 4/29/16.
 */
public class DbcFactory {
    @Contract(" -> !null")
    public static HBaseConnection getHBaseConnection() {
        return new HBaseConnection();
    }
    @Contract(" -> !null")
    public static PostgreConn getPostgreConn(){return new PostgreConn();}
}
