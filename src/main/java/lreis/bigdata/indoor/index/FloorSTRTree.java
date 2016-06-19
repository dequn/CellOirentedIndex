package lreis.bigdata.indoor.index;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.strtree.STRtree;
import lreis.bigdata.indoor.utils.FloorShpUtils;
import lreis.bigdata.indoor.vo.Cell;

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

        List<Cell> cells = FloorShpUtils.getCellList(this.floorShp);
        for (Cell cell : cells) {
            Envelope envelope;
            envelope = cell.getGeom().getEnvelopeInternal();
            this.tree.insert(envelope, cell);
        }

    }


}
