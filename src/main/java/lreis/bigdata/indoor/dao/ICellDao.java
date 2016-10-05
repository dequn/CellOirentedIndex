package lreis.bigdata.indoor.dao;

import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.SemanticCell;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by dq on 5/3/16.
 */
public interface ICellDao {

    List<PositioningPoint> getPOIsByCell(SemanticCell semanticCell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException;

    int countMacInCell(SemanticCell semanticCell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException;

    List<PositioningPoint> getPOISBeenToAllCells(List<SemanticCell> semanticCells, Long beginTimeStamp, Long endTimeStamp) throws IOException;

}
