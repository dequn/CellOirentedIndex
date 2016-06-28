package lreis.bigdata.indoor.dao.impl;


import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dbc.PostgreConn;
import lreis.bigdata.indoor.vo.POI;
import lreis.bigdata.indoor.vo.TraceNode;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dq on 4/29/16.
 */
public class POIDaoImpl implements IPOIDao {


    private PostgreConn pConn;

    protected Connection conn;
    protected String tableName = "wifi";
    protected String idxTableName = "idx_mac";
    protected byte[] columnFamily = Bytes.toBytes("data");


    public POIDaoImpl(Connection conn) {
        super();
        this.conn = conn;
    }

    public POIDaoImpl(PostgreConn conn) {
        this.pConn = conn;
    }

    public boolean insertPOI2HBase(POI poi) throws IOException {

        if (poi.getX() == null || poi.getY() == null) {
            return false;
        }
        if (poi.getMac() == null || poi.getMac().equals("")) {
            return false;
        }
        if (poi.getTime() == null) {
            return false;
        }

        Table table = this.conn.getTable(TableName.valueOf(this.tableName));


        String row = POI.calRowkey(poi);
        byte[] bRow = Bytes.toBytes(row);

        Put put = new Put(bRow);

        put.addColumn(this.columnFamily, Bytes.toBytes("time"), Bytes.toBytes(poi
                .getTime()));
        put.addColumn(this.columnFamily, Bytes.toBytes("mac"), Bytes.toBytes(poi.getMac()));
        put.addColumn(this.columnFamily, Bytes.toBytes("floor"), Bytes.toBytes(poi
                .getFloorNum()));
        put.addColumn(this.columnFamily, Bytes.toBytes("x"),Bytes.toBytes((int)(poi.getX
                () * 1000)));
        put.addColumn(this.columnFamily, Bytes.toBytes("y"),Bytes.toBytes((int)(poi.getY
                () * 1000)));

        table.put(put);


        // mac index
        String idxRow = row.substring(14) + row.substring(4, 14) + row.substring(0, 4);
        this.add2Index(idxRow);
        return true;
    }


    @Override
    public List<TraceNode> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        List<TraceNode> list = new ArrayList<TraceNode>();

        FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);// must between beginTime and endTime

        BinaryPrefixComparator comp = new BinaryPrefixComparator(Bytes.toBytes(String.format("%s%d", mac, beginTimeStamp)));
        RowFilter filter = new RowFilter(CompareFilter.CompareOp.GREATER_OR_EQUAL, comp);

        BinaryPrefixComparator comp2 = new BinaryPrefixComparator(Bytes.toBytes(String.format("%s%d", mac, endTimeStamp)));
        RowFilter filter2 = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL, comp2);

        fl.addFilter(filter);
        fl.addFilter(filter2);


        Scan scan = new Scan();
        scan.setFilter(fl);

        scan.addFamily(this.columnFamily);
        Table table = this.conn.getTable(TableName.valueOf(this.idxTableName));

        ResultScanner resultScanner = table.getScanner(scan);

        int last = 0;
        Long lastTime = 0L;
        for (Iterator<Result> it = resultScanner.iterator(); it.hasNext(); ) {
            Result result = it.next();
            String row = new String(result.getRow());
            Integer node = Integer.parseInt(row.substring(22));
            Long time = Long.parseLong(row.substring(12, 22));


            if (last != node) {

                if (list.size() > 0) {
                    list.get(list.size() - 1).setExitTime(lastTime);
                }

                TraceNode tn = new TraceNode();
                tn.setPolygonNum(node);
                tn.setEntryTime(time);
                list.add(tn);
                last = node;
                lastTime = time;
            } else {
                lastTime = time;
            }
        }


        return list;
    }

    @Override
    public boolean insertPOI2Postgres(POI poi) throws SQLException, IOException, ClassNotFoundException {


        String sql = String.format("INSERT INTO imo(rowkey,x,y,mac,time,floor) VALUES" +
                " (%s,%s,%s,%s,%s,%s)", POI.calRowkey(poi), (int) (poi.getX() * 1000),
                (int) (poi.getY() * 1000), poi.getMac(), poi.getTime(), poi.getFloorNum());


        return false;
    }

    private boolean add2Index(String row) throws IOException {

        Table idxTable = this.conn.getTable(TableName.valueOf(this.idxTableName));
        Put put = new Put(Bytes.toBytes(row));
        put.addColumn(Bytes.toBytes("data"), Bytes.toBytes(""), Bytes.toBytes(""));
        idxTable.put(put);
        return true;

    }

}
