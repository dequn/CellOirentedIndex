package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.factory.DaoFactory;
import lreis.bigdata.indoor.index.FloorSTRIndex;
import lreis.bigdata.indoor.vo.TraceNode;
import org.apache.hadoop.yarn.webapp.hamlet.HamletSpec;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by dq on 5/13/16.
 */
public class POIDaoImplTest {

    @Test
    public void insertPOI() throws Exception {

    }

    @Test
    public void getTraceByMac() throws Exception {

        IPOIDao dao = DaoFactory.getPOIDao();
//        94DBC9B0F387
//        00037F000000
        List<TraceNode> list = dao.getTraceByMac("94DBC9B0F387", 1396322620L, 1396322720L);
        for (TraceNode node : list) {
            System.out.println(FloorSTRIndex.nodeNum2Name(node.getPolygonNum()));
        }

    }
}