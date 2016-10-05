package lreis.bigdata.indoor.vo;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Created by dq on 4/6/16.
 */
public class SemanticCell {

    protected String name;
    protected String nodeNum;

    protected String floorNum;

    protected String category;

    protected Geometry geom;


    public SemanticCell() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(String nodeNum) {
        this.nodeNum = nodeNum;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Geometry getGeom() {
        return geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

}

