package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.impl.HBasePOIDaoImpl;
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

public class HBasePOIDaoProxy implements IPOIDao {

    private IPOIDao dao;

    public HBasePOIDaoProxy() throws IOException {
        HBaseConnection dbc = DbcFactory.getHBaseConnection();
        this.dao = new HBasePOIDaoImpl(dbc.getConnection());
    }


    public boolean insertPOI(POI poi) throws IOException {
        return this.dao.insertPOI(poi);
    }

    @Override
    public List<TraceNode> getBeenToCellsByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return this.dao.getBeenToCellsByMac(mac, beginTimeStamp, endTimeStamp);
    }

    @Override
    public List<POI> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws SQLException, IOException {
        return this.dao.getTraceByMac(mac, beginTimeStamp, endTimeStamp);

    }

}
