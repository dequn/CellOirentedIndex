package lreis.bigdata.indoor.index;

import lreis.bigdata.indoor.dbc.IHBaseConnection;
import lreis.bigdata.indoor.factory.DbcFactory;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;

import java.io.IOException;

/**
 * Created by dq on 5/10/16.
 */
public class SecondaryIndex {


    public static void buildMacIdxTable() {

        String tableName = "idx_mac";

        HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));

        tableDesc.addFamily(new HColumnDescriptor("data"));

        IHBaseConnection conn = DbcFactory.getConnection();

        try {
            conn.getConnection();
            if (conn.createTable(tableDesc)) {
                System.out.println(String.format("Table %s created Success!", tableName));
            } else {
                System.out.println(String.format("Table %s created Failed!", tableName));
            }
            conn.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    public boolean buildMacIndex(){

        return false;

    }




}
