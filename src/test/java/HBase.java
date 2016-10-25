import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by dq on 4/13/16.
 */
public class HBase {
    public static AtomicInteger count = new AtomicInteger();
    private static Configuration conf = null;
    private static Connection conn = null;
    private static Admin admin = null;

    static {

        conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.quorum", "hadoop-master,hadoop-slave1,hadoop-slave2");
        conf.set("hbase.zookeeper.property.clientPort", "2181");
//        conf.set("zookeeper.znode.parent", "/hbase");
    }

    static {
        try {
            conn = ConnectionFactory.createConnection(conf);
            admin = conn.getAdmin();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


//   public static void createTable(String tableName, String[] cfs) {
//
//
//        try {
//            if (admin.tableExists(TableName.valueOf(tableName))) {
//                System.out.println("table already exists!");
//            } else {
//                HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));
////                tableDesc.setValue(HTableDescriptor.SPLIT_POLICY,KeyPrefixRegionSplitPolicy.class.getName());
//                tableDesc.setValue("prefix_split_policy.prefix_length", "4");
//
//                for (int i = 0; i < cfs.length; i++) {
//                    HColumnDescriptor desc = new HColumnDescriptor(cfs[i]);
//                    desc.setMaxVersions(3);
//                    tableDesc.addFamily(desc);
//                }
//
//                admin.createTable(tableDesc);
//                admin.close();
//                System.out.println("create table " + tableName + " ok.");
//            }
//        } catch (MasterNotRunningException e) {
//            e.printStackTrace();
//        } catch (ZooKeeperConnectionException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//
//    public static void dropTable(String tableName) {
//        try {
//            admin.disableTable(TableName.valueOf(tableName));
//            admin.deleteTable(TableName.valueOf((tableName)));
//            System.out.println("delete table " + (tableName) + " ok.");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }


    public static boolean putData(String tableName, String cf, String[] rows, String[] columns, String[][] values) {
        try {
            Table table = conn.getTable(TableName.valueOf(tableName));
//            String[] rows = {"baidu.com_19991011_20151011", "alibaba.com_19990415_20220523"};
//            String[] columns = {"owner", "ipstr", "access_server", "reg_date", "exp_date"};
//            String[][] values = {
//                    {"Beijing Baidu Technology Co.", "220.181.57.217", "北京", "1999年10月11日", "2015年10月11日"},
//                    {"Hangzhou Alibaba Advertising Co.", "205.204.101.42", "杭州", "1999年04月15日", "2022年05月23日"}
//            };

            byte[] family = Bytes.toBytes(cf);
            for (int i = 0; i < rows.length; i++) {
                System.out.println("========================" + rows[i]);
                byte[] rowkey = Bytes.toBytes(rows[i]);
                Put put = new Put(rowkey);
                for (int j = 0; j < columns.length; j++) {
                    byte[] qualifier = Bytes.toBytes(columns[j]);
                    byte[] value = Bytes.toBytes(values[i][j]);
                    put.addColumn(family, qualifier, value);
                }
                table.put(put);
            }
            return true;

        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;

    }

    public static void close() {
        try {
            if (null != admin)
                admin.close();
            if (null != conn)
                conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
