package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.impl.PhoenixPOIDaoImpl;
import lreis.bigdata.indoor.dbc.PhoenixConn;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.vo.POI;
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
    public boolean insertPOI(POI poi) throws IOException {
        return this.dao.insertPOI(poi);
    }

    @Override
    public List<TraceNode> getBeenToCellsByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return null;
    }

    @Override
    public List<POI> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws SQLException, IOException {
        return null;
    }

    @Override
    public void close() throws IOException, SQLException {

    }
}
