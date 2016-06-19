package lreis.bigdata.indoor.factory;

import lreis.bigdata.indoor.dbc.HBaseConnection;
import lreis.bigdata.indoor.dbc.IHBaseConnection;

/**
 * Created by dq on 4/29/16.
 */
public class DbcFactory {
    public static IHBaseConnection getConnection() {
        return new HBaseConnection();
    }
}
