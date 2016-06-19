package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.dao.impl.CellDaoImpl;
import lreis.bigdata.indoor.dbc.IHBaseConnection;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.vo.POI;

import java.io.IOException;
import java.util.List;

/**
 * Created by dq on 5/3/16.
 */
public class CellDaoProxy implements ICellDao {

    private ICellDao dao = null;

    public CellDaoProxy() throws IOException {
        IHBaseConnection conn = DbcFactory.getConnection();
        this.dao = new CellDaoImpl(conn.getConnection());
    }

    public List<POI> getPOIsByCellName(String cellName, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return this.dao.getPOIsByCellName(cellName, beginTimeStamp, endTimeStamp);
    }

    public int countMacInCell(String cellName, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return this.dao.countMacInCell(cellName, beginTimeStamp, endTimeStamp);
    }

    @Override
    public List<POI> getPOISBeenToCells(List<Integer> nodeNumList, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return this.dao.getPOISBeenToCells(nodeNumList, beginTimeStamp, endTimeStamp);
    }


}
