package lreis.bigdata.indoor.factory;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.proxy.CellDaoProxy;
import lreis.bigdata.indoor.dao.proxy.POIDaoProxy;

import java.io.IOException;

/**
 * Created by dq on 4/29/16.
 */
public class DaoFactory {
    public static IPOIDao getPOIDao() throws IOException {
        return new POIDaoProxy();
    }

    public static ICellDao getCellDao() throws IOException {
        return new CellDaoProxy();
    }
}
