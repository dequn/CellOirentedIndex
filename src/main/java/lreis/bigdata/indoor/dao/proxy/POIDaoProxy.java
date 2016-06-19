package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.impl.POIDaoImpl;
import lreis.bigdata.indoor.dbc.IHBaseConnection;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.vo.POI;
import lreis.bigdata.indoor.vo.TraceNode;

import java.io.IOException;
import java.util.List;

/**
 * Created by dq on 4/29/16.
 */

public class POIDaoProxy implements IPOIDao {

    private IPOIDao dao;

    public POIDaoProxy() throws IOException {
        IHBaseConnection dbc = DbcFactory.getConnection();
        this.dao = new POIDaoImpl(dbc.getConnection());
    }


    public boolean insertPOI(POI poi) throws IOException {
        return this.dao.insertPOI(poi);
    }

    @Override
    public List<TraceNode> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return this.dao.getTraceByMac(mac, beginTimeStamp, endTimeStamp);
    }

}
