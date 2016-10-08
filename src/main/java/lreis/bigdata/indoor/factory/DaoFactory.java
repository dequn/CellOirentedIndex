package lreis.bigdata.indoor.factory;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.ISemStopsDao;
import lreis.bigdata.indoor.dao.proxy.*;

import java.io.IOException;

/**
 * Created by dq on 4/29/16.
 */
public class DaoFactory {
    public static IPOIDao getHBasePOIDao() throws IOException {
        return new HBasePOIDaoProxy();
    }

    public static ICellDao getHBaseCellDao() throws IOException {
        return new HBaseCellDaoProxy();
    }

    public static ICellDao getPostgreCellDao() {

        return new PostgreCellDaoProxy();
    }


    public static IPOIDao getPostgrePOIDao() {
        return new PostgrePOIDaoProxy();
    }


    public static IPOIDao getPhoenixPOIDao() {
        return new PhoenixPOIDaoProxy();
    }

    public static ISemStopsDao getPhoenixSemStopsDao() {
        return new PhoenixSemStopsDaoProxy();
    }
}
