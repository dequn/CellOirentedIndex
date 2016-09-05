package lreis.bigdata.indoor.dao.impl;

import lreis.bigdata.indoor.TestStatic;
import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.factory.DaoFactory;
import lreis.bigdata.indoor.utils.RecordUtils;
import lreis.bigdata.indoor.vo.POI;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by zdq on 8/8/16.
 */
public class PhoenixPOIDaoTest {
    @BeforeClass
    public static void beforClass() {

        TestStatic.BuildingInit();
//        TestStatic.readPOIs();
    }



    @Test
    public void InsertPOITest(){
        try {
            IPOIDao dao = DaoFactory.getPhoenixPOIDao();
            POI poi = new POI("ACF7F348B123", RecordUtils.calcTimeStamp("2014-04-01 18:56:15"), (float) 58.544, (float) -81.318, "20010");
            boolean result = dao.insertPOI(poi);
            if(result){
                System.out.println("Phoenix insert a poi OK!");
            }
        } catch (ParseException e) {


            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
