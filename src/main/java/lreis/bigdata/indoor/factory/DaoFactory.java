package lreis.bigdata.indoor.factory;

import lreis.bigdata.indoor.dao.ICellDao;
import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.proxy.CellDaoProxy;
import lreis.bigdata.indoor.dao.proxy.HBasePOIDaoProxy;
import lreis.bigdata.indoor.dao.proxy.PostgrePOIDaoProxy;

import java.io.IOException;

/**
 * Created by dq on 4/29/16.
 */
public class DaoFactory {
    public static IPOIDao getHBasePOIDao() throws IOException {
        return new HBasePOIDaoProxy();
    }

    public static ICellDao getCellDao() throws IOException {
        return new CellDaoProxy();
    }

    public static IPOIDao getPostgrePOIDao(){
        return new PostgrePOIDaoProxy();
    }
}
