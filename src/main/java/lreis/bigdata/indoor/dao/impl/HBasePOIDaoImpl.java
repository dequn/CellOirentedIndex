package lreis.bigdata.indoor.dao.impl;


import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.vo.Building;
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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by dq on 4/29/16.
 */
public class HBasePOIDaoImpl implements IPOIDao {

    protected final String tableName = "pois";
    protected final String idxTableName = "idx_mac";


    protected Connection conn;

    protected byte[] columnFamily = Bytes.toBytes("data");

//    protected Table poiTable = null;
//    protected Table idxTable = null;

    protected BufferedMutator poiTable = null;
    protected BufferedMutator idxTable = null;


    public HBasePOIDaoImpl(Connection conn) throws IOException {
        super();
        this.conn = conn;

//        poiTable = this.conn.getTable(TableName.valueOf(this.tableName));
//        idxTable = this.conn.getTable(TableName.valueOf(this.idxTableName));

        poiTable = this.conn.getBufferedMutator(TableName.valueOf(this.tableName));
        idxTable = this.conn.getBufferedMutator(TableName.valueOf(this.idxTableName));

//        System.out.println(poiTable.getWriteBufferSize());
//        System.out.println(idxTable.getWriteBufferSize());

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


        byte[] bRow = Bytes.toBytes(rowkey);

        Put put = new Put(bRow);

        put.addColumn(this.columnFamily, Bytes.toBytes("time"), Bytes.toBytes(poi
                .getTime()));
        put.addColumn(this.columnFamily, Bytes.toBytes("mac"), Bytes.toBytes(poi.getMac()));
        put.addColumn(this.columnFamily, Bytes.toBytes("floor"), Bytes.toBytes(poi
                .getFloorNum()));
        put.addColumn(this.columnFamily, Bytes.toBytes("x"), Bytes.toBytes((int) (poi.getX
                () * 1000)));
        put.addColumn(this.columnFamily, Bytes.toBytes("y"), Bytes.toBytes((int) (poi.getY
                () * 1000)));

        try {
            poiTable.mutate(put);
//     poiTable.put(put);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // mac index
        String idxRow = rowkey.substring(14) + rowkey.substring(4, 14) + rowkey.substring(0, 4);
        this.add2Index(idxRow);
        return true;
    }


    @Override
    public List<TraceNode> getBeenToCellsByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {

        if (mac == null || mac.equals("") || mac.length() != 12) {
            return null;
        }
        if (beginTimeStamp > endTimeStamp) {
            return null;
        }

        POI[] trace = (POI[]) this.getTraceByMac(mac, beginTimeStamp,
                endTimeStamp).toArray();

        List<TraceNode> result = new ArrayList<TraceNode>();
        TraceNode last = new TraceNode(trace[0].getCellIn(), trace[0].getTime(),
                trace[0].getTime());
        result.add(last);

        for (int i = 1; i < trace.length; i++) {
            if (trace[i].getCellIn() != last.getCell()) {

                last = new TraceNode(trace[i].getCellIn(), trace[i].getTime(), trace[i]
                        .getTime());
                result.add(last);
            } else {
                last.setExitTime(trace[i].getTime());
            }
        }


        return result;
    }

    @Override
    public List<POI> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws SQLException, IOException {

        if (mac == null || mac.equals("") || mac.length() != 12) {
            return null;
        }
        if (beginTimeStamp > endTimeStamp) {
            return null;
        }

        List<POI> res = new ArrayList<POI>();

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
        for (Iterator<Result> it = resultScanner.iterator(); it.hasNext(); ) {
            Result result = it.next();
            String row = new String(result.getRow());

            long time = Timestamp.valueOf(new String(result.getValue(Bytes
                            .toBytes
                                    ("data"),
                    Bytes
                            .toBytes
                                    ("time")))).getTime();
            int x = Integer.parseInt(new String(result.getValue(Bytes
                            .toBytes
                                    ("data"),
                    Bytes
                            .toBytes
                                    ("x"))));
            int y = Integer.parseInt(new String(result.getValue(Bytes
                            .toBytes
                                    ("data"),
                    Bytes
                            .toBytes
                                    ("y"))));

            String floor = new String(result.getValue(Bytes.toBytes("data"), Bytes
                    .toBytes
                            ("floor")));


            POI poi = new POI(mac, time, (float) x / 1000, (float) y / -1000, floor);
            poi.setCellIn(Building.getInstatnce().getCellByNum(Integer.parseInt(row
                    .substring(0, 4))));

            res.add(poi);

            Collections.sort(res, new POI());

        }


        return res;
    }


    private boolean add2Index(String row) throws IOException {

        Put put = new Put(Bytes.toBytes(row));
        put.addColumn(Bytes.toBytes("data"), Bytes.toBytes(""), Bytes.toBytes(""));
//        idxTable.put(put);
        idxTable.mutate(put);
        return true;


    }

}
