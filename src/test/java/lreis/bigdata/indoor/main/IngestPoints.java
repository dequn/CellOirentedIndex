package lreis.bigdata.indoor.main;

import lreis.bigdata.indoor.TestStatic;
import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.factory.DaoFactory;
import lreis.bigdata.indoor.vo.POI;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by zdq on 7/8/16.
 */
public class IngestPoints {

    IPOIDao dao = null;


    @BeforeClass
    public static void beforClass() {
        TestStatic.BuildingInit();
        TestStatic.readPOIs();
    }

    @Test
    public void HBaseIngest() {

        try {
            dao = DaoFactory.getHBasePOIDao();
            for (
                    POI poi : TestStatic.pois) {
                this.dao.insertPOI(poi);

            }
            this.dao.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Ignore
    @Test
    public void PostgreSQLIngest() {

        try {
            dao = DaoFactory.getPostgrePOIDao();
            for (
                    POI poi : TestStatic.pois) {
                this.dao.insertPOI(poi);

            }
            this.dao.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }


}
