package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.TestStatic;
import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.ISemStopsDao;
import lreis.bigdata.indoor.dbc.PhoenixConn;
import lreis.bigdata.indoor.factory.DaoFactory;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.utils.RecordUtils;
import lreis.bigdata.indoor.utils.TraceUtils;
import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.SemStop;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.util.List;

/**
 * Created by zdq on 8/8/16.
 */
public class PhoenixPositioningPointDaoTest {
    @BeforeClass
    public static void beforClass() {

//        TestStatic.readPOIs();
    }


    @Test
    public void InsertPOITest() {
        try {
            IPOIDao dao = DaoFactory.getPhoenixPOIDao();
            PositioningPoint positioningPoint = new PositioningPoint("ACF7F348B123", RecordUtils.calcTimeStamp("2014-04-01 18:56:15"), (float) 58.544, (float) -81.318, "20010");
            boolean result = dao.insertPOI(positioningPoint);
            if (result) {
                System.out.println("Phoenix insert a positioningPoint OK!");
            }
        } catch (ParseException e) {


            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void TestQuery() {

        try {


            IPOIDao dao = DaoFactory.getPhoenixPOIDao();
// FF770C0D0E0F
            List<SemStop> list = dao.getStops("FE3C8C1D9A5C");
            TraceUtils.fixTrace(list);


//            ISemStopsDao stopDao = DaoFactory.getPhoenixSemStopsDao();
//            stopDao.upsert("00037F000000", list);

            System.out.println(list);

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


    @Test
    public void testStops() throws SQLException, IOException, ClassNotFoundException {

        PhoenixConn conn = DbcFactory.getPhoenixConn();

        String sql = "SELECT MAC FROM BIGJOY.MACS WHERE MAC= 'FF770C0D0E0F'";

        Statement stmt = conn.getConnection().createStatement();

        ResultSet rs = stmt.executeQuery(sql);

        IPOIDao dao = DaoFactory.getPhoenixPOIDao();
        ISemStopsDao stopDao = DaoFactory.getPhoenixSemStopsDao();

        while (rs.next()) {
            String mac = rs.getString("mac");
            List<SemStop> stops = dao.getStops(mac);
            TraceUtils.fixTrace(stops);
            stopDao.upsert(mac, stops);
            stopDao.upsertTraj(mac, "2014-04-01", stops);
        }


        conn.close();


    }
}
