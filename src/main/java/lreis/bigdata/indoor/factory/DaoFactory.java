package lreis.bigdata.indoor.factory;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.dao.IShopDao;
import lreis.bigdata.indoor.dao.proxy.POIDaoProxy;
import lreis.bigdata.indoor.dao.proxy.ShopDaoProxy;

import java.io.IOException;

/**
 * Created by dq on 4/29/16.
 */
public class DaoFactory {
    public static IPOIDao getPOIDao() throws IOException {
        return new POIDaoProxy() ;
    }
    public static IShopDao getShopDao() throws IOException {
        return new ShopDaoProxy();
    }
}
