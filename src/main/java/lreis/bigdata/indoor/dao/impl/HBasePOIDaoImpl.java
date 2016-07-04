package lreis.bigdata.indoor.dao.impl;


import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dbc.PostgreConn;
import lreis.bigdata.indoor.vo.POI;
import lreis.bigdata.indoor.vo.TraceNode;
import java.sql.Timestamp;
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
public class HBasePOIDaoImpl implements IPOIDao {

    protected final String tableName = "pois";
    protected String idxTableName = "idx_mac";



    protected Connection conn;

    protected byte[] columnFamily = Bytes.toBytes("data");

    protected  Table poiTable = null;
    protected Table idxTable = null;


    public HBasePOIDaoImpl(Connection conn) throws IOException {
        super();
        this.conn = conn;

            poiTable = this.conn.getTable(TableName.valueOf(this.tableName));
            idxTable = this.conn.getTable(TableName.valueOf(this.idxTableName));

    }

    public boolean insertPOI(POI poi) throws IOException {

        if (poi == null
                ) {
            return false;
        }

        String rowkey =
                POI.calRowkey(poi);
        if (rowkey == null) {
            return false;
        }

        Table table = null;



        byte[] bRow = Bytes.toBytes(rowkey);

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

        try {
            poiTable.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // mac index
        String idxRow = rowkey.substring(14) + rowkey.substring(4, 14) + rowkey.substring(0, 4);
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


    private boolean add2Index(String row) throws IOException {

        Put put = new Put(Bytes.toBytes(row));
        put.addColumn(Bytes.toBytes("data"), Bytes.toBytes(""), Bytes.toBytes(""));
        idxTable.put(put);
        return true;

    }

}
