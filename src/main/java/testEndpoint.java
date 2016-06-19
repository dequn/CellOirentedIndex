import lreis.bigdata.indoor.dbc.HBaseConnection;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.index.coprocessor.BuildMacIndex;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.coprocessor.Batch;
import org.apache.hadoop.hbase.ipc.BlockingRpcCallback;
import org.apache.hadoop.hbase.ipc.ServerRpcController;

import java.io.IOException;
import java.util.Map;


/**
 * Created by dq on 5/11/16.
 */
public class testEndpoint {
    public static void main(String[] args) throws Throwable {

        Connection conn;
        try {

            conn = DbcFactory.getConnection().getConnection();
            Admin admin = conn.getAdmin();
            Table table = conn.getTable(TableName.valueOf("idx_mac"));
            final BuildMacIndex.buildMacIndexRequest req = BuildMacIndex.buildMacIndexRequest.newBuilder().build();

            Map<byte[], Long> tmpRet = table.coprocessorService(BuildMacIndex.BuildMacIndexService.class, null, null, new Batch.Call<BuildMacIndex.BuildMacIndexService, Long>() {
                @Override
                public Long call(BuildMacIndex.BuildMacIndexService instance) throws IOException {

                    ServerRpcController controller = new ServerRpcController();
                    BlockingRpcCallback<BuildMacIndex.buildMacIndexResponse> rpc = new BlockingRpcCallback<BuildMacIndex.buildMacIndexResponse>();
                    instance.buildMacIndex(controller, req, rpc);

                    BuildMacIndex.buildMacIndexResponse resp = rpc.get();
                    return resp.getRowCount();
                }
            });
            long ret = 0;
            for (long l : tmpRet.values())
                ret += l;
            System.out.println("lines: " + ret);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
