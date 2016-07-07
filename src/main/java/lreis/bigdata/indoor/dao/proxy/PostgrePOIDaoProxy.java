package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.impl.PostgrePOIDaoImpl;
import lreis.bigdata.indoor.dbc.PostgreConn;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.vo.POI;
import lreis.bigdata.indoor.vo.TraceNode;

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
    public boolean insertPOI(POI poi) {
        return this.dao.insertPOI(poi);
    }

    @Override
    public List<TraceNode> getBeenToCellsByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return this.dao.getBeenToCellsByMac(mac, beginTimeStamp, endTimeStamp);
    }

    @Override
    public List<POI> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws SQLException {
        return null;
    }
}
