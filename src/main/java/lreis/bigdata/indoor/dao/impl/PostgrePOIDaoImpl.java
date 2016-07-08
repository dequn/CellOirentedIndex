package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dbc.PostgreConn;
import lreis.bigdata.indoor.vo.Building;
import lreis.bigdata.indoor.vo.POI;
import lreis.bigdata.indoor.vo.TraceNode;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Q on 2016/6/29.
 */
public class PostgrePOIDaoImpl implements IPOIDao {

    private PostgreConn pConn = null;

    public PostgrePOIDaoImpl(PostgreConn pConn) {
        this.pConn = pConn;

    }

    @Override
    public boolean insertPOI(POI poi) {

        if (poi == null
                ) {
            return false;
        }

        String rowkey =
                POI.calRowkey(poi);
        if (rowkey == null) {
            return false;
        }
        String sql = String.format("INSERT INTO imo(rowkey,x,y,mac,time,floor) VALUES" +
                        " ('%s',%s,%s,'%s','%s','%s')", rowkey, (int) (poi.getX() * 1000),
                (int) (poi.getY() * -1000), poi.getMac(), new Timestamp(poi.getTime()
                        * 1000),
                poi
                        .getFloorNum());


        try {
            this.pConn.getConnection().prepareStatement(sql).execute();
            return true;

        } catch (SQLException e) {
//            e.printStackTrace();
        }


        return false;
    }

    @Override
    public List<TraceNode> getBeenToCellsByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        if (mac == null || mac.equals("") || mac.length() != 12) {
            return null;
        }


        POI[] trace = this.getTraceByMac(mac, beginTimeStamp, endTimeStamp).toArray(new POI[0]);

        if (trace.length == 0) {
            return null;
        }

        List<TraceNode> result = new ArrayList<TraceNode>();

        result.add(new TraceNode(trace[0].getCellIn(), trace[0].getTime(),
                trace[0].getTime()));

        TraceNode last = result.get(0);
        for (int i = 1; i < trace.length; i++) {
            if (trace[i].getCellIn() != last.getCell()) {
                last.setExitTime(trace[i].getTime());
                last = new TraceNode(trace[i].getCellIn(), trace[i].getTime(),
                        trace[i].getTime());
                result.add(last);
            } else {
                last.setExitTime(trace[i].getTime());
            }
        }

        return result;

    }

    @Override
    public List<POI> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws SQLException {
        if (mac == null || mac.equals("") || mac.length() != 12) {
            return null;
        }

        List<POI> result = new ArrayList<POI>();
        String sql = String.format("SELECT * FROM imo WHERE mac = '%s' AND time " +
                "BETWEEN '%s' AND '%s' ORDER BY time", mac, new Timestamp(beginTimeStamp * 1000), new
                Timestamp(endTimeStamp * 1000));

        Statement stat = this.pConn.getConnection().createStatement();

        ResultSet rs = stat.executeQuery(sql);

        while (rs.next()) {
            POI poi = new POI();

            poi.setX(((float) rs.getInt("x") / 1000));
            poi.setY(((float) rs.getInt("y") / -1000));
            poi.setMac(rs.getString("mac"));
            poi.setFloorNum(rs.getString("floor"));
            poi.setTime(rs.getTimestamp("time").getTime() / 1000);
            poi.setCellIn(Building.getInstatnce().getCellByNum(Integer.parseInt(rs.getString("rowkey").substring
                    (0, 4))));
            result.add(poi);
        }

        return result;
    }

    @Override
    public void close() throws IOException, SQLException {
        this.pConn.close();
    }
}
