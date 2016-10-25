package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dbc.PostgreConn;
import lreis.bigdata.indoor.vo.Building;
import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.SemStop;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
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
    public boolean insertPOI(PositioningPoint positioningPoint) {

        if (positioningPoint == null
                ) {
            return false;
        }

        String rowkey =
                PositioningPoint.calRowkey(positioningPoint);
        if (rowkey == null) {
            return false;
        }
        String sql = String.format("INSERT INTO imo(rowkey,x,y,mac,time,floor) VALUES" +
                        " ('%s',%s,%s,'%s','%s','%s')", rowkey, (int) (positioningPoint.getX() * 1000),
                (int) (positioningPoint.getY() * -1000), positioningPoint.getMac(), new Timestamp(positioningPoint.getTime()),
                positioningPoint
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
    public List<SemStop> getStops(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        if (mac == null || mac.equals("") || mac.length() != 12) {
            return null;
        }


        PositioningPoint[] trace = this.getTraceByMac(mac, beginTimeStamp, endTimeStamp).toArray(new PositioningPoint[0]);

        if (trace.length == 0) {
            return null;
        }

        List<SemStop> result = new ArrayList<SemStop>();

        result.add(new SemStop(trace[0].getSemanticCellIn(), trace[0].getTime(),
                trace[0].getTime()));

        SemStop last = result.get(0);
        for (int i = 1; i < trace.length; i++) {
            if (trace[i].getSemanticCellIn() != last.getSemanticCell()) {
                last.setExitTime(trace[i].getTime());
                last = new SemStop(trace[i].getSemanticCellIn(), trace[i].getTime(),
                        trace[i].getTime());
                result.add(last);
            } else {
                last.setExitTime(trace[i].getTime());
            }
        }

        return result;

    }

    @Override
    public List<SemStop> getStops(String mac) throws SQLException, IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<PositioningPoint> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws SQLException {
        if (mac == null || mac.equals("") || mac.length() != 12) {
            return null;
        }

        List<PositioningPoint> result = new ArrayList<PositioningPoint>();
        String sql = String.format("SELECT * FROM imo WHERE mac = '%s' AND time " +
                "BETWEEN '%s' AND '%s' ORDER BY time", mac, new Timestamp(beginTimeStamp), new
                Timestamp(endTimeStamp));

        Statement stat = this.pConn.getConnection().createStatement();

        ResultSet rs = stat.executeQuery(sql);

        while (rs.next()) {
            PositioningPoint positioningPoint = new PositioningPoint();

            positioningPoint.setX(((float) rs.getInt("x") / 1000));
            positioningPoint.setY(((float) rs.getInt("y") / -1000));
            positioningPoint.setMac(rs.getString("mac"));
            positioningPoint.setFloorNum(rs.getString("floor"));
            positioningPoint.setTime(rs.getTimestamp("time").getTime());
            positioningPoint.setSemanticCellIn(Building.getInstatnce().getSemCellByNum(rs.getString("rowkey").substring
                    (0, 4)));
            result.add(positioningPoint);
        }

        return result;
    }

    @Override
    public void close() throws IOException, SQLException {
        this.pConn.close();
    }
}
