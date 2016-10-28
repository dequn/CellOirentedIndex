package lreis.bigdata.indoor.dbc;

import lreis.bigdata.indoor.factory.DbcFactory;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by zdq on 8/8/16.
 */
public class PhoenixConnectionTest {

    @Test
    public void GetConnectionTest() {

        IConnection conn = DbcFactory.getPhoenixConn();
        if (conn != null) {
            System.out.print("get conn ok");
            try {
                conn.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
