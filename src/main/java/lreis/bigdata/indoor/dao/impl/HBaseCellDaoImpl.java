package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.utils.RecordUtils;
import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.SemanticCell;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.BinaryPrefixComparator;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.RowFilter;
import org.apache.hadoop.hbase.util.Bytes;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Created by dq on 5/3/16.
 */
public class HBaseCellDaoImpl implements ICellDao {

    private Connection conn;
    private String tableName = "wifi";
    private byte[] columnFamily = Bytes.toBytes("data");

    public HBaseCellDaoImpl(Connection conn) {
        super();
        this.conn = conn;
    }

    /**
     * return List of POIs, beginTimeStamp and endTimeStamp are unix TimeStamps with milliseconds.
     *
     * @param semanticCell
     * @param beginTimeStamp
     * @param endTimeStamp
     * @return List<PositioningPoint>
     * @throws IOException
     */
    public List<PositioningPoint> getPOIsByCell(SemanticCell semanticCell, Long beginTimeStamp, Long endTimeStamp) throws IOException {

        if (semanticCell == null) {
            return null;
        }
        if (beginTimeStamp > endTimeStamp) {
            return null;
        }

        List<PositioningPoint> list = new ArrayList<PositioningPoint>();


        String num = semanticCell.getPolygonNum();


        FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);// must between beginTime and endTime

        BinaryPrefixComparator comp = new BinaryPrefixComparator(Bytes.toBytes(String.format("%04d%d", num, beginTimeStamp)));
        RowFilter filter = new RowFilter(CompareFilter.CompareOp.GREATER_OR_EQUAL, comp);

        BinaryPrefixComparator comp2 = new BinaryPrefixComparator(Bytes.toBytes(String.format("%04d%d", num, endTimeStamp)));
        RowFilter filter2 = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL, comp2);

        fl.addFilter(filter);
        fl.addFilter(filter2);


        Scan scan = new Scan();
        scan.setFilter(fl);

        scan.addFamily(this.columnFamily);
        Table table = this.conn.getTable(TableName.valueOf(this.tableName));

        ResultScanner resultScanner = table.getScanner(scan);

        for (Iterator<Result> it = resultScanner.iterator(); it.hasNext(); ) {
            Result result = it.next();
            List<Cell> hCells = result.listCells();

            PositioningPoint positioningPoint = new PositioningPoint();

            for (Cell c : hCells) {

                String qualifier = new String(CellUtil.cloneQualifier(c));
                String value = new String(CellUtil.cloneValue(c), "UTF-8");

                if (qualifier.equals("x")) {
                    positioningPoint.setX(Float.parseFloat(value) / 1000);
                }
                if (qualifier.equals("y")) {
                    positioningPoint.setX(Float.parseFloat(value) / -1000);

                }
                if (qualifier.equals("floor")) {

                    positioningPoint.setFloorNum(value);
                }
                if (qualifier.equals("mac")) {

                    positioningPoint.setMac(value);
                }
                if (qualifier.equals("time")) {
                    try {
                        long time = RecordUtils.calcTimeStamp(value);
                        positioningPoint.setTime(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            list.add(positioningPoint);
        }


        return list;
    }

    public int countMacInCell(SemanticCell semanticCell, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        List<PositioningPoint> list = this.getPOIsByCell(semanticCell, beginTimeStamp, endTimeStamp);
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (PositioningPoint positioningPoint : list) {
            if (map.containsKey(positioningPoint.getMac())) {
                map.put(positioningPoint.getMac(), map.get(positioningPoint.getMac()) + 1);
            } else {
                map.put(positioningPoint.getMac(), 1);
            }
        }
        return map.size();

    }

    public List<PositioningPoint> getPOISBeenToAllCells(List<SemanticCell> semanticCells, Long beginTimeStamp, Long endTimeStamp) throws IOException {

        FilterList filter = new FilterList(FilterList.Operator.MUST_PASS_ONE);

        for (SemanticCell semanticCell : semanticCells) {
            String num = semanticCell.getPolygonNum();
            FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);

            BinaryPrefixComparator comp = new BinaryPrefixComparator(Bytes.toBytes(String.format("%04d%d", num, beginTimeStamp)));
            RowFilter rowFilter = new RowFilter(CompareFilter.CompareOp.GREATER_OR_EQUAL, comp);
            fl.addFilter(rowFilter);

            comp = new BinaryPrefixComparator(Bytes.toBytes(String.format("%04d%d", num, beginTimeStamp)));
            rowFilter = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL, comp);
            fl.addFilter(rowFilter);

            filter.addFilter(fl);
        }


        Scan scan = new Scan();
        scan.setFilter(filter);
        scan.addFamily(this.columnFamily);
        Table table = this.conn.getTable(TableName.valueOf(this.tableName));

        ResultScanner resultScanner = table.getScanner(scan);

        Map<Integer, Set<String>> map = new HashMap<Integer, Set<String>>();

        for (Iterator<Result> it = resultScanner.iterator(); it.hasNext(); ) {

            Result result = it.next();
            String row = new String(result.getRow());
            String mac = row.substring(14);
            Integer node = Integer.parseInt(row.substring(0, 4));
            if (map.get(node) == null) {
                map.put(node, new HashSet<String>());
            }
            map.get(node).add(mac);
        }

        Set<String> result = map.get(semanticCells.get(0));

        for (Set<String> s : map.values()) {
            result.retainAll(s);
        }


        return null;
    }

}
