package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.ISemStopsDao;
import lreis.bigdata.indoor.dao.impl.PhoenixSemStopsDaoImpl;
import lreis.bigdata.indoor.dbc.PhoenixConn;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.vo.SemStop;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by dq on 10/8/16.
 */
public class PhoenixSemStopsDaoProxy implements ISemStopsDao {


    private PhoenixSemStopsDaoImpl dao = null;

    public PhoenixSemStopsDaoProxy() {
        PhoenixConn pConn = DbcFactory.getPhoenixConn();
        this.dao = new PhoenixSemStopsDaoImpl(pConn);
    }


    @Override
    public void upsert(String mac, List<SemStop> stops) throws SQLException, ClassNotFoundException {
        this.dao.upsert(mac, stops);

    }

    @Override
    public void upsertTraj(String mac, String date, List<SemStop> stops) throws SQLException, ClassNotFoundException {
        this.dao
                .upsertTraj(mac, date, stops);
    }
}
