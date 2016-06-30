package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dbc.PostgreConn;
import lreis.bigdata.indoor.vo.POI;
import lreis.bigdata.indoor.vo.TraceNode;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        String sql = String.format("INSERT INTO imo(rowkey,x,y,mac,time,floor) VALUES" +
                        " ('%s',%s,%s,'%s','%s','%s')", POI.calRowkey(poi), (int) (poi.getX() * 1000),
                (int) (poi.getY() * -1000), poi.getMac(), new Timestamp(poi.getTime()
                        * 1000),
                poi
                        .getFloorNum());


        try {
            this.pConn.getConnection().prepareStatement(sql).execute();        return true;

        } catch (SQLException e) {
            e.printStackTrace();
        }



        return false;
    }

    @Override
    public List<TraceNode> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return null;
    }
}
