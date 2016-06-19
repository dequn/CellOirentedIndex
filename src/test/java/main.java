import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.factory.DaoFactory;
import lreis.bigdata.indoor.vo.POI;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by dq on 4/29/16.
 */
public class main {
    public static void main(String[] args) {

        System.out.println("------------- create table \"wifi\" -------------------");
//        String[] family = {"data"};
////        HBase.createTable("wifi", family);
//        HBase.close();

        System.out.println("------------- create table \"wifi\" end -------------------");


        System.out.println("------------- put data begin-------------------");

        String dataFile = "/Users/dq/subject_study/f4_points.csv";

        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String line = null;
            IPOIDao dao = DaoFactory.getPOIDao();
            while ((line = reader.readLine()) != null) {
                String[] items = line.split(",");
                POI poi = new POI(items[0], items[4], items[2], items[3], items[1]);
                dao.insertPOI(poi);
            }
            reader.close();
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        System.out.println("------------- put data end-------------------");
    }


}


