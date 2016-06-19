package lreis.bigdata.indoor.dao.impl;


import lreis.bigdata.indoor.dao.IPOIDao;
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by dq on 4/29/16.
 */
public class POIDaoImpl implements IPOIDao {

    protected Connection conn;
    protected String tableName = "wifi";
    protected String idxTableName = "idx_mac";
    protected byte[] columnFamily = Bytes.toBytes("data");


    public POIDaoImpl(Connection conn) {
        super();
        this.conn = conn;
    }


    public boolean insertPOI(POI poi) throws IOException {

        if ((poi.getStrX() == null || poi.getStrX().equals("")) || (poi.getStrY() == null || poi.getStrY().equals("")))
            return false;
        if (poi.getMac() == null || poi.getMac().equals("")) {
            return false;
        }
        if (poi.getStrTime() == null || poi.getStrTime().equals("")) {
            return false;
        }
        if (poi.getFloorNum().equals("-1")) {
            return false;
        }

        Table table = this.conn.getTable(TableName.valueOf(this.tableName));

        String row = poi.getRow();
        byte[] bRow = Bytes.toBytes(poi.getRow());

        Put put = new Put(bRow);
        put.addColumn(this.columnFamily, Bytes.toBytes("time"), Bytes.toBytes(poi.getStrTime()));
        put.addColumn(this.columnFamily, Bytes.toBytes("mac"), Bytes.toBytes(poi.getMac()));
        put.addColumn(this.columnFamily, Bytes.toBytes("floor"), Bytes.toBytes(poi.getOriginalFloorNum()));
        put.addColumn(this.columnFamily, Bytes.toBytes("x"), Bytes.toBytes(poi.getStrX()));
        put.addColumn(this.columnFamily, Bytes.toBytes("y"), Bytes.toBytes(poi.getStrY()));

        table.put(put);


        String idxRow = row.substring(14) + row.substring(4, 14) + row.substring(0, 4);
        this.add2Index(idxRow);

        return true;
    }

    //TODO: need do on server side
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
                    list.get(list.size() - 1).setOutTime(lastTime);
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

        Table idxTable = this.conn.getTable(TableName.valueOf(this.idxTableName));
        Put put = new Put(Bytes.toBytes(row));
        put.addColumn(Bytes.toBytes("data"), Bytes.toBytes(""), Bytes.toBytes(""));
        idxTable.put(put);
        return true;

    }

}
