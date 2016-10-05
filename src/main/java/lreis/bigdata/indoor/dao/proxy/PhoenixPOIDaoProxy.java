package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.impl.PhoenixPOIDaoImpl;
import lreis.bigdata.indoor.dbc.PhoenixConn;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.TraceNode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by zdq on 8/8/16.
 */
public class PhoenixPOIDaoProxy implements IPOIDao{

    private PhoenixPOIDaoImpl dao = null;

    public PhoenixPOIDaoProxy() {
        PhoenixConn pConn = DbcFactory.getPhoenixConn();
        this.dao = new PhoenixPOIDaoImpl(pConn);
    }


    @Override
    public boolean insertPOI(PositioningPoint positioningPoint) throws IOException {
        return this.dao.insertPOI(positioningPoint);
    }

    @Override
    public List<TraceNode> getBeenToCellsByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException, ClassNotFoundException {
        return this.dao.getBeenToCellsByMac(mac, beginTimeStamp, endTimeStamp);
    }

    @Override
    public List<PositioningPoint> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws SQLException, IOException {
        return null;
    }

    @Override
    public void close() throws IOException, SQLException {

    }
}
