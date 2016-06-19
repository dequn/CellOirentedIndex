package lreis.bigdata.indoor.dao;

import lreis.bigdata.indoor.vo.POI;
import lreis.bigdata.indoor.vo.TraceNode;

import java.io.IOException;
import java.util.List;

/**
 * Created by dq on 4/29/16.
 */
public interface IPOIDao {

    boolean insertPOI(POI poi) throws IOException;

    /**
     * return a list of shops the mac has been , maybe and allow return to a shop on second time ,so use a list.
     *
     * @param mac
     * @param beginTimeStamp
     * @param endTimeStamp
     * @return
     * @throws IOException
     */
    public List<TraceNode> getTraceByMac(String mac, Long beginTimeStamp, Long endTimeStamp) throws IOException;


}
