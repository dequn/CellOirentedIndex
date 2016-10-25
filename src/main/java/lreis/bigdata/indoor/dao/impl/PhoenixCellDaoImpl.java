package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.SemanticCell;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by dq on 10/6/16.
 */
public class PhoenixCellDaoImpl implements ICellDao {

    @Override
    public List<PositioningPoint> getPOIsByCell(SemanticCell semanticCell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return null;
    }

    @Override
    public int countMacInCell(SemanticCell semanticCell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return 0;
    }

    @Override
    public List<PositioningPoint> getPOISBeenToAllCells(List<SemanticCell> semanticCells, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return null;
    }

}
