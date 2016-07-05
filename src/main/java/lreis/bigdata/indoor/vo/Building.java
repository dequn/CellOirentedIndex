package lreis.bigdata.indoor.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Q on 2016/6/20.
 */
public class Building {

    private static Building instance = null;

    private HashMap<String, Floor> floors = new HashMap<String, Floor>();// get floor by floorNum.
    //    private  List<Cell> cells = new ArrayList<Cell>();// use for get cell by num,num from 1 to n.
    private Cell[] cells = new Cell[300];

    private Building() {
    }

    /**
     * Query which Cell the point dropped in.
     *
     * @param floor
     * @param poi
     * @param method
     * @return List<Cell>
     */
    public static Cell queryCell(Floor floor, POI poi, POI.QueryMethod method) {
        if (method == POI.QueryMethod.Grid) {
            return floor.queryInGrid(poi.getPoint());
        } else if (method == POI.QueryMethod.STR) {
            return floor.queryInSTR(poi.getPoint());
        } else {
            return null;
        }

    }

    public static Building getInstatnce() {
        if (instance == null) {
            instance = new Building();
        }
        return instance;
    }

    public void addFloor(Floor floor) {
        floors.put(floor.getFloorNum(), floor);

        for (Cell cell : floor.getCells()) {
//            cells.add(cell.getNodeNum(), cell);
            cells[cell.getNodeNum()] = cell;
        }

    }

    public Cell queryCell(POI poi, POI.QueryMethod method) {
        return queryCell(this.floors.get(poi.getFloorNum()), poi, method);
    }

    public Floor getFloor(String floorNum) {
        return floors.get(floorNum);
    }

    public List<Cell> getCellsByName(String name) {
        List<Cell> result = new ArrayList<Cell>();
        for (Floor floor : floors.values()) {
            result.addAll(floor.getCellsByName(name));
        }
        return result;
    }

    public Cell getCellByNum(Integer num) {
//        return cells.get(num);
        return cells[num];
    }

}
