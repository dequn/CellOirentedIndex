package lreis.bigdata.indoor.vo;

import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.factory.DaoFactory;
import lreis.bigdata.indoor.utils.RecordUtils;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.*;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by Q on 2016/6/21.
 */
public class BuildingTest {

    String[] floorShps = new String[]{
//            "D:\\big_joy\\floors\\20010.dbf",// topology error
//            "D:\\big_joy\\floors\\20020.dbf",
//            "D:\\big_joy\\floors\\20030.dbf",
            "D:\\big_joy\\floors\\20040.dbf"
    };

    String[] pointFiles = new String[]{
//            "D:\\big_joy\\points\\0401\\20010.csv",
//            "D:\\big_joy\\points\\0401\\20020.csv",
//            "D:\\big_joy\\points\\0401\\20030.csv",
            "D:\\big_joy\\points\\0401\\20040.csv",
    };


    ArrayList<POI> pois = new ArrayList<POI>();


    @Before
    public void BuildingInit() {
        Building building = Building.getInstatnce();

        for (String file : floorShps) {
            building.addFloor(new Floor(file.substring(18, 23), file));
        }
    }

    @Before
    public void readPOIs() {
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
                    this.pois.add(new POI(mac, time, x, y, floorNum));
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



    @Ignore
    @Test
    public void CompareEfficiencyBetweenGridAndSTR() {

        int compareNum = 1000;
        try {
            FileWriter writer = new FileWriter(new File("d:\\big_joy\\compare.txt"));
            writer.write("time,compareNum,grid(ms),STR(ms)\n");
            while (compareNum < this.pois.size()) {

                //针对一定数量的pois对比十次
                for (int j = 0; j < 10; j++) {

                    long start = System.currentTimeMillis();

                    for (int i = 0; i < compareNum; i++) {
                        POI poi = this.pois.get(i);
                        Building.queryCell(Building.getInstatnce().getFloor(poi.getFloorNum()),
                                poi, POI
                                        .QueryMethod.Grid);
                    }
                    long stop = System.currentTimeMillis();
                    writer.write(String.format("%s,%s,%s", j, compareNum, stop - start));
                    System.out.println("Grid query time of " + compareNum + " POIs is " + (stop - start));
                    start = System.currentTimeMillis();
                    for (int i = 0; i < compareNum; i++) {
                        POI poi = this.pois.get(i);
                        Building.queryCell(Building.getInstatnce().getFloor(poi.getFloorNum()), poi, POI.QueryMethod.STR);
                    }
                    stop = System.currentTimeMillis();
                    writer.write(String.format(",%s\n", stop - start));
                    System.out.println("STR query time of " + compareNum + " POIs is " + (stop - start));

                }
                compareNum *= 5;
            }
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void comparePostGISAndHBase() {


        try {
            IPOIDao dao = DaoFactory.getPOIDao();
            dao.insertPOI2Postgres(this.pois.get(1));
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }


    }

}