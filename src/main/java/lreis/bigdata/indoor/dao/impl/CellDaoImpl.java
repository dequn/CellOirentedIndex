package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.utils.RecordUtils;
import lreis.bigdata.indoor.vo.Building;
import lreis.bigdata.indoor.vo.POI;
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
public class CellDaoImpl implements ICellDao {

    private Connection conn;
    private String tableName = "wifi";
    private byte[] columnFamily = Bytes.toBytes("data");

    public CellDaoImpl(Connection conn) {
        super();
        this.conn = conn;
    }

    /**
     * return List of POIs, beginTimeStamp and endTimeStamp are unix TimeStamps without milliseconds.
     *
     * @param cellName
     * @param beginTimeStamp
     * @param endTimeStamp
     * @return List<POI>
     * @throws IOException
     */
    public List<POI> getPOIsByCellName(String cellName, Long beginTimeStamp, Long endTimeStamp) throws IOException {


        List<POI> list = new ArrayList<POI>();

        List<Integer> numList = new ArrayList<Integer>();
//        List<lreis.bigdata.indoor.vo.Cell> cells = Building.getCellsByName(cellName);
        List<lreis.bigdata.indoor.vo.Cell> cells = null;

        for (lreis.bigdata.indoor.vo.Cell cell : cells) {
            numList.add(cell.getNodeNum());
        }

        FilterList fls = new FilterList(FilterList.Operator.MUST_PASS_ONE); // can be in multi polygons

        for (Integer num : numList) {


            FilterList fl = new FilterList(FilterList.Operator.MUST_PASS_ALL);// must between beginTime and endTime

            BinaryPrefixComparator comp = new BinaryPrefixComparator(Bytes.toBytes(String.format("%04d%d", num, beginTimeStamp)));
            RowFilter filter = new RowFilter(CompareFilter.CompareOp.GREATER_OR_EQUAL, comp);

            BinaryPrefixComparator comp2 = new BinaryPrefixComparator(Bytes.toBytes(String.format("%04d%d", num, endTimeStamp)));
            RowFilter filter2 = new RowFilter(CompareFilter.CompareOp.LESS_OR_EQUAL, comp2);

            fl.addFilter(filter);
            fl.addFilter(filter2);

            fls.addFilter(fl);

        }

        Scan scan = new Scan();
        scan.setFilter(fls);

        scan.addFamily(this.columnFamily);
        Table table = this.conn.getTable(TableName.valueOf(this.tableName));

        ResultScanner resultScanner = table.getScanner(scan);

        for (Iterator<Result> it = resultScanner.iterator(); it.hasNext(); ) {
            Result result = it.next();
            List<Cell> hCells = result.listCells();

            POI poi = new POI();

            for (Cell cell : hCells) {

                String qualifier = new String(CellUtil.cloneQualifier(cell));
                String value = new String(CellUtil.cloneValue(cell), "UTF-8");

                if (qualifier.equals("x")) {
                    poi.setX(Float.parseFloat(value) / 1000);
                }
                if (qualifier.equals("y")) {
                    poi.setX(Float.parseFloat(value) / -1000);

                }
                if (qualifier.equals("floor")) {

                    poi.setFloorNum(value);
                }
                if (qualifier.equals("mac")) {

                    poi.setMac(value);
                }
                if (qualifier.equals("time")) {
                    try {
                        long time = RecordUtils.calcTimeStamp(value);
                        poi.setTime(time);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            list.add(poi);
        }


        return list;
    }

    public int countMacInCell(String cellName, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        List<POI> list = this.getPOIsByCellName(cellName, beginTimeStamp, endTimeStamp);
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (POI poi : list) {
            if (map.containsKey(poi.getMac())) {
                map.put(poi.getMac(), map.get(poi.getMac()) + 1);
            } else {
                map.put(poi.getMac(), 1);
            }
        }
        return map.size();

    }

    public List<POI> getPOISBeenToCells(List<Integer> nodeNumList, Long beginTimeStamp, Long endTimeStamp) throws IOException {

        FilterList filter = new FilterList(FilterList.Operator.MUST_PASS_ONE);

        for (Integer num : nodeNumList) {

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

        Set<String> result = map.get(nodeNumList.get(0));

        for (Set<String> s : map.values()) {
            result.retainAll(s);
        }


        return null;
    }

}
