package lreis.bigdata.indoor.dao;

import lreis.bigdata.indoor.vo.POI;

import java.io.IOException;
import java.util.List;

/**
 * Created by dq on 5/3/16.
 */
public interface ICellDao {

     List<POI> getPOIsByCellName(String cellName, Long beginTimeStamp, Long endTimeStamp) throws IOException;

     int countMacInCell(String cellName, Long beginTimeStamp, Long endTimeStamp) throws IOException;

     List<POI> getPOISBeenToCells(List<Integer> nodeNumList, Long beginTimeStamp, Long endTimeStamp) throws IOException;
}
