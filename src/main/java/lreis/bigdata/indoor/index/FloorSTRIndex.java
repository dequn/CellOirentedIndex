package lreis.bigdata.indoor.index;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.index.SpatialIndex;
import com.vividsolutions.jts.index.strtree.STRtree;
import lreis.bigdata.indoor.utils.FloorShpUtils;
import lreis.bigdata.indoor.vo.Cell;

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


    private static void build(String floor, List<Cell> cellList) {

        STRtree stRtree = new STRtree(cellList.size());

        for (Cell cell : cellList
                ) {
            Envelope ene;
            ene = cell.getGeom().getEnvelopeInternal();
            stRtree.insert(ene, cell);
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
        List<Cell> list = FloorShpUtils.getCellList(floorShpFileName);

        if (floorIdx == null) {
            floorIdx = new HashMap<String, SpatialIndex>();
        }
        FloorSTRIndex.build(floor, list);
        FloorSTRIndex.buildNameIdx(list);
        FloorSTRIndex.buildNodesIdx(list);
    }


    public static void buildNameIdx(List<Cell> cellList) {
        if (names == null) {
            names = new HashMap<String, ArrayList<Integer>>();
        }
        for (Cell cell : cellList) {
            if (names.get(cell.getName()) == null) {
                names.put(cell.getName(), new ArrayList<Integer>());
            }
            names.get(cell.getName()).add(cell.getNodeNum());
        }
    }


    public static void buildNodesIdx(List<Cell> cellList) {
        if (nodes == null) {
            nodes = new String[cellList.size() + 1];
        }
        for (Cell cell : cellList) {
            nodes[cell.getNodeNum()] = cell.getName();
        }
    }

    public static String nodeNum2Name(Integer nodeNum) {
        return nodes[nodeNum];
    }

}
