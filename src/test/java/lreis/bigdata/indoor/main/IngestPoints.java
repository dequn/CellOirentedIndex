package lreis.bigdata.indoor.main;

import lreis.bigdata.indoor.TestStatic;
import lreis.bigdata.indoor.dao.IPOIDao;
import lreis.bigdata.indoor.factory.DaoFactory;
import lreis.bigdata.indoor.vo.PositioningPoint;
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
        TestStatic.readPOIs();
    }


    @Ignore
    @Test
    public void HBaseIngest() {

        try {
            dao = DaoFactory.getHBasePOIDao();
            for (PositioningPoint positioningPoint : TestStatic.positioningPoints) {
                this.dao.insertPOI(positioningPoint);
            }
            this.dao.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Ignore
    @Test
    public void PostgreSQLIngest() {

        try {
            dao = DaoFactory.getPostgrePOIDao();
            for (
                    PositioningPoint positioningPoint : TestStatic.positioningPoints) {
                this.dao.insertPOI(positioningPoint);

            }
            this.dao.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void PhoenixIngest() {


        try {
            dao = DaoFactory.getPhoenixPOIDao();
            for (PositioningPoint positioningPoint : TestStatic.positioningPoints) {
                this.dao.insertPOI(positioningPoint);
            }
            this.dao.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
