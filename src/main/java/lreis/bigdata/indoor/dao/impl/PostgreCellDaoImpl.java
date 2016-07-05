package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.dbc.PostgreConn;
import lreis.bigdata.indoor.vo.Cell;
import lreis.bigdata.indoor.vo.POI;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Q on 2016/7/5.
 */
public class PostgreCellDaoImpl implements ICellDao {


    private PostgreConn pConn = null;

    public PostgreCellDaoImpl(PostgreConn pConn) {
        this.pConn = pConn;
    }

    @Override
    public List<POI> getPOIsByCell(Cell cell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {

        if (cell == null || cell.equals("")) {
            return null;
        }
        if (beginTimeStamp > endTimeStamp) {
            return null;
        }


        List<POI> result = new ArrayList<POI>();


        Statement stat = this.pConn.getConnection().createStatement();


        String sql = String.format("SELECT * FROM imo WHERE rowkey LIKE '%04d%%' AND " +
                "time BETWEEN '%s' AND '%s'", cell.getNodeNum(), new Timestamp
                (beginTimeStamp * 1000), new Timestamp(endTimeStamp * 1000));
        ResultSet rs = stat.executeQuery(sql);

        while (rs.next()) {
            POI poi = new POI();

            poi.setX(((float) rs.getInt("x") / 1000));
            poi.setY(((float) rs.getInt("y") / -1000));
            poi.setMac(rs.getString("mac"));
            poi.setFloorNum(rs.getString("floor"));
            poi.setTime(rs.getTimestamp("time").getTime());

            result.add(poi);
        }


        return result;
    }

    @Override
    public int countMacInCell(Cell cell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        List<POI> pois = this.getPOIsByCell(cell, beginTimeStamp, endTimeStamp);
        if (pois == null || pois.size() == 0) {
            return 0;
        }
        HashSet<String> macs = new HashSet<String>();

        for (POI poi : pois) {
            macs.add(poi.getMac());
        }

        return macs.size();

    }

    @Override
    public List<POI> getPOISBeenToAllCells(List<Cell> cells, Long beginTimeStamp, Long endTimeStamp) throws IOException {

        if (cells == null || cells.size() == 0) {
            return null;
        }

        List<POI> result = null;


        return null;
    }
}
