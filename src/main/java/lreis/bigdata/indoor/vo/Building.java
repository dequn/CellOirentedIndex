package lreis.bigdata.indoor.vo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Q on 2016/6/20.
 */
public class Building {

    private static Building instance = null;

    private HashMap<String, Floor> floors = new HashMap<String, Floor>();// get floor by floorNum.
    private Map<String, SemanticCell> semanticCells = new HashMap<>();




    private Building() {

    }

    /**
     * Query which SemanticCell the point dropped in.
     *
     * @param floor
     * @param positioningPoint
     * @param method
     * @return List<SemanticCell>
     */
    public static SemanticCell queryCell(Floor floor, PositioningPoint positioningPoint, PositioningPoint.QueryMethod method) {
        if (method == PositioningPoint.QueryMethod.Grid) {
            return floor.queryInGrid(positioningPoint.getPoint());
        } else if (method == PositioningPoint.QueryMethod.STR) {
            return floor.queryInSTR(positioningPoint.getPoint());
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

        for (SemanticCell semanticCell : floor.getSemanticCells()) {
            semanticCells.put(semanticCell.getNodeNum(), semanticCell);
        }

    }

    public SemanticCell queryCell(PositioningPoint positioningPoint, PositioningPoint.QueryMethod method) {
        return queryCell(this.floors.get(positioningPoint.getFloorNum()), positioningPoint, method);
    }

    public Floor getFloor(String floorNum) {
        return floors.get(floorNum);
    }

    public List<SemanticCell> getCellsByName(String name) {
        List<SemanticCell> result = new ArrayList<SemanticCell>();
        for (Floor floor : floors.values()) {
            result.addAll(floor.getCellsByName(name));
        }
        return result;
    }

    public SemanticCell getCellByNum(String strNum) {
        return semanticCells.get(strNum);
    }

}
