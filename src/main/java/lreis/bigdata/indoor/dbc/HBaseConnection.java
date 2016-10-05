package lreis.bigdata.indoor.dbc;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.*;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;

import java.io.IOException;

/**
 * Created by dq on 4/29/16.
 */
public class HBaseConnection {


    private static Configuration conf = null;

    static {
        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "192.168.6.131,192.168.6.132,192.168.6.142");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
    }

    public Connection conn = null;

    public HBaseConnection() {
        try {
            conn = HBaseConnection.createConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Connection createConnection() throws IOException {
        return ConnectionFactory.createConnection(conf);
    }

    public Connection getConnection() {
        return this.conn;
    }

    public boolean createTable(HTableDescriptor desc) {

        Admin admin = null;

        try {
            admin = conn.getAdmin();
            if (admin.tableExists(desc.getTableName())) {
                System.out.println("table already exists!");
                admin.close();
            } else {
                admin.createTable(desc);
                admin.close();
                return true;
            }
        } catch (MasterNotRunningException e) {
            e.printStackTrace();
        } catch (ZooKeeperConnectionException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;

    }


    public boolean dropTable(String tableName) {
        Admin admin = null;
        try {
            admin = conn.getAdmin();
            admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(TableName.valueOf((tableName)));
            System.out.println("delete table " + (tableName) + " ok.");
            admin.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void close() throws IOException {
        this.conn.close();
    }



}
