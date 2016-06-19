import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import lreis.bigdata.indoor.index.FloorSTRIndex;
import lreis.bigdata.indoor.vo.Shop;

import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by dq on 4/11/16.
 */
public class PositioningDataUtils {
    public static String COLUMN_FAMILY_NAME = "data";
    public static String TABLE_NAME = "wifi";


    public static void extractFloorData(String floorNum, String sourceFile) {

        FileWriter fw;
        try {
            fw = new FileWriter(String.format("/Users/dq/subject_study/f%s_points.csv", floorNum));
            BufferedReader reader = new BufferedReader(new FileReader(sourceFile));
            String line;

            while ((line = reader.readLine()) != null) {
                String item[] = line.split(",");
                if (item[1].equals(String.format("200%s0", floorNum))) {
                    fw.write(line + '\n');
                }
            }
            reader.close();
            fw.close();
        } catch (IOException e1) {
            e1.printStackTrace();
        }


    }

    public static Integer queryNodeNum(String line) throws Exception {
        Point p = genPointFeature(line);
        String floorNum = getFloorNum(line);
        if (FloorSTRIndex.floorIdx.get(floorNum) == null) {
            throw new Exception(String.format("Index of Floor %s has not been built!", floorNum));
        } else {
            List<Shop> list = FloorSTRIndex.floorIdx.get(floorNum).query(p.getEnvelopeInternal());

            for (Shop item : list) {
                if (item.getGeom().contains(p)) {
                    return item.getNodeNum();
                }
            }
            return -1;
        }


    }

    public static String getFloorNum(String line) {

        String items[] = line.split(",");
        String floor = items[1].substring(3, 4);
        return floor;

    }


    public static Point genPointFeature(String line) {


        String items[] = line.split(",");

        float x = Float.parseFloat(items[2]) / 1000;
        float y = Float.parseFloat(items[3]) / (-1000);

        Coordinate c = new Coordinate(x, y);
        GeometryFactory gf = new GeometryFactory();
        Point p = gf.createPoint(c);

        return p;

    }


    public static String genRowKey(String line) {


        String nodeNum = null;
        try {
            nodeNum = String.format("%04d", queryNodeNum(line));
        } catch (Exception e) {
            e.printStackTrace();
        }

        Point p = genPointFeature(line);


        String items[] = line.split(",");

        String mac = items[0];


        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Long timeStamp = null;
        try {
            timeStamp = sdf.parse(items[4]).getTime() / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }


        return nodeNum.toString() + timeStamp.toString() + mac;

    }


    public static boolean putLineToHBase(String line) {
        String rowKey = genRowKey(line);

        String items[] = line.split(",");

        String mac = items[0];
        String floorNum = items[1];
        String x = items[2];
        String y = items[3];
        String time = items[4];


        String[] rows = {rowKey};
        String[] columns = {"mac", "floorNum", "x", "y", "time"};

        String[][] values = {{mac, floorNum, x, y, time}};

        return HBase.putData(TABLE_NAME, COLUMN_FAMILY_NAME, rows, columns, values);


    }


}
