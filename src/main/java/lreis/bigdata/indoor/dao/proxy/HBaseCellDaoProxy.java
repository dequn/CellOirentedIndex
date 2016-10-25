package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.dao.impl.HBaseCellDaoImpl;
import lreis.bigdata.indoor.dbc.HBaseConnection;
import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.SemanticCell;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by dq on 5/3/16.
 */
public class HBaseCellDaoProxy implements ICellDao {

    private ICellDao dao = null;

    public HBaseCellDaoProxy() throws IOException {
        HBaseConnection hConn = new HBaseConnection();
        this.dao = new HBaseCellDaoImpl(hConn.getConnection());
    }

    public List<PositioningPoint> getPOIsByCell(SemanticCell semanticCell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return this.dao.getPOIsByCell(semanticCell, beginTimeStamp, endTimeStamp);
    }

    public int countMacInCell(SemanticCell semanticCell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return this.dao.countMacInCell(semanticCell, beginTimeStamp, endTimeStamp);
    }

    @Override
    public List<PositioningPoint> getPOISBeenToAllCells(List<SemanticCell> semanticCells, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return this.dao.getPOISBeenToAllCells(semanticCells, beginTimeStamp, endTimeStamp);
    }


}
