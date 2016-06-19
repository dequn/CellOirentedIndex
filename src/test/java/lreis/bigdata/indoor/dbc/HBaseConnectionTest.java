package lreis.bigdata.indoor.dbc;

import lreis.bigdata.indoor.factory.DbcFactory;
import org.apache.hadoop.hbase.HColumnDescriptor;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.TableName;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by dq on 5/10/16.
 */
public class HBaseConnectionTest {

    @Test
    public void createTable() throws Exception {

        String tableName = "wifi";

        HTableDescriptor tableDesc = new HTableDescriptor(TableName.valueOf(tableName));

        tableDesc.setValue("prefix_split_policy.prefix_length", "4");
        String[] cfs = {"data"};

        for (int i = 0; i < cfs.length; i++) {
            HColumnDescriptor desc = new HColumnDescriptor(cfs[i]);
            desc.setMaxVersions(3);
            tableDesc.addFamily(desc);
        }

        HBaseConnection conn = (HBaseConnection) DbcFactory.getConnection();

        if (conn.createTable(tableDesc)) {

            System.out.println(String.format("Table %s created Success!", tableDesc.getNameAsString()));
        } else {
            System.out.println(String.format("Table %s created Failed!", tableDesc.getNameAsString()));
        }



    }
}