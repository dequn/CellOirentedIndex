package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.dao.impl.HBaseCellDaoImpl;
import lreis.bigdata.indoor.dbc.HBaseConnection;
import lreis.bigdata.indoor.vo.Cell;
import lreis.bigdata.indoor.vo.POI;

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

    public List<POI> getPOIsByCell(Cell cell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return this.dao.getPOIsByCell(cell, beginTimeStamp, endTimeStamp);
    }

    public int countMacInCell(Cell cell, Long beginTimeStamp, Long endTimeStamp) throws IOException, SQLException {
        return this.dao.countMacInCell(cell, beginTimeStamp, endTimeStamp);
    }

    @Override
    public List<POI> getPOISBeenToAllCells(List<Cell> cells, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return this.dao.getPOISBeenToAllCells(cells, beginTimeStamp, endTimeStamp);
    }


}
