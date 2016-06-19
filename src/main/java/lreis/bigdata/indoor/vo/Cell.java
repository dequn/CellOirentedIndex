package lreis.bigdata.indoor.vo;

import com.vividsolutions.jts.geom.Geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dq on 4/6/16.
 */
public class Cell {

    protected String name;
    protected Integer nodeNum;
    protected String floorNum;
    protected String category;
    protected Geometry geom;

    public Cell() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getNodeNum() {
        return nodeNum;
    }

    public void setNodeNum(Integer nodeNum) {
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

