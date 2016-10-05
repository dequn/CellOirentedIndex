package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.TestStatic;
import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.factory.DaoFactory;
import lreis.bigdata.indoor.utils.RecordUtils;
import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.TraceNode;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.List;


/**
 * Created by dq on 5/13/16.
 */
public class PositioningPointDaoImplTest {


    @BeforeClass
    public static void beforClass() {

        TestStatic.BuildingInit();
//        TestStatic.readPOIs();
    }


    @Test
    public void insertPOI() throws Exception {


        IPOIDao dao = DaoFactory.getHBasePOIDao();
        PositioningPoint positioningPoint = new PositioningPoint("ACF7F348B123", RecordUtils.calcTimeStamp("2014-04-01 18:56:15"), (float) 58.544, (float) -81.318, "20010");
        dao.insertPOI(positioningPoint);
        dao.close();


    }


    @Ignore
    @Test
    public void postgreSQLInsertPOI() {
        IPOIDao dao = DaoFactory.getPostgrePOIDao();
        try {
            PositioningPoint positioningPoint = new PositioningPoint("ACF7F348B123", RecordUtils.calcTimeStamp("2014-04-01 18:56:15"), (float) 58.544, (float) -81.318, "20010");

            dao.insertPOI(positioningPoint);
            dao.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
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
    public void postgreSQLGetBeenToCellsTest() {

        IPOIDao dao = DaoFactory.getPostgrePOIDao();

        try {
            long bt = System.currentTimeMillis();
            List<TraceNode> list = dao.getBeenToCellsByMac("C86F1D788244", 1396310400L, 1396346400L);
            long et = System.currentTimeMillis();
            System.out.println(et - bt);


            FileWriter writer = new FileWriter("/home/zdq/big_joy/semantics_trace.example.txt");
            for (
                    TraceNode node : list
                    ) {
                writer.write(String.format("%s,%s,%s,%d\n", node.getSemanticCell().getName(), new Timestamp(node.getEntryTime()).toString(), new Timestamp(node.getExitTime()).toString(), node.getExitTime() - node.getEntryTime()));

                System.out.println(node.getSemanticCell().getName() + "," + (node.getExitTime() - node.getEntryTime()));
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}