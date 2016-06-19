package lreis.bigdata.indoor.vo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import lreis.bigdata.indoor.index.FloorSTRIndex;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by dq on 4/29/16.
 */
public class POI {

    private String mac;
    private String strTime;
    private Long time;

    private String strX;
    private String strY;
    private Float x;
    private Float y;

    private String floorNum;
    private String row;

    public POI(String mac, String strTime, String x, String y, String floorNum) {
        this.mac = mac;
        this.strTime = strTime;

        this.strX = x;
        this.strY = y;

        this.x = Float.parseFloat(x) / 1000;
        this.y = Float.parseFloat(y) / (-1000);

        this.floorNum = floorNum;
        this.calcTimeStamp();
        this.calcRow();
    }

    public POI() {

    }

    public void setStrX(String x) {
        this.strX = x;
        this.x = Float.parseFloat(x) / 1000;
    }

    public void setStrY(String y) {
        this.strY = y;
        this.y = Float.parseFloat(y) / (-1000);
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getStrY() {
        return strY;
    }

    public String getStrX() {
        return strX;
    }

    public String getStrTime() {
        return strTime;
    }

    public void setStrTime(String strTime) {
        this.strTime = strTime;
        this.calcTimeStamp();
    }

    public String getRow() {
        return row;
    }

    public String getFloorNum() {
        return floorNum.substring(3, 4);
    }

    public String getOriginalFloorNum() {
        return floorNum;
    }

    public void setFloorNum(String floorNum) {
        this.floorNum = floorNum;
    }

    private void calcRow() {
        Integer nodeNum = this.queryNodeNum();
        String prefixNum = String.format("%04d", nodeNum);
        this.row = prefixNum.toString() + this.time.toString() + this.getMac();
    }

    private void calcTimeStamp() {

        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            this.time = sdf.parse(this.strTime).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    /**
     * should return nonnegative number.
     *
     * @return
     */
    private Integer queryNodeNum() {

        Coordinate coordinate = new Coordinate(this.x, this.y);
        GeometryFactory gf = new GeometryFactory();
        Point point = gf.createPoint(coordinate);

        if (FloorSTRIndex.floorIdx.get(this.getFloorNum()) == null) {
            return -1;
        } else {
            List<Cell> list = FloorSTRIndex.floorIdx.get(this.getFloorNum()).query(point.getEnvelopeInternal());
            for (Cell item : list) {
                if (item.getGeom().contains(point)) {
                    return item.getNodeNum();
                }
            }
            return -1;
        }
    }
}
