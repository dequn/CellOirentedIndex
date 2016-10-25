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
    private long time;

    private float x;
    private float y;

    private String floorNum;
    private Point point;


    private String semCellNum = null;
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


    public PositioningPoint(PositioningPoint point){
        this.mac = point.getMac();
        this.floorNum = point.getFloorNum();
        this.x = point.getX();
        this.y = point.getY();
        this.time = point.getTime();
        this.updatePoint();

    }



    @Contract("null -> null")
    public static String calRowkey(PositioningPoint positioningPoint, QueryMethod method) {
        if (positioningPoint == null) {
            return null;
        }
        SemanticCell semanticCell = Building.getInstatnce().querySemCell(positioningPoint,
                method);
        if (semanticCell == null) {
            return null;
        }
        positioningPoint.setSemanticCellIn(semanticCell);
        return String.format("%s%s%s", semanticCell.getPolygonNum(), positioningPoint.getTime(), positioningPoint.getMac());
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


        SemanticCell semCell =
                Building.getInstatnce().querySemCell
                        (positioningPoint, QueryMethod.Grid);

        return String.format("%s%s%04d", positioningPoint.getMac(), positioningPoint.getTime(), semCell);


    }

    public SemanticCell getSemanticCellIn() {
        return semanticCellIn;
    }

    public void setSemanticCellIn(SemanticCell semanticCellIn) {
        this.semanticCellIn = semanticCellIn;
    }

    private void updatePoint() {

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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
        this.updatePoint();

    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
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

    public static enum QueryMethod {Grid, STR}

}



