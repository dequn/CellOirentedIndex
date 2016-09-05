package lreis.bigdata.indoor.vo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.jetbrains.annotations.Contract;

import java.util.Comparator;


/**
 * Created by dq on 4/29/16.
 */
public class POI implements Comparator<POI> {
    private String mac;

    private Long time;
    private Float x;
    private Float y;
    private String floorNum;
    private Point point;

    private Cell cellIn = null;

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

    @Contract("null -> null")
    public static String calRowkey(POI poi, QueryMethod method) {
        if (poi == null) {
            return null;
        }
        Cell cell = Building.getInstatnce().queryCell(poi,
                method);
        if (cell == null) {
            return null;
        }
        poi.setCellIn(cell);
        return String.format("%04d%s%s", cell.getNodeNum(), poi.getTime(), poi.getMac());
    }

    @Contract("null -> null")
    public static String calRowkey(POI poi) {
        return POI.calRowkey(poi, QueryMethod.Grid);
    }

    @Contract("null -> null")
    public static String calMacIndexRowkey(POI poi) {

        if (poi == null) {
            return null;
        }
        return String.format("%s%s%04d", poi.getMac(), poi.getTime(), Building.getInstatnce().queryCell
                (poi, QueryMethod.Grid));
    }

    public Cell getCellIn() {
        return cellIn;
    }

    public void setCellIn(Cell cellIn) {
        this.cellIn = cellIn;
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
    public int compare(POI o1, POI o2) {
        return (int) (o1.time - o2.time);
    }

    public enum QueryMethod {Grid, STR}

}



