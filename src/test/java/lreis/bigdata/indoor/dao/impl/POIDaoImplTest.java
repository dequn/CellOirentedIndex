package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.TestStatic;
import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.factory.DaoFactory;
import lreis.bigdata.indoor.utils.RecordUtils;
import lreis.bigdata.indoor.vo.POI;
import lreis.bigdata.indoor.vo.TraceNode;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;


/**
 * Created by dq on 5/13/16.
 */
public class POIDaoImplTest {


    @BeforeClass
    public static void beforClass() {

        TestStatic.BuildingInit();
//        TestStatic.readPOIs();
    }


    @Ignore
    @Test
    public void insertPOI() throws Exception {


        IPOIDao dao = DaoFactory.getHBasePOIDao();
        POI poi = new POI("ACF7F348B123", RecordUtils.calcTimeStamp("2014-04-01 18:56:15"), (float) 58.544, (float) -81.318, "20010");
        dao.insertPOI(poi);
        dao.close();


    }


    @Ignore
    @Test
    public void postgreSQLInsertPOI() {
        IPOIDao dao = DaoFactory.getPostgrePOIDao();
        try {
            POI poi = new POI("ACF7F348B123", RecordUtils.calcTimeStamp("2014-04-01 18:56:15"), (float) 58.544, (float) -81.318, "20010");

            dao.insertPOI(poi);
            dao.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Ignore
    @Test
    public void getTraceByMac() throws Exception {

//        94DBC9B0F387
//        00037F000000
        IPOIDao dao = DaoFactory.getHBasePOIDao();
        long bt = System.currentTimeMillis();
        List<TraceNode> list = dao.getBeenToCellsByMac("4860BC7068D3", 1396310400L, 1396335600L);

        long et = System.currentTimeMillis();
        System.out.println(et - bt);

        for (TraceNode node : list) {
            System.out.println(node.getPolygonNum() + node.getEntryTime() + node.getExitTime());
        }

    }
    //C86F1D78824413963455500204


    @Test
    public void postgreSQLGetBeenToCellsTest(){

        IPOIDao dao = DaoFactory.getPostgrePOIDao();

        try {
            long bt = System.currentTimeMillis();
            List<TraceNode> list = dao.getBeenToCellsByMac("C86F1D788244", 1396310400L,1396346400L);
            long et = System.currentTimeMillis();
            System.out.println(et - bt);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}