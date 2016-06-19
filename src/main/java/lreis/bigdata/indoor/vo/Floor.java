package lreis.bigdata.indoor.vo;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.strtree.STRtree;
import lreis.bigdata.indoor.utils.FloorShpUtils;

import java.util.List;

/**
 * Created by dq on 6/14/16.
 */
public class Floor {

    String floorShp;
    List<Shop> shops;
    STRtree strTree;


    public Floor(String floorShp) {
        this.floorShp = floorShp;
    }

    public String getFloorShp() {
        return floorShp;
    }

    public void setFloorShp(String floorShp) {
        this.floorShp = floorShp;
    }

    public List<Shop> getShops() {
        if (this.shops == null) {
            this.shops = FloorShpUtils.getShopList(this.floorShp);
        }
        return shops;
    }


    public STRtree getStrTree() {
        if (this.strTree == null) {
            this.buildSTRTree();
        }

        return strTree;
    }

    /**
     * 建立str索引
     */
    private void buildSTRTree() {

        if (floorShp == null) return;

        this.strTree = new STRtree();
        for (Shop shop : this.getShops()) {
            Envelope envelope = shop.getGeom().getEnvelopeInternal();
            this.strTree.insert(envelope, shop);
        }

    }
}

