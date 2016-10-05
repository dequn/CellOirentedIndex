package lreis.bigdata.indoor;

import lreis.bigdata.indoor.utils.RecordUtils;
import lreis.bigdata.indoor.vo.Building;
import lreis.bigdata.indoor.vo.Floor;
import lreis.bigdata.indoor.vo.PositioningPoint;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Q on 2016/7/5.
 */
public class TestStatic {
    static final String[] floorShps = new String[]{
//            "D:\\big_joy\\floors\\20010.dbf",
//            "D:\\big_joy\\floors\\20020.dbf",
//            "D:\\big_joy\\floors\\20030.dbf",
//            "D:\\big_joy\\floors\\20040.dbf"
//
//
            "/home/zdq/big_joy/floors/20010.dbf",
            "/home/zdq/big_joy/floors/20020.dbf",
            "/home/zdq/big_joy/floors/20030.dbf",
            "/home/zdq/big_joy/floors/20040.dbf",
    };

    static final String[] pointFiles = new String[]{
//            "D:\\big_joy\\points\\0401\\20010.csv",
//            "D:\\big_joy\\points\\0401\\20020.csv",
//            "D:\\big_joy\\points\\0401\\20030.csv",
//            "D:\\big_joy\\points\\0401\\20040.csv",

            "/home/zdq/big_joy/points/0401/20010.csv",
            "/home/zdq/big_joy/points/0401/20020.csv",
            "/home/zdq/big_joy/points/0401/20030.csv",
            "/home/zdq/big_joy/points/0401/20040.csv",
    };


    public static ArrayList<PositioningPoint> positioningPoints = new ArrayList<PositioningPoint>();


    public static void BuildingInit() {
        Building building = Building.getInstatnce();

        for (String file : floorShps) {
//            building.addFloor(new Floor(file.substring(18, 23), file));// for windows
            building.addFloor(new Floor(file.substring(25, 30), file));// for linux
        }
    }

    public static void readPOIs() {
        for (String file : pointFiles) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                String line = "";
                while ((line = reader.readLine()) != null) {
                    String items[] = line.split(",");

                    String mac = items[0];
                    String floorNum = items[1];
                    Float x = Float.parseFloat(items[2]) / 1000;
                    Float y = Float.parseFloat(items[3]) / -1000;
                    long time = RecordUtils.calcTimeStamp(items[4]);
                    positioningPoints.add(new PositioningPoint(mac, time, x, y, floorNum));
                }
                reader.close();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
