package lreis.bigdata.indoor.index;


import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.index.strtree.STRtree;
import lreis.bigdata.indoor.vo.Floor;
import lreis.bigdata.indoor.vo.Grid;
import lreis.bigdata.indoor.vo.SemanticCell;
import org.apache.commons.collections.CollectionUtils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by dq on 6/14/16.
 */
public class GridIndex {

    static double Fractor = 0.8;
    static double MinGridArea = 100.0;
    Floor floor;
    HashMap<Grid, SemanticCell> grid2CellMap;
    STRtree gridIndex = null;

    public GridIndex(Floor floor) {

        if (floor != null) {
            this.floor = floor;
            this.grid2CellMap = new HashMap<Grid, SemanticCell>();
            this.init();

        }

    }

    private void init() {
        this.calGrid();
        // strindex of grid
        this.gridIndex = new STRtree();
        for (Grid grid : this.grid2CellMap.keySet()) {
            this.gridIndex.insert(grid.getGeom().getEnvelopeInternal(), this.grid2CellMap.get(grid));
        }
    }

    public Floor getFloor() {
        return floor;
    }

    public HashMap<Grid, SemanticCell> getGrid2CellMap() {
        return grid2CellMap;
    }

    private void calGrid() {
        if (this.floor == null) {
            return;
        }

        //get the bounding box of the floor.
        Geometry geom = (Geometry) floor.getSemanticCells().get(0).getGeom().clone();
        for (SemanticCell semanticCell : floor.getSemanticCells()) {
            geom = geom.union(semanticCell.getGeom());
        }

        Grid wholeGrid = new Grid(geom.getEnvelope(), "");

        Queue<Grid> queue = new LinkedList<Grid>();

        queue.add(wholeGrid);
        while (!queue.isEmpty()) {
            Grid g = queue.poll();

            List<SemanticCell> semanticCells = this.floor.getStrTree().query(g.getGeom().getEnvelopeInternal());

            if (semanticCells.size() == 0) {
                //no overlap
                grid2CellMap.put(g, null);
            } else if (semanticCells.size() == 1) {
                // only one overlap
                grid2CellMap.put(g, semanticCells.get(0));
            } else {

                double sumArea = 0.0;
                double maxArea = 0.0;
                SemanticCell maxSemanticCell = null;

                for (SemanticCell semanticCell : semanticCells) {
                    double intersectionArea = g.getGeom().intersection(semanticCell.getGeom()).getArea();
                    sumArea += intersectionArea;
                    if (intersectionArea > maxArea) {
                        maxArea = intersectionArea;
                        maxSemanticCell = semanticCell;
                    }
                }
                //meet one of the two requirements
                if (maxArea >= sumArea * GridIndex.Fractor || sumArea < GridIndex.MinGridArea) {
                    grid2CellMap.put(g, maxSemanticCell);
                } else { // need quarter the grid and add into the queue.
                    CollectionUtils.addAll(queue, g.quarter());
                }


            }

        }


    }


    public List<SemanticCell> query(Point point) {
        return this.gridIndex.query(point.getEnvelopeInternal());
    }


}
