package lreis.bigdata.indoor.index.coprocessor.server;

import com.google.protobuf.RpcCallback;
import com.google.protobuf.RpcController;
import com.google.protobuf.Service;
import lreis.bigdata.indoor.index.coprocessor.BuildMacIndex;
import org.apache.hadoop.hbase.Coprocessor;
import org.apache.hadoop.hbase.CoprocessorEnvironment;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.coprocessor.CoprocessorService;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by dq on 5/11/16.
 */
public class BuildMacIndexEndpoint extends BuildMacIndex.BuildMacIndexService implements Coprocessor, CoprocessorService {

    private Table tableWifi = null;
    private Table indexMac = null;

    public void buildMacIndex(RpcController controller, BuildMacIndex.buildMacIndexRequest request, RpcCallback<BuildMacIndex.buildMacIndexResponse> done) {

        BuildMacIndex.buildMacIndexResponse.Builder resBuilder = BuildMacIndex.buildMacIndexResponse.newBuilder();

        Scan scan = new Scan();
        try {
            ResultScanner resultScanner = this.tableWifi.getScanner(scan);

            for (Iterator<Result> it = resultScanner.iterator(); it.hasNext(); ) {

                Result result = it.next();

                String row = new String(result.getRow());
                String newRow = row.substring(13) + row.substring(4, 13) + row.substring(0, 4);

                Put put = new Put(Bytes.toBytes(newRow));
                this.indexMac.put(put);

            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        resBuilder.setRowCount(100L);

        done.run(resBuilder.build());

    }

    public void start(CoprocessorEnvironment coprocessorEnvironment) throws IOException {
        this.tableWifi = coprocessorEnvironment.getTable(TableName.valueOf("wifi"));
        this.indexMac = coprocessorEnvironment.getTable(TableName.valueOf("idx_mac"));
    }

    public void stop(CoprocessorEnvironment coprocessorEnvironment) throws IOException {

    }

    public Service getService() {
        return this;
    }
}
