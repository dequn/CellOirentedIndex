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
public class HBaseConnection implements IHBaseConnection {

    private static Configuration conf = null;
    private Connection conn = null;

    static {

        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "hadoop-master,hadoop-slave1,hadoop-slave2");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
    }


    public Connection getConnection() throws IOException {
        this.conn = ConnectionFactory.createConnection(conf);
        return this.conn;
    }

    public void close() throws IOException {
        this.conn.close();
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

        return  false;

    }


    public void dropTable(String tableName) {
        Admin admin = null;
        try {
            admin = conn.getAdmin();
            admin.disableTable(TableName.valueOf(tableName));
            admin.deleteTable(TableName.valueOf((tableName)));
            System.out.println("delete table " + (tableName) + " ok.");
            admin.close();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }


//    protected void finalize() {
//        if (this.conn != null) {
//            try {
//                this.close() ;
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//    }


}
