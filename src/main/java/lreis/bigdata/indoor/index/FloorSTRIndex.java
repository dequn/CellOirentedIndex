package lreis.bigdata.indoor.index;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;
import lreis.bigdata.indoor.utils.FloorShpUtils;
import lreis.bigdata.indoor.vo.Shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dq on 4/7/16.
 */
public class FloorSTRIndex {

    static String shpFile = "/Users/dq/subject_study/20150915_xddyc_download/860100010020300001_f4.dbf";

    public static Map<String, SpatialIndex> floorIdx = null;
    public static Map<String, ArrayList<Integer>> names = null;
    public static String[] nodes = null;

    static {
        FloorSTRIndex.build(shpFile);
    }


    private static void build(String floor, List<Shop> shopList) {

        STRtree stRtree = new STRtree(shopList.size());

        for (Shop shop : shopList
                ) {
            Envelope ene;
            ene = shop.getGeom().getEnvelopeInternal();
            stRtree.insert(ene, shop);
        }

        FloorSTRIndex.floorIdx.put(floor, stRtree);

    }

    public static void build(String floorShpFileName) {

        String floor = null;
        try {
            floor = FloorShpUtils.getFloorNum(floorShpFileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        List<Shop> list = FloorShpUtils.getShopList(floorShpFileName);

        if (floorIdx == null) {
            floorIdx = new HashMap<String, SpatialIndex>();
        }
        FloorSTRIndex.build(floor, list);
        FloorSTRIndex.buildNameIdx(list);
        FloorSTRIndex.buildNodesIdx(list);
    }


    public static void buildNameIdx(List<Shop> shopList) {
        if (names == null) {
            names = new HashMap<String, ArrayList<Integer>>();
        }
        for (Shop shop : shopList) {
            if (names.get(shop.getName()) == null) {
                names.put(shop.getName(), new ArrayList<Integer>());
            }
            names.get(shop.getName()).add(shop.getNodeNum());
        }
    }


    public static void buildNodesIdx(List<Shop> shopList) {
        if (nodes == null) {
            nodes = new String[shopList.size() + 1];
        }
        for (Shop shop : shopList) {
            nodes[shop.getNodeNum()] = shop.getName();
        }
    }

    public static String nodeNum2Name(Integer nodeNum) {
        return nodes[nodeNum];
    }

}
