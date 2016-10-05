package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.factory.DaoFactory;
import lreis.bigdata.indoor.utils.RecordUtils;
import lreis.bigdata.indoor.utils.TraceUtils;
import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.TraceNode;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

/**
 * Created by zdq on 8/8/16.
 */
public class PhoenixPositioningPointDaoTest {
    @BeforeClass
    public static void beforClass() {

//        TestStatic.BuildingInit();
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

            List<TraceNode> list = dao.getBeenToCellsByMac("000003FD2D06", RecordUtils.calcTimeStamp("2014-04-01 08:00:00"), RecordUtils.calcTimeStamp("2014-04-01 20:00:00"));

            TraceUtils.fixTrace(list);

            System.out.println(list);


        } catch (Exception e) {
            e.printStackTrace();

        }


    }
}
