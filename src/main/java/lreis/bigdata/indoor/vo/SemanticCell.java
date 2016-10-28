package lreis.bigdata.indoor.vo;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Created by dq on 4/6/16.
 */
public class SemanticCell {

    protected String name;
    protected String polygonNum;
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

    public String getPolygonNum() {
        return polygonNum;
    }

    public void setPolygonNum(String polygonNum) {
        this.polygonNum = polygonNum;
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

    @Override
    public String toString() {
        return name.toString();
    }


}

