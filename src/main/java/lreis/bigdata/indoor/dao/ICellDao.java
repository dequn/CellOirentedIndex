package lreis.bigdata.indoor.dao;

import lreis.bigdata.indoor.vo.Cell;
import lreis.bigdata.indoor.vo.POI;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by dq on 5/3/16.
 */
public interface ICellDao {

    List<POI> getPOIsByCell(Cell cell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException;

    int countMacInCell(Cell cell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException;

    List<POI> getPOISBeenToAllCells(List<Cell> cells, Long beginTimeStamp, Long endTimeStamp) throws IOException;

}
