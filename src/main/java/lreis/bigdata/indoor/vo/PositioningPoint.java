package lreis.bigdata.indoor.vo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.jetbrains.annotations.Contract;

import java.util.Comparator;


/**
 * Created by dq on 4/29/16.
 */
public class PositioningPoint implements Comparator<PositioningPoint> {
    private String mac;
    private Long time;

    private Float x;
    private Float y;

    private String floorNum;
    private Point point;
    private SemanticCell semanticCellIn = null;


    public PositioningPoint() {
    }

    public PositioningPoint(String mac, long time, float x, float y, String floorNum) {
        this.mac = mac;
        this.floorNum = floorNum;
        this.x = x;
        this.y = y;
        this.time = time;

        this.updatePoint();


    }

    @Contract("null -> null")
    public static String calRowkey(PositioningPoint positioningPoint, QueryMethod method) {
        if (positioningPoint == null) {
            return null;
        }
        SemanticCell semanticCell = Building.getInstatnce().queryCell(positioningPoint,
                method);
        if (semanticCell == null) {
            return null;
        }
        positioningPoint.setSemanticCellIn(semanticCell);
        return String.format("%s%s%s", semanticCell.getNodeNum(), positioningPoint.getTime(), positioningPoint.getMac());
    }

    @Contract("null -> null")
    public static String calRowkey(PositioningPoint positioningPoint) {
        return PositioningPoint.calRowkey(positioningPoint, QueryMethod.Grid);
    }

    @Contract("null -> null")
    public static String calMacIndexRowkey(PositioningPoint positioningPoint) {

        if (positioningPoint == null) {
            return null;
        }
        return String.format("%s%s%04d", positioningPoint.getMac(), positioningPoint.getTime(), Building.getInstatnce().queryCell
                (positioningPoint, QueryMethod.Grid));
    }

    public SemanticCell getSemanticCellIn() {
        return semanticCellIn;
    }

    public void setSemanticCellIn(SemanticCell semanticCellIn) {
        this.semanticCellIn = semanticCellIn;
    }

    private void updatePoint() {
        if (this.x == null || this.y == null) {
            return;
        }
        Coordinate coordinate = new Coordinate(this.x, this.y);
        GeometryFactory gf = new GeometryFactory();
        this.point = gf.createPoint(coordinate);
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
        this.updatePoint();

    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
        this.updatePoint();
    }

    public String getFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    public Point getPoint() {
        return point;
    }

    @Override
    public int compare(PositioningPoint o1, PositioningPoint o2) {
        return (int) (o1.time - o2.time);
    }

    public enum QueryMethod {Grid, STR}

}



