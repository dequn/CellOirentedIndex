package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.impl.HBasePOIDaoImpl;
import lreis.bigdata.indoor.dbc.HBaseConnection;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.vo.PositioningPoint;
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


    public boolean insertPOI(PositioningPoint positioningPoint) throws IOException {
        return this.dao.insertPOI(positioningPoint);
    }

    @Override
    public List<TraceNode> getBeenToCellsByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException, ClassNotFoundException {
        return this.dao.getBeenToCellsByMac(mac, beginTimeStamp, endTimeStamp);
    }

    @Override
    public List<PositioningPoint> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws SQLException, IOException {
        return this.dao.getTraceByMac(mac, beginTimeStamp, endTimeStamp);

    }

    @Override
    public void close() throws IOException, SQLException {
        try {
            this.dao.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
