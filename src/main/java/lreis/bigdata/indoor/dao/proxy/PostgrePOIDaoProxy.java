package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.impl.PostgrePOIDaoImpl;
import lreis.bigdata.indoor.dbc.PostgreConn;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.vo.POI;
import lreis.bigdata.indoor.vo.TraceNode;

import java.io.IOException;
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
    public List<TraceNode> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return this.dao.getTraceByMac(mac, beginTimeStamp, endTimeStamp);
    }
}
