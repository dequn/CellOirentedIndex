package lreis.bigdata.indoor.dao;

import lreis.bigdata.indoor.vo.POI;
import lreis.bigdata.indoor.vo.Shop;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by dq on 5/3/16.
 */
public interface IShopDao {

    public List<POI> getPOIsByShopName(String shopName, Long beginTimeStamp, Long endTimeStamp) throws IOException;

    public int countMacInShop(String shopName, Long beginTimeStamp, Long endTimeStamp) throws IOException;

    public List<POI> getPOISBeenToShops(List<Integer> nodeNumList, Long beginTimeStamp, Long endTimeStamp) throws IOException;
}
