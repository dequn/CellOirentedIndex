package lreis.bigdata.indoor.vo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import org.jetbrains.annotations.Contract;


/**
 * Created by dq on 4/29/16.
 */
public class POI {
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

    public enum QueryMethod {Grid, STR}

    @Contract("null -> null")
    public static String calRowkey(POI poi) {
        if (poi == null) {
            return null;
        }
        return String.format("%s%s%s", Building.getInstatnce().queryCell(poi,
                QueryMethod.Grid).getNodeNum(), poi.getTime(), poi.getMac());
    }

    @Contract("null -> null")
    public static String calMacIndexRowkey(POI poi) {

        if (poi == null) {
            return null;
        }
        return String.format("%s%s%s", poi.getMac(), Building.getInstatnce().queryCell
                (poi, QueryMethod.Grid), poi.getTime());
    }

}
