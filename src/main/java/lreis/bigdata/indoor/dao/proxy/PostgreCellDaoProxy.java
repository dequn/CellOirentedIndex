package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.dao.impl.PostgreCellDaoImpl;
import lreis.bigdata.indoor.dbc.PostgreConn;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.vo.Cell;
import lreis.bigdata.indoor.vo.POI;

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
    public List<POI> getPOIsByCell(Cell cell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return this.dao.getPOIsByCell(cell, beginTimeStamp, endTimeStamp);
    }

    @Override
    public int countMacInCell(Cell cell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return this.dao.countMacInCell(cell, beginTimeStamp, endTimeStamp);
    }

    @Override
    public List<POI> getPOISBeenToAllCells(List<Cell> cells, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return this.dao.getPOISBeenToAllCells(cells, beginTimeStamp, endTimeStamp);
    }
}
