package lreis.bigdata.indoor.vo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * Created by Q on 2016/6/20.
 */
public class Grid {
    private String num;
    private Geometry geom;

    public Grid(Geometry geom, String num) {
        this.geom = geom;
        this.num = num;
    }

    public Grid() {

    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public Geometry getGeom() {
        return geom;
    }

    public void setGeom(Geometry geom) {
        this.geom = geom;
    }

    public Grid[] quarter() {
        Grid[] grids = new Grid[4];

        Envelope envelope = this.geom.getEnvelopeInternal();

        Coordinate mid = envelope.centre();

        Coordinate leftBottom = new Coordinate(envelope.getMinX(), envelope.getMinY());
        Coordinate leftUp = new Coordinate(envelope.getMinX(), envelope.getMaxY());
        Coordinate rightUp = new Coordinate(envelope.getMaxX(), envelope.getMaxY());
        Coordinate rightBottom = new Coordinate(envelope.getMaxX(), envelope.getMinY());


        GeometryFactory factory = this.geom.getFactory();

        /*
        -------------------
        |   01   |  11    |
        |        |        |
        |--------|--------|
        |   00   |  10    |
        |        |        |
        -------------------

         */

        //all in anticlockwise
        Geometry lb = factory.createPolygon(factory.createLinearRing(new Coordinate[]{leftBottom, new Coordinate(mid.x, leftBottom.y), mid, new Coordinate(leftBottom.x, mid.y), leftBottom}), null);
        Geometry rb = factory.createPolygon(factory.createLinearRing(new Coordinate[]{rightBottom, new Coordinate(rightBottom.x, mid.y), mid, new Coordinate(mid.x, rightBottom.y), rightBottom}), null);
        Geometry ru = factory.createPolygon(factory.createLinearRing(new Coordinate[]{rightUp, new Coordinate(mid.x, rightUp.y), mid, new Coordinate(rightUp.x, mid.y), rightUp}), null);
        Geometry lu = factory.createPolygon(factory.createLinearRing(new Coordinate[]{leftUp, new Coordinate(leftUp.x, mid.y), mid, new Coordinate(mid.x, rightUp.y), leftUp}), null);

        grids[0] = new Grid(lb, this.num + "00");
        grids[1] = new Grid(rb, this.num + "10");
        grids[2] = new Grid(lu, this.num + "01");
        grids[3] = new Grid(ru, this.num + "11");

        return grids;
    }
}
