package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.dao.impl.PostgreCellDaoImpl;
import lreis.bigdata.indoor.dbc.PostgreConn;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.vo.PositioningPoint;
import lreis.bigdata.indoor.vo.SemanticCell;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Q on 2016/7/5.
 */
public class PostgreCellDaoProxy implements ICellDao {

    private ICellDao dao = null;

    public PostgreCellDaoProxy() {
        PostgreConn pconn = DbcFactory.getPostgreConn();
        this.dao = new PostgreCellDaoImpl(pconn);
    }

    @Override
    public List<PositioningPoint> getPOIsByCell(SemanticCell semanticCell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return this.dao.getPOIsByCell(semanticCell, beginTimeStamp, endTimeStamp);
    }

    @Override
    public int countMacInCell(SemanticCell semanticCell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return this.dao.countMacInCell(semanticCell, beginTimeStamp, endTimeStamp);
    }

    @Override
    public List<PositioningPoint> getPOISBeenToAllCells(List<SemanticCell> semanticCells, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return this.dao.getPOISBeenToAllCells(semanticCells, beginTimeStamp, endTimeStamp);
    }
}
