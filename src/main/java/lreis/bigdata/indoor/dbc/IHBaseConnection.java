package lreis.bigdata.indoor.dbc;

import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.client.Connection;

import java.io.IOException;

/**
 * Created by dq on 4/29/16.
 */
public interface IHBaseConnection {

    Connection getConnection() throws IOException;
    boolean createTable(HTableDescriptor desc);
    void close() throws IOException;

}

