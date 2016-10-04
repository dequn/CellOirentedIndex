package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dbc.IConnection;
import lreis.bigdata.indoor.vo.POI;
import lreis.bigdata.indoor.vo.TraceNode;
import org.apache.commons.net.ntp.TimeStamp;

import java.io.IOException;
import java.sql.*;
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
    public boolean insertPOI(POI poi) throws IOException {
        if (poi == null) {
            return false;
        }

        String rowkey =
                POI.calRowkey(poi, POI.QueryMethod.STR);
        if (rowkey == null) {
            return false;
        }


        String sql = String.format("UPSERT INTO bigjoy.imos(id,floor,time,mac,x,y,cell) VALUES ('%s', '%s', '%s','%s', %s ,%s ,'%s')", rowkey,
                poi.getFloorNum(), new Timestamp(poi.getTime() * 1000), poi.getMac(), (int) (poi.getX() * 1000),
                (int) (poi.getY() * -1000), poi.getCellIn().getNodeNum().toString());


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
    public List<TraceNode> getBeenToCellsByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {


        List<TraceNode> res = new ArrayList<TraceNode>();

        if (mac == null || mac.equals("")) {
            return res;
        }

        if (beginTimeStamp > endTimeStamp) {
            return res;
        }

        String sql = String.format("SELECT mac, cell, time FROM bigjoy.imos WHERE mac = '%S' ADN time BETWEEN to_timestamp('%s') AND to_timestamp('%s') ORDER BY TIME", mac, new Timestamp(beginTimeStamp * 1000), new TimeStamp(endTimeStamp * 1000));
        try {
            PreparedStatement pstmt = this.conn.getConnection().prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public List<POI> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws SQLException, IOException {
        return null;
    }

    @Override
    public void close() throws IOException, SQLException, ClassNotFoundException {
        this.conn.getConnection().commit();
        this.conn.close();
    }
}
