package lreis.bigdata.indoor.dao.proxy;

import lreis.bigdata.indoor.dao.IShopDao;
import lreis.bigdata.indoor.dao.impl.ShopDaoImpl;
import lreis.bigdata.indoor.dbc.HBaseConnection;
import lreis.bigdata.indoor.dbc.IHBaseConnection;
import lreis.bigdata.indoor.factory.DbcFactory;
import lreis.bigdata.indoor.vo.POI;
import lreis.bigdata.indoor.vo.Shop;

import java.io.IOException;
import java.util.List;

/**
 * Created by dq on 5/3/16.
 */
public class ShopDaoProxy implements IShopDao {

    private IShopDao dao = null;

    public ShopDaoProxy() throws IOException {
        IHBaseConnection conn = DbcFactory.getConnection();
        this.dao = new ShopDaoImpl(conn.getConnection());
    }

    public List<POI> getPOIsByShopName(String shopName, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return this.dao.getPOIsByShopName(shopName, beginTimeStamp, endTimeStamp);
    }

    public int countMacInShop(String shopName, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return this.dao.countMacInShop(shopName, beginTimeStamp, endTimeStamp);
    }

    @Override
    public List<POI> getPOISBeenToShops(List<Integer> nodeNumList, Long beginTimeStamp, Long endTimeStamp) throws IOException {
        return this.dao.getPOISBeenToShops(nodeNumList, beginTimeStamp, endTimeStamp);
    }


}
