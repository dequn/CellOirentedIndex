package lreis.bigdata.indoor.index;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.strtree.STRtree;
import lreis.bigdata.indoor.utils.FloorShpUtils;
import lreis.bigdata.indoor.vo.Shop;

import java.util.List;

/**
 * Created by dq on 6/14/16.
 */
public class FloorSTRTree {

    private STRtree tree;
    private String floorShp;

    public FloorSTRTree(String floorShp) {
        this.floorShp = floorShp;
    }


    public STRtree getTree() {
        if (tree == null) {
            this.build();
        }
        return tree;
    }


    public String getFloorShp() {
        return floorShp;
    }

    /**
     * 建立str索引
     */
    private void build() {

        if (floorShp == null) return;

        this.tree = new STRtree();

        List<Shop> shops = FloorShpUtils.getShopList(this.floorShp);
        for (Shop shop : shops) {
            Envelope envelope;
            envelope = shop.getGeom().getEnvelopeInternal();
            this.tree.insert(envelope, shop);
        }

    }


}
