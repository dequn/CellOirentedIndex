package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.impl.PostgrePOIDaoImpl;
import lreis.bigdata.indoor.dbc.PostgreConn;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.SemStop;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Q on 2016/6/29.
 */
public class PostgrePOIDaoProxy implements IPOIDao {


    private PostgrePOIDaoImpl dao = null;

    public PostgrePOIDaoProxy() {
        PostgreConn pConn = DbcFactory.getPostgreConn();
        this.dao = new PostgrePOIDaoImpl(pConn);
    }


    @Override
    public boolean insertPOI(PositioningPoint positioningPoint) {
        return this.dao.insertPOI(positioningPoint);
    }

    @Override
    public List<SemStop> getStops(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return this.dao.getStops(mac, beginTimeStamp, endTimeStamp);
    }

    @Override
    public List<SemStop> getStops(String mac) throws SQLException, IOException, ClassNotFoundException {
        return null;
    }

    @Override
    public List<PositioningPoint> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws SQLException {
        return null;
    }

    @Override
    public void close() throws IOException, SQLException {
        this.dao
                .close();
    }
}
