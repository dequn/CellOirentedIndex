package lreis.bigdata.indoor.vo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

import java.util.List;


/**
 * Created by dq on 4/29/16.
 */
public class POI {
    public enum QueryMethod {Grid, STR};

    private String mac;
    private Long time;
    private Float x;
    private Float y;
    private String floorNum;
    private Point point;

    public POI() {
    }

    public POI(String mac, long time, float x, float y, String floorNum) {
        this.mac = mac;
        this.floorNum = floorNum;
        this.x = x;
        this.y = y;
        this.time = time;

        this.updatePoint();


    }

    private void updatePoint(){
        Coordinate coordinate = new Coordinate(this.x, this.y);
        GeometryFactory gf = new GeometryFactory();
        this.point = gf.createPoint(coordinate);
    }

    public String getMac() {
        return mac;
    }

    public Long getTime() {
        return time;
    }

    public Float getX() {
        return x;
    }

    public Float getY() {
        return y;
    }

    public String getFloorNum() {
        return floorNum;
    }

    public Point getPoint() {
        return point;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public void setX(Float x) {
        this.x = x;
        this.updatePoint();

    }

    public void setY(Float y) {
        this.y = y;
        this.updatePoint();
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }
}
