package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.impl.POIDaoImpl;
import lreis.bigdata.indoor.dbc.HBaseConnection;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.vo.POI;
import lreis.bigdata.indoor.vo.TraceNode;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by dq on 4/29/16.
 */

public class POIDaoProxy implements IPOIDao {

    private IPOIDao dao;

    public POIDaoProxy() throws IOException {
        HBaseConnection dbc = DbcFactory.getHBaseConnection();
        this.dao = new POIDaoImpl(dbc.getConnection());
    }


    public boolean insertPOI2HBase(POI poi) throws IOException {
        return this.dao.insertPOI2HBase(poi);
    }

    @Override
    public List<TraceNode> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return this.dao.getTraceByMac(mac, beginTimeStamp, endTimeStamp);
    }

    @Override
    public boolean insertPOI2Postgres(POI poi) throws SQLException, IOException, ClassNotFoundException {
        return this.dao.insertPOI2Postgres(poi);
    }

}
