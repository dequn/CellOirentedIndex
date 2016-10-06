package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dbc.IConnection;
import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.TraceNode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zdq on 8/8/16.
 */
public class PhoenixPOIDaoImpl implements IPOIDao {


    public IConnection conn;


    public PhoenixPOIDaoImpl(IConnection conn) {
        this.conn = conn;
    }

    @Override
    public boolean insertPOI(PositioningPoint positioningPoint) throws IOException {
        if (positioningPoint == null) {
            return false;
        }

        String rowkey =
                PositioningPoint.calRowkey(positioningPoint, PositioningPoint.QueryMethod.STR);
        if (rowkey == null) {
            return false;
        }


        String sql = String.format("UPSERT INTO bigjoy.imos(id,floor,time,mac,x,y,sem_cell,ltime) VALUES ('%s', '%s', '%s','%s', %s ,%s ,'%s',%s)", rowkey,
                positioningPoint.getFloorNum(), new Timestamp(positioningPoint.getTime()), positioningPoint.getMac(), (int) (positioningPoint.getX() * 1000),
                (int) (positioningPoint.getY() * -1000), positioningPoint.getSemanticCellIn().getNodeNum().toString(), positioningPoint.getTime());


        try {
            Statement stmt = this.conn.getConnection().createStatement();
            stmt.executeUpdate(sql);
            this.conn.getConnection().commit();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    @NotNull
    public List<TraceNode> getBeenToCellsByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException, ClassNotFoundException {

        List<TraceNode> res = new ArrayList<>();

        if (mac == null || mac.equals("") || mac.length() != 12) {
            return res;
        }

        if (beginTimeStamp > endTimeStamp) {
            return res;
        }

        String sql = String.format("SELECT mac, sem_cell, ltime, time FROM bigjoy.imos WHERE mac = '%s' AND ltime between %s AND  %s ORDER BY LTIME", mac, beginTimeStamp, endTimeStamp);


        Statement statement = this.conn.getConnection().createStatement();
        ResultSet rs = statement.executeQuery(sql);


        TraceNode last = null;

        while (rs.next()) {

            String cellNum = rs.getString("sem_cell");
            long time = rs.getLong("ltime");


            TraceNode node = new TraceNode();
            node.setPolygonNum(cellNum);


            node.setEntryTime((time));
            node.setExitTime((time));

            if (last == null || !last.getPolygonNum().equals(node.getPolygonNum())) {
                if (last == null) {
                    last = node;
                } else {
                    last.setExitTime((time));
                }
                res.add(node);

            } else {
                last.setExitTime((time));
            }


        }


        return res;

    }

    @Override
    public List<PositioningPoint> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws SQLException, IOException {

        return null;
    }

    @Override
    public void close() throws IOException, SQLException, ClassNotFoundException {
        this.conn.getConnection().commit();
        this.conn.close();
    }
}
